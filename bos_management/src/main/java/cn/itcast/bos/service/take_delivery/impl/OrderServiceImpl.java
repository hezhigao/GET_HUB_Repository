package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.AreaDao;
import cn.itcast.bos.dao.base.FixedAreaDao;
import cn.itcast.bos.dao.base.OrderRepository;
import cn.itcast.bos.dao.base.WorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.contant.Constants;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.take_delivery.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
	// 注入定区的dao,根据定区ID查询定区关联的快递员,查到则将该快递员关联订单;
	@Autowired
	private FixedAreaDao fixedAreaDao;

	// 注入Order的dao多订单进行操作
	@Autowired
	private OrderRepository orderRepository;

	// 注入AreaDao
	@Autowired
	private AreaDao areaDao;

	// 注入WorkBillRepository保存工单
	@Autowired
	private WorkBillRepository workBillRepository;

	// 注入JmsTemplate,发送短信
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;

	/**
	 * 生成订单并完成自动分单,1通过crm库查询跟地址匹配的客户;2通过输入的分区,模糊查询定区;3手动分单
	 */
	@Override
	public void saveOrder(Order order) {

		// 设置通用的参数
		order.setOrderTime(new Date());// 订单时间
		order.setStatus("1");// 设置订单状态待取件
		order.setOrderNum(UUID.randomUUID().toString());// 设置订单号,原则上不能重复
		Area recArea = areaDao.findByProvinceAndCityAndDistrict(
				order.getRecArea().getProvince(), order.getRecArea().getCity(), order.getRecArea().getDistrict());
		order.setRecArea(recArea);
	
		Area sendArea = areaDao.findByProvinceAndCityAndDistrict(
			order.getSendArea().getProvince(), order.getSendArea().getCity(), order.getSendArea().getDistrict());
		order.setSendArea(sendArea);
		// TODO Auto-generated method stub
		// 1通过WEBService 传入发件人的详细地址SendAddress,
		// 查询是否有匹配的客户,如果有将该客户关联的定去id返回
		String fixedAreaId = WebClient
				.create(Constants.CRM_MANAGEMENT_URL
						+ "/services/customerService/customer/findFixedAreaIdByAddress?Address="
						+ order.getSendAddress())
				.accept(MediaType.APPLICATION_JSON).get(String.class);

		System.out.println("找到了定区的id-------fixedAreaId=======" + fixedAreaId);
		// 通过返回的定去id判断是否可以自动分担,为空证明无法通过这个方式分担
		if (fixedAreaId != null) {
			// 获取定去id成功,下一步查询关联该定区的快递员有哪些,如果没有快递员关联该定区则无法完成自动分担
			System.out.println("找到了定区的id-------fixedAreaId======="
					+ fixedAreaId);
			// 调用DAO通过id查询定区
			FixedArea findOne = fixedAreaDao.findOne(fixedAreaId);
			// 通过定区获得定区关联的所有快递员是一个集合,将集合转成iterator(迭代)
			Iterator<Courier> iterator = findOne.getCouriers().iterator();
			// 判断这个集合中是否有元素可以迭代,如果直接迭代有可能报错,如果没有快递员关联该定区则无法完成自动分担
			if (iterator.hasNext()) {
				// 有元素则获取一个快递员
				Courier courier = iterator.next();
				// 抽取的生成订单方法
				saveOrder(order, courier);
				// 生成工单
				generateWorkBill(order);
				return;
			}

			// 如果第一种方案无法自动分担成功,将通过省市区,查询分区关键字,基于分区完成自动分担

			//Area area = new Area();
			// 根据省市区，查询符合的持久态的地区
			
			// 根据获得的地区对象获得，该地区所有的分区
			Set<SubArea> subareas = sendArea.getSubareas();
			// 遍历分区
			for (SubArea subArea : subareas) {
				// 看看订单中的地址是否包含分区中的关键字
				if (order.getSendAddress().contains(subArea.getKeyWords())) {
					// 根据分区获得定区，获得快递员
					Iterator<Courier> iterator2 = subArea.getFixedArea()
							.getCouriers().iterator();
					// 如果有快递员获取第一个
					if (iterator2.hasNext()) {
						Courier courier = iterator2.next();
						saveOrder(order, courier);
						return;
					}
				}
			}
			// 匹配辅助关键字
			for (SubArea subArea : subareas) {
				// 看看订单中的地址是否包含分区中的关键字
				if (order.getSendAddress()
						.contains(subArea.getAssistKeyWords())) {
					// 根据分区获得定区，获得快递员
					Iterator<Courier> iterator2 = subArea.getFixedArea()
							.getCouriers().iterator();
					// 如果有快递员获取第一个
					if (iterator2.hasNext()) {
						Courier courier = iterator2.next();
						saveOrder(order, courier);
						return;
					}
				}
			}
		}
		// 人工分单
		order.setOrderType("2");
		orderRepository.save(order);
	}

	/**
	 * 生成工单发送短信
	 * 
	 * @param order
	 */
	private void generateWorkBill(Order order) {
		// TODO Auto-generated method stub
		WorkBill workBill = new WorkBill();
		// private Integer id; // 主键
		workBill.setType("新"); // private String type; // 工单类型 新,追,销
		workBill.setPickstate("新单");// private String pickstate; // 取件状态
		workBill.setBuildtime(new Date());// private Date buildtime; // 工单生成时间
		// private Integer attachbilltimes; // 追单次数
		workBill.setRemark(order.getRemark());// private String remark; // 订单备注
		String smsNumber = RandomStringUtils.randomNumeric(4);
		workBill.setSmsNumber(smsNumber); // private String smsNumber; // 短信序号
		workBill.setCourier(order.getCourier());// private Courier courier;//
												// 快递员
		workBill.setOrder(order);// private Order order; // 订单
		// 将生成的实体保存到数据库
		workBillRepository.save(workBill);

		// 发送短信
		// 调用MQ发送一个消息
		jmsTemplate.send("bos_sms", new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone", order.getCourier()
						.getTelephone());
				String msg = "短信序号" + smsNumber + ",取件地址："
						+ order.getSendAddress() + ",联系人" + order.getSendName()
						+ "联系电话：" + order.getSendMobile() + ",给快递员捎话："
						+ order.getSendMobileMsg();
				mapMessage.setString("msg", msg);
				return mapMessage;
			}
		});

		// 修改订单状态为已通知
		workBill.setPickstate("已通知");
	}

	/**
	 * 自动分单保存
	 */
	private void saveOrder(Order order, Courier courier) {
		// 将快递员关联到订单上
		order.setCourier(courier);
		// 设置分单类型1为自动分单,2为人工分单
		order.setOrderType("1");
		// 保存订单
		orderRepository.save(order);
	}

	@Override
	public Order findByOrderNum(String orderNum) {
		// TODO Auto-generated method stub
		return orderRepository.findByOrderNum(orderNum);
	}

}
