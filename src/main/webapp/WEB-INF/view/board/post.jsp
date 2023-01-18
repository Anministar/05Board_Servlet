<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- BS5 -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
	crossorigin="anonymous">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
	crossorigin="anonymous"></script>
<!-- BSICON -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css">
<style>
a {
	text-decoration: none;
	color: black;
}

ul {
	list-style: none;
	padding: 0px;
}

header {
	margin-botoom: 10px;
}

header nav {
	height: 80px;
	background-color: #F2F2F2;
}

header nav ul {
	height: 80px;
	display: flex;
	justify-content: space-around;
	align-items: center;
}

header nav ul li {
	
}

section {
	height: 700px;
}

.container {
	position: relative;
}

.container * {
	margin-bottom: 10px;
}

.container .msg {
	position: absolute;
	left: 0px;
	right: 0px;
	top: -15px;
	margin: auto;
	font-size: 0.5rem;
	color: red;
	padding-left: 15px;
}
</style>


</head>

<body>


	<header>
		<nav>
			<ul>
				<li><a href="javascript:void(0)">회사소개</a></li>
				<li><a href="${pageContext.request.contextPath}/notice/list.do">공지사항</a></li>
				<li><a href="${pageContext.request.contextPath}/board/list.do">자유게시판</a></li>
				<li><a href="javascript:void(0)">개인정보수정</a></li>
				<li><a href="${pageContext.request.contextPath}/auth/logout.do">로그아웃</a></li>
			</ul>
		</nav>
	</header>

	<section class="container">
		<!-- 메시지 -->
		<div class="msg">${msg}</div>
		<!-- 브레드크럼(페이지경로) -->
		<div>
			<a href="${pageContext.request.contextPath}/main.do"> <i
				class="bi bi-house-door"></i>
			</a> > BOARD > POST
		</div>

		<h1>자유게시판</h1>
		<p></p>

		<form action="${pageContext.request.contextPath }/board/post.do" method="post" >
			<!-- enctype="multipart/form-data" ==> 파일을 포함한 데이터를 포멧으로 전달할 때 필요한 코드 -->
			<table class="table w-50">
			<tr>
				<td>Title</td>
				<td><input type="text" class="form-contorl" name="title"/></td>
			</tr>
			
			
			<tr>
				<td>Email</td>
				<td><input type="text" class="form-contorl" name="email" value="${authdto.email }"/></td>
			</tr>
			
			<tr>
				<td>Content</td>
				<td><textarea id="" cols="30" rows="10" name="content" class="form-contorl"></textarea></td>
			</tr>
			
			<!-- <tr> 
				<td>Files</td>
				<td><input type="file"class="form-contorl" name="files" multiple /></td>
			</tr> -->
			
			<tr>
				<td colspan=2>
					<input type="submit" value="전송" class="btn btn-primary" />
					<input type="reset" value="초기화" class="btn btn-secondary"/>
				</td>
			</tr>
			
			</table>
		</form>

		<!-- 글쓰기버튼 -->
		<%-- <div>
			<a href="${pageContext.request.contextPath}/board/post.do">글쓰기</a>
		</div> --%>


		</form>

	</section>


</body>
</html>