package sjzc.hf.store.service;

import sjzc.hf.store.dataobject.ActivationCodeDO;

public interface ActivationCodeService {
	//根据激活码查询出用户id
	public ActivationCodeDO selectByActivationCode(String activationCode) throws Exception;

}
