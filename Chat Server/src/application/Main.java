package application;
	
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	public static ExecutorService threadPool;
	public static Vector<Client> clients = new Vector<Client>();
	
	ServerSocket serverSocket;
	
	//서버를 구동시켜서 클라이언트의 연결을 기다리는 메소드입니다.
	public void startServer(String IP, int port) {
		
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP, port));
		} catch (IOException e) {
			e.printStackTrace();
			if(!serverSocket.isClosed()) {
				stopServer();
			}
			return;
		}
		
		//클라이언트가 접속할 때까지 계속 기다리는 쓰레드입니다.
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						
						Socket socket;
						socket = serverSocket.accept();
						clients.add(new Client(socket));
						System.out.println("[클라이언트 접속]"
								+ socket.getRemoteSocketAddress()
								+": "
								+ Thread.currentThread().getName());
						
					} catch (IOException e) {
						e.printStackTrace();
						
						if(!serverSocket.isClosed()) {
							stopServer();
						}
						break;
					}	
				}
			}
		};
		
		threadPool = Executors.newCachedThreadPool();
		threadPool.submit(thread);
	}
	//서버의 작동을 중지시키는 메소드
	public void stopServer() {
		try {
			//현재 작동중인 모든 소켓 닫기
			Iterator<Client> iterator = clients.iterator();
			while(iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			//서버소켓 객체 닫기
			if(serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			//스레드풀 종료하기
			if(threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//UI를 생성하고, 실질적으로 프로그램을 동작시키는 메소드입니다.
	@Override
	public void start(Stage primaryStage) {
		
	}
	
	//프로그램의 진입점.
	public static void main(String[] args) {
		launch(args);
	}
}
