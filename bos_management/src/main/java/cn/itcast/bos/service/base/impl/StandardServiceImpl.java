package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardDao;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;


//springע��֤����Service��
//��������
@Service
@Transactional
public class StandardServiceImpl implements StandardService {
	//ע��dao��
	@Autowired(required=true)
	private StandardDao standardDao;
	public void setStandardDao(StandardDao standardDao) {
	this.standardDao = standardDao;
}

	@Override
	@CacheEvict(value="standard",allEntries=true)
	public void save(Standard standard) {
		// TODO Auto-generated method stub
		standardDao.save(standard);
	}

	@Override
	public Page<Standard> findPageData(Pageable pageable) {
		// TODO Auto-generated method stub
		return standardDao.findAll(pageable);
	}

	@Override
	@Cacheable("standard")
	public List<Standard> findAll() {
		// TODO Auto-generated method stub
		return standardDao.findAll();
	}


}
