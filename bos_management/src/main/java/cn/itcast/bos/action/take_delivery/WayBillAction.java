	package cn.itcast.bos.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.action.base.utils.BaseAction;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;

/**
 * 运单Action
 * 
 * @author lenovo
 *
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {

	private static final long serialVersionUID = 2702471614749509107L;

	private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);
	// 注入Service
	@Autowired
	private WayBillService wayBillService;
	
	
	/**
	 * 添加運單
	 */
	@Action(value = "waybill_save", results = { @Result(name = "success", type = "json") })
	public String save() {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			// 去除 没有id的order对象
			if (model.getOrder() != null
					&& (model.getOrder().getId() == null || model.getOrder()
							.getId() == 0)) {
				model.setOrder(null);
			}

			wayBillService.save(model);
			// 保存成功
			result.put("success", true);
			result.put("msg", "保存运单成功！");
			LOGGER.info("保存运单成功，运单号：" + model.getWayBillNum());
		} catch (Exception e) {
			e.printStackTrace();
			// 保存失败
			result.put("success", false);
			result.put("msg", "保存运单失败！");
			LOGGER.error("保存运单失败，运单号：" + model.getWayBillNum(), e);
		}
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	/**
	 * 分页显示Waybill运单
	 */
	@Action(value = "waybill_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 无条件查询
//		Pageable pageable = new PageRequest(page - 1, rows, new Sort(
//				new Sort.Order(Sort.Direction.DESC, "id")));
		Pageable pageable=new PageRequest(page-1, rows);
		// 调用业务层进行查询
		Page<WayBill> pageData = wayBillService.findPageData(model, pageable);
		// 压入值栈返回
		
		pushPageDataToValueStack(pageData);
		
		return SUCCESS;
	}
	
	/**
	 * 根据运单号查询运单1,运单录入时如果有这个运单则数据回显到页面
	 */
	@Action(value="wayBill_findByWayBillNum",results={@Result(name="success",type="json")})
	public String findByWayBillNum(){
		WayBill wayBil=	wayBillService.findByWayBillNum(model.getWayBillNum());
		Map<String, Object> result = new HashMap<String, Object>();
		if (wayBil == null) {
			// 订单号 不存在
			result.put("success", false);
		} else {
			// 订单号 存在
			result.put("success", true);
			result.put("wayBillData", wayBil);
		}
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}
}
