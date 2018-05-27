package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;

public interface MenuService {

	List<Menu> findAll();

	List<Menu> findByUser(User user);

	/**
	 * 添加菜單
	 * @return
	 */
	void save(Menu model);

}
