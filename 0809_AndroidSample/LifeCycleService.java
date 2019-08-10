package com.example.androidsample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class LifeCycleService extends Service {
    public LifeCycleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // 3가지 Callback method를 사용
    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 사용할 resource 준비 작업
        Log.i("ServiceExam", "onCreate() 호출");

    }

    // Activity에서 넘겨준 Intent를 첫번째 parameter로 받음
    // Service가 정상 실행되었으면 Intent != null 임
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 하는 로직처리
        Log.i("ServiceExam", "onStartCommand() 호출");
//        return super.onStartCommand(intent,flags,startId);

        if (intent == null) {
            // 강제 종료되어 Activity가 Service를 실행하지 않고
            // 안드로이드 시스템이 서비스만 강제로 재시작 했을 때 intent == null
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            // flags 값 주의!
            // Cleartop, singletop -> 생성되어 있는 Activity가 없는 경우 Activity 생성함
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // 생성되어 있는 Activity인지 확인
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            // 화면이 없는 객체가 화면이 있는 객체로 값을 전달하려면 TASK가 필요함
            //       Service    ->    Activity
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            resultIntent.putExtra("ServiceToActivityData", "결과 데이터 전달");
            // flag에 의해 메모리에 이미 떠있는 Activity는 onCreate() 호출하지 않고
            // 생성되어 있는 Activity 가져옴
            startActivity(resultIntent);

        } else {
            // 정상실행되어 Activity가 보내준 데이터를 서비스가 받음
            String msg = intent.getExtras().getString("ActivityToServiceData");
            Log.i("ServiceExam", "Activity가 보내준 msg : " + msg);

            // 로직처리가 진행되고 발생한
            // Activity에게 전달해야 하는 최종 결과 데이터는 intent로 보냄
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

            // flags 값 주의!
            // Cleartop, singletop -> 생성되어 있는 Activity가 없는 경우 Activity 생성함
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // 생성되어 있는 Activity인지 확인
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            // 화면이 없는 객체가 화면이 있는 객체로 값을 전달하려면 TASK가 필요함
            //       Service    ->    Activity
            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            resultIntent.putExtra("ServiceToActivityData", "결과 데이터 전달");
            // flag에 의해 메모리에 이미 떠있는 Activity는 onCreate() 호출하지 않고
            // 생성되어 있는 Activity 가져옴
            startActivity(resultIntent);
        }

        // 강제 종료되면 자동적으로 재시작함
        return Service.START_STICKY;

        // 강제 종료되면 자동적으로 재시작 안함
//        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // 리소스 해제 작업
        super.onDestroy();
        Log.i("ServiceExam", "onDestroy() 호출");
    }
}
