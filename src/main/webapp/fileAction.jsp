<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.FileWriter"%>
<%@page import="java.io.BufferedWriter"%>
<%@page import="java.io.IOException"%>
<%@page import="bbs.NovelDTO"%>
<%@page import="bbs.NovelDAO"%>
<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@page import="bbs.ContextDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="bbs.ContextDAO" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%
	int bbsID = 0;
	if (request.getParameter("num") != null) {
		bbsID = Integer.parseInt(request.getParameter("num"));
	}
	NovelDTO ndto = new NovelDAO().getBbs(bbsID);
	ContextDAO bbsDAO = new ContextDAO();
	ArrayList<ContextDTO> dtos = bbsDAO.getList(bbsID);
	
	String userName = System.getProperty("user.name");
	
	try{
		File novel = new File("C:\\Users\\"+ userName + "\\Downloads\\" + ndto.getTitle() + ".txt");
		novel.createNewFile();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(novel));
		
		for(ContextDTO cdto : dtos){
			bw.write(cdto.getTitle());
			bw.write("\n");
			bw.write(cdto.getContext());
			bw.write("\n==================\n");
		}
		bw.close();
		
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('소설을 내려받았습니다.')");
		script.println("history.back()");
		script.println("</script>");
	}catch(IOException e){
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('소설 내려받기에 실패 했습니다.')");
		script.println("history.back()");
		script.println("</script>");
	}
	
	PrintWriter script = response.getWriter();
	script.println("<script>");
	script.println("location.href = 'bbs2.jsp?num="+ bbsID +"'");
	script.println("</script>");
	%>
</body>
</html>