package javaNetwork;

import java.util.ArrayList;
import java.util.List;

public class Exam99_Room {
	List<EchoRunnable2> clients = new ArrayList<EchoRunnable2>();
	String roomNum;
	String roomTitle;
	
	
	public void exitRoom(String Id) {
		for(EchoRunnable2 er : clients) {
			if(er.ID.equals(Id)) {
				clients.remove(er);
				break;
			}
		}
		System.out.println("총 클라이언트 수 : "+ clients.size()+"\n");
	}
	
	public Exam99_Room(String roomNum ) {
		this.roomNum =roomNum;
	}
	
	public void addClient(EchoRunnable2 client) {
		clients.add(client);
		System.out.println("총 클라이언트 수 : "+ clients.size()+"\n");
	}
	
	public void broadcast(String str) {
		for(EchoRunnable2 er : clients) {
			System.out.println("방송 합니다.");
			er.out("CHAT,"+str,2);
			System.out.println(str);
		}
	}
	public String getRoomNum() {
		return roomNum;
	}
	public String getRoomTitle() {
		return roomTitle;
	}
	public void setRoomTitle(String title) {
		roomTitle = title;
	}
}
