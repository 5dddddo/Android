# Broadcast Receiver_2 (08/09)

#### Broadcast Receiver란?

- 안드로이드의 **3번째 component**

- Broadcast Receiver는 Broadcast를 청취하는 component

- Broadcast Receiver는 사용자와의 대면은 하지 않음

- Broadcast Receiver 정의하는 2가지 방법

  1. AndroidManifest.xml에 등록해서 시스템에게 알리고

     Activity도 없게 만들기

  2. manifest 등록 방식이 아닌 소스 코드에서

     registerReceiver()를 사용해 시스템에 등록하기

     -> Activity 안에서 Broadcast msg를 전달받아

     ​	바로 다른 작업 수행할 수 있음



-----------------

#### 2번 방식으로 Broadcast Receiver를 정의해보자!



#### MainActivity

``` java
Button btn16 = (Button) findViewById(R.id.btn16);
    btn16.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // 묵시적 INTENT
            Intent i = new Intent();
            i.setAction("START_BROADCAST_ACTIVITY");
            startActivity(i);
    	}
	});
```



#### AndroidManifest.xml

``` java
<activity android:name=".BroadcastTestActivity">
            <intent-filter>
                <action android:name="START_BROADCAST_ACTIVITY" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
</activity>
```



#### BroadcastTestActivity

``` java
public class BroadcastTestActivity extends AppCompatActivity {

    // 필드에 BroadcastReceiver 선언
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_test);
        
        // Broadcast Receiver 등록 버튼
        Button registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Broadcast Receiver를 생성
                // 먼저 Broadcast Receiver가 어떤 Broadcast를 청취할 수 있는지를
                // 나타내는 intent filter가 생성

                // AndroidManifest.xml에 <intent-filter> 태그로 정의한 것을
                // java 코드로 정의
                // 이 filter가 추가한 action을 가진 braodcast를 잡겠다는 의미
                IntentFilter filter = new IntentFilter();
                filter.addAction("MY_BROADCAST");
                // 안드로이드 시스템에서 나오는 여러가지 정해져있는
                // Broadcast를 catch 할 수 있음

                // BroadcastReceiver() : 추상 method , 직접 객체 생성 불가
                // override 필수
                // 안드로이드의 3번째 component인  Broadcast Receiver를 생성
                receiver = new BroadcastReceiver() {
                    @Override
                    // 신호를 잡았을 때 배터리 양, 네트워크 정보 등
                    // 여러가지 부가 정보가 intent에 담겨서 전달
                    public void onReceive(Context context, Intent intent) {
                        // Broadcast를 잡았을 때 처리해야 할 코드 작성
                        
						// -------------------------------------------
                    	// Notification을 사용해보자
                  		// 아래 설명 참고
                	   	// -------------------------------------------
                       
                    }
                };
                // BroadcastReceiver 객체가 있다고 Broadcast(신호)를 catch 하는 것 X
                // Receiver를 Activity(context)에 등록해야 catch 가능
                registerReceiver(receiver, filter);
            }
        });

        // Broadcast Receiver 해제 버튼
        Button unregisterBtn = (Button) findViewById(R.id.unregisterBtn);
        unregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 버튼이 클릭되면 receiver의 등록을 해제해줌
                // 현재 등록이 되어 있는지를 확인한 후 등록 되어있는 경우에만 해제
                if (receiver != null) {
                    unregisterReceiver(receiver);
                }
            }
        });

        // Broadcast 발생 버튼
        Button sendSignalBtn = (Button) findViewById(R.id.sendSignalBtn);
        sendSignalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction("MY_BROADCAST");
                sendBroadcast(i);
            }
        });
    }
}

```



## Notification

- 화면 상단에 정보를 표시하여 사용자에게 알림

- Oreo (8.0) 버전부터 Builder 객체를 만드는 방법이 바뀜

  - Oreo 버전부터는 반드시 NotificationChannel 객체를 생성해서
    NotificationManager에  createNotificationChannel() 설정, 알림 채널을 지정해줘야 함
  - NotificationChannel의 channelID와 channelName : 채널 구분 / Notification : 중요도 설정해줘야 함

- Builder 객체의 build() 호출시 Notification 객체가 생성 됨

- PendingIntent는 Intent와 유사하지만 시스템에서 대기하는 역할을 함

  원하는 상황이 만들어질 때까지 보관되었다가 원하는 상황이 발생하면 시스템에 의해 해석되고 처리됨

  - 여기서는 Notification이 생성될 때까지 대기했다가

     Notification이 생성되면 알림창을 눌렀을 때 처리할 동작을 설정하도록 함 

- 순서

  1. NotificationManager 시스템 서비스를 이용해 화면 상단에 알림 띄움

  2. 알림을 띄우기 위해 Notification 객체가 필요한데

     이 객체는 NotificationCompat.Builder 객처를 이용해 만듦

  3. Notification 생성

     

``` java
// Notification을 사용해보자

// 1. Notification manager 객체 획득하기
NotificationManager nManager = (NotificationManager) context.
    getSystemService(Context.NOTIFICATION_SERVICE);

// 2. channelID와 channelName => 채널 구분,  Notification 중요도 설정
// 안드로이드 버전 oreo 8.0 이상 추가된 기능
// 구/신버전 나눠서 코드 작성
String channelId = "MY_CHANNEL";
String channelName = "MY_CHANNEL_NAME";
// Notification 중요도에 따라 팝업창으로 알려주기도 함
int important = NotificationManager.IMPORTANCE_HIGH;

// 3. Oreo 버전 이상에서는 반드시 channel 객체를 생성해서
// NotificationManager에 설정까지 해줘야 함
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    NotificationChannel channel = new NotificationChannel(
        channelId, channelName, important);
    nManager.createNotificationChannel(channel);
}

// 4. Notification을 생성
NotificationCompat.Builder builder =
    new NotificationCompat.Builder(context, channelId);

// 5. Intent를 하나 생성 -> 나중에 notification을 클릭했을 때
// 화면에 notification을 띄운 App의 Activity를 보여주기 위한 용도
Intent nIntent = new Intent(context, BroadcastTestActivity.class);

// 메모리상에 이미 존재하는 Activity면 가져다 쓰고
// 없으면 메모리에 띄움
nIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
nIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

// 해당 intent에 대한 signal을 받기 위한 변수
int requestID = 10;

// PendingIntent는 intent를 가지고 있는 intent
// intent의 수행을 지연시키는 역할을 하는 intent
PendingIntent pIntent = PendingIntent.getActivity(context,
                                                  requestID, nIntent, PendingIntent.FLAG_UPDATE_CURRENT);

builder.setContentTitle("제목부분이에요~!")
    .setContentText("여기는 내용이에요")
    // Notification을 터치 했을 때 사라지도록 처리
    .setAutoCancel(true)
    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
    // 별 모양의 아이콘
    .setSmallIcon(android.R.drawable.btn_star)
    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.potato))
    .setContentIntent(pIntent);

// NotificationManager를 통해
// 위에서 설정한 Notification을 실제로 실행

nManager.notify(0, builder.build());
```

