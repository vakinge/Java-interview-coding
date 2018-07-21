package io.vakin.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网络多客户端聊天室 功能1： 客户端通过Java NIO连接到服务端，支持多客户端的连接
 * 功能2：客户端初次连接时，服务端提示输入昵称，如果昵称已经有人使用，提示重新输入，如果昵称唯一，则登录成功，之后发送消息都需要按照规定格式带着昵称发送消息
 * 功能3：客户端登录后，发送已经设置好的欢迎信息和在线人数给客户端，并且通知其他客户端该客户端上线
 * 功能4：服务器收到已登录客户端输入内容，转发至其他登录客户端。
 */
public class ChatRoomNIOServer {

	private Selector selector = null;
	static final int port = 9999;
	private Charset charset = Charset.forName("UTF-8");
	// 用来记录在线人数，以及昵称
	private static  Map<SocketChannel, String> onlineUsers = new ConcurrentHashMap<>();

	// 相当于自定义协议格式，与客户端协商好
	private static String USER_CONTENT_SPILIT = ":";

	public void init() throws IOException {
		selector = Selector.open();
		ServerSocketChannel server = ServerSocketChannel.open();
		server.bind(new InetSocketAddress(port));
		// 非阻塞的方式
		server.configureBlocking(false);
		// 注册到选择器上，设置为监听状态
		server.register(selector, SelectionKey.OP_ACCEPT);

		System.out.println("启动完成...");

		while (true) {
			int readyChannels = selector.select();
			if (readyChannels == 0)
				continue;
			Set<SelectionKey> selectedKeys = selector.selectedKeys(); // 可以通过这个方法，知道可用通道的集合
			Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
			while (keyIterator.hasNext()) {
				SelectionKey sk = keyIterator.next();
				keyIterator.remove();
				dealWithSelectionKey(server, sk);
			}
		}
	}

	public void dealWithSelectionKey(ServerSocketChannel server, SelectionKey sk) throws IOException {
		if (sk.isAcceptable()) {
			SocketChannel sc = server.accept();
			// 非阻塞模式
			sc.configureBlocking(false);
			// 注册选择器，并设置为读取模式，收到一个连接请求，然后起一个SocketChannel，并注册到selector上，之后这个连接的数据，就由这个SocketChannel处理
			sc.register(selector, SelectionKey.OP_READ);

			// 将此对应的channel设置为准备接受其他客户端请求
			sk.interestOps(SelectionKey.OP_ACCEPT);
			
			sc.write(charset.encode("SUCCESSED"));
		}
		// 处理来自客户端的数据读取请求
		if (sk.isReadable()) {
			// 返回该SelectionKey对应的 Channel，其中有数据需要读取
			SocketChannel sc = (SocketChannel) sk.channel();
			ByteBuffer buff = ByteBuffer.allocate(1024);
			StringBuilder content = new StringBuilder();
			try {
				while (sc.read(buff) > 0) {
					buff.flip();
					content.append(charset.decode(buff));

				}
				
				if(content.length() == 0){
					// 断开
					sc.close();
					System.out.println("用户【"+onlineUsers.remove(sc)+"】下线");
					return;
				}

				// 将此对应的channel设置为准备下一次接受数据
				sk.interestOps(SelectionKey.OP_READ);
			} catch (IOException io) {
				sk.cancel();
				if (sk.channel() != null) {
					sk.channel().close();
				}
			}
			if (content.length() > 0) {
				String[] arrayContent = content.toString().split(USER_CONTENT_SPILIT);
				// 注册用户
				if (arrayContent != null && arrayContent.length == 1) {
					String name = arrayContent[0];
					onlineUsers.put(sc, name);
					int num = onlineUsers.size();
					String message = "用户 【" + name + "】进入聊天室! 当前在线人数:" + num;
					System.out.println(message);
					// 不回发给发送此内容的客户端
					BroadCast(selector, sc, message);
				}
				else if (arrayContent != null && arrayContent.length > 1) {
					String name = arrayContent[0];
					String message = content.substring(name.length() + USER_CONTENT_SPILIT.length());
					message = "【" + name + " 】:" + message;
					System.out.println(message);
					BroadCast(selector, null, message);
				}
			}

		}
	}

	public void BroadCast(Selector selector, SocketChannel exclude, String content) throws IOException {
		// 广播数据到所有的SocketChannel中
		for (SelectionKey key : selector.keys()) {
			Channel targetchannel = key.channel();
			// 如果except不为空，不回发给发送此内容的客户端
			if (targetchannel instanceof SocketChannel && targetchannel != exclude) {
				SocketChannel dest = (SocketChannel) targetchannel;
				dest.write(charset.encode(content));
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new ChatRoomNIOServer().init();
	}
}