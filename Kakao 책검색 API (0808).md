# Kakao 책검색 API를 이용한 App 구현 (08/08)

### 1. 전체적인 구조

![1565261128598](https://user-images.githubusercontent.com/50972986/62771365-01f44a00-bad8-11e9-95a8-88fde3af6b16.png)



### 2. KAKAO REST API (책 검색) 이용하기

1. KAKAO Developers - [앱 만들기] 
2. REST API 키 얻기
3. curl -> java로 바꿔야 함!

``` 
curl -v -X GET "https://dapi.kakao.com/v3/search/book?target=title" \
--data-urlencode "query=미움받을 용기" \
-H "Authorization: KakaoAK MY_REST_API_KEY"
```

--------------------------------------



### 3. Android 구현

1. Empty project 생성

2. JSON 데이터 처리를 위한 jackson library를 사용하기 위해

   Project - app - libs 폴더에

   > jackson-annotations-2.8.1.jar
   >
   > jackson-core-2.8.1.jar
   >
   > jackson-databind-2.8.1.jar

   추가 후 마우스 우클릭 - [Add as library] 클릭
   
![1565336085803](https://user-images.githubusercontent.com/50972986/62771415-1cc6be80-bad8-11e9-81b9-f81596939bcf.png)



3. AndroidManifest.xml 

   - 인터넷 사용 권한

   ``` xml
   <uses-permission android:name="android.permission.INTERNET" />
   ```

4. **MainActivity의 onCreate() 함수**

   ``` java
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       // final : 이벤트 처리 시점에 memory 상에 남아있도록 상수 처리
       final ListView lv = (ListView) findViewById(R.id.lv);
       Button searchBtn = (Button) findViewById(R.id.searchBtn);
       final EditText keywordEt = (EditText) findViewById(R.id.keywordEt);
       // Anonymous inner class를 이용한 Event 처리
       // Android의 전형적인 event 처리 방식
       // 객체지향적 프로그래밍은 아님!
       searchBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               // 버튼을 눌렀을 때 서비스를 생성하고 실행
               Intent i = new Intent();
               // 명시적 Intent 사용
               ComponentName cname = new ComponentName("com.example.kakaobooksearch",
     "com.example.kakaobooksearch.KAKAOBookSearchService");
               i.setComponent(cname);
               // 사용자가 EditText에 입력한 책 이름을 
               // intent에 담아서 Service에 전달
               i.putExtra("searchKeyword", keywordEt.getText().toString());
               // 서비스 호출
               startService(i);
           }
       });
   }
   ```

   

5. **KAKAOBookSearchService**

   - [File] - [New] - [Service] - [Service] 

   - `public class KAKAOBookSearchService extends Service `

     : Service 상속 후 override

     

   - **onCreate()** 함수

   ``` java
   // 서비스 객체가 만들어지는 시점에 1번 호출
   // 사용할 resource를 준비하는 과정
   @Override
   public void onCreate() {
       super.onCreate();
   }
   ```

   

   - **ononStartCommand()** 함수

   ``` java
   // onCreate() 후에 자동적으로 호출되며
   // startService()에 의해서 호출 됨
   // 실제 로직처리 진행
   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
       // 전달된 키워드를 이용해서
       // 외부 네트워크 접속을 위한 Thread를 생성
       String keyword = (String)intent.getExtras().getString("searchKeyword");
       // Thread를 만들기 위한 Runnable 객체부터 생성
       BookSearchRunnable runnable = new BookSearchRunnable(keyword);
       // Thread 생성
       Thread t = new Thread(runnable);
       // Thread 실행
       t.start();
       // App 강제 종료시 service가 재시작하지 않도록 설정
       // resource 낭비를 막음
       return Service.START_NOT_STICKY;
   }
   ```

   

   - **BookSearchRunnable Class**

     : MainActivity로부터 넘겨 받은 keyword로 KAKAO API를 이용해

     책을 검색하여 얻은 JSON 문자열을 KAKAOBookVO 객체에 담아서 Activity로 전달

   ```java
   // Thread를 생성하기 위해서 runnable interface를 구현한 class 정의
   // Activity로부터 넘겨받은 context-> getApplicationContext()를 쉽게 이용하기 위해
   // KAKAOBookSearchService 내에 inner class로 정의
   private class BookSearchRunnable implements Runnable {
       private String keyword;
       BookSearchRunnable(String keyword) {
           this.keyword = keyword;
       }
       @Override
       public void run() {
           String url = "https://dapi.kakao.com/v3/search/book?target=title&query="
               + keyword;
           String myKey = "MY_REST_API_KEY";
   
           // 네트워크 처리 -> try/catch문으로 감싸줘야 함
           try {
               // 자바가 제공해주는 네트워크 API 이용
               URL urlObj = new URL(url);
               // 네트워크 연결
               HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
   
               // request GET 방식 지정
               con.setRequestMethod("GET");
               // KAKAO API 권한 설정
               con.setRequestProperty("Authorization", "KakaoAK " + myKey);
   
               // 정상적으로 설정을 하면 API 호출이 성공하고
               // 결과를 받아 올 수 있음
   			// 기본적인 Stream을 BufferedReader 형태로 생성 -> 사용하기 편한 연결 통로
               BufferedReader br = new BufferedReader(
                   new InputStreamReader(
                       con.getInputStream()
                   ));
   
               // 연결통로(Stream)를 통해서 Server의 결과를 문자열로 얻어냄
               String line = null;
               StringBuffer sb = new StringBuffer();
               while ((line = br.readLine()) != null) {
                   sb.append(line);
               }
   
               // 데이터를 다 읽은 후 통로(Stream) 닫음
               br.close();
   
               // 데이터가 JSON 형태로 정상적으로 출력되면
               // 외부 API 호출 성공
               // jackson library를 이용해서 JSON 데이터를 처리
               ObjectMapper mapper = new ObjectMapper();
   
               // 키값을 이용해서 책 정보{ documents :[] ... } 중 documents 부분만 추출
               Map<String, Object> map = mapper.readValue(sb.toString(),
                                                                                                               new TypeReference<Map<String, Object>>() {});
               Object obj = map.get("documents");
   
               // 해당 object를 다시 json 문자열 형태로 바꿈
               String resultJsonData = mapper.writeValueAsString(obj);
               Log.i("KAKAOLOG", resultJsonData);
   
               // 결과적으로 우리가 얻은 데이터의 형태는
               // [ {책 1권의 데이터}, {책 1권의 데이터}, {책 1권의 데이터},..]
               // 책 1권의 데이터를 객체화 => KAKAOBookVO class를 이용
               // 책 여러 권의 데이터는 ArrayList로 표현
               // 책 1권의 데이터는 key와 value의 쌍으로 표현
   
               ArrayList<KAKAOBookVO> myObject = mapper.readValue(resultJsonData,
               	new TypeReference<ArrayList<KAKAOBookVO>>() {});
   
               // URL 형태로 전달받은 thumbnail을 bitmap 형태로 바꿔서 저장
               for(KAKAOBookVO vo : myObject){
                   vo.urlToByteArray();
               }
               
               // 정상적으로 객체화가 되었으면 Intent에 
               // 해당 데이터를 붙여서 Activity에게 전달
               // getApplicationContext() : 현재 Service context
               Intent i = new Intent(getApplicationContext(),MainActivity.class);
   
               // 만약 Activity가 메모리에 존재하면
               // 새로 생성하지 않고 기존 Activity를 이용
               i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
               // 보이지 않는 component에서 보이는 component로 데이터 전달시 task가 필요
               // 		Service       ->       Activity
               i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   
               // 전달할 데이터를 intent에 붙임
               i.putExtra("resultData",myObject);
   
               // Activity에게 데이터를 전달
               startActivity(i);
           } catch (Exception e) {
               Log.i("ERROR", e.toString());
           }
       }
   }
   ```



6. **KAKAOBookVO** Class

   - 해당 클래스의 객체가 마샬링이 가능하도록 Parcelable interface를 구현

     - writeToParcel()
     - describeContents()

     2가지 method를 overriding 해야 함 ( dedescribeContents()는 수정할 필요 X)

   - Marshalling과 Unmarshalling의 순서가 정확하게 일치해야 오류나지 않음

     - Marshalling 부분의 writeToParcel() 과

       Unmarshalling 부분의 Creator의 createFromParcel() 의 순서가 반드시 일치해야 함!

   ```java
   package com.example.kakaobooksearch;
   
   import android.graphics.Bitmap;
   import android.graphics.BitmapFactory;
   import android.os.Parcel;
   import android.os.Parcelable;
   import android.util.Log;
   import java.io.InputStream;
   import java.net.HttpURLConnection;
   import java.net.URL;
   import java.util.ArrayList;
   
   // 해당 클래스의 객체가 마샬링이 가능하도록 Parcelable interface를 구현
   public class KAKAOBookVO implements Parcelable {
       private ArrayList<String> authors;
       private String contents;
       private String datetime;
       private String isbn;
       private String price;
       private String publisher;
       private String sale_price;
       private String status;
       private String thumbnail;
       private String title;
       private ArrayList<String> translators;
       private String url;
       private byte[] thumbnailImg;
   
   
       // Default constructor
       public KAKAOBookVO() {}
   
       public KAKAOBookVO(ArrayList<String> authors, String contents, String datetime, String isbn, String price, String publisher, String sale_price, String status, String thumbnail, String title, ArrayList<String> translators, String url) {
           this.authors = authors;
           this.contents = contents;
           this.datetime = datetime;
           this.isbn = isbn;
           this.price = price;
           this.publisher = publisher;
           this.sale_price = sale_price;
           this.status = status;
           this.thumbnail = thumbnail;
           this.title = title;
           this.translators = translators;
           this.url = url;
       }
       
   	//-----------------------------------------------------------------------
       // Unmarshalling (언마샬링) 
       // 복원 작업 때 사용되는 Constuctor
       // 복원시 가장 중요한 부분은 순서
       // 마샬링 순서와 언마샬링 순서가 동일해야 함
        protected KAKAOBookVO(Parcel parcel) {
           authors = parcel.readArrayList(null);
           contents = parcel.readString();
           datetime = parcel.readString();
           isbn = parcel.readString();
           price = parcel.readString();
           publisher = parcel.readString();
           sale_price = parcel.readString();
           status = parcel.readString();
           thumbnail = parcel.readString();
           title = parcel.readString();
           translators = parcel.readArrayList(null);
           url = parcel.readString();
           thumbnailImg = parcel.createByteArray();
       }
       
       // CREATOR라고 불리는 static 상수를 반드시 정의
       public static final Creator<KAKAOBookVO> CREATOR = new Creator<KAKAOBookVO>() {
           // 마샬링된 데이터를 언마샬링(복원)할 때 사용되는 method
           @Override
           public KAKAOBookVO createFromParcel(Parcel parcel) {
               return new KAKAOBookVO(parcel);
           }
   
           // 복원할 객체 개수 -> i개
           @Override
           public KAKAOBookVO[] newArray(int i) {
               return new KAKAOBookVO[i];
           }
       };
       
   	//-----------------------------------------------------------------------
       // Marshalling (마샬링)
       // 데이터를 변환하는 역할을 하는 method
       // 직렬화가 가능한 객체로 변환시키는 method
       // 주의! 데이터를 변화하는 순서(마샬링)와 복원하는 순서(언마샬링)가 반드시 같아야 함
       @Override
       public void writeToParcel(Parcel parcel, int i) {
           try {
               parcel.writeList(authors);
               parcel.writeString(contents);
               parcel.writeString(datetime);
               parcel.writeString(isbn);
               parcel.writeString(price);
               parcel.writeString(publisher);
               parcel.writeString(sale_price);
               parcel.writeString(status);
               parcel.writeString(thumbnail);
               parcel.writeString(title);
               parcel.writeList(translators);
               parcel.writeString(url);
               parcel.writeByteArray(thumbnailImg);
           } catch (Exception e) {
               Log.i("KAKAOLOG", e.toString());
           }
       }
   	// override method
       @Override
       public int describeContents() {
           // 수정할 필요 없음
           return 0;
       }
       //-----------------------------------------------------------------------
       
       // getter & setter 생략
         
       // thumbnail을 bitmap type으로 변환하는 함수
      public void urlToByteArray() {
           Bitmap bmp = null;
           try {
               URL imgUrl = new URL(thumbnail);
               HttpURLConnection con = (HttpURLConnection) imgUrl.openConnection();
               con.setDoInput(true);
               con.connect();
               InputStream is = con.getInputStream();
               bmp = BitmapFactory.decodeStream(is);
               ByteArrayOutputStream stream = new ByteArrayOutputStream();
               bmp.compress(Bitmap.CompressFormat.JPEG,100,stream);
               thumbnailImg = stream.toByteArray();
           } catch(Exception e) {
               Log.i("KAKAOBOOKLog",e.toString());
           }
       }
   }
   ```

   

7. **MainActivity의 onNewIntent()** 함수

   - KAKAOBookSearchService의 

     BookSearchRunnable Class가 책 정보를 

     ArrayList\<KAKAOBookVO\> type으로 변환하여

     intent에 담아 전달함

   - 이 intent를 받는 callback 함수 

   ``` JAVA
   @Override
   public void onNewIntent(Intent intent) {
       super.onNewIntent(intent);
       Log.i("KAKAOLOG", "데이터가 정상적으로 Activity에 도달");
   
       // intent에서 데이터 추출해서 ListView에 출력하는 작업을 진행
       ArrayList<KAKAOBookVO> data = (ArrayList<KAKAOBookVO>) intent.getExtras().get("resultData");
   
       // 만약 그림까지 포함하려면 추가적인 작업이 더 필요
       // ListView에 도서 제목만 먼저 출력
       // 성공 시 CustomListView를 이용해서 임지, 제목, 저자, 가격 등의 데이터를 출력
       
       // ListView에 결과값을 뿌리기 위해서는 Adapter가 필요함
       // 결과값은 KAKAOBookVO type이고 이는 사용자 정의 class이기 때문에
       // Adapter도 새로 정의해줘야 함
       ListView lv = (ListView)this.findViewById(R.id.lv);
       CustomListViewAdapter adapter = new CustomListViewAdapter();
       lv.setAdapter(adapter);
   
       for (KAKAOBookVO vo : data) {
           adapter.addItem(vo);
       }
   }
   ```



8. **CustomListViewAdapter** Class

   ```java
   // 실제로 ListView에 그림을 그리는 작업 수행
   public class CustomListViewAdapter extends BaseAdapter {
   
       private List<KAKAOBookVO> list = new ArrayList<KAKAOBookVO>();
   
       // 반드시 overriding을 해아하는 method가 존재
       @Override
       public int getCount() {
           // ListView 안에 총 몇 개의 Component를 그려야 하는지를 return
           // getCount()가 호출되면
           // 내부적으로 getItemId() -> getItem() -> getView() 순으로 호출됨
           return list.size();
       }
   
       @Override
       public Object getItem(int i) {
           // 몇 번째 데이터 내용을 화면에 출력할지를 결정
           return list.get(i);
       }
   
       @Override
       public long getItemId(int i) {
           // 몇 번째 데이터를 화면에 출력할지 결정
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
           ImageView bookimg = view.findViewById(R.id.bookImg);
           TextView author = view.findViewById(R.id.authorTextView);
           TextView title = view.findViewById(R.id.titleTextView);
           TextView price =  view.findViewById(R.id.priceTextView);
           KAKAOBookVO vo = list.get(i);
   
         try {
             // ImageView에 책 이미지 설정
               Bitmap bmp = BitmapFactory.decodeByteArray(vo.getThumbnailImg(),
                       0,vo.getThumbnailImg().length);
               bookimg.setImageBitmap(bmp);
   
               StringBuilder sb = new StringBuilder();
               for(String s : vo.getAuthors()) {
                   sb.append(s);
                   sb.append(",");
               }
               author.setText(sb.toString());
               title.setText(vo.getTitle());
               price.setText(vo.getPrice());
   
           } catch(Exception e) {
               Log.i("KAKAOBOOKLog",e.toString());
           }
           return view;
       }
   
       // ListView에 뿌릴 데이터를 CustomListViewAdapter 필드에 
       // 선언한 list 변수에 담음
       public void addItem(KAKAOBookVO vo) {
           list.add(vo);
       }
   
   }
   ```



9. **listview_item.xml**

   - ListView에 데이터를 입력하는 Layout 구조를 설정

   ``` xml
   <?xml version="1.0" encoding="utf-8"?>
   <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
       android:orientation="horizontal"
       android:layout_width="match_parent"
       android:layout_height="match_parent">
       <ImageView
           android:layout_width="wrap_content"
           android:layout_height="match_parent"
           android:adjustViewBounds="true"
           android:scaleType="fitCenter"
           android:padding="20px"
           android:id="@+id/bookImg"/>
       <LinearLayout
           android:layout_width="wrap_content"
           android:layout_height="match_parent"
           android:orientation="vertical"
           android:padding="20px">
           <TextView
               android:layout_width="match_parent"
               android:layout_height="wrap_content"
               android:id="@+id/titleTextView"/>
           <LinearLayout
               android:layout_width="match_parent"
               android:layout_height="wrap_content"
               android:orientation="horizontal">
               <TextView
                   android:layout_width="wrap_content"
                   android:layout_height="match_parent"
                   android:layout_weight="1"
                   android:id="@+id/authorTextView"/>
               <TextView
                   android:layout_width="wrap_content"
                   android:layout_height="match_parent"
                   android:layout_weight="1"
                   android:id="@+id/priceTextView"/>
           </LinearLayout>
       </LinearLayout>
   </LinearLayout>
   ```

   
