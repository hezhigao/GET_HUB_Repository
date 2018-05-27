package cn.itcast.bos.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.action.base.utils.BaseAction;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;
/**
 *角色管理
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role> {
	
	//注入service
	@Autowired
	private RoleService roleService ;
	
	
	
	/**
	 * 查询所有角色
	 */
	@Action(value="role_list",results={@Result(name="success",type="json")})
	public String list(){
		List<Role> list=roleService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	/**
	 * 添加角色
	 */
	//属性注入添加角色时调教的权限和菜单
		private String menuIds;//菜单id的集合,号分割
		
	private String[] permissionIds;// 权限复选框选中的数据

		public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	public void setPermissionIds(String[] permissionIds) {
		this.permissionIds = permissionIds;
	}

	@Action(value="role_save",results={@Result(name="success",type="redirect",location="/pages/system/role.html")})
	public String save(){
		roleService.save(model,permissionIds,menuIds);
		return SUCCESS;
	}
}
