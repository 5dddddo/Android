# Lambda를 이용한 java programming 3 (08/19)

> 전체 소스코드 Ex03_LambdaUsingVariable 참고

<br>

##  람다식을 사용할 때 주의해야 할 점

- 클래스의 멤버 (필드 + 메소드)의 로컬 변수 (지역 변수)의 사용에

  약간의 제약이 있음

- 특히, this keyword를 사용할 때 주의해야 함!

- this : 현재 사용되는 객체의  reference

- 람다식은 익명 객체를 만들어 내는 코드

  this keyword를 쓰면 익명 객체를 지정하지 않음

  -> 상위 객체를 지칭하게 됨

<br>

- OuterClass와 InnerClass 정의

``` JAVA
class OuterClass {
	// Field ( 기본적으로 class의 field는 private )
	public int outerField = 100;

	public OuterClass() {
		// default 생성자
		System.out.println(this.getClass().getName());
	}
    
	// class 안에 다른 class를 정의 ( inner class )
	class InnerClass {
		// Field
		int innerField = 200;
		// Field
		Exam03_LambdaIF localLambda;
		Exam03_LambdaIF fieldLambda = () -> {
			// 외부 access
			System.out.println("outfield : " + outerField);
			System.out.println("OuterClass의 객체를 찾아요 : " +
                               OuterClass.this.outerField);

			// 내부 access
			System.out.println("innerfield : " + innerField);
			System.out.println("this.InnerClass의 객체를 찾아요 : " +
                               this.innerField);
			System.out.println(this.getClass().getName());
		};

		public InnerClass() {
			// default 생성자
			System.out.println(this.getClass().getName());
		}

		// Method
		public void innerMethod() {
			// 지역 변수 ( local variable )
			// stack 영역에 저장이 되고
			// method가 호출될 때 생성되고
			// method가 끝나면 메모리에서 사라짐

			// 내부적으로 final 처리가 됨
			int localVal = 100;

			localLambda = () -> {
				System.out.println(localVal);
				// Error
				// 람다식 내에서 지역변수는 값을 바꿀 수가 없음 (readonly)
				// 람다식은 객체를 생성하는데
				// innerMethod() 함수의 지역변수 localVal은
				// 함수가 끝나면 사라지기 때문에
                // 람다 객체 안에서 local 변수를 사용할 수 없음
				//localVal = 50;
			};
			localLambda.myFunc();
		}
	}
}
```

 <br>

- OuterClass와 InnerClass의 객체 생성

``` java
// 프로그램의 시작을 위한 dummy class로 사용
public class Ex03_LambdaUsingVariable {
	public static void main(String[] args) {
		// InnerClass의 field인 람다식을 사용하려면
		// InnerClass의 instance가 존재해야 함
		// 그런데 하필이면 InnerClass가 inner class임
		// inner class의 instance를 생성하려면
		// 먼저 outer class의 instance부터 생성해야 함
		
		// 외부클래스 객체 생성
		OuterClass outer = new OuterClass();
        /* 출력 : javaLambda.OuterClass */
            
		// 내부클래스 객체 생성
		OuterClass.InnerClass inner = outer.new InnerClass();
		/* 출력 : javaLambda.OuterClass$InnerClass */
            
        inner.fieldLambda.myFunc();
        /* 출력 : 
            outfield : 100
            OuterClass의 객체를 찾아요 : 100
            innerfield : 200
            this.InnerClass의 객체를 찾아요 : 200
            javaLambda.OuterClass$InnerClass
        */
		inner.innerMethod();
  
        /* 출력 : 100 */
	}
}
```

 <br>

- this keyword를 쓰면 익명 객체 ( Exam03_LambdaIF ) 를 지정하지 않음

   -> 상위 객체 ( inner class ) 를 지칭하게 됨

<br>

- 람다식 안에서는 지역변수를 reaonly 형태로 사용해야 함

  람다식은 객체를 생성하는데
  
  innerMethod() 함수의 지역변수 localVal은 내부적으로 final로 취급되고
  
  멤버 함수가 끝나면 사라지기 때문에
  
  람다 객체 안에서 local 변수를 사용할 수 없음
