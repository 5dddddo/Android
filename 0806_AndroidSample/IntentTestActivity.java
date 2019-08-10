package com.example.androidsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntentTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_test);

        Button implicitBtn = (Button) findViewById(R.id.implicitBtn);

        implicitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 새로운 Activity를 호출
                // Implicit intent (묵시적 인텐트)를 이용해서 Activity를 호출
                // Component 대신 Action (하나만 지정할 수 있음)과 Category(여러 개 지정 가능) 지정해줌
                Intent i = new Intent();

                //AndroidManifest.xml 파일에 <intent-filter> 에 지정해 놔야 사용할 수 있음
                // Action (하나만 지정할 수 있음) -> set
                i.setAction("MY_ACTION");

                //  Category(여러 개 지정 가능) -> add
                i.addCategory("MY_CATEGORY");
                i.putExtra("DATA", "소리없는 아우성");
                startActivity(i);
            }
        });


        Button dialBtn = (Button) findViewById(R.id.dialBtn);
        dialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 전화걸기 Activity 호출
                // 전화걸기는 안드로이드 시스템 내부에 이미 정해져 있는 기본 APP이기 때문에
                // Activity name을 알 수 없음 -> 묵시적 intent 사용
                Intent i = new Intent();
                i.setAction(Intent.ACTION_DIAL);
                // 전화번호 객체는 Uri.parse("tel:010000000 00") 형식
                i.setData(Uri.parse("tel:01056113427"));
                startActivity(i);

            }
        });
        Button browserBtn = (Button) findViewById(R.id.browserBtn);
        browserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Browser를 이용해서 버튼을 눌렀을 때 특정 URL에 접속하기
                Uri uri = Uri.parse("http://www.naver.com");
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i);
            }
        });

        Button callBtn = (Button) findViewById(R.id.callBtn);
        dialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 직접 Calling하는 activity 호출
                // 1. 사용자의 안드로이드 버전이 6 이하인지 확인
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 추가적인 보안 해제가 필요
                    // 2. 현재 앱에 대해 사용자 권한 중 전화 걸기 기능이 설정되어 있는가?
                    int result = checkSelfPermission(Manifest.permission.CALL_PHONE);
                    if (result == PackageManager.PERMISSION_DENIED) {
                        // 전화 걸기 기능에 대한 보안 설정이 되어있지 않음
                        // 3. 한 번이라도 전화 걸기에 대한 권한 설정을 거부한 적이 있는가?
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                            // 거부한 적이 있음
                            // 다시 dialog를 띄워서 권한 재설정 요청
                            // dialog 생성
                            AlertDialog.Builder builder = new AlertDialog.Builder(IntentTestActivity.this);
                            builder.setTitle("권한 필요");
                            builder.setMessage("전화 걸기 기능이 필요합니다. 수락할까요?");
                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                                }
                            });
                            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            // dialog 실행
                            builder.show();
                        } else {
                            // 사용자 요청이 필요한 권한 list를 작성
                            // 사용자가 수락시 권한 받아옴
                            // 최종적으로 수행되는 함수 : requestPermissions
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                        }
                    } else {
                        // 전화 걸기 기능에 대한 보안 설정이 되어있음
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_CALL);
                        i.setData(Uri.parse("tel:01000000000"));
                        startActivity(i);
                    }
                } else {
                    // 안드로이드 버전 6 미만은
                    // <uses-permission android:name="android.permission.CALL_PHONE"/> 설정만으로 바로 실행 가능
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:01000000000"));
                    startActivity(i);
                }

            }
        });
    }

    //  requestPermissions(new String[] {Manifest.permission.CALL_PHONE},1000);
    // 사용자의 답(grantResults) 에 따라 callback 함수 자동 호출
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:01000000000"));
                startActivity(i);
            }
        }
    }
}
