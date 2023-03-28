<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="container-fluid">
	<h1>예제2</h1>
	<p>화면에서 서버로 데이터를 post방식으로 전달하는 예제</p>
	<form  method="post"action="/spring/ex2">
		<input type="text" name="name">	<br>
		<input type="text" name="age"> <br>
		<button>전송</button>
	</form>
	
	
	<p>post 방식으로 서버에 데이터를 보내는 경우p</p>
	<ol>
		<li>url에 데이터가 노출되면 안되는 경우 : 로그인, 회원가입</li>
		<li>서버에 보내는 데이터가 많은 경우 : 게시글 작성</li>
		<li>파일을 전송하는 경우</li>
	</ol>
</div>





<!--  1일차
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  Hi my name is ${name1}. </P>
<p>  제 나이는 ${age} 살입니다.</p>
<form action="/spring/" method="post">
	<input type="text" name="name"> <br>
	<input type="text" name="age"> <br>
	<button>전송</button>
</form>
</body>
</html>
 -->