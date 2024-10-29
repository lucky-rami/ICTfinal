<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 애니 추가</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css"></link>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/Master.js"></script>

<div class="aniAdd">
    <h2>애니 등록</h2>
    <form id="aniAddForm" enctype="multipart/form-data">
        <div><input type="hidden" id="token" name="token"/></div>

        <div>
            <label for="post_img">포스터 이미지:</label>
            <input type="file" id="post_img" name="post_img" accept="image/*">
        </div>

        <div>
            <label for="title">제목:</label>
            <input type="text" id="title" name="title">
        </div>

        <div>
            <label for="anitype">장르:</label>
            <select id="anitype" name="anitype">
                <option value="1">드라마</option>
                <option value="2">미스터리</option>
                <option value="3">스포츠</option>
                <option value="4">코미디</option>
                <option value="5">판타지</option>
                <option value="6">SF</option>
            </select>
        </div>

        <div>
            <label for="director">감독:</label>
            <input type="text" id="director" name="director">
        </div>

        <div>
            <label for="agetype">연령 등급:</label>
            <select id="agetype" name="agetype">
                <option value="1">전체</option>
                <option value="2">12세 이상</option>
                <option value="3">15세 이상</option>
                <option value="4">19세 이상</option>
            </select>
        </div>

        <div>
            <label for="outline">줄거리:</label>
            <textarea id="outline" name="outline" rows="4"></textarea>
        </div>

        <div>
            <input type="submit" value="애니 등록하기">
        </div>
    </form>
</div>