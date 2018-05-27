package cn.itcast.bos.action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
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
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;

import com.itheima.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;

@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {
	@Autowired
	private FixedAreaService fixedAreaService ;
	public void setFixedAreaService(FixedAreaService fixedAreaService) {
		this.fixedAreaService = fixedAreaService;
	}
//添加定区
	@Action(value="fixedArea_save",results={@Result(name="success",location="./pages/base/fixed_area.html",type="redirect")})
	public String save(){
		fixedAreaService.save(model);
		return SUCCESS;
	}
	//分页查询显示定区
	private int page ;
	private int rows;
	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	@Action(value="fixedArea_pageQuery" , results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page - 1, rows);
		Specification<FixedArea> specification=new Specification<FixedArea>() {
			
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> predicates=new ArrayList<Predicate>();
				if(StringUtils.isNotEmpty(model.getId())){
					Predicate p1=cb.equal(root.get("id").as(String.class), 
							model.getId());
					predicates.add(p1);
				}
				if(StringUtils.isNotEmpty(model.getCompany())){
					Predicate p2=cb.like(root.get("company").as(String.class),
							"%"+model.getCompany()+"%");
					predicates.add(p2);
				}
				if(StringUtils.isNotEmpty(model.getFixedAreaName())){
					Predicate p3=cb.like(root.get("fixedAreaName").as(String.class),
							"%"+model.getFixedAreaName()+"%");
					predicates.add(p3);
				}
				
				return cb.and(predicates.toArray(new Predicate[0]));
			}
		};
		Page<FixedArea> page=fixedAreaService.pageQuery(pageable,specification);
		pushPageDataToValueStack(page);
		return SUCCESS;
	}

	//删除的方法
	//1,接收需要删除的参数
	private String ids;
	public void setIds(String ids) {
		this.ids = ids;
	}
	@Action(value="fixedArea_delBatch" ,results={@Result(name="success",location="./pages/base/fixed_area.html",type="redirect")})
	public String delBatch (){
		String[] split = ids.split(",");
		for (String string : split) {
			fixedAreaService.delBatch(string);
		}
		
		return SUCCESS;
	}
	//异步加载未关联定去快递员
	@Action(value="fixedArea_findNoAssociationCustomers",
			results={@Result(name="success",type="json")})
	public String findNoAssociationCustomers (){
		Collection<? extends Customer> collection = WebClient
		.create("http://localhost:9002/crm_management/services//customerService/noassociationcustomers")
		.accept(MediaType.APPLICATION_JSON)
		.getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}
	//异步加载关联该定区的客户
	@Action(value="fixedArea_findHasAssociationFixedAreaCustomers",
			results={@Result(name="success",type="json")})
	public String findHasAssociationFixedAreaCustomers (){
		
		Collection<? extends Customer> collection = WebClient
		.create("http://localhost:9002/crm_management/services//customerService/associationfixedareacustomers/"+model.getId())
		.accept(MediaType.APPLICATION_JSON)
		.type(MediaType.APPLICATION_JSON)
		.getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);
		return SUCCESS;
	}
	
	//定区关联客户
	//属性驱动
	private String[] customerIds;
	public void setCustomerIds(String[] customerIds) {
		this.customerIds = customerIds;
	}
	@Action(value = "fixedArea_associationCustomersToFixedArea", results = { @Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
	public String associationCustomersToFixedArea(){
		
		 String customerIdStr = StringUtils.join(customerIds,",");
		
		WebClient
		//利用WebClient,访问webservice
		.create("http://localhost:9002/crm_management/services/customerService"
				//访问的方法和参数
				+ "/associationcustomerstofixedarea?customerIdStr="+ customerIdStr + "&fixedAreaId=" + model.getId())
				//put访问方式,没有参数
				.put(null);
		
		return SUCCESS;
	}
}
