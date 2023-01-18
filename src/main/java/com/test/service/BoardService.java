package com.test.service;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.mysql.cj.protocol.x.SyncFlushDeflaterOutputStream;
import com.test.dao.BoardDao;
import com.test.dto.AuthDto;
import com.test.dto.BoardDto;
import com.test.dto.Criteria;
import com.test.dto.PageDto;

public class BoardService {

	BoardDao dao = BoardDao.getInstance();

	private String uploadDir;

	// 싱글톤
	private static BoardService instance;

	public static BoardService getInstance() {
		if (instance == null) {
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

		// 전체 게시물 건수 받기
		int totalcount = dao.getAmount();

		// PageDto 만들기
		PageDto pagedto = new PageDto(totalcount, criteria);

		// 시작게시물 번호 구하기(수정)
		int startno = criteria.getPageno() * criteria.getAmount() - criteria.getAmount();
		// 페이지 번호가 1 x 보여줄 양 10개 - 10 ==> 0 번째부터 보여주겠다
		// 페이지 번호가 2 x 보여줄 양 10개 - 10 ==> 10 번째부터 보여주겠다

		// PageDto를 Session에 추가
		HttpSession session = request.getSession(false);

		List<BoardDto> list = dao.SelectAll(startno, criteria.getAmount());
		if (list != null) {
			request.setAttribute("list", list);
//				request.setAttribute("pagedto", pagedto);
			session.setAttribute("pagedto", pagedto);
			// 글쓰기 후에 넘어가는 페이지가 이전의 페이지로 하고 싶다면 session에 정보를 저장해서 그걸 사용하면 됨.
			// 그러므로 request를 Session으로 올려버리면 pagedto를 어느위치에서나 사용할 수 있게 된다.

			return true;
		}
		return false;

	}

	// 게시물 추가하기
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

	// 게시물 추가하기 (업로드 포함)
	public boolean PostBoard(BoardDto dto, HttpServletRequest request) {

		boolean flag = false;

		// 디렉터리 경로 설정
		System.out.println("DIRPATH : " + request.getServletContext().getRealPath("/"));
		uploadDir = request.getServletContext().getRealPath("/") + "upload";
		// /Users/popo/Desktop/External
		// Exam/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/05Board/

		// 접속한 Email 계정 확인
		HttpSession session = request.getSession(false);
		AuthDto adto = (AuthDto) session.getAttribute("authdto");

		// UUID (랜덤값) 폴더생성
		UUID uuid = UUID.randomUUID();
		String path = uploadDir + File.separator + adto.getEmail() + File.separator + uuid;
		// File.separator == /

		// 추출된 파일정보 저장용 Buffer
		StringBuffer filelist = new StringBuffer();
		StringBuffer filesize = new StringBuffer();

		try {

			Collection<Part> parts = request.getParts();
			for (Part part : parts) {

				if (part.getName().equals("files")) {
					System.out.println("------------------------------------------");

//					System.out.println("PART 명 : " + part.getName());
//					System.out.println("PART 크기 : " + part.getSize());
//					for (String name : part.getHeaderNames()) {
//						System.out.println("Header Name : " + name);
//						System.out.println("Header Value : " + part.getHeader(name));
//					}

					// 파일명 추출
					System.out.println("파일명 : " + getFileName(part));
					// DB에는 파일 전체를 넣는게 아님.파일전체를 넣게되면 DB의 검색속도가 현저히 느려지게 됨.
					// 그래서 파일명과 파일 경로만 DB테이블에 담을 것임.
					String filename = getFileName(part);

					if (!filename.equals("")) {

						// 폴더생성
						File dir = new File(path);
						if (!dir.exists()) {
							dir.mkdirs();
//							System.out.println("Making Folder Sucessed!!");
						} 
//						else {
//							System.out.println("Making Floder Failed!!");
//						}
						
//						System.out.println(dir);
//						/Users/popo/Desktop/External Exam/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/05Board/upload/admin@naver.com/1ab926a4-5e58-411b-adbb-4952ed6e1053

						// 업로드
						part.write(path + File.separator + filename);
						//파일 업로드처리할 때 java.io.IOException: java.io.IOException: Unexpected output data 오류가 발생해서 1시간정도 오류 잡는데 소모함.
						//안되었던 이유로 추정되는게 1. 쓰기 권한이 없어서 2. 경로가 잘못돼서 인데 알고보니깐 위에 dir을 만들때 dir이 존재하지 않는다면 해당 경로에 대한 
						//폴더를 만들어야하는데 !를 적지 않아서 part.write(path + File.sepatator + filename)의 파일 업로드 처리를 할 수 없게 되었음.
						//그래서 1시간을 날렸음. 다음부터는 조심하자. 
						
						

						// 파일명 DB Table에 추가 위한 저장
						filelist.append(filename + ";");

						// 디렉토리 경로 DB Table에 추가 위한 저장
						filesize.append(part.getSize() + ";");
					}

					System.out.println("------------------------------------------");
				}
			}
			
			dto.setFilename(filelist.toString());
			dto.setFilesize(filesize.toString());

			// DB연결
			int result = dao.Insert(dto);
			if (result > 0) {
				flag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;

	}

	private String getFileName(Part part) {
//		------------------------------------------
//		PART 명 : files
//		PART 크기 : 18738763
//		Header Name : content-disposition
//		Header Value : form-data; name="files"; filename="스타일.zip"
//		Header Name : content-type
//		Header Value : application/zip
//		------------------------------------------
		String contentDisp = part.getHeader("content-disposition");
		// String contentDisp = form-data; name="files"; filename="스타일.zip"

		String[] tokens = contentDisp.split(";"); // [form-data, name = "files", filename = "스타일.zip"]

		String filename = tokens[tokens.length - 1]; // filenmae = "스타일.zip"
		// tokens.length가 3이니깐 -1 해서 2 즉 toekn[2] == [filename = " 스타일.zip]

		return filename.substring(11, filename.length() - 1); // 스타일.zip
	}

}
