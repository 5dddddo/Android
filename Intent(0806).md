# Intent (08/06)

# Explicit Intent

: 명시적 intent 생성 시 Component 객체를 통해 새로 띄울 창 지정

```java
// btn1 : Event Source
// id를 가지고 view 객체의 (우리 눈에 보이는 컴포넌트들) reference
// id 라는 필드안에 뭉쳐있고, static 필드에서 btn1을 가져옴
// 타입 모르고 가져오기 때문에 캐스팅 해줘야 함
Button btn1 = (Button) findViewById(R.id.btn1);
btn1.setOnClickListener(new View.OnClickListener() {
    @Override
    // 익명 클래스 : 클래스를 따로 안 만들고 내부적으로 처리 
    public void onClick(View view) {
        // 익명 내부가 일반적
        Intent i = new Intent();                 
        ComponentName cname = new ComponentName("패키지 경로",
                "클래스 경로");
        // intent에 정보를 실어줌
        i.setComponent(cname);  
        // 그 정보를 이용해 Activity 시작
        startActivity(i);       
    }
});
```



# Implicit Intent

- 안드로이드 시스템은 OS - system lib - framework - 기본 android App (Browser, 주소록, 전화 걸기, ...) 구조

- 기본 android App도 각각의 Activity 파일인데

  Activity의 이름을 알 수 없음 -> 묵시적 Intent 사용



####  ImplicitIntentTestActivity

``` JAVA
// 새로운 Activity를 호출
// Implicit intent (묵시적 인텐트)를 이용해서 Activity를 호출
// Component 대신 Action (하나만 지정할 수 있음)과
// Category(여러 개 지정 가능) 지정해줌
Intent i = new Intent();

//AndroidManifest.xml 파일에 <intent-filter> 에 지정해 놔야 사용할 수 있음
// Action (하나만 지정할 수 있음) -> set
i.setAction("MY_ACTION");

//  Category(여러 개 지정 가능) -> add
i.addCategory("MY_CATEGORY");
i.putExtra("DATA", "data");
startActivity(i);
```



##### AndroidManifest.xml

``` JAVA
<activity android:name=".ImplicitIntentTestActivity">
	<intent-filter>
		<action android:name="MY_ACTION" />
        <category android:name="MY_CATEGORY" />
            // default는 항상 넣어줘야 함
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```



# Intent filter의 중복

다른 프로젝트에 같은 묵시적 intent 설정

 -> 같은 name을 가진 action과 category 지정시 어떤 App을 수행할 것인지 확인창 Pop-up



# Browser 띄우기

```
// Browser를 이용해서 버튼을 눌렀을 때 특정 URL에 접속하기
Uri uri = Uri.parse("http://www.naver.com");
Intent i = new Intent(Intent.ACTION_VIEW,uri);
startActivity(i);
```

##### Androidmanifest.xml

```
<uses-permission android:name="android.permission.INTERNET" />
```



# KakaoMap

1. 카카오 개발자 - 로그인

2. 개발자 등록 및 앱 생성

   내 애플리케이션 - 앱 만들기

   Android project package 입력

3. 설정 - 일반 - 플랫폼 추가 - Android 

   ![1565072572450](C:\Users\student\AppData\Roaming\Typora\typora-user-images\1565072572450.png)

4. 키 해시 값 가져오기

   1. Windows의 경우 Openssl 다운로드 필요

      > openssl-1.0.2s-x64_86-win64 

   2. 사용자 환경변수 Path에  Openssl 폴더 Path 추가

   3. cmd 창 열고
      C:\Program Files\Java\jdk1.8.0_211\bin로 가서 

      `keytool -exportcert -alias androiddebugkey -keystore C:\Users\student\.android\debug.keystore -storepass android -keypass android | openssl sha1 -binary | openssl base64`

      입력하면 해시 키 값을 가져올 수 있음

4. 네이티브 앱 키 복사

![1565072299087](C:\Users\student\AppData\Roaming\Typora\typora-user-images\1565072299087.png)



