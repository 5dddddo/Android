# 0906_Service Binding

## Service

- 단말이 항상 실행되어 있는 상태로 다른 단말과 데이터를 주고받거나

  필요한 기능을 백그라운드에서 실행하기 위한 앱의 Component( => 시스템이 관리 )

- 새로 만든 서비스는 Manifest에 반드시 등록하여 앱 설치 시에 시스템이 알 수 있게 함

- 화면 없이 동작

- 실행된 상태를 계속 유지하기 위해 서비스가 비정상적으로 종료되더라도 시스템이 자동으로 재실행함

- 

<br>

### 실행 원리

#### 1. MainActivity -> Service로 intent 전달

``` java
// MainActivity
Intent intent = 
    // Intent 생성자의 인자
    // MainActivity -> Service로 intent 전달하는 방법
    // 출발 -> getApplicationContext() : 현재 Context
    // 도착 -> Intent를 넘길 Service명.class
    new Intent(getApplicationContext(),Service명.class)
    
    // intent에 데이터 담기
    showIntent.putExtra(TAG, 객체);
    startService(intent);
```

- 서비스를 실행하려면 Activity에서 startService() 메서드로 호출

  - 어떤 서비스를 실행할 것인지에 대한 정보가 담긴 intent 객체를 인자로 전달
  - startService(intent)에 담은 intent 객체는 Service의 onStartCommand() 메서드로 전달

- 시스템은 서비스를 시작시킨 후 intent 객체를 서비스에 전달

- 최초 startService() 호출 시

  - 서비스가 메모리에 만들어짐
  - 시스템에서 서비스의 onCreate()를 실행함

- 그 후 startService 호출 시

  - 서비스가 이미 실행중이면 startService()를 여러 번 호출해도 메모리에 만들어진 상태로 유지

  - startService()는 서비스를 시작하는 목적 외에 intent를 전달하는 목적으로도 사용 됨

  - 시스템에서 서비스의 onStartCommand()를 실행함

    onStartCommand() : 서비스로 전달된 intent 객체를 처리하는 메서드

    <br>

#### 2. MainActivity에서 전달받은 intent 처리 후

#### 	Service -> MainActivity로 intent 전달

``` java
// Service

@Override
public int onStartCommand(Intent intent, int flags, int startId){
    if(intent == null)
    // 서비스 비정상 종료 -> 시스템에 의해 자동 재시작
    	return Service.START_STICKY;
    // 
    
    // 서비스 정상 수행
    else{
        ... 
        // Service -> MainActivity로 intent 전달
        Intent showIntent = new Intent(getApplicationContext(), MainActivity.class);
        
        showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
                            Intent.FLAG_ACTIVITY_SINGLE_TOP|
                            Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Intent에 데이터 담기
        showIntent.putExtra(TAG, 객체);
        startActivity(showIntent)
    }
}
```

- Intent에 FLAG 추가

  - FLAG_ACTIVITY_NEW_TASK

    : 화면이 없는 서비스에서 화면이 있는 Activity를 띄우려면 새로운 Task를 생성해야 함

  - FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP

    : Activity가 이미 메모리에 만들어져 있으면 재사용함

<br>

#### 3. Service에서 전달받은 intent 처리

``` JAVA
// MainActivity
	...
    // Activity가 새로 만들어질 때 다른 곳에서 전달된 Intent 받기
    @Override
    protected void onCreate(Bundle savedInstanceState){
        ...
        startService(intent);
        ...
    }
    Intent passedIntent = getIntent();
	// intent 처리
    processIntent(passedIntent);
}

// Activity가 이미 만들어져 있을 때 전달된 Intent 받기
@Override
protected void onNewIntent(Intent intent){
    // intent 처리
    processIntent(passedIntent);
    super.onNewIntent(intent);
}
```

- MainActivity가 메모리에 만들어져 있지 않은 상태에서 처음 만들어지면

  onCreate() 메서드가 호출되고 그 안에서 getIntent()를 호출하여 Intent 객체를 참조함

