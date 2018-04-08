package nz.co.rubz.kiwi.event;

import org.springframework.context.ApplicationEvent;

import nz.co.rubz.kiwi.notify.KiwiNoticeMail;

public class NoticeEvent extends ApplicationEvent {
	 
	private static final long serialVersionUID = 725512853544446840L;
	
	public NoticeEvent(KiwiNoticeMail source) {
		super(source);
	}
	
}
