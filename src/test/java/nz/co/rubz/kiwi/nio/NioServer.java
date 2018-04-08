package nz.co.rubz.kiwi.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

	private int i = 0;
	// 缓冲区大小
	private int blockSize = 4096;

	// 服务端口号
	private int port = 22222;

	// 发送段的缓冲区
	private ByteBuffer sendBuffer = ByteBuffer.allocate(blockSize);

	// 接收端的缓冲区
	private ByteBuffer reciveBuffer = ByteBuffer.allocate(blockSize);

	// 在NIO中实现多路复用的选择器
	private Selector selector;

	// 初始化

	public void initServer() throws IOException {
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);// 设置非阻塞
		ServerSocket ssocket = serverChannel.socket();
		ssocket.bind(new InetSocketAddress(port));
		selector = Selector.open();
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("NIOServer started on port :" + port);
	}

	// 监听事件
	public void listen() throws IOException {
		while (true) {
			selector.select();
			Set<SelectionKey> selectKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = selectKeys.iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();
				handleKeys(key);
//				handlerSelectionKey(key);
			}
		}
	}

	private void handleKeys(SelectionKey key) {

		if (key.isAcceptable()) {
			handleAccessKey(key);
		} else if (key.isReadable()) {
			handleReadKey(key);
		} else if (key.isWritable()) {
			handleWriteKey(key);
		}
	}

	// 服务端响应 客户端 连接事件
	private void handleAccessKey(SelectionKey key) {
		try {
			ServerSocketChannel server = (ServerSocketChannel) key.channel();
			SocketChannel channel = server.accept();
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ); // 连接完毕以后就会有读取动作
		} catch (IOException e) {
			System.out.println("handleAccessKey  " + e.getMessage());
		}
	}

	// 服务端响应 读取事件
	private void handleReadKey(SelectionKey key) {
		SocketChannel channel = (SocketChannel) key.channel();
		try {
			int count = channel.read(reciveBuffer);
			if (count > 0) { // 如果缓冲区有数据
				// 输出缓冲区的数据
				System.out.println(new String(reciveBuffer.array(), 0, count));
			}
			channel.register(selector, SelectionKey.OP_WRITE);
		} catch (IOException e) {
			System.out.println("handleRedKey " + e.getMessage());
		}
	}

	// 服务端响应写入事件
	private void handleWriteKey(SelectionKey key) {
		SocketChannel channel = (SocketChannel) key.channel();
		sendBuffer.clear(); // 向客户端发送数据之前， 清空写缓冲区。
		String text = "服务端 -> 客户端 " + i++;
		sendBuffer.put(text.getBytes());
		sendBuffer.flip();
		try {
			if (channel.isOpen()) {
				channel.write(sendBuffer);
				channel.register(selector, SelectionKey.OP_READ);
			}
		} catch (IOException e) {
			System.out.println("handleWriteKey " + e.getMessage());
		}

	}

	private void handlerSelectionKey(SelectionKey key) throws IOException {
		ServerSocketChannel server = null; // 服务端Channel
		SocketChannel channel = null; // 客户端Channel
		String sendText = "";
		String reviceText = "";
		int count = 0;
		if (key.isAcceptable()) {
			server = (ServerSocketChannel) key.channel();
			channel = server.accept();
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
		} else if (key.isReadable()) {
			channel = (SocketChannel) key.channel();
			count = channel.read(reciveBuffer);
			if (count > 0) {
				reviceText = new String(reciveBuffer.array(), 0, count);
				System.out.println("Server revice " + reviceText + " from client . ");
			}
			channel.register(selector, SelectionKey.OP_WRITE);
		} else if (key.isWritable()) {
			sendBuffer.clear();
			channel = (SocketChannel) key.channel();
			sendText = "服务端发送消息给客户端  -> " + i++;
			sendBuffer.put(sendText.getBytes());
			sendBuffer.flip();
			channel.write(sendBuffer);
			System.out.println("server send text " + sendText + "to client.");
		}

	}

	public static void main(String[] args) throws IOException {
		NioServer server = new NioServer();
		server.initServer();
		server.listen();
	}
}
