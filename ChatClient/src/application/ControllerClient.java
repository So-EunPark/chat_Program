package application;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
		
		//����ĥ�� ������
		input.setOnAction(event -> {
			Main.send(userName.getText() + ": " + input.getText() + "\n");
			input.setText("");
			input.requestFocus();
		});
		
		//���۹�ư Ŭ���� ������
		sendButton.setOnAction( event -> {
			Main.send(userName.getText() + ": " + input.getText() + "\n");
			input.setText("");
			input.requestFocus();
		});
		
		//���� ��ư Ŭ���� �̺�Ʈ ó��
		connectionButton.setOnAction(event ->{
			if(connectionButton.getText().equals("����")) {
				int port = 9876;
				try {
					port = Integer.parseInt(portText.getText());
				} catch (Exception e) {
					e.printStackTrace();
				}
				Main.startClient(IPText.getText(), port, userName.getText());
				connectionButton.setText("����");
				input.setDisable(false);
				sendButton.setDisable(false);
				input.requestFocus();
			}else {
				Platform.runLater(()->{
//					Main.send("[ "+ userName.getText() +"���� �����߽��ϴ� ]\n");
					textArea.clear();
				});
				Main.stopClient();
				
				connectionButton.setText("����");
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

