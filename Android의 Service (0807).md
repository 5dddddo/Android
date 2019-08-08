Android App 작성

KAKAO API를 이용하여 키워드를 이용한 도서 검색

=> 결과를 JSON으로 받아서 Activity에 ListView등을 이용하여 결과를 출력
(얻어오는 데이터는 KAKAO API Reference 확인)

service를 이용하여 처리



# Android의 Service (08/07)

- 안드로이드를 구성하고 있는 4가지 component 중 하나

- Background 작업이 필요한 경우에 많이 사용

- Activity의 가장 큰 관심거리(처리)는 사용자와의 interaction

- Service는 사용자와의 interaction은 없음

  Activity를 위한 연산이나 특정 method를 제공

  -> Thread를 사용해서 처리

- Service의 lifecycle(callback method)에 대해서 알아둬야 함



### Service의 주요한 callback method 3가지

- onCreate() : 서비스 객체가 처음 생성될 때 1번 호출

  ​				      사용할 리소스에 대한 **초기화** 작업

  ​					 

- onStartCommand() : onCreate() 후에 자동적으로 호출되며

  ​								    startService()에 의해서 호출 됨

  ​									**실제 서비스가 일**을 하기 위해 호출되는 method

- onDestroy() : 서비스 객체가  메모리 상에서 종료될 때 1번 호출

  ​						**사용하던 Thread / 리소스 해제**

- [File] - [New] - [Service] - [Service]

  enabled : 인스턴스화 여부

  exported : 서비스를 다른 app에서도 사용할 수 있도록 할지 여부

#### 특징

- Service를 여러 번 시작해도 이미 객체가 만들어져 있는 상황에서는

​	  onCreate() 함수는 호출되지 않고

​	  onStartCommand() 함수만 반복 호출 됨

- 비정상 종료 (강제 종료)시 service가 종료되지 않고 반복 수행되어

  resource(메모리 사용, 네트워크사용량 증가)를 계속 갉아 먹음

  

  - Default 설정값 : Service 재시작 O

  ```java
  		return Service.START_STICKY;
  ```

  - Service 재시작 X

  ```java
  return Service.START_NOT_STICKY;
  ```



- onStartCommand()

``` java
// Activity에서 넘겨준 Intent를 첫번째 parameter로 받음
// Service가 정상 실행되었으면 Intent != null 임
public int onStartCommand(Intent intent, int flags, int startId) {
    // 서비스가 하는 로직처리
    Log.i("ServiceExam", "onStartCommand() 호출");
    //        return super.onStartCommand(intent,flags,startId);

    if (intent == null) {
        // 강제 종료되어 Activity가 Service를 실행하지 않고
        // 안드로이드 시스템이 서비스만 강제로 재시작 했을 때 intent == null
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        // flags 값 주의!
        // Cleartop, singletop -> 생성되어 있는 Activity가 없는 경우 Activity 생성함
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // 생성되어 있는 Activity인지 확인
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // 화면이 없는 객체가 화면이 있는 객체로 값을 전달하려면 TASK가 필요함
        //       Service    ->    Activity
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        resultIntent.putExtra("ServiceToActivityData", "결과 데이터 전달");
        // flag에 의해 메모리에 이미 떠있는 Activity는 onCreate() 호출하지 않고
        // 생성되어 있는 Activity 가져옴
        startActivity(resultIntent);

    } else {
        // 정상실행되어 Activity가 보내준 데이터를 서비스가 받음
        String msg = intent.getExtras().getString("ActivityToServiceData");
        Log.i("ServiceExam", "Activity가 보내준 msg : " + msg);

        // 로직처리가 진행되고 발생한
        // Activity에게 전달해야 하는 최종 결과 데이터는 intent로 보냄
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

        // flags 값 주의!
        // Cleartop, singletop -> 생성되어 있는 Activity가 없는 경우 Activity 생성함
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // 생성되어 있는 Activity인지 확인
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // 화면이 없는 객체가 화면이 있는 객체로 값을 전달하려면 TASK가 필요함
        //       Service    ->    Activity
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        resultIntent.putExtra("ServiceToActivityData", "결과 데이터 전달");
        // flag에 의해 메모리에 이미 떠있는 Activity는 onCreate() 호출하지 않고
        // 생성되어 있는 Activity 가져옴
        startActivity(resultIntent);
    }

    // 강제 종료되면 자동적으로 재시작함
    return Service.START_STICKY;

    // 강제 종료되면 자동적으로 재시작 안함
    // return Service.START_NOT_STICKY;
}
```

