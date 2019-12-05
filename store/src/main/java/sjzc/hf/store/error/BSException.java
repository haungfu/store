package sjzc.hf.store.error;

public class BSException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//默认异常编码
	private int errorCode=500;
	//默认异常信息
	private String errorMsg="操作失败";
	
	//用于自己定义异常信息和异常编码
	public BSException(int errorCode,String errorMsg) {
		super();
		this.errorCode=errorCode;
		this.errorMsg=errorMsg;
		
	}
	//使用默认异常编码，自定义异常信息
	public BSException(String errorMsg) {
		super();
		
		this.errorMsg=errorMsg;
		
	}
	//使用默认异常编码和异常信息
	public BSException() {
		super();
		
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
