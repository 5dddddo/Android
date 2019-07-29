# Android (07/29)



1. Android Studio 다운로드 & 설치

2. 삼성 USB 통합 드라이버

3. 폰 설정
   - USB 디버깅 활성화 ( 개발자 모드 활성화 )
   - 출처를 알 수 없는 앱 활성화

4. Sample Program build, 설치, 실행.

5. 가장 중요하게 생각해야 하는 학습 부분
   - Android Component ( 4가지 component )
     - Activity : 앱을 구성하고 있는 화면
     
       ​				UI (user interface) 를 화면에 표시하고 관리하는 component
     
     - Service : UI와는 별개로 Background로 실행 가능한 component
     
     - Broadcast Receiver 
     
       Broadcast : 시스템의 HW 관련 signal 발생 
     
       -> Broadcast Receiver : signal을 잡아와 적절히 처리하는 component
     
       ex) Broadcast로 배터리 부족 통지 -> Broadcast Receiver가 알림
     
     - Content Provider
     
       Android app은 각 앱이 독자적으로 데이터를 관리 됨
     
       -> Content Provider이 App 간의 데이터를 공유할 수 있도록 기능을 제공함

6. Android

   - google이 중심이 돼서 개발이 진행중인 휴대 단말기용 플랫폼

   - OS (C언어) +

     미들웨어 (C, C++) +

     Android Framework (C, java) +

     기본 application (주소록, 전화) 

     들로 구성된 Software Stack

7. Android Version

   1.5 Cupcake

   1.6 Donut

   ​	....

   ​	....

   4.0.1

   ​	....

   8 OREO

   9 Pie

   10 Q

   

8. 안드로이드 SoftWare Stack

   

9. Android project의 구성

   MainActivity.java => Activity class

   res => 리소스를 담당하는 폴더

    - drawable : app 내에서 사용되는 그림파일 저장

    - layout : Activity 화면 구성을 위한 XML

      ​			  파일명은 소문자로 이용

   - values : 문자열, 색상, 스타일 등 부가적인 리소스들에 대한 정의
   - mipmap : launcher 아이콘 자원을 저장

   AndroidManifest.xml => app 구성과 관련된 설정 정보를 가지고 있는 파일



11. Logcat의 이용 => System.out.println()을 이용할 수 없어요


=========================================================


12. Activity

    

13. view , viewGroup

    view(최상위클래스) : 이미지, 입력상자, 콤보박스, 버튼, ... 등 각각의 componet

    viewGroup : layout 

    View 와 ViewGroup(layout)을 이용해서 화면을 구성할 수 있음



=> 하나의 Activity를 생성하고 내가 원하는 화면을 xml 파일을 이용하여 구성할 수 있음 (화면 구성)



14. Activity의 life cycle

    Activity의 생명 주기

    : 어떤 상태가 존재하며 어떤 method가 callback되는지 확인해 보기

      Activity의 상태전이와 callback 호출 및 활용

    