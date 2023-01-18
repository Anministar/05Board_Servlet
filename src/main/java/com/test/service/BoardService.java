package com.test.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.test.dao.BoardDao;
import com.test.dto.BoardDto;
import com.test.dto.Criteria;
import com.test.dto.PageDto;

public class BoardService {
	
	BoardDao dao = BoardDao.getInstance();	
	
	private String uploadDir;
	
	//싱글톤
	private static BoardService instance;
	public static BoardService getInstance() {
		if(instance == null) {
			instance = new BoardService();
		}
		return instance;
	}
	
	public BoardService() {
		
	}
	
	// 모든 게시물 가져오기
//	public boolean GetBoardList(HttpServletRequest request) {
//		List<BoardDto> list = dao.SelectAll();
//		if(list != null) {
//			request.setAttribute("list", list);
//			return true;	
//		}
//		return false;
//		
//	}
	
	
	
	// 모든 게시물 가져오기
		public boolean GetBoardList(Criteria criteria, HttpServletRequest request) {
			
			//전체 게시물 건수 받기
			int totalcount = dao.getAmount();
			
			//PageDto 만들기
			PageDto pagedto = new PageDto(totalcount, criteria);
			
			
			//시작게시물 번호 구하기(수정)
			int startno = criteria.getPageno() * criteria.getAmount() - criteria.getAmount();
			//				페이지 번호가 1  	   x   보여줄 양 10개       -    10 ==> 0 번째부터 보여주겠다
			//				페이지 번호가 2       x    보여줄 양 10개	     -    10 ==> 10 번째부터 보여주겠다
			
			
			//PageDto를 Session에 추가
			HttpSession session = request.getSession(false);
			
			List<BoardDto> list = dao.SelectAll(startno, criteria.getAmount());
			if(list != null) {
				request.setAttribute("list", list);
//				request.setAttribute("pagedto", pagedto);
				session.setAttribute("pagedto", pagedto);
				//글쓰기 후에 넘어가는 페이지가 이전의 페이지로 하고 싶다면 session에 정보를 저장해서 그걸 사용하면 됨.
				//그러므로 request를 Session으로 올려버리면 pagedto를 어느위치에서나 사용할 수 있게 된다.
				
				return true;	
			}
			return false;
			
		}
		
		
		//게시물 추가하기
//		public boolean PostBoard(BoardDto dto, HttpServletRequest request) {
//			
//			boolean flag = false;
//			
//			int result = dao.Insert(dto);
//			if(result > 0) {
//				flag = true;
//			} 
//			
//			return flag;
//		}
		
		
		
		
		//게시물 추가하기 (업로드 포함)
		public boolean PostBoard(BoardDto dto, HttpServletRequest request) {
			
			boolean flag = false;
			
			// 디렉터리 경로 설정
			
			// UUID (랜덤값)  폴더생성
			
			
			
			
			
			
			
			
			int result = dao.Insert(dto);
			if(result > 0) {
				flag = true;
			} 
			
			return flag;
		}
	
			
}
