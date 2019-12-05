package sjzc.hf.store.dataobject;

import javax.validation.constraints.NotNull;

import sjzc.hf.store.dataobject.UserDO;

public class UserModel extends UserDO{
	@NotNull(message="密码名必填")
	private String encrptPassword;
	@NotNull(message="验证码名必填")
	private String otpCode;
	
	
	
	public String getEncrptPassword() {
		return encrptPassword;
	}

	public void setEncrptPassword(String encrptPassword) {
		this.encrptPassword = encrptPassword;
	}

	public String getOtpCode() {
		return otpCode;
	}

	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}

	
	
}


