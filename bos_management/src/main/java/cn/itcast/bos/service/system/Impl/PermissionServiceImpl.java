package cn.itcast.bos.service.system.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;

/**
 * 获取权限
 * @return
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

	
	//注入dao
	@Autowired
	private PermissionRepository permissionRepository;
	
	/**
	 * 根据登陆的用户获取权限
	 */
	@Override
	public List<Permission> findByUser(User user) {
		// TODO Auto-generated method stub
		//设置超级管理员
		if(user.getUsername().equals("admin")){
			//为超级管理员赋予最大权限
			return permissionRepository.findAll();
		}
		
		return permissionRepository.findByUser( user.getId());
	}

	/**
	 * 查询所有权限
	 */
	@Override
	public List<Permission> findAll() {
		// TODO Auto-generated method stub
		return permissionRepository.findAll();
	}

	/**
	 * 添加保存权限
	 */
	@Override
	public void save(Permission permission) {
		// TODO Auto-generated method stub
		permissionRepository.save(permission);
	}

	
	
	
	
}
