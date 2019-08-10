java 

1. 어제했던 모델은

​			상당히 로드가 많이 걸리는 JDBC 작업 -> 비효율적
​		// Connection Pool을 사용하는 코드로 재작성
​		// Apache Tomcat은 DBCP라는 ConnectionPool 기능을 제공
​		// DBCP는 JNDI를 이용

​	

C:\androidWS\BookSearchForAndorid\WebContent\META-INF에 context.xml 복사

jdbc 연동 코드를 설정파일로 대체함



2. Project - java resource - src - common 복붙

-----------------------------------------------



Android 부분

UI Thread (Activity Thread, Main Thread) 에서는 network 연결을 할 수 없음
-> Network 처리는 시간이 오래 걸리는 작업이기 때문에

​	사용자의 interrupt에 대한 처리를 놓칠 위험 있음

main thread의 최우선순위 처리는 사용자와의 interrupt이다!



```
// 독자적인 thread로 수행시키기 위해 Runnable interface 구현함
```

```
// 문제 ) 외부 Thread이기 때문에 UI를 제어할 수 없음
// ->  해결 ) Handler를 이용해서 UI Thread에 ListView에 사용할 데이터 넘김
```

```
// 외부 Thread에서 handler에게 msg가 전달되면 아래의 method가 자동으로 callback  됨
// 메인 thread와 외부 thread가 handler를 통해 데이터를 주고 받아야 하기 때문에
// 외부 Thread 생성시 Handler를 인자로 넘겨줘야 함
```





project-  android - app - libs - jackson  library 복사

우클릭 - add as library - ok

