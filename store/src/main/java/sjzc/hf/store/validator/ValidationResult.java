package sjzc.hf.store.validator;

import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.buf.StringUtils;

public class ValidationResult {
	// 结果是否有错的标识
	private boolean hasErrors = false;

	// 错误集合
	private Map<String, String> errorMsgMap = new HashMap<String, String>();

	public boolean isHasErrors() {
		return hasErrors;
	}

	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}

	public Map<String, String> getErrorMsgMap() {
		return errorMsgMap;
	}

	public void setErrorMsgMap(Map<String, String> errorMsgMap) {
		this.errorMsgMap = errorMsgMap;
	}

	// 格式化输出异常信息
	public String getErrorMsg() {

		return StringUtils.join(errorMsgMap.values(), ',');
	}

}
