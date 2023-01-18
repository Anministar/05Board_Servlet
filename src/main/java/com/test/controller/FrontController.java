package com.test.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//파일 업로드 정보
@MultipartConfig(
		
		fileSizeThreshold = 1024 * 1024 * 10, //10MB 업로드 파일이 10MB 이상이 될 떄 임시경로 (temp)에 저장.
		maxFileSize = 1024 * 1024 * 50,		  //50MB 업로드 파일의 최대 크기
		maxRequestSize = 1024 * 1024 * 100	  //100MB request 전체의 크기
		// multiple 설정으로 여러개의 파일을 올릴 수 있는데 개당 파일의 최대 크기가 50MB이고 전체 request 요청의 최대가 크기가 100MB임.
		
		)
//FrontController 에서 multipart로 요청이 들어왔을 때를 대비한 파일관련 처리를 해야함. 노테이션(어떤 어떤 코드라고 전달하는 역할)으로 전달해야함.
// ==> 이 서블릿은 Multipart/form-data를 받을 수 있는 서블릿이다 라는걸 알려주는 역할임.



public class FrontController extends HttpServlet {

	private Map<String, SubController> list;

	// init
	@Override
	public void init(ServletConfig config) throws ServletException {
		String path = config.getServletContext().getContextPath();  // /02MVC2 경로 저장 
		System.out.println("[FC] CONTEXT PATH : " + path);
		
		list = new HashMap();
		
		//URL, SubController 저장
		//Member
		list.put(path + "/member/join.do", new MemberJoinController());
		
		//Board
		list.put(path + "/board/list.do", new BoardListController());
		list.put(path + "/board/post.do", new BoardPostController());
		
		
		//Notice
		list.put(path + "/notice/list.do", new NoticeListController());
		list.put(path + "/notice/post.do", new NoticePostController());
		
		//Auth
		list.put(path + "/auth/login.do", new LoginController());
		list.put(path + "/auth/logout.do", new LogoutController());
		
		//Main
		list.put(path + "/main.do", new MainController());
		
	}

	// service
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//문자셋 설정(Filter 이동예정)
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset = UTF-8");
		
		//SubController 실행
		String uri = req.getRequestURI();
		System.out.println("[FC] URI : " + uri);
		//uri정보와 일치하는 컨트롤러가 꺼내지게 됨. 그래서 자연스레 업캐스팅이 일어나고 오버라이딩된 메서드 execute가 실행되는거임.
		SubController sub = list.get(uri);
		sub.execute(req, resp);
		
		
		
		
	}

}
