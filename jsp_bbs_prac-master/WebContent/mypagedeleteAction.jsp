  <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="user.User" scope="page" />
<jsp:setProperty name="user" property="userID" />
<jsp:setProperty name="user" property="userPassword" />
<jsp:setProperty name="user" property="userName" />
<jsp:setProperty name="user" property="userGender" />
<jsp:setProperty name="user" property="userEmail" />
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
         userID = (String) session.getAttribute("userID");
      } 
      if(user.getUserPassword() == null || user.getUserName() == null || user.getUserGender() == null || user.getUserEmail() == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('입력되지 않은 사항이 있습니다')");
			script.println("history.back()");
			script.println("</script>");
      }else {
         UserDAO userDAO = new UserDAO();
         int result = userDAO.delete(userID);
         if(result == -1){
            PrintWriter script = response.getWriter();
            script.println("<script>");
            script.println("alert('회원탈퇴에 실패했습니다.')");
            script.println("history.back()");
            script.println("</script>");
         } else { // 회원탈퇴 성공
            PrintWriter script = response.getWriter();
            session.invalidate();
            script.println("<script>");
            script.println("location.href = 'main.jsp'");
            script.println("alert('회원탈퇴가 완료되었습니다.')");
            script.println("</script>");
         }
      }
   
   %>
</body>
</html>