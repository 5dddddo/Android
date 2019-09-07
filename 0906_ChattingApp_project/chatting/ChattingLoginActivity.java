package com.example.androidpractice.chatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidpractice.R;

public class ChattingLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_login);

        Button loginBtn = (Button)findViewById(R.id.chattingLoginButton);
        final EditText idEditText = (EditText)findViewById(R.id.chattingLoginIdEditText);
        EditText pwEditText = (EditText)findViewById(R.id.chattingLoginPwEditText);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidpractice",
                        "com.example.androidpractice.chatting.ClientService");
                i.setComponent(cname);
                i.putExtra("ID",idEditText.getText().toString());
                // 서비스 클래스를 찾아서 객체화 시키고 실행!
                startService(i);

                intent.setAction("CHATTING_WAITING_ROOM");
                intent.putExtra("ID",idEditText.getText().toString());
                startActivity(intent);
            }
        });

    }
}
