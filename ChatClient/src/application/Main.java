package application;
	
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ModuleLayer.Controller;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	static Socket socket;
	public static String messages;
	public static Parent root;
	
//	static TextArea textArea;
	
	//클라이언트 프로그램 동작 메소드입니다.
	public static void startClient(String IP, int port) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					socket = new Socket (IP, port);
					receive();
					
				} catch (Exception e) {
					if(!socket.isClosed()) {
						stopClient();
						System.out.println("[서버 접속 실패]");
						Platform.exit();
					}
				}
			}
		};
		thread.start();
	}
	
	//클라이언트 프로그램 종료 메소드입니다.
	public static void stopClient() {
		try {
			if(socket != null && !socket.isClosed()) {
				socket.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//서버로부터 메시지를 전달받는 메소드
	public static void receive() {
		while(true) {
			try {
				InputStream in = socket.getInputStream();
				byte[] buffer = new byte[512];
				int length = in.read(buffer);
				if(length == -1 ) throw new IOException();
				messages = new String(buffer,0,length,"UTF-8");
				System.out.println("[Client Main : 메시지 수신 성공]\n"+ messages);
				appendMessage(messages, root);
			}catch (Exception e) {
				stopClient();
				break;
			}
		}
	}
	
	//서버로 메세지를 전송하는 메소드
	public static void send(String message) {
		Thread thread = new Thread() {
			public void run() {
				try {
					OutputStream out = socket.getOutputStream();
					byte[] buffer = message.getBytes("UTF-8");
					out.write(buffer);
					out.flush();
//					messages = message;
				}catch (Exception e){
					stopClient();
				}
			}
		};
		thread.start();
	}
	
	//실제로 프로그램을 동작시키는 메소드
	@Override
	public void start(Stage primaryStage) throws Exception {
		root = FXMLLoader.load(getClass().getResource("clientMain.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("[ 카카우톡 ]");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(event -> stopClient());
		primaryStage.show();
	}
	
	public static void appendMessage(String message, Parent root)  {
		TextArea textArea = (TextArea) root.lookup("#textArea");
		textArea.appendText(message);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
