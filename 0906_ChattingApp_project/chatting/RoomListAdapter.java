package com.example.androidpractice.chatting;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.androidpractice.R;

import java.util.ArrayList;


public class RoomListAdapter extends BaseAdapter {

    private ArrayList<Room> roomList;

    public void setRoomList(ArrayList<Room> roomList){
        Log.i("RoomList","RoomList세팅 ," + roomList.get(0).getRoomTitle());
        this.roomList = roomList;
    }

    public void clearList(){

        for(int i =0 ; i < roomList.size() ; i++){
            roomList.remove(i);
        }

    }

    @Override
    public int getCount() {
        return roomList.size();
    }

    @Override
    public Object getItem(int i) {
        return roomList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final Context context = viewGroup.getContext();
        // 출력할 뷰 객체를 생성
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 생성된 view객체에 xml layout을 설정
            view = inflater.inflate(R.layout.list_view_room , viewGroup,false);
        }
        TextView textView1 = (TextView) view.findViewById(R.id.roomTitle);
        TextView textView2 = (TextView) view.findViewById(R.id.roomNum);
        Room room = roomList.get(i);  // 화면에 출력할 데이터를 가져와요~
        Log.i("RoomList","RoomList textView 세팅 ," + room.getRoomTitle());
        textView1.setText("방 제목 : "+room.getRoomTitle());
        textView2.setText(room.getRoomNum()+"번 방");

        return view;
    }
}
