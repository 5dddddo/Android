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

- onCreate() : 서비스 객체가 처음 생성될 때 호출

  ​				      사용할 리소스에 대한 **초기화** 작업

- onStartCommand() : **실제 서비스가 일**을 하기 위해 호출되는 method

- onDestroy() : 서비스 객체가 종료될 때 호출

  ​						**사용하던 Thread를 중지 / 리소스 해제**