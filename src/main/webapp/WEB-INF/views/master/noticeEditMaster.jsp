<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 공지사항 수정</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css"></link>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="/ckeditor/ckeditor.css" type="text/css" />
<script src="https://cdn.ckeditor.com/ckeditor5/39.0.0/super-build/ckeditor.js"></script>
<script src="/ckeditor/ckeditor.js"></script>
<script src="/js/Master.js"></script>

<div class="noticeEdit">
    <h2>공지사항 수정</h2>
    <form id="noticeEditForm" action="/master/noticeEditMasterOk" method="post">
        <input type="hidden" id="idx" name="idx" value="${noticeEdit.idx}">
        <input type="hidden" id="token" name="token" value="">

        <!-- 제목 -->
        <div>
            <label for="title">제목</label>
            <input type="text" id="title" name="title" value="${noticeEdit.title}" required>
        </div>

        <!-- 내용 (CKEditor 적용) -->
        <div>
            <label for="content">내용</label>
            <textarea id="content" name="content" rows="10" required>${noticeEdit.content}</textarea>
        </div>

        <!-- 제출 버튼 -->
        <div>
            <input type="submit" value="공지사항 수정">
        </div>
    </form>
</div>