<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="container-fluid">
	<h1>예제1</h1>
	<p>화면에서 서버로 데이터를 get방식으로 전달하는 예</p>
	<h2>예제1-1</h2>
	<a href="/spring/ex1?name=HerryPotter&age=20">서버로 HerryPotter 전송</a>
	<h2>예제1-2</h2>
	<form  method="get"action="/spring/ex1">
		<input type="text" name="name">	<br>
		<input type="text" name="age"> <br>
		<button>전송</button>
	</form>
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