<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Enrolment.EnrolmentDAO" %>
<%@ page import="Enrolment.Enrolment" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String)session.getAttribute("userID");
		}
		if(userID == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("location.href = 'login.jsp'");
			script.println("</script>");
		}
		
		int stuID = 0;
		// url에 bbsID가 넘어온다면 bbsID 변수에 정보 저장
		if(request.getParameter("stuID") != null){
			stuID = Integer.parseInt(request.getParameter("stuID"));
		}
		if (stuID == 0){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다.')");
			script.println("location.href = 'Enrolment.jsp'");
			script.println("</script>");
		}
		Enrolment stu = new EnrolmentDAO().getstu(stuID);
		if(!userID.equals(stu.getUserID())){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('권한이 없습니다.')");
			script.println("location.href = 'Enrolment.jsp'");
			script.println("</script>");
		} else {
			EnrolmentDAO stuDAO = new EnrolmentDAO();
			int result = stuDAO.delete(stuID);
			if(result == -1){
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('글 삭제에 실패했습니다.')");
				script.println("history.back()");
				script.println("</script>");
			} else { // 글삭제 성공
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("location.href = 'Enrolment.jsp'");
				script.println("</script>");
			}
		}
		
	%>
</body>
</html>