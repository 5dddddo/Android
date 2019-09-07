package com.example.androidpractice.chatting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidpractice.R;



public class ChattingClient extends AppCompatActivity {
    TextView textView , titleTextView;
    Button sendbutton;
    ScrollView scrollView;
    EditText editText;
    String roomNum;
    ClientService clientService;
    Boolean isService = false;
    String clientId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_client);
        Intent getIntent = getIntent();

        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                ClientService.MyBinder mb = (ClientService.MyBinder) iBinder;
                clientService = mb.getService();
                isService = true;
                clientService.clientToServer("enter",roomNum);
                clientService.clientToServer("STATE" , "2");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isService = false;
            }
        };

        Intent intent = new Intent(ChattingClient.this , ClientService.class);
        bindService(intent , conn , Context.BIND_ABOVE_CLIENT);

        Log.i("ChattingClientError","ChattingClient");
        scrollView = (ScrollView) findViewById(R.id.chattingClientScrollView);
        editText = (EditText) findViewById(R.id.chattingClientEditText) ;
        titleTextView = (TextView) findViewById(R.id.chattingClientTitleTextView);
        textView = (TextView) findViewById(R.id.chattingClientTextView);
        sendbutton = (Button)findViewById(R.id.chattingClientBtn);

        roomNum = (String)getIntent.getExtras().get("roomNum");
        clientId = (String)getIntent.getExtras().get("ID");
        titleTextView.setText(roomNum+"번방 입니다.");

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("ChattingClientError","sendbutton.onClick");
                String sendData = editText.getText().toString();
                if (!isService) {
                    Toast.makeText(getApplicationContext(),
                            "서비스중이 아닙니다, 데이터받을수 없음",
                            Toast.LENGTH_LONG).show();
                }
                if(clientService == null){
                    Toast.makeText(getApplicationContext(),
                            "서비스가 바인딩이 안됬어 ㅠㅠ",
                            Toast.LENGTH_LONG).show();
                } else{
                    if(sendData.length() >= 5 ){
                        if(sendData.substring(0,5).equals("/all ")){
                            clientService.clientToServer("/@CHAT2",roomNum+","+clientId+" : "+sendData.substring(5));
                        } else {
                            clientService.clientToServer("/@CHAT",roomNum+","+clientId+" : "+sendData);
                        }
                    }
                    else{
                        clientService.clientToServer("/@CHAT",roomNum+","+clientId+" : "+sendData);
                    }

                    editText.setText("");
                }
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String msg = intent.getExtras().getString("CHAT");
        String strChat[] = msg.split(",");
        if (strChat[0].equals("CHAT") ) {
            textView.append(strChat[1]+"\n");
        }
        String chat2Msg = null;
        chat2Msg = intent.getExtras().getString("CHAT2");
        if(chat2Msg!=null){
            Toast.makeText(getApplicationContext(),chat2Msg, Toast.LENGTH_LONG).show();
        }
        chat2Msg = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        clientService.clientToServer("STATE" , "1,"+roomNum);
    }
}