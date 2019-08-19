# Content Provider (08/09)

- 안드로이드의 4번째 component

- Content Provider ( 데이터를 제공할 때 사용 )

  Content Resolver ( 제공된 데이터를 사용할 때 사용 )

- 안드로이드 앱은 sanbox model을 기반으로 동작

  => 하나의 앱에서 생성한 데이터는 해당 앱에 종속되고

  ​	 다른 앱에서 사용할 수 없음

- 사용자가 만드는 Custom App은 일반적으로 Content Provider를 만들지 않음

  Content Resolver만 사용하고 

  안드로이드 시스템이 제공하는 Content Provider에 접근해서

  기본 App의 데이터를 가져와 사용

  

### 안드로이드 앱이 데이터를 관리(저장)하는  4가지 방법

1. File 처리 - 속도가 느림

   - 여러분은 모바일 디바이스 상에 또는 분리될 수 있는 저장 매체 상에 직접적으로 파일들을 저장할 수 있다. 디폴트로 다른 애플리케이션들은 이러한 파일들에 접근할 수 없다. 여러분은 단지 로컬 파일들에만 접근할 수 있다. 
     만약 여러분이 컴파일 시점에 여러분의 애플리케이션과 함께 패키지할 정적static 파일을 가지고 있다면, 여러분 프로젝트의 res/raw/myDataFile로 그 파일을 저장할 수 있으며, 그리고 나서 Resources.openRawResource(R.raw.myDataFile)를 사용해서 그것을 오픈할 수 있다.

2. Database 처리

   - 안드로이드 API는 SQLite 데이터베이스를 생성하고 사용하는 것에 대한 지원을 포함한다. 각각의 데이터베이스는 그것을 생성한 애플리케이션에게 프라이빗 private하다. 
     SQLiteDatabase 오브젝트는 데이터베이스를 나타내고, 그것과 상호작용하는 - 쿼리query를 하고 데이터를 관리하는 - 메쏘드들을 가지고 있다. 데이터베이스를 생성하기 위해서는 SQLiteDatabase.create()를 호출하고, 또한 서브클래스 SQLiteOpenHelper를 호출하라.

     모든 데이터베이스(SQLite와 그 밖의 것들)는 디바이스 상의 /data/data/package_name/databases 안에 저장된다.

   - 안드로이드 시스템 내부에 있는 SQLite라는 소형 DB를 이용해서 데이터 처리
     - SQLite를 어떻게 이용할까?
     - 이 DB를 외부에 제공하려면 어떻게 해야할까?
     - 다른 앱이 가지고 있는 DB에 접근하려면 어떻게 해야할까?

3. Preferences 처리

   프레퍼런스Preference는 기본적인 데이터 타입에 대한 키와 값의 쌍key-value pair을 저장하고 가져오는 가벼운 메커니즘이다. 이것은 전형적으로 애플리케이션이 시작될 때마다 로드되어야 하는 기본적인 환영 인사말이나 텍스트 폰트와 같은, 애플리케이션의 환경설정 정보를 저장하기 위해 사용된다.

   여러분은 (컨텐트 프로바이더를 사용하는 것에 의하지 않고서는) 프레퍼런스를 애플리케 이션의 경계를 넘어서 공유할 수 없다. //두가지 어플리케이션을 동시 실행했을 때의 경우

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