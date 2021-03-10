package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

//�Ѹ��� Ŭ���̾�Ʈ�� ����Ҽ� �ִ� Ŭ����
public class Client {
	
	Socket socket;
	
	public Client(Socket socket) {
		this.socket = socket;
		receive();
	}
	
	//Ŭ���̾�Ʈ�κ��� �޽��� ���Ÿ޼ҵ�
	public void receive() {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					while(true) {
						InputStream in = socket.getInputStream();
						byte[] buffer = new byte[512];
						int length = in.read(buffer);
						//�޼��� �۽� ������� ����ó��
						if(length == -1) throw new IOException();
						System.out.println("[���� : �޽��� ���� ����]"
								+ socket.getRemoteSocketAddress()
								+ " : "
								+ Thread.currentThread().getName()
								);
						String message = new String(buffer, 0, length, "UTF-8");
						
						//�ٸ� Ŭ���̾�Ʈ �鿡�Ե� ä�� ���� ���̱�
						for(Client client : Main.clients) {
							client.send(message);
						}
					}
				} catch(Exception e) {
					//��ø�� ����ó��
					try {
						System.out.println("[���� : �޽��� ���� ����] "
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
	
	//Ŭ���̾�Ʈ���� �޼��� ����
	public void send(String message) {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
					System.out.println("[���� : �޽��� �۽� ����]");
				} catch (Exception e) {
					try {
						System.out.println("[���� : �޽��� �۽� ����]"
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
