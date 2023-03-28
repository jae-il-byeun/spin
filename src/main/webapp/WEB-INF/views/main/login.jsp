<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
	<title>login</title>
</head>
<body>
<h1>
	아이디 비번을 입력하세요
</h1>
<form action="/spring/login" method="post">
<!-- /spring/login 뒤에 login을 해야지 URL login과 연결이 된다. -->
	<input type="text" name="id" placeholder="아이디"> <br>
	<input type="password" name="passWord" placeholder="비밀번호"> <br>
	<button>전송</button>
</form>
</body>
</html>
