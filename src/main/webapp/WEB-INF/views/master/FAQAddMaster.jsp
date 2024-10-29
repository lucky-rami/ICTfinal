<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 자주묻는질문 추가</title>
<link rel="stylesheet" href="/ckeditor/ckeditor.css" type="text/css" />
    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/super-build/ckeditor.js"></script>
    <script src="/ckeditor/ckeditor.js"></script>
    <script>
        window.onload = () =>{
        // CKEditor 5 초기화
            CKEDITOR.ClassicEditor.create(document.getElementById('answer'), option)
                .then(editor => {
                    window.editorInstance = editor;  // 전역에 editor 객체 저장
                    console.log('CKEditor 초기화 완료:', editor);
                })
                .catch(error => {
                    console.error('CKEditor 초기화 오류:', error);
                });

            // 로컬스토리지에서 토큰값 가져오기
            var token = localStorage.getItem('token');
            document.getElementById('token').value = token;
        };


        // 공지사항 등록 함수
        function submitfaqForm() {

            if(!window.editorInstance || window.editorInstance.getData().trim() === '') {
                alert("answer 내용을 입력해주세요.");
                return false;
            }

            const code = document.getElementById('code').value;
            const question = document.getElementById('question').value;
            const answer = window.editorInstance.getData().trim();  // CKEditor에서 content 가져오기
            const token = localStorage.getItem('token');  // 로컬 스토리지에서 토큰 값 가져오기

            if(!code || code.trim() ===""){
            alert("분류를 선택하세요.");
            console.log("분류 선택한 코드는 " + code);
            return false;
            }

            if (!token) {
                alert('로그인 후 다시 시도해 주세요.');
                location.href = '/user/login';  // 로그인 페이지로 이동
                return false;
            }

            // URL 인코딩 형식으로 데이터 설정
            const postData = new URLSearchParams();
            postData.append('code', code);
            postData.append('question', question);
            postData.append('answer', answer);
            postData.append('token', token);  // token 추가

            // AJAX 요청 보내기
                     $.ajax({
                         url: "/master/FAQAddMasterOk",
                         type: "POST",
                         data: postData.toString(),
                         contentType: "application/x-www-form-urlencoded",
                         headers: {
                             "Authorization": `Bearer ${token}`  // Authorization 헤더에 JWT 토큰 추가
                         },
                         success: function(data) {
                             alert('글 작성이 완료되었습니다.');  // 성공 메시지
                             location.href = "/master/FAQMasterList";  // 글 목록 페이지로 이동
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
<div class="FAQAdd">
<h2>자주묻는 질문 등록</h2>
<form action="/master/FAQAddMasterOk" method="post" onsubmit="return submitfaqForm()">
        <input type="hidden" name="idx" />
        <input type="hidden" id="token" name="token" />

    <div>
         <label for="faqtype">카테고리</label>
         <select id="code" name="code" required>
             <option value="">카테고리 선택</option>
             <option value="1">쇼핑</option>
             <option value="2">계정</option>
             <option value="3">포인트</option>
             <option value="4">애니</option>
             <option value="5">서비스</option>
         </select>
    </div>

    <div>
        <label for="question">질문</label>
        <input type="text" id="question" name="question" placeholder="자주 묻는 질문 제목을 입력하세요."  required>
    </div>

    <div>
        <label for="answer">답변 </label>
        <textarea id="answer" name="answer" rows="10"></textarea>
    </div>
    <div>
        <input type="submit" value="자주 묻는 질문 등록">
    </div>
</form>
</div>

