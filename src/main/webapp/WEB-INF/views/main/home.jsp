<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %><!-- user정보가 뜨는 것을 막음 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="container-fluid">
<h1>Spring</h1>
<a href="/spring/ex1">데이터 전송 예제1</a> <br>
<a href="/spring/ex2">데이터 전송 예제2</a> <br>
<a href="/spring/ex3">데이터 전송 예제3</a>	<br>
<a href="/spring/ex4">데이터 전송 예제4</a>	<br>
<a href="/spring/ex5?num=2022123001">DB 연결 예제5</a>	<br>
</div>
${user}<!-- user정보가 뜸 -->

<!-- 
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