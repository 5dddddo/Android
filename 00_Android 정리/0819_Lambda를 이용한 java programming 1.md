# Lambda를 이용한 java programming 1 (08/19)

> 전체 소스코드 Ex01_LambdaBasic 참고

## Java Lambda

![1566173602873](C:\Users\student\AppData\Roaming\Typora\typora-user-images\1566173602873.png)

- 함수적 프로그래밍 패턴을 위해 Java는 8 버전부터 Lambda를 지원
- 람다는 익명함수를 만들기 위한 expression (식)
- 객체지향 언어보다는 함수지향적 언어에서 사용 됨

- 기존 자바 개발자들은 이 Lambda라는 개념에 익숙해지기가 쉽지 않음

 * But, java가 lambda를 도입한 이유는 크게 2가지

   1. 코드의 간소화

   2. Java Stream을 이용하기 위해 이용

      - Java Stream은 Collection(List, Map, Set, Array, ...)을 굉장히 효율적으로 처리 

      - method를 이용한 병렬 처리가 가능



###  람다식의 기본 형태

 * (매개변수)->{실행 코드}

 * 익명함수를 정의하는 형태로 표현하지만

   실제로는 익명 클래스의 인스턴스를 생성함

* 람다식이 어떤 객체를 생성하느냐는 

  람다식이 대입되는 interface 변수가 어떤 interface인가에 따라 다름

  ex) Thread 생성시 반드시 Thread class를 상속받아야 함

  ​	  가장 기본적인 Thread 생성 방법이지만

  ​	  상속은 객체지향 관점에서 좋지 않음

  <br>

1. Thread의 subclass를 이용해서 Thread 생성

``` java
class MyThread extends Thread {
	public void run() {
		System.out.println("Thread 실행");
	}
}
public class Ex01_LambdaBasic {
	public static void main(String[] args) {
		// Thread를 생성하려고 함
		// 1. Thread의 subclass를 이용해서 Thread 생성
		// 객체지향 관점에서 그다지 좋은 방식이 아님
		MyThread t = new MyThread();
		t.start();
    }
}
```

<br>

2. Runnable interface를 구현한 class를 injection해서 Thread를 생성

``` java
class MyRunnable implements Runnable {
	public void run() {
		System.out.println("Thread 실행");
	}
}
public class Ex01_LambdaBasic {
	public static void main(String[] args) {
        MyRunnable runnable = new MyRunnable();
        Thread t = new Thread(runnable);
        t.start();
    }
}
```

<br>

3. Runnable interface를 구현한 **익명 class**를 이용해서 Thread를 생성

   ( 안드로이드에서의 일반적인 형태 )

``` java
public class Ex01_LambdaBasic {
    Runnable runnable = new Runnable() {
        // 객체를 생성 못하는 이유는 추상 method
        // -> run()이 존재하기 때문
        // 이 method를 overriding하면 객체를 생성할 수 있음
        @Override
        public void run() {}
    };
}
```

<br>

4. 람다식으로 표현

```java 
// 3번의 
// new Runnable() {
// @Override
// public void run(){} }; 의 
// run의 () , {} 을 람다식으로 표현
Runnable runnable = ()->{};
Thread t = new Thread(runnable);
```

<br>

``` java
// 컴파일러의 타입 유추를 통해 람다식 표현 가능
new Thread(()->{
    System.out.println("Thread 실행"); 	
}).start();
```

<Br>

-------------------------

<br>

- 일반적인 interface를 정의해서 람다식으로 표현해 보자!

``` java
interface Ex01_LambdaIF {
	// interface 객체는 추상(abstract) method만 올 수 있음
	// abstract method : 정의는 없고 선언만 존재
	void myFunc(int k);
}
public class Ex01_LambdaBasic {
	public static void main(String[] args) {
        Ex01_LambdaIF sample = new Ex01_LambdaIF() {
			@Override
			public void myFunc(int k) {
				System.out.println("Thread 실행");
			}
		};
    }
}
```

위의 코드를 

``` java
// 람다 객체 생성
Ex01_LambdaIF sample = (int k) -> {
    System.out.println("Thread 실행");
};

// myFunc() 실행
sample.myFunc(100);
```

와 같이 람다식으로 표현 가능


