package cn.itcast.bos.service.base.impl;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierDao;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService{
	//注入DAO
	@Autowired
	private CourierDao courierDao ;
	public void setCourierDao(CourierDao courierDao) {
		this.courierDao = courierDao;
	}
//保存操作,可添加,和修改
	@Override
	@RequiresPermissions("Courier_add")
	public void save(Courier model) {
		// TODO Auto-generated method stub
		courierDao.save(model);
	}
//分页查询所有数据
	@Override
	@RequiresPermissions("Courier_add")
	public Page<Courier> findPageData(Specification<Courier> specification,Pageable pageable) {
		// TODO Auto-generated method stub
		return courierDao.findAll(specification,pageable);
	}
//修改快递员状态为作废
	@Override
	public void delBatch(String ids) {
		// TODO Auto-generated method stub
		String[] split = ids.split(",");
		for (int i = 0; i < split.length; i++) {
			int id=Integer.parseInt(split[i]);
			courierDao.updateDelTag(id);
		}
	}
	//恢复快递员
	@Override
	public void recoverBatch(String ids) {
		// TODO Auto-generated method stub
		String[] split = ids.split(",");
		for (int i = 0; i < split.length; i++) {
			int id=Integer.parseInt(split[i]);
			courierDao.updateRecoverTag(id);
		}
	}


}
