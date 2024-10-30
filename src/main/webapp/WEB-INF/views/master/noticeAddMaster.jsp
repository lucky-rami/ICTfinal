<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 공지사항 추가</title>
<!-- CKEditor 5 스크립트만 포함 -->
    <link rel="stylesheet" href="/ckeditor/ckeditor.css"/>
    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/super-build/ckeditor.js"></script>
      <script src="/ckeditor/ckeditor.js"></script>
      <style>
    /* 경고 메시지 박스 스타일 */
    .warning-box {
        display: none;
        position: fixed;
        bottom: 20px;
        left: 50%;
        transform: translateX(-50%);
        background-color: #f44336;
        color: white;
        padding: 15px;
        border-radius: 5px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
        z-index: 1000;
        font-size: 16px;
        opacity: 0;
        transition: opacity 0.5s ease;
    }

    .warning-box.show {
        display: block;
        opacity: 1;
    }

    .warning-box.hide {
        opacity: 0;
        transition: opacity 0.5s ease;
    }

    /* 프로그레스 바 스타일 */
    .progress-bar {
        width: 100%;
        height: 5px;
        background-color: rgba(255, 255, 255, 0.5);
        border-radius: 3px;
        margin-top: 10px;
        overflow: hidden;
        position: relative;
    }

    .progress-bar::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 0;
        height: 100%;
        background-color: #4caf50;
        transition: width 3s linear; /* 3초 동안 프로그레스 바가 채워지도록 애니메이션 */
    }

    .warning-box.show .progress-bar::before {
        width: 100%;
    }
      </style>
<script>
window.onload = () => {
    // CKEditor 5 초기화
    CKEDITOR.ClassicEditor.create(document.getElementById('content'), option)
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
function submitNoticeForm() {
    const title = document.getElementById('title').value.trim();
    const content = window.editorInstance.getData().trim();  // CKEditor에서 content 가져오기
    const token = localStorage.getItem('token');  // 로컬 스토리지에서 토큰 값 가져오기

    if (!title) {
        showWarning("제목을 입력해주세요.");
        return false;
    }

    if (!content) {
        showWarning("내용을 입력해주세요.");
        return false;
    }

    if (!token) {
        alert('로그인 후 다시 시도해 주세요.');
        location.href = '/user/login';  // 로그인 페이지로 이동
        return false;
    }

    // URL 인코딩 형식으로 데이터 설정
    const postData = new URLSearchParams();
    postData.append('title', title);
    postData.append('content', content);
    postData.append('token', token);  // token 추가

    // AJAX 요청 보내기
    $.ajax({
        url: "/master/noticeAddMasterOk",
        type: "POST",
        data: postData.toString(),
        contentType: "application/x-www-form-urlencoded",
        headers: {
            "Authorization": `Bearer ${token}`  // Authorization 헤더에 JWT 토큰 추가
        },
        success: function(data) {
            alert('글 작성이 완료되었습니다.');  // 성공 메시지
            location.href = "/master/noticeMasterList";  // 글 목록 페이지로 이동
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

// 경고 메시지 박스를 표시하고 자동으로 사라지게 하는 함수
function showWarning(message) {
    const warningBox = document.getElementById('warningMessage');
    const warningText = document.getElementById('warningText');
    warningText.textContent = message;

    // 경고 메시지 박스 보이기
    warningBox.classList.add('show');

    // 3초 후에 경고 메시지 박스 숨기기
    setTimeout(() => {
        warningBox.classList.remove('show');
        warningBox.classList.add('hide');
    }, 3000);
}

</script>
<div class="noticeAdd">
    <h2>공지사항 등록</h2>
    <form id="noticeForm" method="POST" action="/master/noticeAddMasterOk" onsubmit="return submitNoticeForm()">
        <div>
            <label for="title">제목</label>
            <input type="hidden" name="idx" />
            <input type="hidden" id="token" name="token" />
            <input type="text" id="title" name="title" placeholder="공지사항 제목을 입력하세요.">
        </div>

        <div>
            <label for="content">내용</label>
            <textarea id="content" name="content" rows="10"></textarea>
        </div>
        <div>
            <input type="submit" value="공지사항 등록">
        </div>

        <!-- 경고 메시지 박스 -->
        <div id="warningMessage" class="warning-box">
            <span id="warningText">경고 메시지</span>
            <div id="progressBar" class="progress-bar"></div> <!-- 프로그레스 바 추가 -->
        </div>
    </form>
</div>