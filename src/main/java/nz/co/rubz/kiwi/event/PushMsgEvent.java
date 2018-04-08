package nz.co.rubz.kiwi.event;

import org.springframework.context.ApplicationEvent;

import nz.co.rubz.kiwi.notify.KiwiPushMessage;

public class PushMsgEvent extends ApplicationEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 725512853544446840L;
	
	public PushMsgEvent(KiwiPushMessage source) {
		super(source);
	}
	
}
