package com.test.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.dto.BookVO;
import com.test.service.BookService;

@WebServlet("/search")
public class BookSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BookSearchServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

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

		// 클라이언트에 보낼 데이터 형식을 먼저 지정해 줘야 함 -> 기본 문자열로 문자코드 = utf8
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
