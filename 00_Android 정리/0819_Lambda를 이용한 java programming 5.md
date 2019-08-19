# Lambda를 이용한 java programming 5 (08/19)

> 전체 소스코드 Ex05_LambdaUsingSupplier 참고

<br>

# Supplier

- Supplier 인터페이스는 매개변수가 없고 return type이 있음

- getXXX()라는 method가 추상 메소드 형태로

  인터페이스 안에 선언되어 있음

  <br>



### Supplier 예제 1 - List 값 랜덤하게 출력

친구 목록을 List\<String\> 형태로 만들고 

supplier를 이용해 랜덤으로 친구 1명 출력

``` java
public static void main(String[] args) {
    final List<String> myBuddy 
        = Arrays.asList("홍길동","김길동", "이순신", "강감찬");

    // Supplier를 이용해서 랜덤으로 1명의 친구를 출력하기
    // Math.random() : 0 이상 1 미만의 실수
    Supplier<String> supplier = () -> {
        return myBuddy.get((int) (Math.random() * 4));
    };
    System.out.println(supplier.get());
}
```



### Supplier 예제 2 - 로또

``` java
public class Ex05_LambdaUsingSupplier {

	// 로또 번호 (1~45)를 자동으로 생성하고
    // 출력하는 간단한 method 작성
    // IntSupplier : 정수값을 1개 리턴하는 supplier
	public static void generateLotto(IntSupplier intsupplier,
			Consumer<Integer> consumer) {
		// Set : 중복 배제
		Set<Integer> set = new HashSet<Integer>();
		while (set.size() != 6)
			set.add(intsupplier.getAsInt());
		
		for (Integer i : set) 
			consumer.accept(i);
	}

	public static void main(String[] args) {
		// generateLotto(supplier,consumer);
			generateLotto(() -> {
			return (int) (Math.random() * 45 + 1);
		}, t -> System.out.print(t + " "));
	}
}
```