- MainActivity가 이미 메모리에 만들어져 있다면 onNewIntent()가 호출되고

  intent를 파라미터로 전달

  <br>

#### 4. 실행된 Service 종료

- stopService() 호출하면 됨

<br>

## IntentService

- Service처럼 백그라운드에서 실행되는 것은 같지만 길게 지속되는 서비스라기보다는 한 번 실행되고끝나는 작업을 수행할 때 사용
- IntentService 내 onHandleIntent()는 onStartCommend()로 전달된 intent를 전달받으면서 실행됨

- onHandleIntent() 함수의 실행이 끝나면 서비스는 자동 종료됨

<br>

-------------------

# Service Binding

-  Binding된 서비스는 클라이언트-서버 인터페이스 안의 서버를 말함
- component(Activity)를 서비스에 바인딩하고 요청을 보내고 응답을 수신하여 프로세스 간 통신까지 수행할 수 있게 해줌
- 백그라운드에서 무한히 수행되지 않고 바인딩된 client가 모두 종료되면 실행 종료

<br>

#### Service를 Binding하는 방법

1. 서비스의 **onBind()** 콜백 메서드를 구현해서 바인딩 서비스 구현
   
- onBind()는 클라이언트가 서비스와 상호작용하는 데 사용할 수 있는 프로그래밍 인터페이스를 정의하는 IBinder 객체를 반환
   
2. startService()를 호출해서 시작된 서비스를 시작하면 서비스가 무한히 실행되도록 할 수 있고 **bindService()**를 호출하여 클라이언트가 해당 서비스에 바인딩되도록 할 수 있음

   - startService()를 통해 서비스를 시작하고 bindService()로 바인딩하면

     클라이언트가 모두 바인딩을 해제해도 서비스를 소멸시키지 않음

     - 따라서, stopSelf() 또는 stopService()를 호출해서 서비스를 직접 중단해야 함

   

   - bindService()를 호출하여 클라이언트가 서비스에 바인딩되는데 

     이때 반드시 서비스와의 연결을 모니터링하는 **ServiceConnection**을 구현해야 함
     - Android  시스템이 클라이언트와 서비스 사이에 연결을 설정하면 ServiceConnection에서 onServiceConnected()를 호출
     - 이 함수는 IBinder 인자를 갖고 클라이언트는 **IBinder**를 사용해 바인딩된 서비스와 통신

   - bindService()의 **반환값**은 요청된 서비스가 존재하는지, 클라이언트에 서비스 접근 권한이 있는지 나타냄

   <br>

#### Binding된 서비스 생성

- 서비스가 App 전용이고 클라이언트 역할로 실행되는 경우 인터페이스를 생성할 때 Binder 클래스를 확장하고 그 객체를 onBind() 에서 반환하는 방식으로 사용
  - 클라이언트는 Binder를 받고 Binder 구현이나 Service에서 제공되는 메서드에 직접 액세스할 수 있음
  - 다른 App이나 별도의 프로세스에서 사용될 때가 아닌 서비스가 단순히 하나의 프로세스의 백그라운드에서 작동할 때 사용하는 방법
  - Binder 클래스 확장 방법
    - 서비스에서 Binder의 객체 생성
    - Binder의 객체를 onBind() 콜백 메서드에서 반환
    - 클라이언트의 경우,  IBinder를 수신하려면 ServiceConnection 객체를 생성하고 이를 bindService()에 전달해야 함
    - ServiceConnection 은 시스템이 IBinder를 전달하기 위해 호출하는 onServiceConnected() 콜백 메서드를 포함함

``` java
// Service
public class LocalService extends Service {
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            // Return this instance of LocalService so clients can call public methods
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    /** method for clients */
    public int getRandomNumber() {
      return mGenerator.nextInt(100);
    }
}
```

``` java
public class BindingActivity extends Activity {
    LocalService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
 		...
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

    public void onButtonClick(View v) {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            int num = mService.getRandomNumber();
            Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
    }

    // 시스템이 IBinder를 전달하기 위해 호출하는 콜백 메소드
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
```

