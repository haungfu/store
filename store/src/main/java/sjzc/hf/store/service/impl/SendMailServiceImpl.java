package sjzc.hf.store.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import sjzc.hf.store.service.SendMailService;
@Service
public class SendMailServiceImpl implements SendMailService {

	@Value("${spring.mail.username}")
	private String from;
	@Autowired
	private JavaMailSender mailSender;
	
	
	Logger logger=LoggerFactory.getLogger(SendMailServiceImpl.class);

	/**
	 * 方法说明：发送文本邮件
	 * @author 皇甫振天
	 * @date 2018-12-17 14:28:06
	 * @param to 接受者,content 邮件内容，subject 主题
	 * @return 
	 */
	@Override
	public void sendTextMail(String to, String content, String subject) {
		logger.info(from+"向邮箱"+to+"发送:"+subject+"邮件，邮件内容："+content);
		// 发送文本邮件
		
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(to);
			mail.setSubject(subject);
			mail.setText(content);
			mail.setFrom(from);
			mailSender.send(mail);
			logger.info("邮件发送成功");

	}
	
	/**
	 * 方法说明：发送HTML邮件(模板邮件)
	 * @author 皇甫振天
	 * @date 2018-12-17 14:28:06
	 * @param to 接受者,content 邮件内容，subject 主题
	 * @return 
	 * @throws MessagingException 
	 */
	@Override
	public void sendHtmlMail(String to, String content, String subject) throws MessagingException {
		// 发送HTML邮件
		

		logger.info(from+"向邮箱"+to+"发送:"+subject+"邮件，邮件内容："+content);
		
		
			MimeMessage message = mailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(from);
			helper.setTo(to);
			helper.setText(content, true);
			helper.setSubject(subject);
			mailSender.send(message);
			logger.info("邮件发送成功");
		
		

	}
	
	/**
	 * 方法说明：发送附件邮件
	 * @author 皇甫振天
	 * @date 2018-12-17 14:28:06
	 * @param to 接受者,content 邮件内容，subject 主题，List<String> path 附件地址列表
	 * @return 
	 * @throws MessagingException 
	 */
	@Override
	public void sendFileMail(String to, String content, String subject, List<String> path) throws MessagingException {
		logger.info(from+"向邮箱"+to+"发送:"+subject+"邮件，邮件内容："+content);
		// 附件邮件
		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper;
		
			helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setFrom(from);
			helper.setSubject(subject);
			helper.setText(content, true);
			for (String p : path) {
				FileSystemResource file = new FileSystemResource(new File(p));
				String fileName = file.getFilename();
				helper.addAttachment(fileName, file);
			}
			mailSender.send(message);
			logger.info("邮件发送成功");
		

	}

	/**
	 * 方法说明：发送图片邮件
	 * @author 皇甫振天
	 * @date 2018-12-17 14:28:06
	 * @param to 接受者,content 邮件内容，subject 主题,Map<String,String> id与图片地址的键值对
	 * @return 
	 * @throws Exception 
	 */
	@Override
	public void sendPicMail(String to, String content, String subject, Map<String,String> pic) throws Exception {
		logger.info(from+"向邮箱"+to+"发送:"+subject+"邮件，邮件内容："+content);
		// 图片邮件(html)
		MimeMessage message = mailSender.createMimeMessage();

		
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setFrom(from);
			helper.setSubject(subject);
			helper.setText(content, true);
			
			for (String p : pic.keySet()) {
				FileSystemResource file = new FileSystemResource(new File(pic.get(p)));
				
				helper.addInline(p, file);
			}
			mailSender.send(message);
			logger.info("邮件发送成功");
	}

	
}
