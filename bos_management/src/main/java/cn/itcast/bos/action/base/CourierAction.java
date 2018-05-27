package cn.itcast.bos.action.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.action.base.utils.BaseAction;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.CourierService;
@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class CourierAction extends BaseAction<Courier> {

	@Autowired
	private CourierService courierService;
	public void setCourierService(CourierService courierService) {
		this.courierService = courierService;
	}

	@Action(value="courier_save",results={@Result(name="success",type="redirect",location="./pages/base/courier.html")})
	public String save(){
		courierService.save(model);
		return SUCCESS;
	}
	
	
	//分页显示+条件查询
	@Action(value="courier__pageQuery",results={@Result(name="success",type="json")})
	public String pageQuery(){
		//将数据封装到为我们提供的对象中
		Pageable pageable=new PageRequest(page-1, rows);
		//处理接收过来的条件查询，并封装
		Specification<Courier> specification=new Specification<Courier>() {
			@Override
			// Root 用于获取属性字段，CriteriaQuery可以用于简单条件查询，CriteriaBuilder 用于构造复杂条件查询
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				//创建一个条件集合
				List<Predicate> predicates=new ArrayList<Predicate>();
				//判断条件查询的选型是否输入，如果输入则进入
				//工号条件
				if(StringUtils.isNotBlank(model.getCourierNum())){
					//生成查询条件，equal的意思时相等查询，第一个参数是获取数据库中的字段和类型，第二个参数时查询时输入的，相当于第一个参数所对应的值
					Predicate p1=cb.equal(root.get("courierNum").as(String.class),
							model.getCourierNum());
					System.out.println(model.getCourierNum());
					//生成查询条件后，加入查询条件的集合
					predicates.add(p1);
				}
				//所属单位条件
				if(StringUtils.isNotBlank(model.getCompany())){
					Predicate p2=cb.like(root.get("company").as(String.class),
							"%"+model.getCompany()+"%");
					System.out.println(model.getCompany());
					//生成查询条件后，加入查询条件的集合
					predicates.add(p2);
				}
				//类型
				if(StringUtils.isNotBlank(model.getType())){
					Predicate p3=cb.like(root.get("type").as(String.class),
							"%"+model.getType()+"%");
					//生成查询条件后，加入查询条件的集合
					System.out.println(model.getType());
					predicates.add(p3);
				}
				//难点。。。收派标准。。需关联另一张表
				Join<Courier, Standard> Standardjoin = root.join("standard",JoinType.INNER);
				if(model.getStandard()!=null&&StringUtils.isNotBlank(model.getStandard().getName())){
					Predicate p4=cb.like(Standardjoin.get("name").as(String.class),
							"%"+ model.getStandard().getName()+"%");
					System.out.println(model.getStandard().getName());
					predicates.add(p4);
				}
				
				return cb.and(predicates.toArray(new Predicate[0]));
			}
		};
		Page<Courier> pageData=courierService.findPageData(specification,pageable);
		this.pushPageDataToValueStack(pageData);
		return SUCCESS;
	}
	
	//作废快递员
	//获取传过来需要作废的ID
	private String ids;
	public void setIds(String ids) {
		this.ids = ids;
	}
	@Action(value="courier_delBatch",results={@Result(name="success",type="redirect",location="./pages/base/courier.html")})
	public String delBatch(){
		//System.out.println(ids+"-----------------");
		courierService.delBatch(ids);
	return SUCCESS;
	}
	//恢复快递员
	@Action(value="courier_recoverBatch",results={@Result(name="success",type="redirect",location="./pages/base/courier.html")})
	public String recoverBatch(){
		//System.out.println(ids+"-----------------");
		courierService.recoverBatch(ids);
	return SUCCESS;
	}
}
