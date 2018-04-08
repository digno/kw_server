package nz.co.rubz.kiwi.notify;

import java.util.concurrent.LinkedBlockingQueue;

import nz.co.rubz.kiwi.protocol.beans.KiwiMessage;

public class KiwiContext {


	private static LinkedBlockingQueue<KiwiMessage> notifyQueue = null;
	
	public static LinkedBlockingQueue<KiwiMessage> getNotificationQueue() {
		if (notifyQueue == null) {
			notifyQueue = new LinkedBlockingQueue<KiwiMessage>();
		}
		return notifyQueue;
	}

}
