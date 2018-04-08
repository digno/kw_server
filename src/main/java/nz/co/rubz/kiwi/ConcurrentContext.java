package nz.co.rubz.kiwi;

import java.util.concurrent.ConcurrentHashMap;

import nz.co.rubz.kiwi.server.KiwiChannel;

public class ConcurrentContext {

	private static volatile ConcurrentHashMap<String, KiwiChannel> map = null;
	private static volatile ConcurrentHashMap<Integer, KiwiChannel> cMap  = null;

	public static ConcurrentHashMap<String, KiwiChannel> getChannelMapInstance() {
		if (map == null) {
			map = new ConcurrentHashMap<String, KiwiChannel>();
		}
		return map;
	}
	
	public static ConcurrentHashMap<Integer, KiwiChannel> getAvailableChannelMapInstance() {
		if (cMap == null) {
			cMap = new ConcurrentHashMap<Integer, KiwiChannel>();
		}
		return cMap;
	}

}
