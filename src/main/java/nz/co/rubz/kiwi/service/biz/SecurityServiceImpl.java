package nz.co.rubz.kiwi.service.biz;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import nz.co.rubz.kiwi.KiwiBiz;
import nz.co.rubz.kiwi.protocol.beans.Content;


@Service
public class SecurityServiceImpl {
	
	private Logger log = Logger.getLogger(SecurityServiceImpl.class);

	
	@KiwiBiz("send_verifycode")
	public Content sendVerifyCode(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		
		return resultContent;
	}
	@KiwiBiz("auth_verifycode")
	public Content authVerifyCode(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		
		return resultContent;
	}
	@KiwiBiz("forgot_pass")
	public Content forgotPassword(HashMap<String, Object> contentMap) {
		Content resultContent = new Content();
		
		return resultContent;
	}
	
}
