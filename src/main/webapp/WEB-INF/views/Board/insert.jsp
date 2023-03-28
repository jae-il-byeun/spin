<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


 <link href="<c:url value='/resources/css/summernote-bs4.min.css'></c:url>" rel="stylesheet">
 <script src="<c:url value='/resources/js/summernote-bs4.min.js'></c:url>"></script>
<div class="container">
	<h1>게시글 작성</h1>
	<form action="<c:url value='/Board/insert'></c:url>"method="POST" enctype="multipart/form-data">
		<div class="form-group">
			<label for="type">게시판:</label>
			<select class="from-control" name="bo_bt_num" id="type">
				<option value="0">게시판을 선택하세요.</option>
				<c:forEach items="${btList}" var="bt">
					<option value="${bt.bt_num}">${bt.bt_name}</option>
				</c:forEach>
			</select>
		</div>
		
		<div class="form-group" >
			<label for="title">제목:</label>
			<input type="text" class="form-control" id="title" name="bo_title">
		</div>
		<div id="common" style="display:none">
			<div class="form-group">
				<label for="content">내용:</label>
			</div>
			<textarea id="content" name="bo_content"></textarea>
			<div class="form-group mt-3">
				<label>첨부파일:</label>
				<input type="file" class="form-control"  name="files">
				<input type="file" class="form-control"  name="files">
				<input type="file" class="form-control"  name="files">
			</div>
		</div>
		<div id="image" style="display:none">
			<div class="form-group mt-3">
				<label>첨부파일:</label>
				<input type="file" class="form-control"  name="files" accept="image/*">
				<input type="file" class="form-control"  name="files" accept="image/*">
				<input type="file" class="form-control"  name="files" accept="image/*">
			</div>	
		</div>
		<button class="btn btn-outline-success col-12 mt-3">작성완료</button>
	</form>
	</div>
	 <script>
      $('#content').summernote({
        placeholder: '내용을 입력하세요',
        tabsize: 2,
        height: 400
      });
      
      $('#type').change(function(){

    	  let val = $(this).val();
    	  $('#common').hide();
		  $('#image').hide();
		  if(val == 0)
			  return;
    	  if(common.indexOf(val) >= 0){//공통이면 기본키를 가져오기때문에 이미지는 기본키를 가져오지 않는다. 그래서 기본키로  데이터가 있으면 공통 없으면 이미지로 판단한다.
    		  $('#common').show();
    	  }else{
    		  $('#image').show();
    	  }
      });
      $('form').submit(function(){
      	let bo_bt_num = $('[name=bo_bt_num]').val();
      if(bo_bt_num == 0){
    	  alert('게시판을 선택하세요');
    	  $('[name=bo_bt_num]').focus();
    	  return false;
      }
      let bo_title = $('[name=bo_title]').val();
      if(bo_title.trim().length == 0){
    	  alert('제목을 입력하세요');
    	  $('[name=bo_title]').focus();
    	  return false;
      }
      let bo_content = $('[name=bo_content]').val();
      if(bo_content.trim().length == 0){
    	  alert('내용을 입력하세요');
    	  return false;
      }
      });
      let common = []; //기본키를 배열에 넣는다.
      <c:forEach items="${btList}" var="bt">
      	<c:if test="${bt.bt_type == '일반'}">common.push('${bt.bt_num}')</c:if>
      </c:forEach>
      
    </script>