package cn.itcast.bos.service.take_delivery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.take_delivery.WayBill;

public interface WayBillService {

	Page<WayBill> findPageData(WayBill model, Pageable pageable);

	void save(WayBill model);
/**
 * 根据WayBillNum(运单号)查询WayBill(运单)
 */
	WayBill findByWayBillNum(String wayBillNum);

}
