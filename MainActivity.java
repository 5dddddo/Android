package com.example.androidsample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // btn1 : Event Source
        Button btn1 = (Button) findViewById(R.id.btn1);   // id를 가지고 view 객체를 (우리 눈에 보이는 컴포넌트들)을 찾아올거야.
        // id 라는 필드안에 뭉쳐있고, 스태틱 필드에서 btn1을 가져옴
        // 뭔 타입인지 모르고 가져오기 때문에 앞에 캐스팅 해줘야함!
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            // 클래스를 따로 안만들고 내부적으로 처리 ( 익명의(어나니모스) 클래스 )
            public void onClick(View view) {            // 외부에 명시적이냐 익명 내부클래스냐의 차이. 익명 내부가 일반적
                Intent i = new Intent();                 // 인터페이스라 오버라이딩 해야함
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.LinearLayoutExampleActivity");
                i.setComponent(cname);  // 인텐트에 정보를 실어줌
                startActivity(i);       // 그 정보를 이용해 액티비티 스타트
            }
        });

        // btn2 : Event Source
        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            // 클래스를 따로 안만들고 내부적으로 처리 ( 익명의(어나니모스) 클래스 )
            public void onClick(View view) {

                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.ChattingActivity");
                i.setComponent(cname);  // 인텐트에 정보를 실어줌
                startActivity(i);       // 그 정보를 이용해 액티비티 스타트
            }
        });

        // btn3 : Event Source
        Button btn3 = (Button) findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            // 클래스를 따로 안만들고 내부적으로 처리 ( 익명의(어나니모스) 클래스 )
            public void onClick(View view) {
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.ImageActivity");
                i.setComponent(cname);  // 인텐트에 정보를 실어줌
                startActivity(i);       // 그 정보를 이용해 액티비티 스타트
            }
        });

        // btn4 : Event Source
        Button btn4 = (Button) findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            // 클래스를 따로 안만들고 내부적으로 처리 ( 익명의(어나니모스) 클래스 )
            public void onClick(View view) {
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.TouchActivity");
                i.setComponent(cname);  // 인텐트에 정보를 실어줌
                startActivity(i);       // 그 정보를 이용해 액티비티 스타트
            }
        });

        Button btn5 = (Button) findViewById(R.id.btn5);
        btn5.setOnClickListener(new View.OnClickListener() {
            // this : event 처리 객체 -> Listener 객체를 지칭
            @Override
            public void onClick(View view) {
                // EditText의 인자로 context를 넘겨줘야 함
                // Activity가 Context 객체를 상속하고 있기 때문에 is a 관계임
                // But, 현재 사용하고 있는 객체 : this == Listener 객체
                // MainActivity.this로 MainActivity 객체로 지정함
                final EditText et = new EditText(MainActivity.this);
                // AlertDialog 생성하기
                final AlertDialog.Builder dialog =
                        new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Activity에 데이터 전달");
                dialog.setMessage("전달할 내용을 입력하세요!!");
                dialog.setView(et);
                dialog.setPositiveButton("Activity 호출", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Activity 호출 버튼을 눌렀을 때 수행 -> 다른 Activity 호출하기
                        Intent intent = new Intent();
                        ComponentName cname = new ComponentName("com.example.androidsample",
                                "com.example.androidsample.SecondActivity");
                        intent.setComponent(cname);
                        intent.putExtra("sendMsg", et.getText().toString());
                        intent.putExtra("anoterMsg", "같이가~!");
                        startActivity(intent);
                    }
                });
                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // 취소 버튼을 눌렀을 때 Activity 종료

                    }
                });
                dialog.show();

            }
        });


        Button btn6 = (Button) findViewById(R.id.btn6);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.DataFromActivity");
                i.setComponent(cname);
                // second Activity에서 값 받아오기 위해 startActivityForResult(intent, requestCode : 누가 보냈는지 확인하는 번호) 사용
                startActivityForResult(i, 3000);

            }
        });

        Button btn7 = (Button) findViewById(R.id.btn7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.ANRActivity");
                i.setComponent(cname);
                // second Activity에서 값 받아오기 위해 startActivityForResult(intent, requestCode : 누가 보냈는지 확인하는 번호) 사용
                startActivity(i);

            }
        });

        Button btn8 = (Button) findViewById(R.id.btn8);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.NoCounterActivity");
                i.setComponent(cname);
                // second Activity에서 값 받아오기 위해 startActivityForResult(intent, requestCode : 누가 보냈는지 확인하는 번호) 사용
                startActivity(i);

            }
        });

        Button btn9 = (Button) findViewById(R.id.btn9);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.CounterActivity");
                i.setComponent(cname);
                // second Activity에서 값 받아오기 위해 startActivityForResult(intent, requestCode : 누가 보냈는지 확인하는 번호) 사용
                startActivity(i);

            }
        });
        Button btn10 = (Button) findViewById(R.id.btn10);
        btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.BookSearchActivity");
                i.setComponent(cname);
                // second Activity에서 값 받아오기 위해 startActivityForResult(intent, requestCode : 누가 보냈는지 확인하는 번호) 사용
                startActivity(i);

            }
        });

        Button btn11 = (Button) findViewById(R.id.btn11);
        btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.CustomBookSearchActivity");
                i.setComponent(cname);
                // second Activity에서 값 받아오기 위해 startActivityForResult(intent, requestCode : 누가 보냈는지 확인하는 번호) 사용
                startActivity(i);

            }
        });

        Button btn12 = (Button) findViewById(R.id.btn12);
        btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 명시적 인텐트 ( Explicit Intent )
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.IntentTestActivity");
                i.setComponent(cname);
                // second Activity에서 값 받아오기 위해 startActivityForResult(intent, requestCode : 누가 보냈는지 확인하는 번호) 사용
                startActivity(i);

            }
        });

        Button btn13 = (Button) findViewById(R.id.btn13);
        btn13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 지도를 표현할 Activity 실행
                // 명시적 인텐트 ( Explicit Intent )
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.KAKAOMAPActivity");
                i.setComponent(cname);
                startActivity(i);

            }
        });

        Button searviceStartBtn = (Button) findViewById(R.id.searviceStartBtn);
        searviceStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Service 객체를 생성
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.LifeCycleService");
                i.setComponent(cname);
                i.putExtra("ActivityToServiceData", "소리없는 아우성");
                startService(i);


            }
        });

        Button searviceStopBtn = (Button) findViewById(R.id.searviceStopBtn);
        searviceStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Service 객체를 생성
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.LifeCycleService");
                i.setComponent(cname);
                i.putExtra("ActivityToServiceData", "소리없는 아우성");
                stopService(i);


            }
        });

        Button btn14 = (Button) findViewById(R.id.btn14);
        btn14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 명시적 인텐트 ( Explicit Intent )
                Intent i = new Intent();
                ComponentName cname = new ComponentName("com.example.androidsample",
                        "com.example.androidsample.KakaoSearchActivity");
                i.setComponent(cname);
                startActivity(i);

            }
        });
    }


    // second activity가 종료되면 호출되는 함수
    // second activity에서 넘긴 requestcode, resultcode, intent 모두 자동으로 전달 됨
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3000 && resultCode == 5000) {
            String result = data.getExtras().getString("DATA");
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }

    }

    // Service에서 Intent 데이터가 return 되는 순간 callback 함수 onNewIntent()가 호출 됨
    // Intent 데이터는 인자값으로 받음
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String msg= intent.getExtras().getString("ServiceToActivityData");
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}