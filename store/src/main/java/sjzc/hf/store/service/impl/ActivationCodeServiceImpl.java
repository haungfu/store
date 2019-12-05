package sjzc.hf.store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sjzc.hf.store.dao.ActivationCodeDOMapper;
import sjzc.hf.store.dataobject.ActivationCodeDO;
import sjzc.hf.store.error.BSException;
import sjzc.hf.store.service.ActivationCodeService;
@Service
public class ActivationCodeServiceImpl implements ActivationCodeService {

	@Autowired
	private ActivationCodeDOMapper activationCodeDOMapper;
	
	
	

	/**
	 * 方法说明：根据激活码查询用户id
	 * @author 皇甫振天
	 * @date 2018-18-17 10:05:06
	 * @param activationCode 激活码
	 * @return 
	 */
	@Override
	public ActivationCodeDO selectByActivationCode(String activationCode) throws Exception {
		//根据激活码查询用户id
		ActivationCodeDO activationCodeDO=activationCodeDOMapper.selectByActivationCode(activationCode);
		if(activationCodeDO==null) {
			throw new BSException("激活码错误");
		}
		
		return activationCodeDO;
	}

}
