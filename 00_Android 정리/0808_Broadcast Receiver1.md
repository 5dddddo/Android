# Broadcast Receiver_1 (08/08)

#### Broadcast란?

- 여러 앱 구성 요소에 메시지를 전달하는 것 -> **Global Event**

- 안드로이드 시스템으로부터 상태 변화에 대해 나오는 신호 

  ex) 배터리 용량 부족 , 네트워크 환경 변화, USB 케이블 연결/비연결

- 사용자 Application에서 발생시키는 임의의 신호



#### Broadcast Receiver란?

- Broadcast Receiver는 Broadcast를 청취하는 component
- Broadcast Receiver는 사용자와의 대면은 하지 않음

- Broadcast Receiver 정의하는 2가지 방법

  1. AndroidManifest.xml에 등록해서 시스템에게 알리고

     Activity도 없게 만들기

  2. manifest 등록 방식이 아닌 소스 코드에서

     registerReceiver()를 사용해 시스템에 등록하기

     -> Activity 안에서 Broadcast msg를 전달받아

     ​	바로 다른 작업 수행할 수 있음
     
     

-----------

#### 1번 방식으로 Broadcast Receiver를 정의해보자!



#### MainActivity

```java
 btn15.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         // Broadcasting
         Intent i = new Intent();
         // intent를 구분하기 위해 action 지정
         i.setAction("MY_BROADCAST");
         i.putExtra("broadcastMsg", "메시지가 전파돼요!");
         sendBroadcast(i);
     }
});
```



#### BroadcastReceiver

- BroadcastReceiver를 상속받는 Broadcast Receiver Class 생성
- [File] - [New] - [Other] - [Broadcast Receiver]

![1565351407296](https://user-images.githubusercontent.com/50972986/62777066-5ce16d80-bae7-11e9-9230-fe7a4735e582.png)



```JAVA
public class MyFirstReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = 
            intent.getExtras().getString("broadcastMsg");
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
```



#### AndroidManifest.xml

- 새로운  Broadcast Receiver를 만들면 AndroidManifest.xml에 등록해야 시스템이 알 수 있고 Activity도 없게 됨

``` xml
<receiver
          android:name=".MyFirstReceiver"
          android:enabled="true"
          android:exported="true">
    <intent-filter>
        <action android:name="MY_BROADCAST"></action>
    </intent-filter>
</receiver>
```

