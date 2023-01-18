package com.test.controller;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.test.dto.Criteria;
import com.test.service.BoardService;

public class BoardListController implements SubController {

	private static String msg;
	private BoardService service = BoardService.getInstance();

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) {
		
//		 System.out.println("[MJC] Start-");

		try {

			// 1 파라미터 받기 
			Map<String, String[]> params = req.getParameterMap();
			
			// 파라미터 확인
//			for (String name : params.keySet()) {
//				System.out.println("name : " + name + " val : " + params.get(name)[0]);
//			}

			// 2 Validation
			boolean isvalid = isValid(params);
			if (!isvalid) {
				// 유효성 체크 오류 발생시 전달할 메시지 + 이동될 경로
				req.setAttribute("msg", msg); // msg를 담아주고
				req.getRequestDispatcher("/WEB-INF/view/main.jsp").forward(req, resp); // 다시 join.jsp로 넘어가는것임.
				return ;
			}

			// 3 Service
			Criteria criteria = null;
			if(params.get("pageno") == null) { // 최초 /board/list.do 접근 
				//parmas.get("pageno")[0] == null 이 안되는 이유는 처음에는 파라미터 값도 없는 거기에서 배열의 값을 뽑아내겠다고 하니깐  NullPointerException 오류가 계속 떴었음.
				
				criteria = new Criteria(); //pageno = 1 , amount = 10
				
			} else {
				// 페이지 이동 요청 했을때
				int pageno = Integer.parseInt(params.get("pageno")[0]);
				criteria = new Criteria(pageno, 10);
				
			}
			boolean result = service.GetBoardList(criteria, req);

			
			// 4 View(로그인페이지로 이동)

			if (result) {
				req.getRequestDispatcher("/WEB-INF/view/board/list.jsp").forward(req, resp);

			} 

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean isValid(Map<String, String[]> params) {

		return true;
	}

}
