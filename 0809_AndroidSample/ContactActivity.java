package com.example.androidsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        final TextView tv = (TextView)findViewById(R.id.contactTv);
        Button contactBtn = (Button)findViewById(R.id.contactBtn);
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 보안관련 코드가 나와요!
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 마쉬멜로우 버전 6 보다 높은 경우
                    if(checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                        // 이전에 허용을 한 적이 없는 경우
                        // 사용자가 허가를 받아야 해요!!
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},1111);
                    }else {
                        // 이전에 이미 허용버튼을 누른 경우
                        ContentResolver cr = getContentResolver();
                        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // com. 이랑 다르게 정해져 있는것임.
                        // content: // com.text.data/Member
                        Cursor c = cr.query(uri,null,null,
                                null,null);
                        String result = "";
                        while(c.moveToNext()){
                            result += c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            result += ", ";
                            result += c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            result += "\n";
                        }
                        final TextView tv = (TextView)findViewById(R.id.contactTv);
                        tv.setText(result);
                    }
                }else{
                    // 마쉬멜로우 버전 6 보다 낮을 경우
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1111) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // 사용자가 주소록 접근에 대한 권한 요청에 허용을 누르면
                // ContentResolver를 이용해서 주소록에 접근!!
                ContentResolver cr = getContentResolver();
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // com. 이랑 다르게 정해져 있는것임.
                // content: // com.text.data/Member
                Cursor c = cr.query(uri,null,null,
                        null,null);
                String result = "";
                while(c.moveToNext()){
                    result += c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    result += ", ";
                    result += c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    result += "\n";
                }
                final TextView tv = (TextView)findViewById(R.id.contactTv);
                tv.setText(result);
            }
        }
    }
}