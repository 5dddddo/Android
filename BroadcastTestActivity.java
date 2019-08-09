package com.example.androidsample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class BroadcastTestActivity extends AppCompatActivity {

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_test);

        Button registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Broadcast Receiver를 생성
                // 먼저 Broadcast Receiver가 어떤 Broadcast를 청취할 수 있는지를
                // 나타내는 intent filter가 생성

                // AndroidManifest.xml에 <intent-filter> 태그로 정의한 것을
                // java 코드로 정의
                // 이 filter가 Action을 가진 braodcast를 잡겠다는 의미
                IntentFilter filter = new IntentFilter();
                filter.addAction("MY_BROADCAST");
                // 안드로이드 시스템에서 나오는 여러가지 정해져있는
                // Broadcast를 catch 할 수 있음


                // BroadcastReceiver() : 추상 method , 직접 객체 생성 불가
                // override 필수
                // 안드로이드의 3번째 component인  Broadcast Receiver를 생성
                receiver = new BroadcastReceiver() {
                    @Override
                    // 신호를 잡았을 때 배터리 양, 네트워크 정보 등
                    // 여러가지 부가 정보가 intent에 담겨서 전달
                    public void onReceive(Context context, Intent intent) {
                        // Broadcast를 잡았을 때 처리해야 할 코드 작성

                        // Notification을 사용해보자

                        // 1. Notification manager 객체 획득하기
                        NotificationManager nManager = (NotificationManager) context.
                                getSystemService(Context.NOTIFICATION_SERVICE);

                        // 2. channelID와 channelName => 채널 구분,  Notification 중요도 설정
                        // 안드로이드 버전 oreo 8.0 이상 추가된 기능
                        // 구/신버전 나눠서 코드 작성
                        String channelId = "MY_CHANNEL";
                        String channelName = "MY_CHANNEL_NAME";
                        // Notification 중요도에 따라 팝업창으로 알려주기도 함
                        int important = NotificationManager.IMPORTANCE_HIGH;

                        // 3. Oreo 버전 이상에서는 반드시 channel 객체를 생성해서
                        // NotificationManager에 설정까지 해줘야 함
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(channelId, channelName, important);
                            nManager.createNotificationChannel(channel);
                        }

                        // 4. Notification을 생성
                        NotificationCompat.Builder builder =
                                new NotificationCompat.Builder(context, channelId);

                        // 5. Intent를 하나 생성 -> 나중에 notification을 클릭했을 때
                        // 화면에 notification을 띄운 App의 Activity를 보여주기 위한 용도
                        Intent nIntent = new Intent(context, BroadcastTestActivity.class);
                        nIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        nIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        // 해당 intent에 대한 signal을 받기 위한 변수
                        int requestID = 10;

                        // PendingIntent는 intent를 가지고 있는 intent
                        // intent의 수행을 지연시키는 역할을 하는 intent
                        PendingIntent pIntent = PendingIntent.getActivity(context,
                                requestID, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        builder.setContentTitle("제목부분이에요~!")
                                .setContentText("여기는 내용이에요")
                                // Notification을 터치 했을 때 사라지도록 처리
                                .setAutoCancel(true)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                // 별 모양의 아이콘
                                .setSmallIcon(android.R.drawable.btn_star)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                        R.drawable.potato)).setContentIntent(pIntent);

                        // NotificationManager를 통해
                        // 위에서 설정한 Notification을 실제로 실행
                        nManager.notify(0, builder.build());

                    }
                };
                // BroadcastReceiver 객체가 있다고 Broadcast(신호)를 catch 하는 것 X
                // Receiver를 Activity(context)에 등록해야 catch 가능
                registerReceiver(receiver, filter);
            }
        });

        Button unregisterBtn = (Button) findViewById(R.id.unregisterBtn);
        unregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 버튼이 클릭되면 receiver의 등록을 해제해줌
                // 현재 등록이 되어 있는지를 확인한 후 등록 되어있는 경우에만 해제
                if (receiver != null) {
                    unregisterReceiver(receiver);
                }
            }
        });

        Button sendSignalBtn = (Button) findViewById(R.id.sendSignalBtn);
        sendSignalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction("MY_BROADCAST");
                sendBroadcast(i);
            }
        });
    }
}
