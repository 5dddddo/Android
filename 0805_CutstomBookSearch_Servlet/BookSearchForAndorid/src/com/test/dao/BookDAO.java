// connection을 공용으로 사용해서 service가 transaction 단위로 동작
// dao에 있던 connection 코드를 service로 옮김

package com.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.test.dto.BookVO;

public class BookDAO {

	// 하나의 Transaction으로 동작하기 위해
	// 멤버 변수로 Connection을 선언하고 이 Connection을 공유하여 사용
	private Connection con;

	public BookDAO(Connection con) {
		this.con = con;
	}

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

		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

	public List<BookVO> select(String keyword) {
		List<BookVO> list = new ArrayList<BookVO>();
		try {
			String sql = "select bimgurl, btitle, bauthor, bprice from " + "books where btitle like ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, "%" + keyword + "%");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				// 각각의 책을 객체화시켜서 ArrayList에 저장
				BookVO temp = new BookVO();
				temp.setBauthor(rs.getString("bauthor"));
				temp.setBimgurl(rs.getString("bimgurl"));
				temp.setBprice(rs.getString("bprice"));
				temp.setBtitle(rs.getString("btitle"));
				list.add(temp);
			}

		} catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}
	
}
