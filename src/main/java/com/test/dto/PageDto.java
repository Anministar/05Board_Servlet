package com.test.dto;

public class PageDto {
	
	// 페이지 정보 (전체페이지, 현재페이지) 
	private int totalpage;		// 총게시물건수 / amount  
								// @ 예를 들자면 1000건의 게시물이 있는데 10개만 한 페이지에 보여준다면 100페이지가 나와야함. 
	
	private Criteria criteria;	// 현재페이지, 한페이지 당 읽을 게시물의 건수가 저장되어 있음. 
	
	// 블럭정보 
	private int pagePerBlock;	// 블럭에 표시할 페이지 개수(15건 지정)
	
	private int totalBlock;		// totalpage / pagePerBlock 
								// @ 예를 들면 100페이지가 있다면 한 블럭당 15페이지를 보여주고 싶다면 6블럭이 나오고 10페이지가 더 나옴. 
	
	private int nowBlock;		// 현재페이지 번호 / pagePerBlock 
								// @ 1블럭의 3페이지라면 15로 나누어서 몫 = 0 근데 여기서 +1을 더해줘서 현재 블럭을 알 수 있고
								// 막약 17페이지라면 15로 나누어서 몫 = 1.xx 이고 여기서 소수점을 지우는 등의 계산 처리를 해줘야함
								// 다음페이지 번호를 구하기 위해서도 필요하고, 이전,이후의 버튼을 활성화 시키기 위함도 있음.
	
	// 표시할 페이지 (블럭에 표시할 시작페이지 - 마지막 페이지) 
	private int startPage;
	private int endPage;
	
	//NextPrev 버튼 표시여부
	private boolean prev, next;
	
	public PageDto() {
		
	}
	
	public PageDto(int totalcount, Criteria criteria) {
		
		
		this.criteria = criteria;
		
		
		// 전체 페이지 계산
		totalpage = (int)Math.ceil( (1.0 * totalcount) / criteria.getAmount() );
		// Math.ceil은 소수값이 존재할 때 값을 올리는 역할
		// 예를 들어 1001건의 totalcount가 있다면 그걸 criteria.getAmount() == 10 으로 나누었을 때 100.1 이 나올꺼고 그걸 Math.ceil을 이용하여 올리는
		// 이유는 101로 만들어서 마지막 1건의 게시물을 저장할 수 있는 페이지 101페이지를 살리기 위해서 Math.ceil을 사용함.
		
		
		// 블럭 계산
		pagePerBlock = 15;
		totalBlock = (int) Math.ceil( (1.0 * totalpage) / pagePerBlock );
		nowBlock = (int) Math.ceil( (1.0 * criteria.getPageno()) / pagePerBlock ); // 현재 페이지 번호 / 15건
	
		// Next, Prev 버튼 활성화 유무
		prev = nowBlock > 1;
		next = nowBlock < totalBlock;
		
		// 블럭에 표시할 페이지 번호 계산
//		this.endPage = (int) Math.ceil((1.0 * criteria.getPageno()) / pagePerBlock ) * pagePerBlock;
		// totalpage는 실제로 100페이지까지 밖에 없는데 101 - 105까지 페이지는 건수가 없어도 생김. 이게 밑에 코드때문에 그럼.
//		this.endPage = nowBlock * pagePerBlock;
		
		// 그래서 삼항연산자를 이용하여 totalpage가 더 작다면 totalpage가 마지막 페이지번호로 오게끔 했음.
		this.endPage = (nowBlock * pagePerBlock < totalpage)?nowBlock * pagePerBlock : totalpage;
		
		this.startPage = nowBlock * pagePerBlock - pagePerBlock + 1;
		// @ 2번쨰 블럭 마지막 번호는 30 - 15 ==> 2번째 블럭의 첫번째 번호는 15? 아니다. 그렇기 때문에 + 1 을 해주는것.
		// nowBlock * pagePerBlock - pagePerBlock
		
	}
	
	// Getter and Setter

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	public int getPagePerBlock() {
		return pagePerBlock;
	}

	public void setPagePerBlock(int pagePerBlock) {
		this.pagePerBlock = pagePerBlock;
	}

	public int getTotalBlock() {
		return totalBlock;
	}

	public void setTotalBlock(int totalBlock) {
		this.totalBlock = totalBlock;
	}

	public int getNowBlock() {
		return nowBlock;
	}

	public void setNowBlock(int nowBlock) {
		this.nowBlock = nowBlock;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public void setPrev(boolean prev) {
		this.prev = prev;
	}

	public boolean isNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	// toString
	
	@Override
	public String toString() {
		return "PageDto [totalpage=" + totalpage + ", criteria=" + criteria + ", pagePerBlock=" + pagePerBlock
				+ ", totalBlock=" + totalBlock + ", nowBlock=" + nowBlock + ", startPage=" + startPage + ", endPage="
				+ endPage + ", prev=" + prev + ", next=" + next + "]";
	}
	

}
