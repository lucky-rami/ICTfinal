<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 자주묻는질문 수정</title>
<link rel="stylesheet" href="/ckeditor/ckeditor.css" type="text/css" />
    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/super-build/ckeditor.js"></script>
    <script src="/ckeditor/ckeditor.js"></script>
    <script src="/js/Master.js"></script>
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
            </script>
<div class="FAQEdit">
<h2>자주묻는 질문 수정</h2>
<form id="FAQEditForm" action="/master/FAQEditMasterOk" method="post">
    <input type="hidden" name="idx" id="idx" value="${FAQ.idx}">
            <input type="hidden" id="token" name="token">


        <div>
                 <label for="faqtype">카테고리</label>
                 <select id="code" name="code">
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
               <input type="text" id="question" name="question" placeholder="자주 묻는 질문 제목을 입력하세요."  value="${FAQ.question}">
             </div>

        <div>
            <label for="answer">답변</label>
            <textarea id="answer" name="answer" rows="10">${FAQ.answer}</textarea>
        </div>
        <div>
        <input type="submit" value="자주 묻는 질문 수정">
    </div>
</form>
</div>

