<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>JSP Template</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<link href="/css/Event.css" rel="stylesheet" type="text/css">
</link>


<bady>
<div class="outer-div">
    <div class="left-div">
        <div class="bottom-left-div">
            <div class="image-wrapper">
                <div class="banner-card">
                    <div class="tag">${title}</div>
                    <div class="card-content">
                        <ul class="icon-list">
                            <li><span class="heart-icon">❤</span><span class="num">5</span></li>
                            <li><span class="view-icon">👁</span><span class="num">42</span></li>
                        </ul>
                    </div>
                    <img src="https://bc8azosk4j.ecn.cdn.ofs.kr/aniplustv/banner/banner_20240913115309.png" alt="이미지"
                        class="left-image">
                </div>
            </div>
        </div>
    </div>
            <div class="right-div">
                <ul class="image-list">
                    <c:forEach var="event" items="${aniListWithEvents}">
                        <li class="right-banner-card">
                            <div class="tag">1</div>
                            <div class="card-content">
                                <h2 class="title">${event.title}</h2>
                                <p class="content"></p>
                                <a href="/Event_view?aniId=${event.idx}">
                                <img src="/img/${event.thumfile}" alt="${event.title}" ></a>
                                <ul class="icon-list">
                                    <li><span class="heart-icon">❤</span><span class="num">21</span></li>
                                    <li><span class="view-icon">👁</span><span class="num">12</span></li>
                                </ul>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
</body>