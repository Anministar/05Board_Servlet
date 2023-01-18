package com.test.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.test.dto.BoardDto;
import com.test.dto.PageDto;
import com.test.service.BoardService;

public class BoardPostController implements SubController {

	private static String msg;
	private BoardService service = BoardService.getInstance();
	
	
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) {

		try {
			// 0 Get 구별
			String method = req.getMethod();
			if (method.equals("GET")) {
				System.out.println("[BPC] 요청 Method : " + method);
				req.getRequestDispatcher("/WEB-INF/view/board/post.jsp").forward(req, resp);
				return;
			}
			
			
			// 1 파라미터 받기
			Map<String, String[]> params = req.getParameterMap();

			// 파라미터 확인
//						for (String name : params.keySet()) {
//							System.out.println("name : " + name + " val : " + params.get(name)[0]);
//						}

			// 2 Validation
			boolean isvalid = isValid(params);
			if (!isvalid) {
				// 유효성 체크 오류 발생시 전달할 메시지 + 이동될 경로
				req.setAttribute("msg", msg); // msg를 담아주고
				req.getRequestDispatcher("/WEB-INF/view/board/post.jsp").forward(req, resp); // 다시 join.jsp로 넘어가는것임.
				return;
			}

			// 3 Service
			
			BoardDto dto = new BoardDto();
			dto.setEmail(params.get("email")[0]);
			dto.setTitle(params.get("title")[0]);
			dto.setContent(params.get("content")[0]);
			
			
			boolean result = service.PostBoard(dto, req);

			// 4 View
			if (result) {
				HttpSession session = req.getSession(false);
				PageDto pagedto = (PageDto) session.getAttribute("pagedto");
				
				
				req.getRequestDispatcher("/board/list.do?pageno="+pagedto.getCriteria().getPageno()).forward(req, resp);
				//							여기 경로 부분이 /WEB-INF/view/board/list.jsp 이렇게 되어있었는데
				// /board/list.do로 바꾼이유는 포워딩이 계속 되어 있어서 요청 컨트롤러로 전달이 안되었던 것 같다.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private boolean isValid(Map<String, String[]> params) {
//		msg="유효성 체크 오류";
		return true;
	}

}
