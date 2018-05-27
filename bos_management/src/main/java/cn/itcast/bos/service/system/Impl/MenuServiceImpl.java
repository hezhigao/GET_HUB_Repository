package cn.itcast.bos.service.system.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;

@Service
@Transactional
public class MenuServiceImpl implements MenuService {
	// 注入dao
	@Autowired
	private MenuRepository menuRepository;

	/**
	 * 获取所有菜单列表
	 */
	@Override
	public List<Menu> findAll() {
		// TODO Auto-generated method stub
		return menuRepository.findAll();
	}

	/**
	 * 添加菜單
	 * 
	 * @return
	 */
	@Override
	public void save(Menu menu) {
		// 防止用户没有选中 父菜单
		if (menu.getParentMenu() != null && menu.getParentMenu().getId() == 0) {
			menu.setParentMenu(null);
		}
		// 调用DAO
		menuRepository.save(menu);
	}

	/**
	 * 根据不同的登陆用户显示不同的菜单列表
	 */
	@Override
	public List<Menu> findByUser(User user) {
		// TODO Auto-generated method stub
		if(user.getUsername().equals("admin")){
			return menuRepository.findAll();
		}
		
		return menuRepository.findByUser(user.getId());
	}

}
