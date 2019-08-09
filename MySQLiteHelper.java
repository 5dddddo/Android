package com.example.androidsample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// Override 뿐만 아니라 생성자도 다시 작성해 줘야 함

public class MySQLiteHelper extends SQLiteOpenHelper {

    public MySQLiteHelper(Context context){
        // 상위 클래스 SQLiteOpenHelper class의 생성자 호출
        // 인자 4개 짜리 : context, DB 관련 데이터 파일 이름,
        super(context, "Member.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 초기 DB 세팅 코드가 들어감
        // 테이블을 생성하고 초기 데이터 insert하는 작업 수행

        // execSQL() : ResultSet을 가져오지 않는 SQL 구문 실행
        // member Table 생성
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS member(userName TEXT, userAge INTEGER);");

        // 데이터입력
        sqLiteDatabase.execSQL("INSERT INTO member VALUES('홍길동',30);");
        sqLiteDatabase.execSQL("INSERT INTO member VALUES('오길동',10);");
        sqLiteDatabase.execSQL("INSERT INTO member VALUES('이길동',40);");
        Log.i("DatabaseExam", "Helper의 onCreate() 호출");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
