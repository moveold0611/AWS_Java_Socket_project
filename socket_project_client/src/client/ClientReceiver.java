package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;

import client.dto.RequestBodyDto;

public class ClientReceiver extends Thread{
		
	@Override
	public void run() {
		Client simpleGUIClient = Client.getInstance();
		while(true) {
			try {
				BufferedReader bufferedReader = 
						new BufferedReader(new InputStreamReader(simpleGUIClient.getSocket().getInputStream()));
				String requestBody = bufferedReader.readLine();
//				simpleGUIClient.getTextArea().append(requestBody + "\n");
				requestController(requestBody);
				
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
	

	
	private void requestController(String requestBody) {
		Gson gson = new Gson();
		String resource = gson.fromJson(requestBody, RequestBodyDto.class).getResource();
		
		switch(resource) {
		
			case "updateRoomList" :
				List<String> roomList = (List<String>) gson.fromJson(requestBody, RequestBodyDto.class).getBody();
				Client.getInstance().getRoomListModel().clear();
				Client.getInstance().getRoomListModel().addAll(roomList);
				break;
		
			case "updateUserList" :
				List<String> usernameList = (List<String>) gson.fromJson(requestBody, RequestBodyDto.class).getBody();
				Client.getInstance().getUserListModel().clear();
				usernameList.set(0, usernameList.get(0) + " <방장>");
				Client.getInstance().getUserListModel().addAll(usernameList);				
				break; 				
				
			case "showMessage" :
				String messageContent = (String) gson.fromJson(requestBody, RequestBodyDto.class).getBody();
				Client.getInstance().getChattingTextArea().append(messageContent + "\n");
				break;			
				
			case "removeTextArea" :
				String removeTextArea = (String) gson.fromJson(requestBody, RequestBodyDto.class).getBody();
				Client.getInstance().getChattingTextArea().setText("");
				break;
				
			case "exitChattingRoom" : // 서버에서 명령을 받아 클라이언트를 채팅방에서 내보내는 스위치
				String receiveExitMessage = (String) gson.fromJson(requestBody, RequestBodyDto.class).getBody();
				JOptionPane.showMessageDialog(Client.getInstance().getChattingRoomListPanel(), receiveExitMessage);
				Client.getInstance().getMainCardLayout().show(Client.getInstance().getMainCardPanel(), "chattingRoomListPanel");
				break;
				
			case "whisper" :
				String whispherToUser = (String) gson.fromJson(requestBody, RequestBodyDto.class).getBody();
				Client.getInstance().getChattingTextArea().append(whispherToUser + "\n");
		}
	}
	
		
		
			
}
		
	

