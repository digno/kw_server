package nz.co.rubz.kiwi.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioClient {

	private static int i = 0;
	// 缓冲区大小
	private static int blockSize = 4096;

	// 服务端口号
	private int port = 22222;

	// 发送段的缓冲区
	private static ByteBuffer sendBuffer = ByteBuffer.allocate(blockSize);

	// 接收端的缓冲区
	private static ByteBuffer reviceBuffer = ByteBuffer.allocate(blockSize);

	private final static InetSocketAddress address = new InetSocketAddress("127.0.0.1", 22222);

	public static void main(String[] args) throws IOException {
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_CONNECT);
		channel.connect(address);

		String reviceText = "";
		String sendText = "";
		SocketChannel client = null;
		Set<SelectionKey> keys;
		Iterator<SelectionKey> it;

		while (true) {
			selector.select();
			keys = selector.selectedKeys();
			it = keys.iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				if (key.isConnectable()) {
					System.out.println("Client connecting to Server..");
					client = (SocketChannel) key.channel();
					if (client.isConnectionPending()) {
						client.finishConnect();
						System.out.println("Client connected Server");
						sendBuffer.clear();
						sendBuffer.put("你好，服务器！".getBytes());
						sendBuffer.flip();
						client.write(sendBuffer);

					}
					client.register(selector, SelectionKey.OP_READ);

				}
				if (key.isReadable()) {
					System.out.println("Client channel can read.");
					client = (SocketChannel) key.channel();
					reviceBuffer.clear();
					int count = client.read(reviceBuffer);
					if (count > 0) {
						reviceText = new String(reviceBuffer.array(), 0, count);
						System.out.println("revice Text :" + reviceText + " from server.");
						client.register(selector, SelectionKey.OP_WRITE);
					}
					
				}
				if (key.isWritable()) {
					System.out.println("client Channel can write");
					client = (SocketChannel) key.channel();
					sendText = "客户端发给服务器  ->  " + i++;
					sendBuffer.clear();
					sendBuffer.put(sendText.getBytes());
					sendBuffer.flip();
					client.write(sendBuffer);
					System.out.println("client send Text [" + sendText + "] to server");
					client.register(selector, SelectionKey.OP_READ);
				}
			}
			 keys.clear();
		}
	}

}
