# MySQL Setting (08/01)

1. MySQL 다운로드

2. C:\Users\student\Desktop\mysql-5.6.44-winx64\bin에서 Shift + 마우스 우클릭 -> Console 실행

3. Console에 

   \>\> `mysqld` 

    MySQL DBMS 기동시킴

    기본적으로 MySQL Server에 접속하기 위한 port  : 3306

4. MySQL daemon process가 실행되면 MySQL에 접속할 수 있음

5. 새 MySQL Console 실행

6. MySQL Console에 root 사용자로 접속하기

    \>\> `mysql -u root`

7. 사용자 생성부터 시작

   `mysql > create user 유저ID identified by "유저PW";`

   JDBC를 이용해, 네트워크를 통해 외부에서 접근할 수 있도록 유저를 하나 더 생성

   `mysql > create user 유저ID@localhost identified by "유저PW";`

8. DB 생성

   => 1개의 MySQL DBMS 안에 여러 개의 DB 생성 / 관리가 가능

   `mysql > create database DB명;`

9. 7번에서 만든 새로운 사용자에게 DB 권한 부여

   `mysql > grant all privileges on DB명.* to 유저ID;`

   `mysql > grant all privileges on DB명.* to 유저ID@localhost;`

   

10. 권한 flush : 권한 업데이트

    `mysql > flush privileges;` 

11. MySQL Console 종료

    `mysql > exit;`

12. 사용할 데이터를 DB에 구축하는 작업 실행

    제공된 Script file을 이용해서 데이터 구축 진행

    \>\> `mysql -u 유저ID -p DB명 < SQL_file명`

    





