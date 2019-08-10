package com.example.androidsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
// 실제로 ListView에 그림을 그리는 작업 수행
public class CustomListViewAdapter extends BaseAdapter {

    private List<BookVO> list = new ArrayList<BookVO>();

    // 반드시 overriding을 해아하는 method가 존재
    @Override
    public int getCount() {
        // ListView 안에 총 몇 개의 Component를 그려야 하는지를 return
        // getCount()가 호출되면
        // 내부적으로 getItemId() -> getItem() -> getView() 순으로 호출하도록 설정 되어 있음
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        // 몇 번째 데이터 내용을 화면에 출력할지를 결정
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        // 몇 번째 데이터를 화면에 출력할지를 결정
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        // 1. View 구조 생성
        // 출력할 View 객체 생성
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            // 생성된 View 객체에 xml layout을 설정
            view = inflater.inflate(R.layout.listview_item, viewGroup, false);
        }
        // 실제 데이터 세팅하기 위해 View component의 reference를 획득
        final ImageView iv = (ImageView) view.findViewById(R.id.customIv);
        TextView tv1 = (TextView) view.findViewById(R.id.customTv1);
        TextView tv2 = (TextView) view.findViewById(R.id.customTv2);
        Thread t;
        // 2. 실제 데이터를 View에 입력
        // 화면에 출력할 데이터를 가져옴
        BookVO vo = list.get(i);
        tv1.setText(vo.getBtitle());
        tv2.setText(vo.getBauthor());
        iv.setImageDrawable(vo.getDrawable());
        return view;
    }

    public void addItem(BookVO vo) {
        list.add(vo);
    }

}
