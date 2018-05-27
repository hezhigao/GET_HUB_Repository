package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.take_delivery.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	Order findByOrderNum(String orderNum);

}
