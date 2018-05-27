package cn.itcast.bos.action.take_delivery;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.action.base.utils.BaseAction;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.OrderService;

/**
 * 有关order订单的action
 * 
 * @author lenovo
 *
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {

	private static final long serialVersionUID = -195966866969971411L;

	// 注入orderservice
	@Autowired
	private OrderService orderService;

	/**
	 * 根据订单号查询订单,1录入运单时用于回显数据
	 */
	@Action(value = "order_findByOrderNum", results = { @Result(name = "success", type = "json") })
	public String findByOrderNum() {

		Order order = orderService.findByOrderNum(model.getOrderNum());
		Map<String, Object> result = new HashMap<String, Object>();
		if (order == null) {
			// 订单号 不存在
			result.put("success", false);
		} else {
			// 订单号 存在
			result.put("success", true);
			result.put("orderData", order);
		}
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	/**
	 * 保存订单order
	 */
	@Action(value = "order_save", results = { @Result(name = "success", type = "redirect", location = "./pages/take_delivery/order.html") })
	public String save() {
		orderService.saveOrder(model);
		return SUCCESS;
	}

}
