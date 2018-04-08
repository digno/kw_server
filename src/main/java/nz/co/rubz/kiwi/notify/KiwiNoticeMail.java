package nz.co.rubz.kiwi.notify;

import java.util.Set;

import nz.co.rubz.kiwi.protocol.beans.Content;

public class KiwiNoticeMail {

	
	private String noticeType;
	private Set<String> recipients;
	private Content content;
	public Set<String> getRecipients() {
		return recipients;
	}

	public void setRecipients(Set<String> recipients) {
		this.recipients = recipients;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
}
