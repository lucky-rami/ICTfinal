<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>



	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="/ckeditor/ckeditor.css"/>
    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/super-build/ckeditor.js"></script>
    <script src="/ckeditor/ckeditor.js"></script>
    <link href="/css/cmWrite.css" rel="stylesheet" type="text/css">



    <div class="top_info">
        <div class="cm_tab">
        <h1>커뮤니티</h1>
            <ul class="list">
                <li class="selected">
                    <a href="#tap1" class="btn">자랑</a>
                </li>
                <li>
                    <a href="#tap2" class="btn">덕질</a>
                </li>
                <li>
                    <a href="#tap3" class="btn">친목</a>
                </li>
                <li>
                    <a href="#tap4" class="btn">팬아트</a>
                </li>
            </ul>
        </div>
    </div>


<div class="container">
  <form class="write_tbl"method="post" action="/cafein/commu/writeOk" onsubmit="return commuFormCheck()">
     <table class="cm-write">
           <tr>
                  <th>
               <div class="topics" >
                   <select class="filter-dropdown" id="header_no" name="header_no" value="" >
                        <option value="">분류를 선택하세요 </option>
                     <c:if test="${logId=='goguma1234'}">
                        <option value="10">자랑</option>
                     </c:if>
                        <option value="20">덕질</option>
                        <option value="30">친목</option>
                        <option value="40">팬아트</option>
                   </select>
               </div>
                </th>
                <td>
                <input type="text" name="subject" value="" id="subject" size="100"placeholder="제목을 입력하세요">
                </td>
            </tr>
     </table>
      <input type="hidden" id="sub_content" name="sub_content">
      <div>
          <textarea id="content" name="content" class="smarteditor2"placeholder="내용을 입력하세요" style="width: 90%; height: 100%;"></textarea>
      </div>
     <div  style="margin: 30px auto 0 auto; text-align:center;">
          <button class="write_btn">글등록하기</button>
     </div>
  </form>
 </div>

 <script>
     window.onload =()=>{
         CKEDITOR.ClassicEditor.create(document.getElementById("content"),option)
             .then(editor => {
                 console.log('CKEditor 5 is ready.');
                 window.editorInstance = editor; // 에디터 인스턴스를 전역 변수로 저장
             })
             .catch(error => {
                 console.error('CKEditor 5 initialization error:', error);
             });
     };

     function commuFormCheck() {
          if (!window.editorInstance) {
              console.error('CKEditor instance is not initialized.');
              return false;
          }

          // CKEditor에서 HTML 데이터를 가져옵니다.
          const contentValue = window.editorInstance.getData();
          console.log('Content Value:', contentValue);

          // HTML에서 텍스트만 추출
          const tempDiv = document.createElement('div');
          tempDiv.innerHTML = contentValue;
          const textContent = tempDiv.textContent || tempDiv.innerText || '';

          // 텍스트의 처음 20자 추출
          const first68Chars = textContent.slice(0, 68);
          console.log('First 68 Characters:', first80Chars);

          document.getElementById('sub_content').value = first68Chars;

          if (document.getElementById('header_no').value === '') {
              alert('분류를 선택하세요');
              return false;
          }
          if (document.getElementById('subject').value === '') {
              alert('제목을 입력하세요.');
              return false;
          }

          if (document.getElementById('header_no').value ==='20' || document.getElementById('header_no').value ==='30') {
              if (contentValue.indexOf('<img') === -1) {
                  alert('최소한 사진 한 장을 올려주세요.');
                  return false;
              }
          }
          return true;
     }
 </script>
</body>
</html>