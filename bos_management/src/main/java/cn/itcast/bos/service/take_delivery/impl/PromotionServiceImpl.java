package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.PromotionRepository;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;
@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
//注入PromotionRepository
	@Autowired
	private PromotionRepository promotionRepository;
	@Override
	public void save(Promotion promotion) {
		// TODO Auto-generated method stub
		promotionRepository.save(promotion);
	}
	//查询所有的活动
	@Override
	public Page<Promotion> findPageData(Pageable pageable) {
		// TODO Auto-generated method stub
		return promotionRepository.findAll(pageable);
	}
	
	// 根据page和rows 返回分页数据//提供WEBService服务的接口的实现
	@Override
	public PageBean<Promotion> findPageData(int page, int rows) {
		// TODO Auto-generated method stub
		//创建查询条件对象参数
		Pageable pageable=new PageRequest(page-1, rows);
		//根据条件查询数据并封装
		Page<Promotion> pageData = promotionRepository.findAll(pageable);
		//创建返回值类型//并给类型对象赋值
		PageBean<Promotion> pageBean=new PageBean<Promotion>();
		//从page对象中获取对应的参数封装到pageBean
		pageBean.setTotalcount(pageData.getTotalElements());
		pageBean.setPageData(pageData.getContent());
		return pageBean;
	}
	
	/**
	 * 根据id查询相应的活动数据,
	 */
	@Override
	public Promotion findById(Integer id) {
		// TODO Auto-generated method stub
		return promotionRepository.findOne(id);
	}
	
	/**
	 * 定時任務,定時執行这个任务,将过期的,活动状态改为2(已结束)
	 */
	@Override
	public void updateStatus(Date date) {
		// TODO Auto-generated method stub
		promotionRepository.updateStatus(date);
	}

}
