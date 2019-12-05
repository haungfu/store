package sjzc.hf.store.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import sjzc.hf.store.error.BSException;


@Component
public class ValidatorImpl implements InitializingBean {
	public static Logger logger = LoggerFactory.getLogger(ValidatorImpl.class);
	private static Validator validator;

	// 该方法用来校验
	public static void validata(Object bean) throws Exception {
		ValidationResult result = new ValidationResult();
		// 进行校验
		Set<ConstraintViolation<Object>> set = validator.validate(bean);

		// 判断是否有错
		if (set.size() > 0) {

			result.setHasErrors(true);
			// 便利并将错误信息进行存储
			set.forEach(s -> {
				// 得到错误信息
				String msg = s.getMessage();
				// 得到错误的字段名
				String name = s.getPropertyPath().toString();
				// 将名称与信息存入result中
				result.getErrorMsgMap().put(name, msg);
			});

		}

		if (result.isHasErrors()) {
			logger.error(result.getErrorMsg());
			throw new BSException(result.getErrorMsg());
		}
	}

	@Override
	// 该方法实现在类生成后调用
	public void afterPropertiesSet() throws Exception {
		// 该方法实现在类生成后调用

		// 生成校验器
		validator = Validation.buildDefaultValidatorFactory().getValidator();

	}

}
