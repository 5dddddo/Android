package com.example.androidpractice.chatting;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientService extends Service {

    IBinder mBinder = new MyBinder();
    private Socket socket;
    BufferedReader br;
    PrintWriter out;
    InputStreamReader isr;
    String clientId;
    BlockingQueue blockingQueue = new ArrayBlockingQueue(10);
    String Id;

    class ClientSendRunnable implements Runnable {
        BlockingQueue blockingQueue;

        ClientSendRunnable(BlockingQueue blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String msg = (String) blockingQueue.take();
                    Log.i("ChattingClientError", "blocking queue send: " + msg);
                    out.println(msg);
                    out.flush();
                } catch (Exception e) {
                    Log.i("ChattingClientError", "blocking queue 문제 : " + e.toString());
                }
            }


        }
    }


    class ClientReceiveRunnable implements Runnable{

        Intent receiveIntent = new Intent();

        @Override
        public void run() {
            try {
                try {
                    socket = new Socket("70.12.115.60", 7878);
                    Log.i("ChattingClientError", "서버 연결 성공!!");
                    isr = new InputStreamReader(socket.getInputStream());
                    br = new BufferedReader(isr);
                    out = new PrintWriter(socket.getOutputStream());
                } catch (Exception e) {
                    Log.i("ChattingClientError", e.toString());
                }

                out.println("/@CLID," + clientId);
                out.flush();
                String s = "";
                /////////////자이제 시작이야
                while ((s = br.readLine()) != null) {
                    Log.i("ChattingClientError", "서버로 받는 데이터 : " + s);
                    Bundle bundle = new Bundle();
                    String strRoomList[] = s.split(",");
                    if (strRoomList[0].equals("ROLI")) {

                        ComponentName cname = new ComponentName("com.example.androidpractice",
                                "com.example.androidpractice.chatting.ChattingWaitingRoom");
                        receiveIntent.setComponent(cname);
                        receiveIntent.putExtra("RoomList" , s);
                        receiveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        receiveIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        receiveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(receiveIntent);

                    } else if (strRoomList[0].equals("CHAT")){
                        ComponentName cname = new ComponentName("com.example.androidpractice",
                                "com.example.androidpractice.chatting.ChattingClient");
                        receiveIntent.setComponent(cname);
                        receiveIntent.putExtra("CHAT" , s);
                        receiveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        receiveIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        receiveIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(receiveIntent);
                    } else if (strRoomList[0].equals("CHAT2")){
                        String state = strRoomList[1];
                        Intent broadcastIntent = new Intent();
                        if (state.equals("1")){
                            ComponentName cname = new ComponentName("com.example.androidpractice",
                                    "com.example.androidpractice.chatting.ChattingWaitingRoom");
                            broadcastIntent.setComponent(cname);
                        } else if (state.equals("2")){
                            ComponentName cname = new ComponentName("com.example.androidpractice",
                                    "com.example.androidpractice.chatting.ChattingClient");
                            broadcastIntent.setComponent(cname);
                        }
                        broadcastIntent.putExtra("CHAT2" , strRoomList[2]);
                        broadcastIntent.putExtra("CHAT" , s);
                        broadcastIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        broadcastIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        broadcastIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(broadcastIntent);
                    }

                }
            } catch (Exception e) {
                Log.i("ChattingClientError", "읽기 문제 : " + e.toString());
            }

        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.i("serviceClient","서비스 시작 , 서버랑 연결 시작.");

    }
    class MyBinder extends Binder{
        ClientService getService(){
            return ClientService.this;
        }
    }
    public boolean connectServer(){
        boolean flag = false;

        return flag;
    }
    public void serverToClient(){

    }
    public void clientToServer(String protocol , String msg){

        if(protocol.equals("enter")){
            blockingQueue.add("/@ENRO,"+msg);
        } else if (protocol.equals("MKRO")){
            blockingQueue.add("/@MKRO,"+msg);
        } else if (protocol.equals("/@CHAT")){
            blockingQueue.add("/@CHAT,"+msg);
        } else if (protocol.equals("STATE")){
            blockingQueue.add("/@STATE,"+msg);
        } else if (protocol.equals("/@ROLI")){
            blockingQueue.add("/@ROLI");
        } else if (protocol.equals("/@CHAT2")) {
            blockingQueue.add("/@CHAT2,"+msg);
        } else {
            Log.i("serviceClient" , "보낼때 프로토콜 문제 있음");
        }

		switch(protocol){
			case "enter":
				blockingQueue.add("/@ENRO,"+msg);
				break;
			case "MKRO":
				blockingQueue.add("/@MKRO,"+msg);
				break;
			case "/@CHAT":
				blockingQueue.add("/@CHAT,"+msg);
				break;
			case "STATE":
				blockingQueue.add("/@STATE,"+msg);
				break;
			case "/@ROLI":
				blockingQueue.add("/@ROLI,"+msg);
				break;
			case "/@CHAT2":
				blockingQueue.add("/@CHAT2,"+msg);
				break;
			default :
				Log.i("serviceClient" , "보낼때 프로토콜 문제 있음");
				break;


		}
    }

    public ClientService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        Intent getLoginIntent = intent;
        clientId = (String)getLoginIntent.getExtras().get("ID");

        ClientReceiveRunnable receiveRunnable = new ClientReceiveRunnable();
        ClientSendRunnable sendRunnable = new ClientSendRunnable(blockingQueue);
        Thread thread1 = new Thread(receiveRunnable);
        Thread thread2 = new Thread(sendRunnable);
        thread1.start();
        thread2.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        // 서비스가 종료될 때 실행

    }

}