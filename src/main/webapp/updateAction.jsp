<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bbs.ContextDAO" %>
<%@ page import="bbs.ContextDTO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
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
		if (userID == null) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("location.href = 'login.jsp'");
			script.println("history.back()");
			script.println("</script>");
		}

		int bbsID = 0;
		if (request.getParameter("bbsID") != null) {
			bbsID = Integer.parseInt(request.getParameter("bbsID"));
		}
		ContextDTO bbs = new ContextDAO().getBbs(bbsID);
		if (bbsID == 0) {
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않는 글입니다.')");
			script.println("location.href = 'bbs2.jsp?num="+ bbs.getCode() +"'");
			script.println("history.back()");
			script.println("</script>");
		}
			if (request.getParameter("title") == null || request.getParameter("context") == null
					|| request.getParameter("title") == "" || request.getParameter("context") == ""){
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('입력이 안된 사항이 있습니다.')");
					script.println("history.back()");
					script.println("</script>");
				} else {
					ContextDAO bbsDAO = new ContextDAO();
					int result = bbsDAO.update(code, bbsID, request.getParameter("title"), request.getParameter("context"));
					if (result == -1) {
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("alert('글 수정에 실패 했습니다.')");
						script.println("history.back()");
						script.println("</script>");
					} else {
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("location.href = 'bbs2.jsp?num="+ code +"'");
						script.println("</script>");
					}
				}		
	%>
</body>
</html>