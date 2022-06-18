<%@page import="user.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bbs.CommentDAO" %>
<%@ page import="bbs.ContextDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="bbs" class="bbs.CommentDTO" scope="page" />
<jsp:setProperty name="bbs" property="comment" />
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
		int code = 0;
		if (request.getParameter("code") != null) {
			code = Integer.parseInt(request.getParameter("code"));
		}
		int bbsID = 0;
		if (request.getParameter("bbsID") != null) {
			bbsID = Integer.parseInt(request.getParameter("bbsID"));
		}
		bbs.setCode(new ContextDAO().checkNum(code, bbsID));
		bbs.setTalker(new MemberDAO().searchUcode(userID));	
		if (userID == null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("location.href = 'login.jsp'");
			script.println("history.back()");
			script.println("</script>");
		}else {
			if (bbs.getComment() == null) {
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('입력된 내용이 없습니다.')");
				script.println("history.back()");
				script.println("</script>");
			} else {
				CommentDAO bbsDAO = new CommentDAO();
				int result = bbsDAO.insertWrite(bbs);
				if (result == -1) {
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('글쓰기에 실패 했습니다.')");
					script.println("history.back()");
					script.println("</script>");
				} else {
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("location.href = 'view.jsp?code="+ code +"&num=" + bbsID + "'");
					script.println("</script>");
				}
			}
	}			
	%>
</body>
</html>