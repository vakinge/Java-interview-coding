package io.vakin.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class ChatRoomNIOClient {

	private Selector selector = null;
	static final int port = 9999;
	private Charset charset = Charset.forName("UTF-8");
	private SocketChannel sc = null;
	private String name;
	private static String SPILITER = ":";

	public void init() throws IOException {
		
		generateName();
		
		selector = Selector.open();
		// 连接远程主机的IP和端口
		sc = SocketChannel.open(new InetSocketAddress("127.0.0.1", port));
		sc.configureBlocking(false);
		sc.register(selector, SelectionKey.OP_READ);
		// 开辟一个新线程来读取从服务器端的数据
		new Thread(new ClientThread()).start();
		
		System.out.println(">>>当前系统分配用户名:"+this.name);
		sc.write(charset.encode(this.name));
		
		// 在主线程中 从键盘读取数据输入到服务器端
		Scanner scan = new Scanner(System.in);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if ("".equals(line)){				
				continue; // 不允许发空消息
			}
			line = name + SPILITER + line;
			sc.write(charset.encode(line));
		}

	}

	private class ClientThread implements Runnable {
		public void run() {
			try {
				while (true) {
					int readyChannels = selector.select();
					if (readyChannels == 0)
						continue;
					Set<SelectionKey> selectedKeys = selector.selectedKeys(); // 可以通过这个方法，知道可用通道的集合
					Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
					while (keyIterator.hasNext()) {
						SelectionKey sk = (SelectionKey) keyIterator.next();
						keyIterator.remove();
						dealWithSelectionKey(sk);
					}
				}
			} catch (IOException io) {
			}
		}

		private void dealWithSelectionKey(SelectionKey sk) throws IOException {
			if (sk.isReadable()) {
				// 使用 NIO 读取 Channel中的数据，这个和全局变量sc是一样的，因为只注册了一个SocketChannel
				// sc既能写也能读，这边是读
				SocketChannel sc = (SocketChannel) sk.channel();

				ByteBuffer buff = ByteBuffer.allocate(1024);
				String content = "";
				while (sc.read(buff) > 0) {
					buff.flip();
					content += charset.decode(buff);
				}
				
				if(content.length() == 0){
					sc.close();
					return ;
				}
				if("SUCCESSED".equals(content)){
					content = "[系统消息]:进入聊天室成功，可以开始聊天了";
				}
				System.out.println(content);
				sk.interestOps(SelectionKey.OP_READ);
			}
		}
	}
	
	private void generateName(){
		char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		StringBuilder nameBf = new StringBuilder(6);
		for (int i = 0; i < 8; i++) {
			nameBf.append(chars[Math.abs(new Random().nextInt(chars.length))]);
		}
		this.name = nameBf.toString();
	}

	public static void main(String[] args) throws IOException {
		new ChatRoomNIOClient().init();
	}
}