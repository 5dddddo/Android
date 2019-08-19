# Lambda를 이용한 java programming 2 (08/19)

> 전체 소스코드 Ex02_LambdaExpression 참고

<br>

## 람다식의 표현

- 람다식을 표현하는 방식은

  (인자1, 인자2, 인자3, ...) -> {실행 코드}

- 람다식 (매개변수)의 이름은 개발자가 지정할 수 있음

- 매개변수의 타입은 컴파일러의 타입 유추에 의해서 알 수 있기 때문에

  람다식에서는 일반적으로 매개변수의 타입을 지정하지 않음

 * 만약 매개변수가 1개인 경우 ()도 생략할 수 있음
 * 만약 실행문이 1개인 경우 {}도 생략할 수 있음
 * 매개변수가 없다면 ()는 생략할 수 없음
 * 실행문에 당연히 return 구문이 들어올 수 있음

- 만약 실행문에 return 구문이 1개만 존재하면

  {}와 return keyword도 생략 가능

<br>

## 람다의 target type (= interface type)

- 람다식은 interface type의 instance를 생성하는 expression

- 람다식은 결국 익명 객체를 생성하는 코드

  람다식이 생성하는 객체는 결국 어떤 interface type의 변수에

  assign이 되는가에 달려있음 

- 이렇게 람다식으로 만들어지는 interface type을

  람다의 target type이라고 함

- **람다의 target type이 될려면** 
  
  **해당 interface는 반드시 추상 메소드가 1개만 존재해야 함**
  
- interface를 사용할 때 어노테이션을 이용해서 check 할 수 있음
  
  함수적 인터페이스  : @FunctionalInterface을 이용해서 
  
  해당 interface가 람다의 target type이 될 수 있는지
  
  compiler에 의한 check를 해줌 



- Thread를 생성할 때 Runnable interface를 사용하는데

  이 Runnable interface는 public void run()이라는

  추상 method 1개만 가짐

  따라서, 이 interface는 람다의 target type이 될 수 있고

  Runnable interface는 함수적 인터페이스라고 할 수 있음



- 이벤트를 처리하는 interface는 대체로 함수적 interface 

  ex) setOnClickListener(new View.onClickListener(){} 의

  onClickListener()가 함수적 인터페이스
