package cn.itcast.bos.service.take_delivery;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.struts2.convention.annotation.Action;

import cn.itcast.bos.domain.take_delivery.Order;

public interface OrderService {

	/**
	 * 客户端通过webService调用,实现添加订单
	 * @param order
	 */
	@Path("/order")
	@POST
	@Consumes({"application/xml","application/json"})
	public void saveOrder(Order order);

	
	/**
	 * 根据订单号查询订单,1录入运单时用于回显数据
	 */
	
	public Order findByOrderNum(String orderNum);
}
