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
        <form class="write_tbl" method="post" action="/cmWriteOk" onsubmit="return commuFormCheck()">
            <table class="cm-write">
                <tr>
                    <th>
                        <div class="topics">
                            <select class="filter-dropdown" id="code" name="code" value="">
                                <option value="">분류를 선택하세요</option>
                                <option value="10">자랑</option>
                                <option value="20">덕질</option>
                                <option value="30">친목</option>
                                <option value="40">팬아트</option>
                            </select>
                        </div>
                    </th>
                    <td>
                        <input type="text" name="title" value="" id="title" size="100" placeholder="제목을 입력하세요">
                    </td>
                </tr>
            </table>

            <div>
                <textarea id="content" name="content" class="smarteditor2" placeholder="내용을 입력하세요" style="width: 90%; height: 100%;"></textarea>
            </div>

            <div style="margin: 30px auto 0 auto; text-align: center;">
                <button type="submit" class="write_btn">글등록하기</button>
            </div>

        </form>
    </div>

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



     // 글 작성 폼 제출 시 호출되는 함수
     function commuFormCheck() {
         // CKEditor 내용 유효성 검사
         if (!window.editorInstance || window.editorInstance.getData().trim() === '') {
             alert('내용을 입력하세요.');
             return false;
         }

         // 입력 필드 값 가져오기
         const code = document.getElementById("code").value;
         const title = document.getElementById("title").value;
         const content = window.editorInstance.getData().trim(); // CKEditor의 내용 가져오기

         if (!code || code.trim() === "") {
             alert("분류를 선택하세요.");  // code가 비어 있으면 경고 메시지 출력
             console.log("Received code: " + code);  // 디버그용 로그 출력
             return false;
         }

         // 로컬 스토리지에서 JWT 토큰 가져오기
         const token = localStorage.getItem("token");
         if (!token) {
             alert('로그인이 필요합니다.');  // 토큰이 없을 경우 로그인 필요 메시지
             location.href = "/user/login";  // 로그인 페이지로 이동
             return false;
         }

         // 서버로 전송할 데이터를 FormData 객체에 추가
         const postData = new URLSearchParams();
         postData.append("code", code);  // code 파라미터 추가
         postData.append("title", title);
         postData.append("content", content);
         postData.append("token", token);  // token 추가

         // AJAX 요청 보내기
         $.ajax({
             url: "/cmWriteOk",
             type: "POST",
             data: postData.toString(),
             contentType: "application/x-www-form-urlencoded",
             headers: {
                 "Authorization": `Bearer ${token}`  // Authorization 헤더에 JWT 토큰 추가
             },
             success: function(data) {
                 alert('글 작성이 완료되었습니다.');  // 성공 메시지
                 location.href = "/cmList";  // 글 목록 페이지로 이동
             },
             error: function(xhr, status, error) {
                 if (xhr.status === 401) {
                     alert('인증에 실패했습니다. 다시 로그인하세요.');
                     location.href = "/user/login";  // 로그인 페이지로 이동
                 } else {
                     alert("요청 처리 중 오류가 발생했습니다.");
                 }
                 console.error("Error:", error);  // 오류 출력
             }
         });

         return false;  // 기본 폼 제출 방지
     }

 </script>
</body>
</html>