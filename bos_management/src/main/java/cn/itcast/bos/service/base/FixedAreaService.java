package cn.itcast.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.itcast.bos.domain.base.FixedArea;

public interface FixedAreaService {


	void save(FixedArea model);

	Page<FixedArea> pageQuery(Pageable pageable,
			Specification<FixedArea> specification);

	void delBatch(String string);

}
