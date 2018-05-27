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
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.PermissionService;
/**
 * 权限操作
 * @author lenovo
 *
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PermissionAction extends BaseAction<Permission> {
	private static final long serialVersionUID = -4061475786171728978L;
	
	//注入service
	@Autowired
	private PermissionService PermissionService ;
	
	/**
	 * 查询所有权限
	 * @return
	 */
	@Action(value="permission_list",results={@Result(name="success",type="json")} )
	public String list(){
		//查询数据
		List<Permission> list=PermissionService.findAll();
		//将结果返回,ValueStack会将栈顶元素转为json
	ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	/**
	 * 添加权限
	 */
	@Action(value="permission_save",results={@Result(name="success",type="redirect",location="/pages/system/permission.html")})
	public String save(){
		//保存输入的数据
		PermissionService.save(model);
		return SUCCESS;
	}
}
