package com.example.androidpractice.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.androidpractice.R;


import java.util.ArrayList;


class Room implements Parcelable {
    String roomTitle;
    int roomNum;

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public Room() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        try {

            parcel.writeString(roomTitle);
            parcel.writeInt(roomNum);

        } catch (Exception e) {

        }
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel parcel) {
            return new Room(parcel);
        }

        @Override
        public Room[] newArray(int i) {
            return new Room[i];
        }
    };

    protected Room(Parcel parcel) {
        roomTitle = parcel.readString();
        roomNum = parcel.readInt();
    }

}

class RoomManager {
    ArrayList<Room> roomList = new ArrayList<Room>();


    public ArrayList<Room> getRoomList() {
        return roomList;
    }

    public boolean isExitRoom(int roomNum) {
        boolean flag = false;
        for (Room room : roomList) {
            if (room.getRoomNum() == roomNum) {
                flag = true;
            }
        }
        return flag;
    }

    public void addRoom(Room room) {
        roomList.add(room);
    }


}

public class ChattingWaitingRoom extends AppCompatActivity {

    String clientId;
    ClientService clientService;
    Boolean isService = false;

    RoomManager roomManager = new RoomManager();
    RoomListAdapter roomListAdapter = new RoomListAdapter();
    ListView roomListView;

    public void makeRoomListView() {
        roomListAdapter.setRoomList(roomManager.getRoomList());
        roomListView.setAdapter(roomListAdapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_waiting_room);

        Button makeRoomBtn = (Button) findViewById(R.id.makeRoomBtn);
        roomListView = (ListView) findViewById(R.id.roomListView);
        Intent intent = getIntent();
        clientId = (String) intent.getExtras().get("ID");
        Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {

                super.handleMessage(msg);
                Bundle bundle = new Bundle();
                bundle = msg.getData();
                String receiveData = bundle.getString("strRoomList");
                String strRoomList[] = receiveData.split(",");
                if (strRoomList[0].equals("ROLI") && !strRoomList[1].equals("0")) {

                    for (int i = 2; i < strRoomList.length; ) {
                        int roomNum = Integer.parseInt(strRoomList[i]);
                        if (roomManager.isExitRoom(roomNum)) {
                            i++;
                            i++;
                        } else {
                            Room room = new Room();
                            room.setRoomNum(Integer.parseInt(strRoomList[i]));
                            i++;
                            room.setRoomTitle(strRoomList[i]);
                            i++;
                            roomManager.addRoom(room);
                        }
                    }
                    makeRoomListView();
                }


            }
        };

        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ClientService.MyBinder mb = (ClientService.MyBinder) iBinder;
                clientService = mb.getService();
                clientService.clientToServer("STATE" , "1");
                isService = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isService = false;
            }
        };

        Intent intent2 = new Intent(ChattingWaitingRoom.this , ClientService.class);
        bindService(intent2 , conn , Context.BIND_ABOVE_CLIENT);


        roomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction("CHATTINGCLIENT_ACTIVITY");
                int selectedRoomNum = roomManager.getRoomList().get(position).getRoomNum();
                Log.i("ChattingClientError", "리스트 누름 : " + selectedRoomNum);
                intent.putExtra("roomNum", selectedRoomNum + "");
                intent.putExtra("ID", clientId);
                Log.i("ChattingClientError", "blockingQueue.add 방들어갔어 : " + "/@ENRO," + selectedRoomNum);

                startActivity(intent);
            }
        });

        makeRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(ChattingWaitingRoom.this);
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChattingWaitingRoom.this);
                dialog.setTitle("방 만들라꼬? ㅎㅎ");
                dialog.setMessage("원하는 방 제목을 적어주세요");
                dialog.setView(editText);
                dialog.setPositiveButton("방만들기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Log.i("ChattingClientError", "blockingQueue.add하즈아 : " + "/@MKRO," + editText.getText().toString());
                        clientService.clientToServer("MKRO" , editText.getText().toString());

                    }
                });
                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();

            }
        });

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String msg = null;
        msg = intent.getExtras().getString("RoomList");
        if(msg != null){
            String strRoomList[] = msg.split(",");
            if (strRoomList[0].equals("ROLI") && !strRoomList[1].equals("0")) {

                for (int i = 2; i < strRoomList.length; ) {
                    int roomNum = Integer.parseInt(strRoomList[i]);
                    if (roomManager.isExitRoom(roomNum)) {
                        i++;
                        i++;
                    } else {
                        Room room = new Room();
                        room.setRoomNum(Integer.parseInt(strRoomList[i]));
                        i++;
                        room.setRoomTitle(strRoomList[i]);
                        i++;
                        roomManager.addRoom(room);
                    }
                }
                makeRoomListView();
            }
        }

        String chat2Msg = null;
        chat2Msg = intent.getExtras().getString("CHAT2");
        if(chat2Msg!=null){
            Toast.makeText(getApplicationContext(),chat2Msg, Toast.LENGTH_LONG).show();
        }

    }

}
