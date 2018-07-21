package io.vakin.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


/**
 * 
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2018年7月21日
 */
public class SimpleNIOClient {

	public void startClient() throws IOException, InterruptedException {

		InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8090);
		SocketChannel client = SocketChannel.open(hostAddress);

		System.out.println("Client... started");

		String threadName = Thread.currentThread().getName();

		// Send messages to server
		String[] messages = new String[] { threadName + ": test1", threadName + ": test2", threadName + ": test3" };

		for (int i = 0; i < messages.length; i++) {
			byte[] message = new String(messages[i]).getBytes();
			ByteBuffer buffer = ByteBuffer.wrap(message);
			client.write(buffer);
			System.out.println(messages[i]);
			buffer.clear();
			Thread.sleep(5000);
		}
		client.close();
	}

	public static void main(String[] args) {
		Runnable client = new Runnable() {
			@Override
			public void run() {
				try {
					new SimpleNIOClient().startClient();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		};

		new Thread(client, "client-A").start();
		new Thread(client, "client-B").start();
	}
}
