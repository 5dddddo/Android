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
import com.test.service.BookService;

/**
 * Servlet implementation class BookSearchTtitleServelt
 */
@WebServlet("/searchTitle")
public class BookSearchTtitleServelt extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public BookSearchTtitleServelt() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 입력받고
		
		String keyword = request.getParameter("keyword");
		
		// 2. 로직처리를 Service에게 위임
		BookService service = new BookService();
		
		// 3. Service를 생성해서 service에게 일을 시킨 후 결과를 받아옴
		List<String> list = service.getBooksTitle(keyword);
				
		// 4. 출력처리 (JSON)
		response.setContentType("text/plain;charset=UTF8");
		// 데이터 연결 통로 생성
		PrintWriter out = response.getWriter();
		
		// 5. jackson library
		// list 배열을 json으로 변환
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(list);
		out.print(json);
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
