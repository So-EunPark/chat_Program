package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;

public class ControllerServer implements Initializable{

	@FXML ToggleButton toggleButton;
	@FXML TextArea textArea;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		String IP= "127.0.0.1";
		int port = 9876;
		
		Main main = new Main();
		
		toggleButton.setOnAction(event -> {
			if(toggleButton.getText().equals("시작하기")) {
				main.startServer(IP,port);				
				Platform.runLater(()-> {
				String message = String.format("[서버 시작]\n", IP, port);
					textArea.appendText(message);
					toggleButton.setText("종료하기");
				});
			}else {
				main.stopServer();
				Platform.runLater(()->{
					String message = String.format("[서버 종료]\n",IP,port);
					textArea.appendText(message);
					toggleButton.setText("시작하기");
					});
				}
			});
		
	}

}
