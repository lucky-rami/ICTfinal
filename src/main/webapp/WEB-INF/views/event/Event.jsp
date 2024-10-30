<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/inc/page_header.jspf" %>

<link href="/css/Event.css" rel="stylesheet" type="text/css">

<div class="event_top"><span>EVENT</span></div>
<div class="outer-div">
    <div class="left-div">
        <div class="bottom-left-div">
            <c:forEach var="newevent" items="${newEvent}">
                <div class="image-wrapper">
                    <div class="banner-card">
                        <div class="tag">RECENT EVENT</div>
                        <div class="card-content">
                            <h2 class="title">${newevent.title}</h2>
                        </div>
                        <a href="/Event_view?idx=${newevent.idx}">
                            <img src="http://192.168.1.180:8000/${newevent.thumfile}" alt="${newevent.title}" class="left-image">
                        </a>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    <div class="right-div">
        <ul class="image-list">
            <c:forEach var="event" items="${fetchEvent}">
                <li class="right-banner-card">
                    <div class="tag">EVENT</div>
                    <div class="card-content">
                        <h2 class="title">${event.title}</h2>
                        <a href="/Event_view?idx=${event.idx}">
                        <img src="http://192.168.1.180:8000/${event.thumfile}" alt="${event.title}" ></a>

                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>

<%@include file="/WEB-INF/inc/footer.jspf"%>