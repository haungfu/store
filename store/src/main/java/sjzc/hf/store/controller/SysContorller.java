package sjzc.hf.store.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sjzc.hf.store.dataobject.ActivationCodeDO;
import sjzc.hf.store.dataobject.UserDO;
import sjzc.hf.store.dataobject.UserModel;
import sjzc.hf.store.error.BSException;
import sjzc.hf.store.response.R;
import sjzc.hf.store.service.UserInfoService;
import sjzc.hf.store.service.impl.ActivationCodeServiceImpl;
import sjzc.hf.store.validator.ValidatorImpl;

@RestController
@RequestMapping("/sys")
//@CrossOrigin//解决跨域(通过地址项目地址访问，根路径相同无跨域)
public class SysContorller extends BaseController {

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private ActivationCodeServiceImpl activationCodeServiceImpl;

	
	
	/**
	 * @Description: 用户登录
	 * @author: 皇甫振天
	 * @param user password
	 * @return CommonReturnType
	 * @throws Exception 
	 * @throws @version V_1.0.0
	 * @date: 2018-12-17 10:27:29
	 *
	 *        --------------------------Modification
	 *        History-------------------------------------------* Date Author
	 *        Version Description
	 *        ------------------------------------------------------------------------------------------*
	 *        2018-12-17 10:27:29 皇甫振天 V_1.0.1 修改原因
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.GET })
	public R login(HttpServletRequest request, String name, String encrptPassword) throws Exception {

		logger.info("用户：" + name + "正在进行登陆");

		if (name == null || encrptPassword == null) {

			return R.error("账号或密码错误");
		}
		//判断用户状态
		UserDO user=userInfoService.selectByName(name);
		if(user.getStatus().equals("0")) {
			return R.error("账号未激活");
		}else if(user.getStatus().equals("2")) {
			return R.error("账号不可用");
		}else if(user.getStatus().equals("3")) {
			return R.error("账号不存在");
		}
		
		// 创建token令牌，记录用户认证的身份和凭证即账号和密码
		UsernamePasswordToken token = new UsernamePasswordToken(name, encrptPassword);
		Subject subject = SecurityUtils.getSubject();

		try {
			subject.login(token);
		} catch (UnknownAccountException e) {
			return R.error(e.getMessage());
		} catch (IncorrectCredentialsException e) {
			logger.error("账号或密码不能为空");
			return R.error("账号或密码不正确");
		} catch (LockedAccountException e) {
			logger.error("账号已被锁定,请联系管理员");
			return R.error( "账号已被锁定,请联系管理员");
		} catch (AuthenticationException e) {
			logger.error("账户验证失败");
			return R.error("账户验证失败");
		}
		return R.ok("登陆成功");
	}

	/**
	 * @Description: 用户注册（直接注册）
	 * @author: 皇甫振天
	 * @param UserModel 用户领域模型
	 * @return CommonReturnType
	 * @throws @version V_1.0.0
	 * @date: 2018-12-17 10:27:29
	 *
	 *        --------------------------Modification
	 *        History-------------------------------------------* Date Author
	 *        Version Description
	 *        ------------------------------------------------------------------------------------------*
	 *        2018-12-17 10:27:29 皇甫振天 V_1.0.1 修改原因
	 */
	@RequestMapping(value = "/signIn", method = { RequestMethod.POST })
	public R signIn(@ModelAttribute UserModel user, HttpServletRequest request) throws Exception {

		// 调用自己封装的校验方法进行校验

		ValidatorImpl.validata(user);
		

		String sessionOtpCode = (String) request.getSession().getAttribute(user.getTelphone());
		if (!sessionOtpCode.equals(user.getOtpCode())) {

			throw new BSException("验证码错误");
		}
		userInfoService.signIn(user);

		return R.ok("注册成功");
	}

	/**
	 * @Description: 发送验证码
	 * @author: 皇甫振天
	 * @param telphone 用户手机号
	 * @return CommonReturnType
	 * @throws @version V_1.0.0
	 * @date: 2018-12-17 10:27:29
	 *
	 *        --------------------------Modification
	 *        History-------------------------------------------* Date Author
	 *        Version Description
	 *        ------------------------------------------------------------------------------------------*
	 *        2018-12-17 10:27:29 皇甫振天 V_1.0.1 修改原因
	 */
	@RequestMapping(value = "/sendMassage", method = { RequestMethod.POST })
	public R sendMessage(String telphone) throws Exception {
		String randomCode = null;

		// 生成验证码
		Random random = new Random();
		Integer randomInt = random.nextInt(99999);
		randomInt += 1000;
		randomCode = randomInt.toString();

		// 手机号验证码绑定
		session.setAttribute(telphone, randomCode);

		// 第三方接口发送验证码
		// 第三方接口暂无

		return R.ok(randomCode);
	}

	/**
	 * @Description: 账户激活
	 * @author: 皇甫振天
	 * @param activationCode 激活码
	 * @return 
	 * @throws @version V_1.0.0
	 * @date:  2018-12-18 9:50:29 
	 *
	 *        --------------------------Modification
	 *        History-------------------------------------------* Date Author
	 *        Version Description
	 *        ------------------------------------------------------------------------------------------*
	 *        2018-12-18 9:50:29 皇甫振天 V_1.0.1 修改原因
	 */
	@RequestMapping(value = "/activation", method = { RequestMethod.GET })
	public void activation(String activationCode,HttpServletResponse response,HttpServletRequest request) throws Exception {

		//调用方法去根据激活码查询，并根据状态做相应操作
		ActivationCodeDO activationCodeDO=activationCodeServiceImpl.selectByActivationCode(activationCode);
		//查询用户
		UserModel userModel=userInfoService.selectByPrimaryKey(activationCodeDO.getUserId());
		
		//验证用户状态
		String status=userModel.getStatus();
		if(status.equals("0")) {
			//将用户激活
			userModel.setStatus("1");
			userInfoService.updateByPrimaryKeySelective(userModel);
			request.setAttribute("meg", "恭喜您激活成功");
		}else {
			//提示用户该账户已被激活
			request.setAttribute("meg", "该账户已被成功激活");

		
		}
		response.sendRedirect("localhost:8090/message.html");
	}
}
