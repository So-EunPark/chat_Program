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
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	public static ExecutorService threadPool;
	public static Vector<Client> clients = new Vector<Client>();
	//���� ���̺귯��

	ServerSocket serverSocket;
	
	//������ �������Ѽ� Ŭ���̾�Ʈ�� ������ ��ٸ��� �޼ҵ��Դϴ�.
	public void startServer(String IP, int port) {
		
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(IP, port));
		} catch (Exception e) {
			e.printStackTrace();
			if(!serverSocket.isClosed()) {
				stopServer();
			}
			return;
		}
		
		//Ŭ���̾�Ʈ�� ������ ������ ��� ��ٸ��� �������Դϴ�.
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						
						Socket socket;
						socket = serverSocket.accept();
						clients.add(new Client(socket));
						System.out.println("[Ŭ���̾�Ʈ ����]"
										+ socket.getRemoteSocketAddress()
										+": "
										+ Thread.currentThread().getName()
										);
					} catch (Exception e) {
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
	
	//������ �۵��� ������Ű�� �޼ҵ�
	public void stopServer() {
		try {
			//���� �۵����� ��� ���� �ݱ�
			Iterator<Client> iterator = clients.iterator();
			while(iterator.hasNext()) {
				Client client = iterator.next();
				client.socket.close();
				iterator.remove();
			}
			//�������� ��ü �ݱ�
			if(serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			//������Ǯ �����ϱ�
			if(threadPool != null && !threadPool.isShutdown()) {
				threadPool.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//UI�� �����ϰ�, ���������� ���α׷��� ���۽�Ű�� �޼ҵ�
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
//		FXMLLoader loader = new FXMLLoader();
//
//	    loader.setLocation(Main.class.getResource("server.fxml"));
//	    Parent root = loader.load();
		
//		Button toggleButton = (Button) root.lookup("toggleButton");
//		TextArea textArea = (TextArea) root.lookup("textArea");
		
//		Button toggleButton = new Button("�����ϱ�");
//		((BorderPane) root).setBottom(toggleButton);
//		
		Parent root = FXMLLoader.load(getClass().getResource("server.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("ä�� ���� / Chat Server");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(event -> stopServer());
		primaryStage.show();
	}
	
	//���α׷��� ������.
	public static void main(String[] args) {
		launch(args);
	}
}
