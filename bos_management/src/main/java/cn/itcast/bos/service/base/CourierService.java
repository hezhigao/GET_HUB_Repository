package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.Courier;

public interface CourierService {

	void save(Courier model);


	Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable);


	void delBatch(String ids);


	void recoverBatch(String ids);

}
