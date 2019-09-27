# MVC Web 구현 2 (08/02)

> 0801_BookSearch_Servlet 

위의 모델을 업그레이드 시켜보자!

> 전체 코드는 CustomBookSearch 프로젝트 참고

## Servlet 

- 문제 : 위의 모델의 DAO에서

  로드가 많이 걸리는 JDBC 작업을 비효율적으로 처리함

  1. MySQL을 이용해 DB 접속을 위한 JDBC Driver Loading
  2. getConnection()을 호출하여 DB Connection 객체를 얻어옴
  3. Query문 수행을 위한 PreparedStatement 객체를 생성
  4. Query문을 실행해서 얻은 결과 처리
  5. 사용한 Resource해제

  

- BookDAO.java

![image](https://user-images.githubusercontent.com/50972986/62818772-f90e8180-bb86-11e9-9c78-6854d5fb293f.png)



![image](https://user-images.githubusercontent.com/50972986/62818863-48a17d00-bb88-11e9-8659-2d075a76ccaf.png)



- 해결 : Connection Pool을 사용하는 코드로 재작성

  ​		   Connection pool로부터 connection을 빌려와서 사용하므로

  ​		   사용자가 늘어나도 load가 커지지 않음

  - Apache Tomcat은 DBCP라는 ConnectionPool 기능을 제공

  - DBCP는 JNDI를 이용함

  - JNDI (Java Naming and Directory Interface)  

    : directory 서비스에서 제공하는 데이터 및 객체를

      발견(discover)하고 참고(lookup)하기 위한 자바 API

  - DBCP (DataBase Connection Pool) :

    - DB와 Connection을 하고 있는 객체를 관리하기 위한 Connection pool

    - WAS 실행시 미리 일정량의 Connection 객체를 생성하고

      연결된 상태의 Connection 객체를

      Pool이고 칭하는 Connection을 관리하는 곳에서

      가지고 있다가 필요한 곳에 Connection을 할당해주고

      작업이 끝나면 다시 Pool이 Connection을 관리함



- WebContent - META-INF - context.xml

  ``` xml
  <?xml version="1.0" encoding="UTF-8"?>
  <Context>
      <!-- Default set of monitored resources -->
  	<WatchedResource>WEB-INF/web.xml</WatchedResource>
  	<Resource type="javax.sql.DataSource" 
  			  name="jdbc/mySQLDB" auth="Container"
  			  username="android" 
  			  password="android" 
  			  driverClassName="com.mysql.jdbc.Driver"
  			  url="jdbc:mysql://127.0.0.1:3306/library?characterEncoding=UTF-8" />
  </Context>
  ```

  

- DBTemplate.java

  ``` java
  public class DBTemplate {
      static int count;
      
      public DBTemplate() {}
      
      public static Connection getConnection() {
          Connection conn = null;
          try { 
              Context initContext = new InitialContext();
              // context.xml에서 등록한 이름(jdbc/mySQLDB)으로
              // 데이터소스(connection pool)를 찾아서 사용
              DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/mySQLDB");
              conn = ds.getConnection();
              // connection을 얻어오면서 transaction 시작
              conn.setAutoCommit(false);
              
          } catch (Exception e) {
              System.out.println("[JdbcTemplate.getConnection] : " + e.getMessage());
              e.printStackTrace();
          }
          return conn;
      }
  
      public static boolean isConnected(Connection conn) {
  
          boolean validConnection = true;
          try {
              if (conn == null || conn.isClosed())
                  validConnection = false;
          } catch (SQLException e) {
              validConnection = false;
              e.printStackTrace();
          }
          return validConnection;
      }
  
      public static void close(Connection conn) {
          if (isConnected(conn)) {
              try {
                  conn.close();
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          }
      }
  
      public static void close(Statement stmt) {
          try {
              if (stmt != null) {
                  stmt.close();
              }
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      public static void close(ResultSet rset) {
          try {
              if (rset != null)
                  rset.close();
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      public static void commit(Connection conn) {
          try {
              if (isConnected(conn)) {
                  conn.commit();
                  System.out.println("[JdbcTemplate.commit] : DB Successfully Committed!");
              }
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  
      public static void rollback(Connection conn) {
          try {
              if (isConnected(conn)) {
                  conn.rollback();
                  System.out.println("[JdbcTemplate.rollback] : DB Successfully Rollbacked!");
              }
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }
  }
  ```



- BookDAO.java

  ``` java
  public class BookDAO {
  	// 하나의 Transaction으로 동작하기 위해
  	// 멤버 변수로 Connection을 선언하고 이 Connection을 공유하여 사용
  	private Connection con;
  
  	public BookDAO(Connection con) {
  		this.con = con;
  	}
  ```

  

  - MySQL을 이용해 DB 접속을 위한 JDBC Driver Loading
  - getConnection()을 호출하여 DB Connection 객체를 얻어옴

  위의 과정을 DBCP가 대신 수행

  

- BookService.java

  ``` java
  // service 객체를 만들기 위한 class
  public class BookService {
  	// Business Logic(Transaction)에 대한 Method만 명시
  	// 하나의 Transaction(기능)당 1개의 Method가 이용
  	public List<BookVO> getBooks(String keyword) {
  		// 로직처리 (DB 처리, 더하기, 빼기, for, if, etc..)
  		// Transaction : 작업의 최소 단위
  		Connection con = null;
  		List<BookVO> list = null;
  		try {
  			con = common.DBTemplate.getConnection();
  			// common.DBTemplate.getConnection() 코드에
  			// con.setAutoCommit(false); 가 있어서
  			// con 얻어오는 순간 transaction이 시작 됨
  			System.out.println("getBooks() 연결 성공!");
  
              // Pool로부터 할당받은 Connection 객체를
              // BookDAO 객체에 넘겨줌
  			BookDAO dao = new BookDAO(con);
  			list = dao.select(keyword);
  			// 얻어온 결과를 이용해서 transaction의 commit과 rollback을 판단
  			if (list != null) {
  				// transaction이 성공적으로 수행 됨
  				// DB 처리 작업을 실제 DB에 적용
  				con.commit();
  			} else {
  				// transaction 처리에 오류가 있는 경우
  				// DB 처리 작업을 무효화 시킴
  				con.rollback();
  			}
  		} catch (Exception e) {
  			System.out.println(e);
  		} finally {
  			try {
  				con.close();
  			} catch (Exception e) {
  				System.out.println(e);
  			}
  		}
  		return list;
  	}
  }
  ```

  

- BookSearchServlet.java

  ``` java
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
  			throws ServletException, IOException {
  
  		// 1. 입력받고
  		String keyword = request.getParameter("search_keyword");
  
  		// 2. 로직처리해서
  		// 로직처리하기 위한 객체인 Service 객체를 생성
  		BookService service = new BookService();
  
  		// Service 객체를 생성한 후 실제 작업을 지시
  		List<BookVO> result = service.getBooks(keyword);
  
  		// 3. 결과 데이터 출력
  		// JSON으로 변형해서 클라이언트에 전달
  		ObjectMapper mapper = new ObjectMapper();
  		String json = mapper.writeValueAsString(result);
  
  		// 클라이언트에 보낼 데이터 형식을 먼저 지정해 줘야 함
      	// 기본 문자열로 문자코드 = utf8
  		response.setContentType("text/plain; charset=utf8");
  
  		// 데이터 통로 생성
  		PrintWriter out = response.getWriter();
  		// 데이터 통로에 데이터 쌓기
  		out.println(json);
  		// 데이터 보내기
  		out.flush();
  		// 데이터 통로 닫기
  		out.close();
  	}
  
  ```

  

## Android

- 문제 1 : UI Thread (Activity Thread, Main Thread) 에서는 Network 연결을 할 수 없음

  ​		  Network 처리는 시간이 오래 걸리는 작업이기 때문에

  ​		  사용자의 interrupt에 대한 처리를 놓칠 위험이 있음

  ​		  Main thread의 최우선 순위 처리는 사용자와의 interrupt여야 함

- 해결 1 : Runnable interface를 구현한 객체를 생성하고

  독자적인 외부 Thread를 생성해서 이 객체를 수행하면 됨 

  

- 문제 2 : 결과 데이터를 ListView에 뿌려야 하는데

  ​		      외부 Thread는 UI를 제어할 수 없음

  ​			   Main Thread (UI Thread)에서만 UI를 제어할 수 있음

- 해결 2 : Handler를 이용해서 외부 Thread에서 UI Thread로

  ​			  ListView에 사용할 데이터 넘김

  ​			  따라서 , Runnable을 구현한 Class 생성시 Handler를 인자로 넘겨줘야 함

  > 0805_MVC Web 구현 3.md 참고
