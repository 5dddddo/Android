package com.test.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.test.dao.BookDAO;
import com.test.dao.BookDAO_old2;
import com.test.dto.BookVO;

// service 객체를 만들기 위한 class
public class BookService {

	// Business Logic(Transaction)에 대한 Method만 명시
	// 하나의 Transaction(기능)당 1개의 Method가 이용

	public List<String> getBooksTitle(String keyword) {
		// 로직처리 (더하기, 빼기, for, if, etc..)
		// 로직처리 코드가 많이 나옴
		// Database 처리가 나와야 함 -> DAO에서 처리해야 함
		// service -> dao로 일 시킴
		// dao로 DB처리 후 결과 받아옴
		Connection con = null;
		List<String> list = null;
		try {
			con = common.DBTemplate.getConnection();
			BookDAO dao = new BookDAO(con);
			list = dao.selectTitle(keyword);
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
			} catch (SQLException e) {
				System.out.println(e);
			}
		}
		return list;
	}

	// 원래는 이 함수가 transation의 한 단위가 되어야 함
	// but, 문제 : 각각의 dao method들이 하나의 transaction 단위로 동작함
	// 해결 : transaction을 설정하는 connection을 공용해서 사용해야 함
	public List<BookVO> getBooks(String keyword) {
		// 로직처리 (DB 처리를 포함)
		// Transaction : 작업의 최소 단위
		Connection con = null;
		List<BookVO> list = null;
		try {
			con = common.DBTemplate.getConnection();
			// common.DBTemplate.getConnection() 코드에
			// con.setAutoCommit(false); 가 있어서
			// con 얻어오는 순간 transaction이 시작 됨
			System.out.println("getBooks() 연결 성공!");

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
