<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.MemberDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="user.MemberDTO" scope="page" />
<jsp:setProperty name="user" property="uid" />
<jsp:setProperty name="user" property="upw" />
<jsp:setProperty name="user" property="uname" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html"; charset="UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body> 
	<%
		String userID = null;
		if (session.getAttribute("uid") != null) {
			userID = (String) session.getAttribute("uid");
		}
		if (userID != null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 로그인이 되어 있습니다.')");
			script.println("location.href = 'main.jsp'");
			script.println("history.back()");
			script.println("</script>");
		}
	
		if (user.getUid() == null || user.getUpw() == null || user.getUname()== null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('입력이 안된 사항이 있습니다.')");
			script.println("history.back()");
			script.println("</script>");
		} else {
			MemberDAO userDAO = new MemberDAO();
			int result = userDAO.join(user);
			if (result == -1) {
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('이미 존재하는 아이디입니다.')");
				script.println("history.back()");
				script.println("</script>");
			} else {
				session.setAttribute("uid", user.getUid());
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("location.href = 'main.jsp'");
				script.println("</script>");
			}
		}
	%>
</body>
</html>