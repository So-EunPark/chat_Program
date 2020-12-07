package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
	//한명의 클라이언트와 통신할수 있는 클래스
	Socket socket;
	
	public Client(Socket socket) {
		this.socket = socket;
		receive();
	}
	
	public void receive() {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					while(true) {
						InputStream in = socket.getInputStream();
						byte[] buffer = new byte[512];
						int length = in.read(buffer);
						while(length == -1) throw new IOException();
						System.out.println("[메시지 수신 성공]"
								+ socket.getRemoteSocketAddress()
								+ " : "
								+ Thread.currentThread().getName()
								);
						String message = new String(buffer, 0, length, "UTF-8");
						
						//다른 클라이언트 들에게도 채팅 내용 보이기
						for(Client client : Main.clients) {
							client.send(message);
						}
					}
					
				} catch(Exception e) {
					//중첩된 예외처리
					try {
						System.out.println("[메시지 수신 오류] "
								+ socket.getRemoteSocketAddress()
								+ " : "
								+ Thread.currentThread().getName()
								);
					}catch(Exception e2) {
						e.printStackTrace();
					}
					
				}
			}
		};
		Main.threadPool.submit(thread);
	}
	
	public void send(String message) {
		Runnable thread = new Runnable() {

			@Override
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
				} catch (Exception e) {
					try {
						System.out.println("[메시지 송신 오류]"
								+ socket.getRemoteSocketAddress()
								+ " : "
								+ Thread.currentThread().getName()
								);
						Main.clients.remove(Client.this);
						socket.close();
					} catch (Exception e2) {
						e.printStackTrace();
					}
				}
			}
			
		};
	}
}
