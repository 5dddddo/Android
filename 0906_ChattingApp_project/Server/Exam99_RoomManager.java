package javaNetwork;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Exam99_RoomManager {
	List<Exam99_Room> roomList = new ArrayList<Exam99_Room>();
	Set<Integer> roomNumSet = new HashSet<Integer>();
	
	public String getStringRoomList() {
		String ROLI = "ROLI";
		String strRoomList = "";
		int roomListSize = roomList.size();
		
		for(Exam99_Room room : roomList) {
			strRoomList += ","+room.getRoomNum()+","+room.getRoomTitle();
		}
		if(roomListSize == 0) {
			return "ROLI,0";
		}else {
			return ROLI +","+roomListSize+strRoomList;
		}
		
	}
	public int makeRoomNum() {
        int randRoomNum;
        Random random = new Random();
        int a = 0, b = 0;
        while (true) {
            a = roomNumSet.size();
            randRoomNum = random.nextInt(100);
            roomNumSet.add(randRoomNum);
            b = roomNumSet.size();
            if (a != b) {
                break;
            }
            if (roomNumSet.size() == 100) {
                //더이상 방을 만들 수 없습니다.
                break;
            }
        }

        return randRoomNum;
    }
	
	public void addRoom(Exam99_Room room) {
		roomList.add(room);
	}
	
	public boolean isExitRoom(String roomNum) {
		boolean flag = false;
		for(Exam99_Room room : roomList) {
			if(room.getRoomNum().equals(roomNum)) {
				flag= true;
			}
		}
		
		return flag;
	}
	public Exam99_Room getRoom(String roomNum) {
		for(Exam99_Room room : roomList) {
			if(room.getRoomNum().equals(roomNum)) {
				return room;
			}
		}
		
		return null;
	}
}
