# Content Provider (08/09)

- 안드로이드의 4번째 component

- Content Provider ( 데이터를 제공할 때 사용 )

  Content Resolver ( 제공된 데이터를 사용할 때 사용 )

- 안드로이드 앱은 sanbox model을 기반으로 동작

  => 하나의 앱에서 생성한 데이터는 해당 앱에 종속되고

  ​	 다른 앱에서 사용할 수 없음

- 앱을 만들어서 주소록에서 데이터를 가져와서 이용하고자 함

- 사용자가 만드는 Custom App은 일반적으로 Content Provider를 만들지 않음

  Content Resolver만 사용하고 

  안드로이드 시스템이 제공하는 Content Provider에 접근해서

  기본 App의 데이터를 가져와 사용

  

### 안드로이드 앱이 데이터를 관리(저장)하는  4가지 방법

1. File 처리

   - 모바일 디바이스 / 외부 저장매체에 직접 파일을 저장할 수 있음
   - 이 파일에 대해서는 다른 App에서 접근 불가
   - 속도가 느림
   
2. Database 처리

   - 안드로이드 시스템 내부에 있는 SQLite라는 소형 DB를 이용해서 데이터 처리
     - SQLite를 어떻게 이용할까?
     - 이 DB를 외부에 제공하려면 어떻게 해야할까?
     - 다른 앱이 가지고 있는 DB에 접근하려면 어떻게 해야할까?
  
   - DB를 만든 App에 종속
   - SQLiteDatabase 객체는 DB를 처리하는 Query Method를 가짐

3. Preferences 처리

   - 기본형 데이터 타입에 대한 key와 value의 쌍을 저장
   - App이 실행될 때마다 load되어야 하는 기본적인 App의 환경 설정이나 인사말 등을 저장하기 위해 사용

4. 네트워크 처리


## SQLite DB를 이용해보자

#### SQLite를 어떻게 이용할까?

1.

2. SQL

   ```
   // table이 없으면 userName TEXT, userAge INTEGER를 가진 member table생성
   CREATE TABLE IF NOT EXISTS member(userName TEXT, userAge INTEGER);
   
   // 데이터 입력
   INSERT INTO member VALUES('홍길동',30);
   INSERT INTO member VALUES('오길동',10);
   INSERT INTO member VALUES('이길동',40);
   
   ```

   

#### 이 DB를 외부에 제공하려면 어떻게 해야할까?

[file] - [New] - [Other] - [Provider]

![1565327259276](C:\Users\student\AppData\Roaming\Typora\typora-user-images\1565327259276.png)

uri authorities : 외부 앱에서 접근할 수 있는 경로 지정

외부 앱에 db 자체를 제공하는 것이 아니라 method를 제공해 줌



```
public class MyContentProvider extends ContentProvider {
```

필드에 선언

```
private SQLiteDatabase db;
```

#### 다른 앱이 가지고 있는 DB에 접근하려면 어떻게 해야할까?

```
ContentResolver cr = getContentResolver();
Uri uri = Uri.parse("content://URI_Authorities(프로바이더)/데이터베이스 .db의 이름");
```
