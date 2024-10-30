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
<link href="/css/Event_view.css" rel="stylesheet" type="text/css">
</link>
<div>

    <div class="test" style="padding-top: 180px;">
        <c:forEach var="view" items="${event}"> <!-- events 리스트를 반복 -->
            <div class="box_txtPhoto">
                <h2 class="ev_tit">${view.title}</h2> <!-- 제목 표시 -->
                <hr/>
                <div class="box_img">
                    <img src="/img/${view.content}" alt="${view.title}"> <!-- 이미지 표시 -->

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