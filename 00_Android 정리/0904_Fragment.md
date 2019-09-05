# 0904_Fragment

- 화면의 일부분을 독립적인 레이아웃으로 구성하고 싶을 때 사용
  - 하나의 화면을 여러 부분으로 나눔
  - 각각의 부분 화면 단위로 바꿈

- Activity를 전환하지 않고도 화면이 전환된 효과를 줄 수 있음

- Activity와 마찬가지로 1개의 java 소스 파일과 1개의 XML 레이아웃을 가짐

  - 소스파일과 XML 레이아웃을 매칭하는 과정 필요

    - setContentView()가 없기 때문에 LayoutInflater를 사용해 인플레이션 진행 하고

      클래스에서 사용하도록 하는 코드는 onCreateView() 안에 들어감

    - onCreateView()는 콜백 메서드로 인플레이션이 필요한 시점에서 자동으로 호출

      이 안에서 인플레이션을 위한 inflate()를 호출하면 되고

      이 과정이 끝나면 fragment가 하나의 뷰처럼 동작할 수 있음

- Fragment 사용법

  1. Activity의 XML 레이아웃 파일에 \<fragment\> 태그를 사용해서 Activity에 추가

  2. Activity의 소스 파일에서  새로 정의한 fragment 클래스의 객체를 new 연산자로 만든 후

     Fragment Manager 객체의 add()를 통해 Activity에 추가

- Activity와의 차이점

  ![1567646862701](https://user-images.githubusercontent.com/50972986/64306487-da58aa80-cfcd-11e9-8f94-eb3f6eaf77b3.png)

|                           Activity                           |                           Fragment                           |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
|             Component로 안드로이드 시스템이 관리             |            안드로이드 시스템 역할을 Activity가 함            |
| → Activity를 관리하는 시스템 객체 <br/>= Activity manager에 의해 독립적으로 동작 |       → 따라서 항상 액티비티 위에<bR>올라가 있어야 함        |
| →Activity manager가<br/>Activity의동작 순서나 처리방식을 결정 |      → fragment를 관리하는 객체<br/>= fragment manager       |
| 시스템에서 관리하므로 <br/>시스템이 이해하는 형식으로<br/>명령이나 데이터 전송 = intent | Activity와 fragment 간 데이터를 전달할 때<br>단순히 메서드를 만들어 호출 |

