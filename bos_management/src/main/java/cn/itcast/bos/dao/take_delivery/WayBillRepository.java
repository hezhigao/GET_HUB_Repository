package cn.itcast.bos.dao.take_delivery;


import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillRepository extends JpaRepository<WayBill, Integer> {
	/**
	 * 根据WayBillNum(运单号)查询WayBill(运单)
	 */
	WayBill findByWayBillNum(String wayBillNum);

}
