<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<!-- 필드 이름을 썼을 때 필드를 직접 호출한는게 아니라 getter를 호출 
XXX.name을 쓰면 XXX.getName()이 호출된다
url : /login
화면 : login.jsp
- 아이디, 비번을 입력해서 서버로 전송하도록 화면을 구성

- 서버
-화면에서 전송한 아이디 비번을 콘솔에 출력
-->
${info1.name},${info1.num}

</body>
</html>
