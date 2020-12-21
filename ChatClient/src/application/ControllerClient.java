package application;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ControllerClient implements Initializable{

	@FXML TextField userName, IPText, portText, input;
	@FXML TextArea textArea;
	@FXML Button sendButton, connectionButton;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		IPText.setText("127.0.0.1");
		portText.setText("9876");
		
		//엔터칠때 보내기
		input.setOnAction(event -> {
			Main.send(userName.getText() + ": " + input.getText() + "\n");
			input.setText("");
			input.requestFocus();
		});
		
		sendButton.setOnAction( event -> {
			Main.send(userName.getText() + ": " + input.getText() + "\n");
			input.setText("");
			input.requestFocus();
		});
		
		connectionButton.setOnAction(event ->{
			if(connectionButton.getText().equals("접속")) {
				int port = 9876;
				try {
					port = Integer.parseInt(portText.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
				Main.startClient(IPText.getText(), port);
				Platform.runLater(()->{
//					textArea.appendText("[ "+ userName.getText() +"님이 채팅방에 접속했습니다 ]\n");
					Main.send("[ "+ userName.getText() +"님이 채팅방에 접속했습니다 ]\n");
					System.out.println("[ "+ userName.getText() +"님이 채팅방에 접속했습니다 ]\n");
				});
				connectionButton.setText("종료");
				input.setDisable(false);
				sendButton.setDisable(false);
				input.requestFocus();
				
			}else {
				Main.stopClient();
				Platform.runLater(()->{
					textArea.appendText("[ 채팅방 퇴장 ]\n");
				});
				connectionButton.setText("접속");
				input.setDisable(true);
				sendButton.setDisable(true);
			}
		});
		
		
	}

//	public void appendMessage(String message) {
//		Platform.runLater(()->{
//			try {
//				System.out.println(message);
//				Parent root = FXMLLoader.load(Main.class.getResource("clientMain.fxml"));
//				TextArea textArea1 = (TextArea) root.lookup("#textArea");
//				textArea1.appendText(message);	
//				System.out.println(textArea);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		});
//	}
}

