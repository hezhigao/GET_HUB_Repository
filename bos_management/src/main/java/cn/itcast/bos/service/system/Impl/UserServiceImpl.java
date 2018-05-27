package cn.itcast.bos.service.system.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.dao.system.UserRepository;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;

/**
 * 利用apche shiro 实现用户认证和授权
 * @return
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	
	//注入dao 
	@Autowired
	private UserRepository  userRepository;
	@Autowired
	private RoleRepository  roleRepository;
	
	@Override
	public User findByUsername(String name) {
		User user = userRepository.findByUsername(name);
		return user;
	}
	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}
	/**
	 * 增加保存用戶user
	 */
	@Override
	public void save(User user, String[] roleIds) {
		// TODO Auto-generated method stub
		//保存輸入的user
		userRepository.save(user);
		
		//遍歷選擇的角色,关联user
		for (String roleId : roleIds) {
			Role role = roleRepository.findOne(Integer.parseInt(roleId));
			user.getRoles().add(role);
		}
		
	}
	
	
}
