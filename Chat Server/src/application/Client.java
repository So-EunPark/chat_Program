package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

//한명의 클라이언트와 통신할수 있는 클래스
public class Client {
	
	Socket socket;
	
	public Client(Socket socket) {
		this.socket = socket;
		receive();
	}
	
	//클라이언트로부터 메시지 수신메소드
	public void receive() {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					while(true) {
						InputStream in = socket.getInputStream();
						byte[] buffer = new byte[512];
						int length = in.read(buffer);
						//메세지 송신 오류라면 오류처리
						if(length == -1) throw new IOException();
						System.out.println("[서버 : 메시지 수신 성공]"
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
						System.out.println("[서버 : 메시지 수신 오류] "
								+ socket.getRemoteSocketAddress()
								+ " : "
								+ Thread.currentThread().getName()
								);
						Main.clients.remove(Client.this);
						socket.close();
					}catch(Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		};
		Main.threadPool.submit(thread);
	}
	
	//클라이언트에게 메세지 전송
	public void send(String message) {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
					System.out.println("[서버 : 메시지 송신 성공]");
				} catch (Exception e) {
					try {
						System.out.println("[서버 : 메시지 송신 오류]"
								+ socket.getRemoteSocketAddress()
								+ " : "
								+ Thread.currentThread().getName()
								);
						Main.clients.remove(Client.this);
						socket.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		};
		Main.threadPool.submit(thread);
	}
}
