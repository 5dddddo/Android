package com.example.androidsample;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SQLiteSampleActivity extends AppCompatActivity {
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_sample);

        Button selectDbBtn = (Button) findViewById(R.id.selectDbBtn);
        Button createDbBtn = (Button) findViewById(R.id.createDbBtn);
        final TextView tv = (TextView) findViewById(R.id.selecttv);
        createDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 클릭하면 DB를 생성하고 Table을 만듦
                // SQLite를 사용하기 쉽도록 도와주는 Helper class가 제공 됨
                // 이 Helper class를 직접 이용하는 것이 아니라
                // 상속받아 작성한 사용자 정의 class의 객체를 이용
                // Helper class를 작성하러 감 -> MySQLiteHelper class


                // Helper를 통해서 DB에 대한 Handler를 얻어올 수 있음
                MySQLiteHelper helper = new MySQLiteHelper(SQLiteSampleActivity.this);
                db = helper.getWritableDatabase();

            }
        });

        selectDbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DB Handle을 이용해서 DB 처리를 할 수 있음
                // raqQuery() : select 계열의 sql문을 실행 할 때 사용 됨
                // Cursor : JDBC의 Resultset과 동일한 역할, 결과 레코드 집합에 대한 handle
                Cursor c = db.rawQuery("SELECT * FROM member", null);
                String result = "";
                while (c.moveToNext()) {
                    // 한 명의 정보를 한 줄로 연결
                    result += c.getString(0);
                    result += ",";
                    result += c.getInt(1);
                    result += "\n";
                }
                // 이렇게 데이터를 다 얻어오면 해당 결과를 TextView에 출력
                tv.setText(result);
            }

        });
    }
}
