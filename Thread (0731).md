# Thread (07/31)

문제 1 : 액티비티 내에서 시간이 오래 걸리는 로직(DB 연결, Network 연결 등)을 수행하면

사용자의 요청 (Toast button)을 즉각적으로 처리할 수 없게 되어 ANR 오류가 발생함

``` java
public class ANRActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anr);
        
        final TextView tv = (TextView) findViewById(R.id.countTv);
        Button countBtn = (Button) findViewById(R.id.countBtn);
        Button toastBtn = (Button) findViewById(R.id.ToastBtn);


        countBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Activity 단에서는 logic 연산 X
                // logic을 처리하다가 사용자의 요청을 수행 못하는 경우가 발생하면 안 됨!
                // logic 연산은 Thread를 파생하여 처리해야 함
                long sum = 0;
                for (long i = 0; i < 1000000000; i++) {
                    sum += i;

                }
                tv.setText("총합은 " + sum);
            }
        });
        toastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ANRActivity.this,
                        "Toast가 실행됨", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```



=> 해결 1 : 로직 부분을 외부 Thread에서 처리하도록 따로 빼주고

Activity는 사용자의 요청에 즉각적으로 반응할 수 있도록 함

``` java
// Thread 만드는 방법 1
// Thread class를 직접 상속받아서 생성하는 방법
//class mySum extends Thread{
// 객체 지향 관점에서 상속의 단점 때문에 사용하지 않음
//}

// Thread 만드는 방법 2
// 특정 interface(Runable)를 구현하는 방식으로 class를 만들어서
// 이 class의 객체를 파생시키고 Thread의 instance에 인자로 넘겨 Thread를 생성하는 방법
class mySum implements Runnable {
    private TextView tv;
    // Injection
    mySum(TextView tv) {
        this.tv = tv;
    }
    @Override
    public void run() {
        // Thread가 실행이 되면 수행되는 코드를 여기에 작성
        long sum = 0;
        for (long i = 0; i < 1000000000; i++) {
            sum += i;
        }
        // 외부 Thread에서는 화면 제어를 할 수 없음!
        tv.setText("총합은 " + sum);
    }
}

public class ANRActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anr);

        final TextView tv = (TextView) findViewById(R.id.countTv);
        Button countBtn = (Button) findViewById(R.id.countBtn);
        Button toastBtn = (Button) findViewById(R.id.ToastBtn);
        
        countBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Runable interface를 구현한 객체일 뿐 Thread가 생성되지는 않음
                mySum mysum = new mySum(tv);
                
                // Thread 객체를 파생시켜야 함
                // Thread를 실제로 생성하는 코드
                Thread t = new Thread(mysum);
                t.start();
                // non-blocking method
                // 함수가 끝나기 전에 코드 계속 진행 됨 , 비순차적 처리 수행
                // Thread는 새로운 실행 흐름을 만들어 낼 수 있음
                // 동시에 여러 프로그램을 수행하는 것처럼 동작
                // Multi-processer에서는 동시에 여러 프로그램 수행 가능!
            }
        });
        toastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ANRActivity.this,
                        "Toast가 실행됨", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

```



문제 2 : 화면 제어는 외부 Thread에서 할 수 없고 무조건 Activity 에서만 제어해야 함!

``` java
class mySum implements Runnable {
     @Override
    protected void onCreate(Bundle savedInstanceState) {
   		...
		tv.setText("총합은 " + sum);
        ...
}
```



=> 해결 2 : UI 제어는 UI 관련 Thread를 파생하거나 Activity에서 해야 함



# UI Thread

문제 2 : 화면 제어는 외부 Thread에서 할 수 없음

``` java
package com.example.androidsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;
class MyCount implements Runnable{
    private TextView tv ;
    MyCount(TextView tv){
        this.tv = tv;
    }
    @Override
    public void run() {
        // 1초마다 카운트를 증가시켜서 TextView에 출력하기
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                tv.setText("count : "+ i);
            } catch (Exception e) {

            }
        }
    }
}
public class NoCounterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_counter);

        final TextView tv = (TextView) findViewById(R.id.counterTv);
        Button startBtn = (Button) findViewById(R.id.startBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCount counter = new MyCount(tv);
                Thread t = new Thread(counter);
                t.start();
            }
        });
    }
}
```



=> 해결 2 : UI Thread(Activity)와 외부 Thread 간 msg를

​				   handler라고 하는 중간 매개체를 통해 데이터를 주고 받으며 제어해야 함

``` java
class MyCount1 implements Runnable{
    private Handler handler;

    MyCount1(Handler handler){
        this.handler = handler;
    }
    @Override
    public void run() {
        // 1초마다 카운트를 증가시켜서 TextView에 출력하기
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
                // Bundle : 데이터를 묶을 수 있는 단위, hashMap 구조 (key, value)
                Bundle bundle = new Bundle();
                bundle.putString("count", i+"");

                // bundle을 msg에 붙여서 보내야 하기 때문에 msg 객체 생성
                Message msg = new Message();
                msg.setData(bundle);

                // Handler를 이용해 UI에 뿌릴 데이터를 msg에 담아
                // UI Thread(Activity)로 전송
                // 보낸 msg는 handleMessage()가 받아서 처리
                handler.sendMessage(msg);
            } catch (Exception e) {

            }
        }
    }
}
public class CounterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        final TextView tv1 = (TextView) findViewById(R.id.counterTv1);
        Button startBtn1 = (Button) findViewById(R.id.startBtn1);


        final Handler handler = new Handler(){
          // Handler는 interface가 아니고 class라서 꼭 override 할 필요 없지만
          // 따로 class 만들 필요 없이 선언과 동시에 handler의 동작 정의
          // msg를 받는 순간 아래 method가 호출 됨
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                Bundle b = msg.getData();
                tv1.setText(b.getString("count"));
            }
        };
        startBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Thread에 handler 객체 넘김
                MyCount1 counter = new MyCount1(handler);
                Thread t = new Thread(counter);
                t.start();
            }
        });
    }
}
```





1. MySQL DB Setting (도서 정보)

-> 제공된 Script file을 이용해서 db 세팅

2. java Servlet으로 DB access program 작성

   => 입력 : 책 제목의 keyword

   => 출력 : 책 제목 리스트 (json)

3. android에서 java network 기능 중 HTTP request 호출 기능을 이용하여

   Servlet 호출 후 json 받기

4. json 형태로 데이터를 받아온 후 Parsing 해서 ListView에 출력



