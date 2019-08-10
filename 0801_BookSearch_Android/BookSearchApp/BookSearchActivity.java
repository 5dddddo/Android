package com.example.androidsample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// 독자적인 thread로 수행시키기 위해 Runnable interface 구현함
class SearchTitleRunnable implements Runnable {

    private String keyword;
    private Handler handler;

    SearchTitleRunnable(Handler handler, String keyword) {
        //thread에 keyword 전달
        this.handler = handler;
        this.keyword = keyword;
    }

    @Override
    public void run() {
        // keyword를 이용해서 web program에 접속한 후 결과를 받아오기!
        // 결과로 받아온 JSON 문자열을 이용해서 ListView에 출력해야 함
        // 문제 ) 외부 Thread이기 때문에 UI를 제어할 수 없음
        // 해결 ) Handler를 이용해서 UI Thread에 ListView에 사용할 데이터 넘김

        // localhost -> app을 실행하는 휴대폰의 ip 주소를 지칭
        // 실제 server의 ip를 입력해야 함
        String url = "http://Device_IP_ADDRESS:PORT/bookSearch/searchTitle?keyword=" + keyword;

        // Network code는 예외처리가 필요 -> try-catch
        try {
            // url 문자열을 URL 객체로 만듦
            // HttpURLConnection에 URL 객체를 넘겨서 실제 url에 연결할 수 있음
            URL urlObj = new URL(url);

            // Network 연결 객체 : HttpURLConnection
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            // Network 연결이 성공한 후
            // 데이터를 주고 받기 위한 데이터 연결 통로 Stream을 생성할 수 있음

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            // con.getInputStream() -> 구형 stream
            // new InputStreamReader(con.getInputStream()) -> upgrade stream
            // new BufferedReader(new InputStreamReader(con.getInputStream())); -> 향상된 Stream

            String input = "";
            StringBuffer sb = new StringBuffer();
            // stream을 통해 data 읽어오기
            while ((input = br.readLine()) != null) {
                sb.append(input);
            }

            //Log.i("DATA",sb.toString());
            // 얻어온 결과 json 문자열을 jackson library를 이용해서
            // java 객체 형태 (String[])로 변형

            ObjectMapper mapper = new ObjectMapper();

            //jackson library를 이용하여 json 문자열을 String[] 형태로 변환
            String[] resultArr = mapper.readValue(sb.toString(), String[].class);

            Bundle bundle = new Bundle();
            // Bundle 바구니에 key,value 형식으로 데이터 담음
            bundle.putStringArray("BOOKARRAY", resultArr);

            // handler의 msg를 통해 전달
            Message msg = new Message();
            msg.setData(bundle);
            handler.sendMessage(msg);

        } catch (Exception e) {
            Log.i("DATAERROR", e.toString());
        }
    }
}

public class BookSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);

        final EditText keywordEt = (EditText) findViewById(R.id.keywordEt);
        Button searchBtn = (Button) findViewById(R.id.searchBookBtn);
        final ListView lv = (ListView) findViewById(R.id.lv);

        final Handler handler = new Handler() {
            // 외부 Thread에서 handler에게 msg가 전달되면 아래의 method가 자동으로 callback  됨
            // 메인 thread와 외부 thread가 handler를 통해 데이터를 주고 받아야 하기 때문에
            // 외부 Thread 생성시 Handler를 인자로 넘겨줘야 함
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String[] result = bundle.getStringArray("BOOKARRAY");

                // adapter 라는 객체는 데이터를 가져다가 view를 그리는 역할을 담당
                // 이미 정의 되어있는 android.R.layout.simple_list_item_1 형태로 listview에 찍기
                ArrayAdapter adapter = new ArrayAdapter(BookSearchActivity.this,
                        // android.R.layout.simple_list_item_1의 TextView 형태로 만들어서 찍기
                        android.R.layout.simple_list_item_1, result);
                lv.setAdapter(adapter);
            }
        };

        // 웹서버에 접속해서 데이터를 받아온 후 해당 데이터를 Listview에 세팅
        // UI Thread (Activity Thread, Main Thread) 에서는 network 연결을 할 수 없음
        // -> 시간이 오래걸리기 때문에 사용자의 interrupt에 대한 처리를 놓칠 수 있음

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자가 입력한 keyword를 가지고 Thread를 파생
                SearchTitleRunnable runnable = new SearchTitleRunnable(handler, keywordEt.getText().toString());
                // Thread 생성
                Thread t = new Thread(runnable);
                // Thread 시작
                t.start();
            }
        });


    }

}
