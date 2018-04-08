package nz.co.rubz.kiwi.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioClient1 {

	// 客户端使用的selector
	private Selector selector;
	
	// 客户端需要连接的服务器地址
	private InetSocketAddress address ; 
	
	// 客户端发送，接受消息的缓冲区
	private ByteBuffer readBuffer;
	
	private ByteBuffer writeBuffer;
	
	
	
	public void initClient(String ip,int port){
		address  = new InetSocketAddress(ip, port);
		readBuffer = ByteBuffer.allocate(4096);
		writeBuffer = ByteBuffer.allocate(4096);
		try {
			SocketChannel channel = SocketChannel.open();
			channel.configureBlocking(false);
			selector = Selector.open();
			channel.register(selector, SelectionKey.OP_CONNECT);
			channel.connect(address);
		} catch (IOException e) {
			 System.out.println(" initClient : " + e.getMessage());
		}
	}
	
	public void reviceData(){
		
	}
	
	public void listen(){
		while(true){
			try {
				selector.select();
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while(it.hasNext()){
					SelectionKey key = it.next();
					it.remove();
					handleKey(key);
				}
			} catch (IOException e) {
				System.out.println("client listen : " + e.getMessage());
			}
			
		}
	}
	
	private void handleKey(SelectionKey key) {
		
		if (key.isConnectable()){
			SocketChannel clientChannel = (SocketChannel) key.channel();
			if (clientChannel.isConnectionPending()){
				try {
					 if (clientChannel.finishConnect()){
						 writeBuffer.clear();
						 String text = new String ("你好，服务器。".getBytes());
						 writeBuffer.put(text.getBytes());
						 writeBuffer.flip();
						 clientChannel.write(writeBuffer);
						 clientChannel.register(selector, SelectionKey.OP_READ);
					 }
				} catch (IOException e) {
					System.out.println("handleKey -> connectEvent : " + e.getMessage()); 
				}
			}
		}else if (key.isReadable()){
			SocketChannel clientChannel = (SocketChannel) key.channel();
			try {
				readBuffer.clear();
				int count = clientChannel.read(readBuffer);
				if(count>0){
					System.out.println(new String(readBuffer.array(),0,count));
				}
				clientChannel.register(selector, SelectionKey.OP_WRITE);
			} catch (IOException e) {
				System.out.println("handleKey -> readEvent : " + e.getMessage()); 
			}
			
		}
		else if (key.isWritable()){
			SocketChannel clientChannel = (SocketChannel) key.channel();
			writeBuffer.clear();
			String text = "客户端 -> 服务端" + System.currentTimeMillis();
			writeBuffer.put(text.getBytes());
			writeBuffer.flip();
			try {
				clientChannel.write(writeBuffer);
				clientChannel.register(selector, SelectionKey.OP_READ);
			} catch (IOException e) {
				System.out.println("handleKey -> writeEvent : " + e.getMessage()); 
			}
			
		}	
		
	}

	public static void main(String[] args) {
		NioClient1 client = new NioClient1();
		client.initClient("127.0.0.1", 22222);
		client.listen();
	}
}
