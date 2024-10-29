<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 게시판 전체 목록</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css"></link>
<script src="/js/Master.js"></script>
<div class="boardManagement">
            <h2>게시판 전체 목록</h2>
            <div class="summary">
                <div>
                    <strong>총 게시글 수</strong>
                    <p id="totalBoard">${totalBoard} 개</p>
                </div>
                <div>
                    <strong>오늘 작성된 게시글 수</strong>
                    <p id="todayBoard">${todayBoard} 개</p>
                </div>
                <div>
                    <strong>7일간 작성된 게시글 수</strong>
                    <p id="lastWeekBoard">${lastWeekBoard} 개</p>
                </div>
            </div>

            <table class="table table-hover table-bordered">
                <thead class="table-light">
                    <tr>
                        <th style="width:2%">No.</th>
                        <th style="width:6%">카테고리</th>
                        <th style="width:30%">제목</th>
                        <th style="width:5%">작성자</th>
                        <th style="width:7%">작성일</th>
                        <th style="width:4%">조회수</th>
                        <th style="width:10%">관리</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="board" items="${boardList}">
                    <tr>
                        <td>${board.idx}</td>
                        <td>
                            <c:choose>
                                <c:when test="${board.commtype == 10}">자랑</c:when>
                                <c:when test="${board.commtype == 20}">덕질</c:when>
                                <c:when test="${board.commtype == 30}">친목</c:when>
                                <c:when test="${board.commtype == 40}">팬아트</c:when>
                                <c:when test="${board.commtype == 50}">추천</c:when>
                                <c:otherwise>기타</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${board.title}</td>
                        <td>${board.author}</td>
                        <td>${board.regDT}</td>
                        <td>${board.hit}</td>
                        <td>
                            <a href="/cmView/${board.idx}" class="btn btn-outline-success btn-sm">상세보기</a>
                            <button class="btn btn-outline-danger btn-sm deleteBoardBtn" data-idx="${board.idx}">삭제</button>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- 페이징 영역 -->
        <nav>
           <ul class="pagination justify-content-center">
               <c:set var="pageGroupSize" value="5" />
               <c:set var="startPage" value="${((currentPage - 1) / pageGroupSize) * pageGroupSize + 1}" />
               <c:set var="endPage" value="${startPage + pageGroupSize - 1 > totalPages ? totalPages : startPage + pageGroupSize - 1}" />

               <!-- 이전 그룹으로 이동 -->
               <c:if test="${startPage > 1}">
                   <li class="page-item">
                       <a class="page-link" href="/master/boardMasterAll?currentPage=${startPage - 1}&pageSize=${pageSize}">&laquo;</a>
                   </li>
               </c:if>

               <!-- 페이지 번호 -->
               <c:forEach var="i" begin="${startPage}" end="${endPage}">
                   <li class="page-item ${i == currentPage ? 'active' : ''}">
                       <a class="page-link" href="/master/boardMasterAll?currentPage=${i}&pageSize=${pageSize}">${i}</a>
                   </li>
               </c:forEach>

               <!-- 다음 그룹으로 이동 -->
               <c:if test="${endPage < totalPages}">
                   <li class="page-item">
                       <a class="page-link" href="/master/boardMasterAll?currentPage=${endPage + 1}&pageSize=${pageSize}">&raquo</a>
                   </li>
               </c:if>
           </ul>
        </nav>

