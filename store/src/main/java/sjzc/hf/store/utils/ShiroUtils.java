package sjzc.hf.store.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import sjzc.hf.miaosha.model.UserModel;
import sjzc.hf.store.error.BusinessException;
import sjzc.hf.store.error.EmBusinessError;


/**
 * Shiro工具类
 * 
 * @author asiainfo
 *  
 * @date 2016年11月12日 上午9:49:19
 */
public class ShiroUtils {
	public static Session getSession() {
		return SecurityUtils.getSubject().getSession();
	}

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	public static UserModel getUserEntity() {
		return (UserModel)SecurityUtils.getSubject().getPrincipal();
	}

	public static Integer getUserId() {
		return getUserEntity().getId();
	}
	
	public static void setSessionAttribute(Object key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static Object getSessionAttribute(Object key) {
		return getSession().getAttribute(key);
	}

	public static boolean isLogin() {
		return SecurityUtils.getSubject().getPrincipal() != null;
	}

	public static void logout() {
		SecurityUtils.getSubject().logout();
	}
	
	public static String getKaptcha(String key) throws Exception {
		Object kaptcha = getSessionAttribute(key);
		if(kaptcha == null){
			throw new BusinessException(EmBusinessError.LOGIN_ERROR,"验证码已失效");
		}
		getSession().removeAttribute(key);
		return kaptcha.toString();
	}

}
