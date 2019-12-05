package sjzc.hf.store.service;

import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

public interface SendMailService {
	
	public void sendTextMail(String to,String content,String subject);
	
	public void sendHtmlMail(String to,String content,String subject) throws MessagingException;
	
	public void sendFileMail(String to,String content,String subject,List<String> path) throws MessagingException;
	
	public void sendPicMail(String to,String content,String subject,Map<String,String> pic) throws Exception;
	
	

}
