package com.example.androidsample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class BookSearchRunnable implements Runnable {
    private Handler handler;
    private String keyword;

    BookSearchRunnable(Handler handler, String keyword) {
        this.handler = handler;
        this.keyword = keyword;
    }
    @Override
    public void run() {
        String url = "http://Device_IP_ADDRESS:PORT/bookSearch/search?search_keyword=" + keyword;
        try {
            URL urlObj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
            // default method : GET 방식
            con.setRequestMethod("GET");
            // 사용하기 좋은 입력 형태로 upgrade
            // BufferedReader를 이용하면 server가 보내준 데이터를 라인 단위로 읽어 올 수 있음
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String input = "";
            StringBuffer sb = new StringBuffer();
            while ((input = br.readLine()) != null) {
                sb.append(input);
            }
            // 서버에 대한 스트림 종료
            br.close();
            // JSON 형태의 문자열을 얻음
            // JSON 문자열을 원래 객체형태로 복원
            ObjectMapper mapper = new ObjectMapper();
            BookVO[] resultArr = mapper.readValue(sb.toString(), BookVO[].class);

            for (BookVO vo : resultArr) {
                vo.drawableFromURL();
            }
            // 최종적으로 얻어낸 객체를 UI Thread (Main Thread, Activity Thread)에게 전달해야 함
            Bundle bundle = new Bundle();
            bundle.putSerializable("BOOKS", resultArr);

            Message msg = new Message();
            msg.setData(bundle);
            handler.sendMessage(msg);

            for (BookVO vo : resultArr) {
                Log.i("customListView", vo.toString());
            }

        } catch (Exception e) {
            Log.i("customListView", e.toString());
        }
    }
}

public class CustomBookSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_book_search);

        // 필요한 reference 얻어오기
        Button customBtn = (Button) findViewById(R.id.customBtn);
        final EditText customEt = (EditText) findViewById(R.id.customEt);
        final ListView customLv = (ListView) findViewById(R.id.customLv);

        // Handler를 이렇게 사용하면
        // Activity가 비정상 종료 되었을 때
        // 내부 handler가 죽지 않고 살아있어서
        // resource를 낭비하게 됨-> link handler 처리

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                // 외부 thread에서 message를 받으면 호출
                Bundle bundle = msg.getData();
                BookVO[] bookvo = (BookVO[]) bundle.getSerializable("BOOKS");

                // ListView에 Adapter를 적용해서 ListView를 그리기
                // ListView : 화면에 리스트 형태를 보여주는 widget
                // 실제로 ListView에 그림을 그리는 작업은 Adapter가 함
                CustomListViewAdapter adapter = new CustomListViewAdapter();
                customLv.setAdapter(adapter);

                // Adapter에 그려야하는 데이터 세팅
                for (BookVO vo : bookvo) {
                    adapter.addItem(vo);
                }

            }
        };

        customBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 검색 버튼이 클릭되면 호출 됨
                // 사용자가 입력한 keyword를 이용해서 network 접속을 시도
                // 외부 servlet web program을 호출해서 결과를 받아 와야 함
                // -> Thread를 이용해서 network 연결 처리를 해야 함
                // -> BookSearchRunnable class 생성

                // Thread에 입력 keyword 값과 handler 객체가 인자로 넘어가야 함
                BookSearchRunnable runnable = new BookSearchRunnable(handler, customEt.getText().toString());
                Thread t = new Thread(runnable);
                t.start();
            }
        });
    }
}
