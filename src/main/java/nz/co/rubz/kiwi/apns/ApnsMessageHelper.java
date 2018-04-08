package nz.co.rubz.kiwi.apns;

/**
 * noti_type 0:快速语音；1快速图片；2 一般文字通知不含附件；3 一般通知含图片（有没或没有文字）；4一般通知含语音（有或没有文字）6 其它 

 */
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.notnoop.apns.APNS;
import com.notnoop.apns.PayloadBuilder;

import nz.co.rubz.kiwi.annotations.Config;
import nz.co.rubz.kiwi.utils.MyStringUtils;

@Component
public class ApnsMessageHelper {
	
	@Config("apns_msg_alert_sound")
	private String apnsSound = "default";

	private Logger log = Logger.getLogger(ApnsMessageHelper.class);
	
	public String buildApnsPayload(String notiType, String contentStr, String className) {
		try {
			PayloadBuilder payloadBuilder = APNS.newPayload();
			payloadBuilder.sound(apnsSound);
			payloadBuilder.badge(1);
			StringBuilder sb = new StringBuilder("您收到了一条来自");
			sb.append(className);
			if (StringUtils.isBlank(notiType)) {
//				String alert = "您收到了一条来自" + className + "的消息。";
				sb.append("的消息");
				payloadBuilder.alertBody(sb.toString());
			} else {
				if ("0".equals(notiType)){
					sb.append("的一段语音 ");
					sb.append("\ue142");
					payloadBuilder.alertBody(sb.toString());
					return payloadBuilder.build();
				}
				if ("1".equals(notiType)){
					sb.append("的一张图片 ");
					sb.append("\ue449");
					payloadBuilder.alertBody(sb.toString());
					return payloadBuilder.build();
				}
//				if (payloadBuilder.length() < 2000) {
					// 消息体里面能显示的内容的大小
					String content = className + " : " + contentStr;
					String canShowContent = getStringBytesValue(content, 2000 - payloadBuilder.length(), "utf-8");
					log.info("content size is :" + (2040 - payloadBuilder.length())
							+ "====apns message can show content is:" + canShowContent);
					if ("3".equals(notiType)){
						canShowContent = canShowContent + "\ue142";
					}
					if ("4".equals(notiType)){
						canShowContent = canShowContent + "\ue449";
					}
					payloadBuilder.alertBody(canShowContent);
					
//				}
			}
//			if (payloadBuilder.isTooLong()) {
//				payloadBuilder = payloadBuilder.shrinkBody();
//			}
			return payloadBuilder.build();

		} catch (Exception ex) {
			log.info("buildApnsPayload Caught Exception : ", ex);
		}
		return null;
	}
	
	public String buildIppushContent(String notiType, String contentStr, String className) {
		try {
			StringBuilder sb = new StringBuilder("您收到了一条来自");
			sb.append(className);
			if (StringUtils.isBlank(notiType)) {
				sb.append("的消息");
			} else {
				if ("0".equals(notiType)) {
					sb.append("的一段语音 ");
					return sb.toString();
				}
				if ("1".equals(notiType)) {
					sb.append("的一张图片 ");
					return sb.toString();
				}
				// 不做限制多长都可以。 notice:content.toJsonStr()
				String content = className + " : " + contentStr;
				return content;
//				String canShowContent = getStringBytesValue(content, 799, "utf-8");
//				log.info(" message can show content is:" + canShowContent);
				// if ("3".equals(notiType)){
				// canShowContent = canShowContent + "\ue142";
				// }
				// if ("4".equals(notiType)){
				// canShowContent = canShowContent + "\ue449";
				// }
//				return canShowContent;
			}

		} catch (Exception ex) {
			log.info("buildIppushContent Caught Exception : ", ex);
		}
		return null;
	}

	private static String getStringBytesValue(String str, int length, String charset)
			throws UnsupportedEncodingException {
		int strLen = str.getBytes(charset).length;
		String retVal = "";
		if (strLen <= length) { // 小于等于要截取的字符串情况
			retVal = str;
			return str;
		}

		try {
			retVal = MyStringUtils.splitChinese(3, str, length - 3);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return retVal + "...";
	}
	
	
}
