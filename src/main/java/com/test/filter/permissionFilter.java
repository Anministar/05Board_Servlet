package com.test.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.dto.AuthDto;

public class permissionFilter implements Filter{
	
	//grade
	//0 - 익명계정
	//1 - 일반계정
	//2 - 관리자계정
	
	
	// /board/list - grade 0 이상
	// /board/post - grade 1 이상
	
	// /notice/list - grade 1 이상
	// /notice/post - grade 2 이상
	
	Map<String, Integer> pageGradeMap = new HashMap();
	
	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {  // 최초 한번만 실행되는 얘임. FilterConfig라는 안에는 Servlet도 포함하고 있음.
		String path = filterConfig.getServletContext().getContextPath(); // fiflterConfig 안에 Servlet에 대한 내용도 가지고 있음. Servlet 객체에서 위치 정보를 꺼내오는것.
		
		pageGradeMap.put(path + "/board/list.do", 0);
		pageGradeMap.put(path + "/board/post.do", 1);
		
		pageGradeMap.put(path + "/notice/list.do", 1);
		pageGradeMap.put(path + "/notice/post.do", 2);
		
	}



	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		//Request 요청 전처리
		System.out.println("[PF] PermissionFilter Start!");
		
		//HttpServletRequest, Response 캐스팅
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		//URI 꺼내오기
		String uri = request.getRequestURI();
		System.out.println("[PF] URI : " + uri);
		
		//Session으로부터 authdto꺼내오기
		HttpSession session = request.getSession(false);
		AuthDto adto = (AuthDto) session.getAttribute("authdto");
		if(adto != null) {
			// 페이지 권한 가져오기
			int pageGrade = 0;
			pageGrade = pageGradeMap.get(uri);
			
			//유저 권한 가져오기
			int userGrade = 0;
			userGrade = Integer.parseInt(adto.getGrade());
			
			System.out.println("[PF] USERGrade : " + userGrade);
			System.out.println("[PF] PAGEGrade : " + pageGrade);
			
			//익명계정(0) 이 로그인이 필요한 (1이상) 페이지로 접근 요청한 경우
			if(pageGrade >= 1 && userGrade ==0) { //익명계정이 접근 권한이 1 이상인 페이지에 접근하려고 하는것 --> 안됨. 
				throw new ServletException("로그인이 필요한 페이지입니다..");
			}
			if(pageGrade == 2 && userGrade == 1) { //일반계정이 접근 권한이 2인 관리자 페이지에 접근하려고 하는것 --> 안됨.
				throw new ServletException("관리자 권한이 필요한 페이지입니다..");
			}
			
		}
		
		
		
		
		chain.doFilter(req, resp);
			
		System.out.println("[PF] PermissionFilter End!");
		
	}
}
