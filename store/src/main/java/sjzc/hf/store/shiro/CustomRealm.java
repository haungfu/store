package sjzc.hf.store.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import sjzc.hf.store.dao.UserDOMapper;
import sjzc.hf.store.dao.UserPasswordDOMapper;
import sjzc.hf.store.dataobject.UserDO;
import sjzc.hf.store.dataobject.UserPasswordDO;
import sjzc.hf.store.utils.ShiroUtilsSHA;

public class CustomRealm extends AuthorizingRealm {
	
	@Autowired
	private UserDOMapper userDOMapper;
	@Autowired
	private UserPasswordDOMapper userPasswordDOMapper;
	
	// 支持什么类型的token
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof UsernamePasswordToken;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// 授权
		
		
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		
		return simpleAuthorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// 认证
		//根据用户姓名查询用户相关信息
		UserDO user=userDOMapper.selectByName((String)token.getPrincipal());
		//由用户id查询用户密码
		UserPasswordDO userPassword=userPasswordDOMapper.selectByUserId(user.getId());
		//传给认证器认证用户身份
		// 返回认证信息由父类AuthenticatingRealm进行认证
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
				user, userPassword.getEncrptPassword(), ByteSource.Util.bytes(user.getSalt()),getName());

		return simpleAuthenticationInfo;
	}

	//用来配置加密方式
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
		shaCredentialsMatcher.setHashAlgorithmName(ShiroUtilsSHA.hashAlgorithmName);
		shaCredentialsMatcher.setHashIterations(ShiroUtilsSHA.hashIterations);
		super.setCredentialsMatcher(shaCredentialsMatcher);
	}
}
