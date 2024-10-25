<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 신고계정 유저리스트</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css"></link>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/MasterPage.js"></script>
<div class="reportinguser-list-container">
            <h2>신고 계정 목록</h2>
            <table class="reportinguser-list table table-hover table-bordered">
                <thead class="table-light">
                    <tr>
                        <th style="width:5%">아이디</th>
                        <th style="width:12%">신고유형</th>
                        <th style="width:30%">제제내용</th>
                        <th style="width:12%">Start</th>
                        <th style="width:12%">End</th>
                        <th style="width:8%">신고횟수</th>
                        <th style="width:20%">관리</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="ban" items="${reportingUser}">
                    <tr>
                        <td>${ban.userid}</td>
                        <td>
                            <c:if test="${not empty ban.report_type}">
                                    <c:forEach var="type" items="${fn:split(ban.report_type, ',')}">
                                        ${type}<br/>
                                    </c:forEach>
                                </c:if>
                                <c:if test="${empty ban.report_type}">신고 유형 없음</c:if>
                        </td>
                        <td>${ban.reason}</td>
                        <td>${ban.stopDT}</td>
                        <td>${ban.endDT}</td>
                        <td>${ban.totalUserReport}</td>
                        <td>
                            <button class="btn btn-outline-primary btn-sm">메모</button>
                            <button class="btn btn-outline-danger btn-sm">삭제</button>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>

           <!-- 페이지네이션 -->
                  <nav>
                             <ul class="pagination justify-content-center">
                                 <c:set var="pageGroupSize" value="5" />
                                 <c:set var="startPage" value="${((currentPage - 1) / pageGroupSize) * pageGroupSize + 1}" />
                                 <c:set var="endPage" value="${startPage + pageGroupSize - 1 > totalPages ? totalPages : startPage + pageGroupSize - 1}" />

                                 <!-- 첫 번째 페이지로 이동 -->
                                 <c:if test="${startPage > 1}">
                                     <li class="page-item">
                                         <a class="page-link" href="/master/reportinguserMasterList?currentPage=1&pageSize=${pageSize}">
                                             &laquo;&laquo;
                                         </a>
                                     </li>
                                 </c:if>

                                 <!-- 이전 그룹으로 이동 -->
                                 <c:if test="${startPage > 1}">
                                     <li class="page-item">
                                         <a class="page-link" href="/master/reportinguserMasterList?currentPage=${startPage - 1}&pageSize=${pageSize}">
                                             &lsaquo;
                                         </a>
                                     </li>
                                 </c:if>

                                 <!-- 페이지 번호 -->
                                 <c:forEach var="i" begin="${startPage}" end="${endPage}">
                                     <li class="page-item ${i == currentPage ? 'active' : ''}">
                                         <a class="page-link" href="/master/reportinguserMasterList?currentPage=${i}&pageSize=${pageSize}">
                                             ${i}
                                         </a>
                                     </li>
                                 </c:forEach>

                                 <!-- 다음 그룹으로 이동 -->
                                 <c:if test="${endPage < totalPages}">
                                     <li class="page-item">
                                         <a class="page-link" href="/master/reportinguserMasterList?currentPage=${endPage + 1}&pageSize=${pageSize}">
                                             &rsaquo;
                                         </a>
                                     </li>
                                 </c:if>

                                 <!-- 마지막 페이지로 이동 -->
                                 <c:if test="${endPage < totalPages}">
                                     <li class="page-item">
                                         <a class="page-link" href="/master/reportinguserMasterList?currentPage=${totalPages}&pageSize=${pageSize}">
                                             &raquo;&raquo;
                                         </a>
                                     </li>
                                 </c:if>
                             </ul>
                         </nav>