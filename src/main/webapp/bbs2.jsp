<%@page import="bbs.NovelDAO"%>
<%@page import="bbs.NovelDTO"%>
<%@page import="user.MemberDAO"%>
<%@page import="user.MemberDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="bbs.ContextDTO" %>
<%@ page import="bbs.ContextDAO" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html> 
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html"; charset="UTF-8">
<meta name="viewport" content="width=device-width", initial-scale"="1">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/custom.css">
<title>JSP 게시판 웹 사이트</title>
<style type="text/css">
	a, a:hover {
		color: #000000;
		text-decoration: none;
	}
</style>
</head>
<body>
	<% 
		String userID = null;
		if (session.getAttribute("uid") != null) {
			userID = (String) session.getAttribute("uid");
		}
		int pageNumber = 1;
		if (request.getParameter("pageNumber") != null) {
			pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
		}
		int bbsID = 0;
		if (request.getParameter("num") != null) {
			bbsID = Integer.parseInt(request.getParameter("num"));
		}
	%>
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expanded="false">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="main.jsp">웹 소설 게시판 사이트</a>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li class="active"><a href="main.jsp">메인</a></li>
				<li><a href="bbs.jsp">게시판</a></li>
			</ul>
			<% 
				if (userID == null) {
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">접속하기<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="login.jsp">로그인</a></li>
						<li><a href="join.jsp">회원가입</a></li>
					</ul>
				</li>
			</ul>
			<% 		
				} else {
			%>
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">회원관리<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><%= new MemberDAO().checkUsername(userID) %></li>
						<li><a href="logoutAction.jsp">로그아웃</a></li>
					</ul>
				</li>
			</ul>
			<%		
				}
			%>
		</div>
	</nav>
	<div class="container">
		<div class="row">
			<%
				NovelDTO ndto = new NovelDAO().getBbs(bbsID);
			%>
			<h2 style="text-align: center; padding-bottom: 1em"><%=ndto.getTitle() %></h2>
			<h4 style="text-align: center; border: 1px solid black; padding:1em; margin-bottom: 1em"><%=ndto.getIntroduce() %></h4>
		</div>
		<div class="row">
			<table class="table table-striped" style="text-align: center; border: 1px solid #dddddd">
				<thead>
					<tr>
						<th style="background-color: #eeeeee; text-align: center;">번호</th>
						<th style="background-color: #eeeeee; text-align: center;">제목</th>
						<th style="background-color: #eeeeee; text-align: center;">조회수</th>
					</tr>
				</thead>
				<tbody>				
					<%
						ContextDAO bbsDAO = new ContextDAO();
						MemberDAO mdao = new MemberDAO();
						ArrayList<ContextDTO> list = bbsDAO.getList(pageNumber, bbsID);
						for (int i = 0; i < list.size(); i++) {
					%>
					<tr>
						<td><%=(pageNumber-1)*10 + list.get(i).getRownum() %></td>
						<td><a href="view.jsp?code=<%= bbsID%>&num=<%= list.get(i).getNum() %>">
							<%= list.get(i).getTitle()%>
						</a></td>
						<td><%= list.get(i).getVisitcount() %></td>
					</tr>
					<%		
						}
					%>
				</tbody>
			</table>
			<a href="bbs.jsp" class="btn btn-primary">목록</a>
			<% 
				if (pageNumber != 1) {
			%>
				<a href="bbs2.jsp?num=<%=bbsID %>&pageNumber=<%=pageNumber - 1%>" class="btn btn-success btn-arrow-left">이전</a>
			<%
				} if (bbsDAO.nextPage(pageNumber + 1, bbsID)) {
			%>
				<a href="bbs2.jsp?num=<%=bbsID %>&pageNumber=<%=pageNumber + 1%>" class="btn btn-success btn-arrow-left">다음</a>
			<%
				} if(mdao.searchUser(bbsID).equals(mdao.checkUsername(userID))){
			%>
			<a href="write2.jsp?num=<%=bbsID %>" class="btn btn-primary pull-right">소설작성</a>
			<a href="fileAction.jsp?num=<%=bbsID %>" class="btn btn-primary pull-right">소설내려받기</a>
			<%
				}
			%>
		</div>
	</div>
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
</body>
</html>