<%@page import="bbs.CommentDTO"%>
<%@page import="java.util.List"%>
<%@page import="bbs.CommentDAO"%>
<%@page import="bbs.NovelDAO"%>
<%@page import="user.MemberDAO"%>
<%@page import="user.MemberDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="bbs.ContextDTO" %>
<%@ page import="bbs.ContextDAO" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html"; charset="UTF-8">
<meta name="viewport" content="width=device-width", initial-scale"="1">
<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="css/custom.css">
<title>JSP 게시판 웹 사이트</title>
</head> 
<body>
	<% 
		String userID = null;
		if (session.getAttribute("uid") != null) {
			userID = (String) session.getAttribute("uid");
		}
		int code = 0;
		if (request.getParameter("code") != null) {
			code = Integer.parseInt(request.getParameter("code"));
		}
		int bbsID = 0;
		if (request.getParameter("num") != null) {
			bbsID = Integer.parseInt(request.getParameter("num"));
		}
		if (bbsID == 0) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않는 글입니다.')");
			script.println("location.href = 'bbs.jsp'");
			script.println("history.back()");
			script.println("</script>");
		}
		ContextDAO bbsDAO = new ContextDAO();
		ContextDTO bbs = bbsDAO.getBbs(code, bbsID);
		bbsDAO.updateVisitCount(bbs.getTitle());
		bbs = bbsDAO.getBbs(code, bbsID);
		String uname = new MemberDAO().searchUser(new NovelDAO().searchNovel(bbs.getTitle()));
		CommentDAO cdao = new CommentDAO();
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
			<table class="table table-striped"
				style="text-align: center; border: 1px solid #dddddd">
				<thead>
					<tr>
						<th colspan="3"
							style="background-color: #eeeeee; text-align: center;">게시판
							글보기</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td style="width: 20%;">글제목</td>
						<td colspan="2"><%= bbs.getTitle().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">","&gt;").replaceAll("\n", "<br>") %></td>
					</tr>
					<tr>
						<td>작성자</td>
						<td colspan="2"><%= uname%></td>
					</tr>
					<tr>
						<td>조회수</td>
						<td colspan="2"><%= bbs.getVisitcount()%></td>
					</tr>
					<tr>
						<td>내용</td>
						<td colspan="2" style="min-height: 200px; text-align: left;"><%= bbs.getContext().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">","&gt;").replaceAll("\n", "<br>") %></td>
					</tr>
				</tbody>
			</table>
			<a href="bbs2.jsp?num=<%=bbs.getCode() %>" class="btn btn-primary">목록</a>

			<%
				if (userID != null && userID.equals(new MemberDAO().checkUserId(uname))) {
			%>
			<a href="update.jsp?code=<%= code%>&bbsID=<%= bbsID %>" class="btn btn-primary">수정</a>
			<a onclick="return confirm('정말로 삭제하시겠습니까?')"
				href="deleteAction.jsp?code=<%= code%>&bbsID=<%= bbsID %>" class="btn btn-primary">삭제</a>
			<%
			}
			%>
			<br><br>
			<div class="container">
				<div class="row" style="padding-bottom: 1em">
					<h4>댓글 - <span><%=cdao.selectCount(bbs.getTitle())%>개</span></h4>
				</div>
				<ul class="list-group list-group-flush">
					<%
					List<CommentDTO> dtos = cdao.selectList(bbs.getTitle());
					for (CommentDTO dto : dtos) {
					%>
					<li class="list-group-item">
						<div>
							<p><%= new MemberDAO().checkUser(dto.getTalker())%></p>
							<div><%= dto.getComment()%></div>
						</div>
					</li>
					<%
					}
					%>
				</ul>
			</div>
			<form method="post" action="comment.jsp?code=<%= code%>&bbsID=<%= bbsID %>">
				<textarea class="form-control" placeholder="글 내용" name="comment" maxlength="2048" style="height: 300px; margin-bottom: 1em"></textarea>
				<input type="submit" class="btn btn-primary pull-right" style="width: 100px; margin-bottom: 1em"" value="작성">
			</form>
		</div>
	</div>
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="js/bootstrap.js"></script>
</body>
</html>