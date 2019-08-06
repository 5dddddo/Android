package com.example.androidsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class KAKAOMAPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakaomap);

        MapView map = new MapView(this);
        // ViewGroup : linearlayout과 같이 눈에 보이는 component들을 담는 클래스
        ViewGroup group = (ViewGroup)findViewById(R.id.mapll);

        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(37.501386, 127.039644);
        map.setMapCenterPoint(mapPoint,true);
        group.addView(map);

    }
}
