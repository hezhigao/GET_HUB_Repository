package cn.itcast.bos.service.system.Impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	// 注入dao
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private MenuRepository menuRepository;
	/**
	 * 根据用户获取所属角色
	 */
	@Override
	public List<Role> findByUser(User user) {
		// 设置超级管理员
		if ("admin".equals(user.getUsername())) {
			// 为超级管理员赋予所有角色
			return roleRepository.findAll();
		}
		return roleRepository.findByUser(user.getId());
	}

	/**
	 * 查询所有角色
	 */
	@Override
	public List<Role> findAll() {
		// TODO Auto-generated method stub
		return roleRepository.findAll();
	}

	/**
	 * 添加保存角色
	 */
	
	@Override
	public void save(Role role, String[] permissionIds, String menuIds) {
		// TODO Auto-generated method stub
		//保存role将瞬时对象转为持久对象,对持久对象操作会自动更新数据库
		roleRepository.save(role);
		
		//判断permissionIds是否为空,
		if(permissionIds!=null){
			//遍历数组
			for (String permissionid : permissionIds) {
				//根据结果查询数据库查询到permission,然后关联role的permissions字段
				Permission permission = permissionRepository.findOne(Integer.parseInt(permissionid));
				//获取集合添加权限
				role.getPermissions().add(permission);
			}
		}
		
		//判断menuIds是否为空
		if(StringUtils.isNotBlank(menuIds)){
			//切割
			String[] split = menuIds.split(",");
			for (String menuId : split) {
				//查询
				Menu menu = menuRepository.findOne(Integer.parseInt(menuId));
			//
				role.getMenus().add(menu);
			}
			
		}
	}

}
