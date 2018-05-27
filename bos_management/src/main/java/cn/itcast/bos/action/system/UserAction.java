package cn.itcast.bos.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.action.base.utils.BaseAction;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User>{

	private static final long serialVersionUID = -3929659011867033255L;

	
	/**
	 * 利用apche shiro 实现用户登录
	 * @return
	 */
	@Action(value = "user_login", results = {
			@Result(name = "login", type = "redirect", location = "login.html"),
			@Result(name = "success", type = "redirect", location = "index.html") })
	public String login(){
		//获取Subject
		Subject subject = SecurityUtils.getSubject();
		//封装token
		AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), model.getPassword());
		//
		try {
			subject.login(token);
			
			return SUCCESS;
		} catch (Exception e) {
			// TODO: handle exception
			return LOGIN;
		}
	}
	
	/**
	 * 利用apche shiro 实现用户退出
	 * @return
	 */
	@Action(value = "user_logout", results = {
			@Result(name = "success", type = "redirect", location = "login.html") })
	public String logout(){
	//获得subject
		Subject subject = SecurityUtils.getSubject();
		//退出登录
		subject.logout();
	return SUCCESS;
	}
	
	//注入service
	@Autowired
	private UserService userService;
	/**
	 * user_list
	 */
	@Action(value="user_list",results={@Result(name="success",type="json")})
	public String list(){
		List<User> list = userService.findAll();
		ActionContext.getContext().getValueStack().push(list);
		return SUCCESS;
	}
	
	/**
	 * 新增保存用戶user
	 */
	//接收參數roleIds
	private String[] roleIds;
	
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
@Action(value="user_save" , results={@Result(name="success",type="redirect",location="/pages/system/userlist.html")})
	public String save(){
	userService.save(model,roleIds);
	
	return SUCCESS;
	}
}
