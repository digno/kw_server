package nz.co.rubz.kiwi.server;

import io.netty.channel.Channel;

public class KiwiChannel {

	// 通道ID
	private Integer channelId;
	
	// 通道
	private Channel channel;
	
	private long lastAliveTime = System.currentTimeMillis();

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public boolean isActive() {
		return channel.isActive() && channel.isWritable();
	}

	
	public long getLastAliveTime() {
		return lastAliveTime;
	}

	public void setLastAliveTime(long lastAliveTime) {
		this.lastAliveTime = lastAliveTime;
	}

}
