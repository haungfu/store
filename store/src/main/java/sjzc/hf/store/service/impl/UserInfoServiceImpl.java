package sjzc.hf.store.service.impl;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import sjzc.hf.store.dataobject.UserModel;
import sjzc.hf.store.dao.ActivationCodeDOMapper;
import sjzc.hf.store.dao.UserDOMapper;
import sjzc.hf.store.dao.UserPasswordDOMapper;
import sjzc.hf.store.dataobject.ActivationCodeDO;
import sjzc.hf.store.dataobject.UserDO;
import sjzc.hf.store.dataobject.UserPasswordDO;
import sjzc.hf.store.error.BSException;
import sjzc.hf.store.service.SendMailService;
import sjzc.hf.store.service.UserInfoService;
import sjzc.hf.store.utils.ShiroUtilsSHA;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	private UserDOMapper userDOMapper;
	@Autowired
	private UserPasswordDOMapper userPasswordDOMapper;
	@Autowired
	private SendMailService sendMailService;
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private ActivationCodeDOMapper activationCodeDOMapper;

	/**
	 * 方法说明：根据用户id查询用户信息
	 * 
	 * @author 皇甫振天
	 * @date 2018-12-17 10:43:06
	 * @param 用户id
	 * @return UserModel
	 */
	public UserModel selectByPrimaryKey(Integer id) {
		UserDO user = userDOMapper.selectByPrimaryKey(id);
		UserPasswordDO password = userPasswordDOMapper.selectByUserId(id);
		UserModel userModel = convertFromDataObject(user, password);
		return userModel;
	}

	/**
	 * 方法说明：用户注册
	 * 
	 * @author 皇甫振天
	 * @date 2018-12-17 10:43:06
	 * @param 用户id
	 * @return UserModel
	 */
	@Transactional
	public void signIn(UserModel user) throws Exception {
		// 昵称不可重复
		UserDO userDO = userDOMapper.selectByName(user.getName());
		if (userDO != null && !userDO.getStatus().equals("3")) {
			throw new BSException("该用户名已被注册");
		} else {
			// 添加创建时间
			user.setCreateTime(new Date());
			// 添加状态（新增的均未激活）
			user.setStatus("0");
			// 生成盐
			String salt = RandomStringUtils.randomAlphanumeric(20);

			// 添加盐
			user.setSalt(salt);

			userDOMapper.insertSelective(user);
			UserPasswordDO password = new UserPasswordDO();
			// 对密码进行加密

			user.setEncrptPassword(ShiroUtilsSHA.sha256(user.getEncrptPassword(), user.getSalt()));

			password.setEncrptPassword(user.getEncrptPassword());
			password.setUserId(user.getId());
			userPasswordDOMapper.insertSelective(password);

			// 生成验激活码
			String activationCode = UUID.randomUUID().toString().replace("-", "");
			activationCode = activationCode + new Date().getTime();
			// 存储到数据库
			ActivationCodeDO activation = new ActivationCodeDO();
			activation.setActivationCode(activationCode);
			activation.setUserId(user.getId());
			activationCodeDOMapper.insertSelective(activation);

			// 发送模板邮件
			Context context = new Context();
			// 第一个参数为模板内参数名称，第二个参数为对应的值
			context.setVariable("activation", activationCode);
			// 加载模板
			String content = templateEngine.process("activation", context);

			sendMailService.sendHtmlMail(user.getEmail(), content, "账户激活");

		}
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {

		return 0;
	}

	@Override
	public int insert(UserDO record) {

		return 0;
	}

	@Override
	public int insertSelective(UserDO record) {

		return 0;
	}

	private UserModel convertFromDataObject(UserDO user, UserPasswordDO password) {
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(user, userModel);
		userModel.setEncrptPassword(password.getEncrptPassword());
		return userModel;
	}

	@Override
	public int updateByPrimaryKeySelective(UserDO record) {
		int i = 0;
		i = userDOMapper.updateByPrimaryKeySelective(record);
		return i;
	}

	@Override
	public UserDO selectByName(String name) throws Exception {
		UserDO user = userDOMapper.selectByName(name);
		return user;
	}

}
