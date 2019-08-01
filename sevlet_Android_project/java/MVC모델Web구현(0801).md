# 기본 MVC pattern이 적용된 Web 구현 (08/01)

> ##### 기본 MVC pattern이 적용된 Web App을 통해 DB에 접근하여 가져온 Data를 JSON 형식으로 출력하기





### 1. MySQL DB Setting (도서 정보)  

1. 제공된 Script file을 이용해서 DB 세팅

   > MySQL_Setting(0801).md 참고



### 2. java Servlet으로 DB access program 작성

- 입력 : 책 제목의 keyword

- 출력 : 책 제목 리스트 (json)

  

  1. Eclipse 설정 -> Encoding

     > 새로운 Workspace 생성

     - window - preferences - general - workspace - encoding - UTF-8 설정

  2. Tomcat WAS를 Eclipse와 연동

     >  client (Web broswer)가 Tomcat을 통해서 서버 프로그램을 호출할 때 데이터를 전달할 수 있음
     >
     > 기본적으로 이 데이터 연결 통로가 ISO9958_1이라는 영문 Encoding으로 되어 있음 -> 한글 깨짐
     >
     > 데이터 연결 통로의 Encoding을 UTF-8로 변경해야 함

     - Servers project - Tomcat v7.0 Server at localhost-config - server.xml - Connector 부분

       URIEncoding = "UTF8"  추가

``` XML
<Connector URIEncoding = "UTF8" connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443"/>

<Connector URIEncoding = "UTF8" port="8009" protocol="AJP/1.3" redirectPort="8443"/>
```



### MVC pattern

![1564649877421](C:\Users\student\AppData\Roaming\Typora\typora-user-images\1564649877421.png)

#### 1. Model(javabeans) 

- **DAO** (Data Access Object) **: DB 관련 작업 담당**

- **VO** (Value Object) , DO (Domain Object), DTO (Data Transfer Object), Entity 

  - 데이터 전달에 대한 **형식**을 정의한 객체

  - Controller , Service, DAO 간의 데이터 전달이 특정 데이터 형식(VO)으로 이루어짐

- **Service** : 입력을 받은 데이터를 가지고 **business logic (transaction)을 처리**하는 component

  ​				Controller가 입력을 받으면 제어권과 데이터를 넘겨줌

  ​				처리 완료 후 Controller에게 제어권과 결과값을 넘겨줌

#### 2. View (jsp)

- Web broswer의 UI 작업

#### 3. Controller (Servlet)

- Client(Android App)의 **입력**을 받고 결과를 **출력**하는 작업 



3. Dynamic web project 생성

   > Project Name : BookSearchForAndroid
   >
   > Context root : bookSearch

    0. library 파일 포함시키기 

       > C:\androidWS\BookSearchForAndorid\WebContent\WEB-INF\lib 폴더에
       >
       > mysql-connector-java-5.1.29-bin.jar
       >
       > jackson-databind-2.8.1.jar
       >
       > jackson-core-2.8.1.jar
       >
       > jackson-annotations-2.8.1.jar
       >
       > 추가하기

   

   1. Servlet 생성 

      class name : BookSearchTtitleServelt

      java package : com.test.controller

      servlet은 controller package에 모아놓음

      

   2. URL Mapping : /searchTitle

      Android가 호출할 이름

      

   3. doGet()

   ![1564646491055](C:\Users\student\AppData\Roaming\Typora\typora-user-images\1564646491055.png)

   

   4. Logic에 관련된 처리를 하기 위한 Service Class 생성

      > class name : BookService

   ![1564646552179](C:\Users\student\AppData\Roaming\Typora\typora-user-images\1564646552179.png)

   5. Database 처리를 위해 DAO 생성

      - JDBC 동작 순서

      ![1564645411157](C:\Users\student\AppData\Roaming\Typora\typora-user-images\1564645411157.png)



   6. URL 주소에 http://localhost:8080/booksearch/searchTitle?keyword=첵제목입력 입력해서 

      Web broswer에 json 형식으로 결과 뿌리기



### 3. Android에서 java network 기능 중 HTTP request 호출 기능을 이용하여 Servlet 호출 후 json 받기



### 4. json 형태로 데이터를 받아온 후 Parsing 해서 ListView에 출력



