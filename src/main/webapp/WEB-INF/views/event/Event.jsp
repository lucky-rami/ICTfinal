<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/inc/page_header.jspf" %>
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
</head>

<body>
    <div class="outer-div">
        <div class="left-div">
            <div class="bottom-left-div">
                <c:forEach var="newevent" items="${newEvent}">
                    <div class="image-wrapper">
                        <div class="banner-card">
                            <div class="tag">🔥HOT🔥</div>
                            <div class="card-content">
                                <h2 class="title">${newevent.title}</h2>
                                <ul class="icon-list">
                                    <li><span class="heart-icon">❤</span><span class="num">62</span></li>
                                    <li><span class="view-icon">👁</span><span class="num">20</span></li>
                                </ul>
                            </div>
                            <a href="/Event_view?idx=${newevent.idx}">
                                <img src="/img/${newevent.thumfile}" alt="${newevent.title}" class="left-image">
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

        <div class="right-div">
                <ul class="image-list">
                    <c:forEach var="event" items="${fetchEvent}">
                        <li class="right-banner-card">
                            <div class="tag">Event</div>
                            <div class="card-content">
                                <h2 class="title">${event.title}</h2>
                                <p class="content"></p>
                                <a href="/Event_view?idx=${event.idx}">
                                <img src="http://192.168.1.180:8000/${event.thumfile}" alt="${event.title}" ></a>
                                <ul class="icon-list">
                                    <li><span class="heart-icon">❤</span><span class="num">6</span></li>
                                    <li><span class="view-icon">👁</span><span class="num">2</span></li>
                                </ul>
                            </div>
                        </li>
                    </c:forEach>
                </ul>
            </div>
    </div>
</body>
</html>
