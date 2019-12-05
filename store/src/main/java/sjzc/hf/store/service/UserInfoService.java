package sjzc.hf.store.service;


import sjzc.hf.store.dataobject.UserDO;
import sjzc.hf.store.dataobject.UserModel;

public interface UserInfoService {

	 public UserModel selectByPrimaryKey(Integer id)throws Exception;
	 
	 public UserDO selectByName(String name)throws Exception;
	 
	 public int deleteByPrimaryKey(Integer id)throws Exception;

	 public int insert(UserDO record)throws Exception;

	 public int insertSelective(UserDO record)throws Exception;
	 public void signIn(UserModel user) throws Exception;
	 
	 int updateByPrimaryKeySelective(UserDO record);
}
