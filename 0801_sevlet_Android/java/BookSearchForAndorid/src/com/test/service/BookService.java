package com.test.service;

import java.util.List;

import com.test.dao.BookDAO;

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
		
		BookDAO dao = new BookDAO();
		
		// dao class에는 CRUD 4가지 method만 사용!
		List<String> list = dao.selectTitle(keyword);
		return list;
	}

}
