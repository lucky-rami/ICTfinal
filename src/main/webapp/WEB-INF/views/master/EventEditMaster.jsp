<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 이벤트 수정</title>
<link rel="stylesheet" href="/ckeditor/ckeditor.css" type="text/css" />
    <script src="https://cdn.ckeditor.com/ckeditor5/39.0.0/super-build/ckeditor.js"></script>
    <script src="/ckeditor/ckeditor.js"></script>
    <script src="/js/Master.js"></script>
<div class="EventAdd">
<h2>이벤트 수정</h2>
<form id="eventEditForm" action="/master/EventEditMasterOk" method="post">
<div><input type="hidden" id="idx" name="idx" value="${event.idx}"></div>
<div><input type="hidden" id="token" name="token" value="${token}"></div>
    <div>
        <label for="title">제목</label>
        <input type="text" id="title" name="title" value="${event.title}">
    </div>

    <div>
        <label for="내용">내용</label>
        <textarea id="content" name="content" rows="10">${event.content}</textarea>
    </div>
     <div>
            <label for="event_date">출시일:</label>
            <input type="date" id="event_date" name="event_date" value="${event.event_date}">
        </div>

        <div>
        <label for="셈네일 이미지">썸네일</label>
        <input type="file" id="thumfile" name="thumfile" accept="image/*">
        </div>
    <div>
        <input type="submit" value="이벤트 수정">
    </div>
</form>
</div>

