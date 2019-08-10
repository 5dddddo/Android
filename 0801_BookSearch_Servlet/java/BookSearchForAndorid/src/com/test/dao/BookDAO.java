package com.test.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

	public List<String> selectTitle(String keyword) {
		// keyword를 입력 받아서 DB 검색해서 
		// String[] 만들어서 return 해주는 DB 처리
		// logic 나오면 안 됨!
			
		List <String> list = new ArrayList<String>();
		try {
			// JDBC를 이용한 DB 처리
			
			// 1. Driver Loading
			// MySQL을 이용한 JDBC Driver class를 로딩
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("로딩 성공!");
			
			// 2. Connection 단계
			String id = "android";
			String pw = "android";
			String jdbcUrl = "jdbc:mysql://localhost:3306/library?"
					+ "characterEncoding=UTF8";
			
			Connection con = DriverManager.getConnection(jdbcUrl,id,pw);
			System.out.println("연결 성공!");
			
			// 3. Statement 생성
			String sql = "select btitle from books where btitle like ?";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1,  "%"+keyword+"%");
			
			// 4. Query 설정
			ResultSet rs = pstmt.executeQuery();
			
			// 5. 결과 처리
			while (rs.next()) {
				list.add(rs.getString("btitle"));
			}
			
			// 6. 사용한 resource 해제
			rs.close();
			pstmt.close();
			con.close();
			
		}catch (Exception e) {
			System.out.println(e);
		}
		return list;
	}

}
