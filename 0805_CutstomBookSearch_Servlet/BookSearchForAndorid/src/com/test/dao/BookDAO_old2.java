package com.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.test.dto.BookVO;

public class BookDAO_old2 {

	public List<String> selectTitle(String keyword) {
		// keyword를 입력 받아서 DB 검색해서
		// String[] 만들어서 return 해주는 DB 처리
		// logic 나오면 안 됨!

		List<String> list = new ArrayList<String>();
		try {
			// 상당히 로드가 많이 걸리는 JDBC 작업 -> 비효율적
			// Connection Pool을 사용하는 코드로 재작성
			// Apache Tomcat은 DBCP라는 ConnectionPool 기능을 제공
			// DBCP는 JNDI를 이용

			// Connection pool로부터 connection을 빌려와서 사용하므로
			// 사용자가 늘어나도 load가 커지지 않음
			Connection con = common.DBTemplate.getConnection();
			System.out.println("연결 성공!");

			con.setAutoCommit(false); // transaction이 시작
			// 3. Statement 생성
			String sql = "select btitle from books where btitle like ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");

			// 4. Query 설정
			ResultSet rs = pstmt.executeQuery();

			// 5. 결과 처리
			while (rs.next()) {
				list.add(rs.getString("btitle"));
			}

			// 6. 사용한 resource 해제
			rs.close();
			pstmt.close();
			// transaction 적용시킴
			//con.commit(); 
			// transaction 취소
			//con.rollback(); 
			con.close();

		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	// 원래는 이 함수가 Transation의 한 단위가 되어야 함
	// but, 문제 : 현재 각각의 dao method들이
	// 개별적으로 Connection 객체를 생성하며 하나의 transaction 단위로 동작함
	// 해결 : transaction을 설정하는 하나의 connection을 공용해서 사용해야 함
	public List<BookVO> getBooks(String keyword) {
		// 로직처리 (DB 처리를 포함)
		// Transaction : 작업의 최소 단위
		BookVO dao = new BookVO();

		// DB 처리 첫번째
		// dao.firstMethod();

		// DB 처리 두번째
		// dao.secondMethod();

		// DB 처리 세번째
		// dao.thirdMethod();

		return null;

	}
}
