package io.vakin.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NIOHttpServer {
	private int DEFULT_PORT = 8080; 
	private ServerSocketChannel ssc;

	public NIOHttpServer() {
		try {
			start(DEFULT_PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start(int port) throws IOException {
		ssc = ServerSocketChannel.open(); // 打开服务器套接字通道
		ssc.socket().bind(new InetSocketAddress(port)); // 绑定到特定端口
		ssc.configureBlocking(false); // 设置为非阻塞模式
		Selector selector = Selector.open(); // 打开一个选择器
		ssc.register(selector, SelectionKey.OP_ACCEPT); // 向给定的选择器注册此通道，返回一个选择键
		while (true) {
			if (selector.select() > 0) { // 等待请求，请求过来继续执行，没有请求一直等待
				Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator(); // 获取等待处理的请求
				while (keyIter.hasNext()) {
					SelectionKey key = keyIter.next();
					new Thread(new HttpHandler(key)).run(); // 启动新线程处理SelectionKey
					keyIter.remove(); // 处理完后，从待处理的SelectionKey迭代器中移除当前所使用的key
				}
			}

		}
	}


	/**
	 * 打开服务的主入口
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new NIOHttpServer();
	}

	private class HttpHandler implements Runnable {

		SelectionKey key;

		public HttpHandler(SelectionKey key) {
			super();
			this.key = key;
		}

		@Override
		public void run() {
			try {
				if (key.isAcceptable()) {
					 System.out.println("Receive connection");
				        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
				        SocketChannel channel = serverSocketChannel.accept();
				        if (channel != null) {
				            channel.configureBlocking(false);
				            channel.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocate(1024));
				        }
				        System.out.println("Connection end");
				}else if (key.isReadable()) {
			        SocketChannel channel = (SocketChannel) key.channel();
			        ByteBuffer buffer = (ByteBuffer)key.attachment();
			        buffer.clear();
			        
			        StringBuilder content = new StringBuilder();
			        while (channel.read(buffer) > 0) {
			            buffer.flip(); //切换为读模式
			            content.append(StandardCharsets.UTF_8.decode(buffer));
			        }
			        
			        if (content.length() > 0) {
			        	 System.out.println("-----request--------\n"+content);
			            //设置interestOps，用于写响应
			            key.interestOps(SelectionKey.OP_WRITE);
			        } else {
			            channel.close();
			        }
				}else if (key.isWritable()) {
					System.out.println("-----response--------");
					SocketChannel channel = (SocketChannel) key.channel();
					String content = "HTTP/1.1 200 OK \nContent-type:text/html; charset=utf-8\n<title>Hello</title><h1>This is a Simple HTTP Server</h1>";
			        ByteBuffer buffer = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
			        while(buffer.hasRemaining()){
			        	channel.write(buffer);
			        }
			        channel.close();
			        System.out.println(content);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}