5. Android project - app - manifests - AndroidManifest.xml -

   \<application\> 태그 아래에 meta-data 추가

```
<meta-data
    android:name="com.kakao.sdk.AppKey"
    android:value="네이티브앱키" />
```

6. SDK 다운로드

   > Android_DaumMap_SDK_1.4.0.0

7. library 추가

   > Android_DaumMap_SDK_1.4.0.0\libs\libDaumMapAndroid.jar

   1. Android project - app - libs 폴더에 추가

   2. 파일 우클릭 Add as Library

   3. Android project - app - src - main - jniLibs dir 생성

   4. jniLibs 폴더에 SDK 내 파일 3개

      arm64-v8a

      armeabi

      armeabi-v7a 폴더 추가

8. KakaoMapActivity

   ```
   MapView map = new MapView(this);
   // ViewGroup : linearlayout과 같이 눈에 보이는 component들을 담는 클래스
   ViewGroup group = (ViewGroup)findViewById(R.id.mapll);
   
   MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(37.501386, 127.039644);
   map.setMapCenterPoint(mapPoint,true);
   group.addView(map);
   ```



# 전화 Dial 창 띄우기

``` java
Button dialBtn = (Button) findViewById(R.id.dialBtn);
dialBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        // 전화 Dial 걸기 Activity 호출
        // 전화 Dial 걸기는 안드로이드 시스템 내부에
        // 이미 정해져 있는 기본 APP이기 때문에
        // Activity name을 알 수 없음 -> 묵시적 intent 사용
        Intent i = new Intent();
        i.setAction(Intent.ACTION_DIAL);
        // 전화번호 객체는 Uri.parse("tel:01000000000") 형식
        i.setData(Uri.parse("tel:01000000000"));
        startActivity(i);
    }
});
```



# 전화 직접 걸기

1. Android project - app - manifests - AndroidManifest.xml

   권한 추가

```
<uses-permission android:name="android.permission.CALL_PHONE"/>
```

Android marshmallow이후 버전부터 보안 기능 강화

``` java
 Button callBtn = (Button) findViewById(R.id.callBtn);
        dialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 직접 Calling하는 activity 호출
                // 1. 사용자의 안드로이드 버전이 6 이하인지 확인
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 추가적인 보안 해제가 필요
                    // 2. 현재 앱에 대해 사용자 권한 중 전화 걸기 기능이 설정되어 있는가?
                    int result = checkSelfPermission(Manifest.permission.CALL_PHONE);
                    if (result == PackageManager.PERMISSION_DENIED) {
                        // 전화 걸기 기능에 대한 보안 설정이 되어있지 않음
                        // 3. 한 번이라도 전화 걸기에 대한 권한 설정을 거부한 적이 있는가?
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                            // 거부한 적이 있음
                            // 다시 dialog를 띄워서 권한 재설정 요청
                            // dialog 생성
                            AlertDialog.Builder builder = new AlertDialog.Builder(IntentTestActivity.this);
                            builder.setTitle("권한 필요");
                            builder.setMessage("전화 걸기 기능이 필요합니다. 수락할까요?");
                            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                                }
                            });
                            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            // dialog 실행
                            builder.show();
                        } else {
                            // 사용자 요청이 필요한 권한 list를 작성
                            // 사용자가 수락시 권한 받아옴
                            // 최종적으로 수행되는 함수 : requestPermissions
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
                        }
                    } else {
                        // 전화 걸기 기능에 대한 보안 설정이 되어있음
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_CALL);
                        i.setData(Uri.parse("tel:01000000000"));
                        startActivity(i);
                    }
                } else {
                    // 안드로이드 버전 6 미만은
                    // <uses-permission android:name="android.permission.CALL_PHONE"/> 설정만으로 바로 실행 가능
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:01000000000"));
                    startActivity(i);
                }

            }
        });
```



#### 사용자의 답(grantResults)에 따라 requestPermissions callback 함수 자동 호출

`requestPermissions(new String[] {Manifest.permission.CALL_PHONE},1000);`

```java
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:01000000000"));
                startActivity(i);
            }
        }
    }
```



