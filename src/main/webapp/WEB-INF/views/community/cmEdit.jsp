<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf" %>



	<meta name="viewport" content="width=device-width, initial-scale=1">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="/ckeditor/ckeditor.css"/>
    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/super-build/ckeditor.js"></script>
    <script src="/ckeditor/ckeditor.js"></script>
    <link href="/css/cmWrite.css" rel="stylesheet" type="text/css">



    <section class="top_info">
        <div class="cm_tab">
            <h1>커뮤니티</h1>
            <div class="cm_opt">
                <ul class="list">
                    <li class="${commtype == 'all' ? 'selected' : ''}">
                        <a href="javascript:void(0);" class="btn" onclick="showTab('all')">최신글보기</a>
                    </li>
                    <li class="${commtype == '10' ? 'selected' : ''}">
                        <a href="javascript:void(0);" class="btn" onclick="showTab('10')">자랑</a>
                    </li>
                    <li class="${commtype == '20' ? 'selected' : ''}">
                        <a href="javascript:void(0);" class="btn" onclick="showTab('20')">덕질</a>
                    </li>
                    <li class="${commtype == '30' ? 'selected' : ''}">
                        <a href="javascript:void(0);" class="btn" onclick="showTab('30')">친목</a>
                    </li>
                    <li class="${commtype == '40' ? 'selected' : ''}">
                        <a href="javascript:void(0);" class="btn" onclick="showTab('40')">팬아트</a>
                    </li>
                    <li>
                        <a href="/notice2" class="btn">통합공지</a>
                    </li>
                </ul>
            </div>
        </div>
    </section>


    <div class="container">
        <form class="write_tbl" method="post" action="/cmEditOk" onsubmit="return commuFormCheck()">
        <input type="hidden" name="idx" value="${vo.idx}"/>
            <table class="cm-write">
                <tr>
                    <th>
                        <div class="topics">
                            <select class="filter-dropdown" id="code" name="code">
                                <option value="">분류를 선택하세요</option>
                                <option value="10" ${vo.commtype == '자랑' ? 'selected' : ''}>자랑</option>
                                <option value="20" ${vo.commtype == '덕질' ? 'selected' : ''}>덕질</option>
                                <option value="30" ${vo.commtype == '친목' ? 'selected' : ''}>친목</option>
                                <option value="40" ${vo.commtype == '팬아트' ? 'selected' : ''}>팬아트</option>
                            </select>

                        </div>
                    </th>
                    <td>
                        <input type="text" name="title" value="${vo.title}" required id="title" size="100" placeholder="제목을 입력하세요">
                    </td>
                </tr>
            </table>

            <div>
                <textarea id="content" name="content" class="smarteditor2" placeholder="내용을 입력하세요" style="width: 90%; height: 100%;">
                ${vo.content}
                </textarea>
            </div>

            <div style="margin: 30px auto 0 auto; text-align: center;">
                <button type="submit" class="write_btn">수정하기</button>
            </div>

        </form>
    </div>

<%@include file="/WEB-INF/inc/footer.jspf" %>

<script>

    setTimeout(function() {


     // 커뮤니티 페이지 전용 로그인 상태 확인 함수 호출
     checkLoginStatusForCommunity();
    }, 400);

    function checkLoginStatusForCommunity() {
     CKEDITOR.ClassicEditor.create(document.getElementById("content"),option)
         .then(editor => {
             console.log('CKEditor 5 is ready.');
             window.editorInstance = editor; // 에디터 인스턴스를 전역 변수로 저장
         })
         .catch(error => {
             console.error('CKEditor 5 initialization error:', error);
         });

        var token = localStorage.getItem("token"); //토근 값 가져오기
        document.getElementById("token").value=token;
     };




     function commuFormCheck() {
        alert("수정 되었습니다.");
          if (!window.editorInstance) {
              console.error('CKEditor instance is not initialized.');
              return false;
          }


          if (document.getElementById('code').value == '') {
              alert('분류를 선택하세요');
              return false;
          }
          if (document.getElementById('title').value == '') {
              alert('제목을 입력하세요.');
              return false;
          }
          if (window.editorInstance.getData().trim() === '') {
              alert('내용을 입력하세요.');
              return false;
          }

          // CKEditor 내용 반영
              document.getElementById('content').value = window.editorInstance.getData();

          return true;
     }


     // showTab 함수 정의 (탭 전환)
     function showTab(commtype) {
         // commtype 값에 따라 페이지 이동
         switch (commtype) {
             case 'all':
                 window.location.href = '/cmList?commtype=all'; // 최신글보기
                 break;
             case '10':
                 window.location.href = '/cmList?commtype=10'; // 자랑
                 break;
             case '20':
                 window.location.href = '/cmList?commtype=20'; // 덕질
                 break;
             case '30':
                 window.location.href = '/cmList?commtype=30'; // 친목
                 break;
             case '40':
                 window.location.href = '/cmList?commtype=40'; // 팬아트
                 break;
             default:
                 console.error('잘못된 commtype 값입니다: ' + commtype);
         }
     }
 </script>
</body>
