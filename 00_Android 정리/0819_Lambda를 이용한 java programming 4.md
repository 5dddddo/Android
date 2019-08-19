# Lambda를 이용한 java programming 4 (08/19)

> 전체 소스코드 Ex04_LambdaUsingConsumer 참고

<br>

## Lambda의 interface 객체 

- 람다식은 추상 메소드가 1개인 interface의 객체를 생성하는 표현식

- 이때, 사용하는 인터페이스를 직접 만들어야 할까?

  람다식이 대입되는 target type은

  일반적으로 java가 제공하는 API를 이용

- 대표적으로 Runnable, Event 처리 interface를 람다의 target type으로 사용

<br>

- java에서는 람다의 target type으로

  사용될 수 있는 interface를 여러 개 만들어서

  우리에게 package로 제공

  - package name : java.util.function

<br>

- 제공되는 interface는 총 5가지 종류로 분류할 수 있음

  **Consumer, Supplier, Function, Operator, Predicate**

  

## Consumer 

- 함수적 인터페이스

- 람다식이 대입될 수 있는 Target type으로

  사용할 수 있는 interface를 지칭

- 자바가 제공하는 interface이고 accept()라는 추상 메소드 1개만 가지고 있음

- accept() 함수는 한 개의 인자를 받고 return type은 void인 추상 메소드

- 값을 소비하는 역할을 담당

``` java
printName(t -> System.out.println(t));
Consumer<String> consumer = t -> {System.out.println(t);};
consumer.accept("소리없는 아우성!!");
/* 출력 : 소리없는 아우성!! */
```

<br>

#### Consumer의 여러가지 종류

``` java
// 매개변수 2개를 소비하는 Consumer
BiConsumer<String, String> biconsumer = (a, b) -> {
    System.out.println(a + b);
};
biconsumer.accept("소리없는", "아우성");
/* 출력 : 소리없는아우성 */

// int를 소비하는 Consumer
IntConsumer intconsumer = i -> System.out.println(i);
intconsumer.accept(100);
/* 출력 : 100 */

// 객체와 int를 소비하는 Consumer
ObjIntConsumer<String> objconsumer = (a, b) -> {
    System.out.println(a + b);
};
objconsumer.accept("Hello", 100);
/* 출력 : Hello100 */
```

<br>

#### First-classes function (  일급 함수 )

- 람다의 실행코드를 인자로 넘겨서 특정 method 내에서 활용하는 방식으로 사용

- 일반적인 method 호출은 사용하는  data가 인자로 전달되는 형태이지만

  람다식을 사용하면 method를 호출할 때 data가 아니라

  실행코드를 넘겨줄 수 있음

  **눈에 보이는 형태는 실행 코드지만 실제로는 객체 / reference가 넘어감**

<br>

- 일급 함수 예제 코드

``` java
public class Ex04_LambdaUsingConsumer {
	// method를 static으로 정의 (편리하므로)
	public static List<String> names = Arrays.asList("홍길동", "김길동", "최길동", "박길동");

	// 일반적인 method 호출은 사용하는 data가 인자로 전달되는 형태
	// 람다식을 사용하면 method를 호출할 때 data가 아니라
	// 실행코드를 넘겨줄 수 있음 (눈에 보이는 형태는 코드지만 실제로는 객체 / reference가 넘어감 )
	// 일반적으로 프로그래밍 언어에서 이렇게 함수를 다른 함수의 인자로
	// 사용할 수 있는데 이런 함수를 first-classes function이라고 함
	// 일급함수라고도 표현 ( Java Script가 대표적 )
	public static void printName(Consumer<String> consumer) {
		for (String name : names) {
			consumer.accept(name);
		}
	}
}
```

