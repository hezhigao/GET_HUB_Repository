package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.FixedAreaDao;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {
	@Autowired
	private FixedAreaDao fixedAreaDao ;
	public void setFixedAreaDao(FixedAreaDao fixedAreaDao) {
		this.fixedAreaDao = fixedAreaDao;
	}

	@Override
	public void save(FixedArea model) {
		// TODO Auto-generated method stub
	fixedAreaDao.save(model);
	}

	@Override
	public Page<FixedArea> pageQuery(Pageable pageable,
			Specification<FixedArea> specification) {
		// TODO Auto-generated method stub
		return fixedAreaDao.findAll(specification,pageable);
	}

	@Override
	public void delBatch(String string) {
		// TODO Auto-generated method stub
		fixedAreaDao.delete(string);
	}

	

}
