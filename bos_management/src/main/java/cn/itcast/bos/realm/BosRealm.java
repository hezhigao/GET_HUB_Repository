package cn.itcast.bos.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.service.system.UserService;

public class BosRealm extends AuthorizingRealm{
	
	//注入service
	@Autowired
	private UserService userService;//用户
	
	@Autowired
	private RoleService RoleService;//角色
	@Autowired
	private PermissionService permissionService;//权限
	
/**
 * 授权
 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		System.out.println("授权方法执行了");
		//返回类型是接口需要创建返回参数的实现类
		SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
		//获得subject
		Subject subject = SecurityUtils.getSubject();
		//获得登陆的用户
		User user = (User) subject.getPrincipal();
		//根据登陆的用户查询角色
		List<Role> roles=RoleService.findByUser(user);
		//多对多遍历集合,
	for (Role role : roles) {
		authorizationInfo.addRole(role.getKeyword());
	}
	//根据登陆的用户查询权限
	List<Permission> permissions=permissionService.findByUser(user);
	//遍历权限
	for (Permission permission : permissions) {
		authorizationInfo.addRole(permission.getKeyword());
	}
		return authorizationInfo;
	}
	
	
/**
 * 认证
 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		System.out.println("认证方法执行了");
		//将传过来的token转成UsernamePasswordToken
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		//根据用户名获取用户对象,如果为空证明没有这个用户,如果不为空则用输入的密码和返回用户的密码作对比
		User user = userService.findByUsername(usernamePasswordToken.getUsername());
		if(user==null){
			//用户名有误
			return null;
		}else{
			//用户名存在
			//
			return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
		}
		
	}

	

}
