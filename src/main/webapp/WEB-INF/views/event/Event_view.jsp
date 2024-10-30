<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf" %>

<link href="/css/Event_view.css" rel="stylesheet" type="text/css">

<div class="event_view_container">
    <div class="test event_view_info" style="padding-top: 180px;">
        <div class="event_top"><span>EVENT</span></div>
        <c:forEach var="view" items="${event}"> <!-- events 리스트를 반복 -->
            <div class="box_txtPhoto">
                <h2 class="ev_tit">${view.title}</h2> <!-- 제목 표시 -->
                <hr/>
                <div class="box_img" style="margin-top: 30px;">

                        ${view.content}
                </div>
            </div>
        </c:forEach>
    </div>

    <div class="box_but">
        <a href="/Event" alt="배지콕콕 페이지로 이동">
            <input type="button" value="목록으로 돌아가기" class="but" />
        </a>
    </div>
</div>


	</div>
</div>

<%@include file="/WEB-INF/inc/footer.jspf"%>