<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 공지사항</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css"></link>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/MasterPage.js"></script>
<script src="/js/Master.js"></script>
<div class="notice-list-container">
            <h2>공지사항 리스트</h2>
            <table class="notice-list table table-hover table-bordered">
                <thead class="table-light">
                    <tr>
                        <th style="width:5%">No.</th>
                        <th style="width:50%">제목</th>
                        <th style="width:10%">작성자</th>
                        <th style="width:10%">작성일</th>
                        <th style="width:10%">수정일</th>
                        <th style="width:15%;">관리<a href="/master/noticeAddMaster" class="btn btn-secondary btn-sm" style="margin-left:5px">추가</a></th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="notice" items="${noticeList}">
                    <tr>
                        <td>${notice.idx}</td>
                        <td>${notice.title}</td>
                        <td>${notice.adminid}</td>
                        <td>${notice.regDT}</td>
                        <td>${notice.modifyDT}</td>
                        <td>
                            <button class="btn btn-outline-secondary btn-sm"><a href="/master/noticeEditMaster/${notice.idx}">수정</a></button>
                            <button class="btn btn-outline-danger btn-sm deleteNoticeBtn" data-idx="${notice.idx}">삭제</button>
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
                                                 <a class="page-link" href="/master/noticeMasterList?currentPage=1&pageSize=${pageSize}">
                                                     &laquo;&laquo;
                                                 </a>
                                             </li>
                                         </c:if>

                                         <!-- 이전 그룹으로 이동 -->
                                         <c:if test="${startPage > 1}">
                                             <li class="page-item">
                                                 <a class="page-link" href="/master/noticeMasterList?currentPage=${startPage - 1}&pageSize=${pageSize}">
                                                     &lsaquo;
                                                 </a>
                                             </li>
                                         </c:if>

                                         <!-- 페이지 번호 -->
                                         <c:forEach var="i" begin="${startPage}" end="${endPage}">
                                             <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                 <a class="page-link" href="/master/noticeMasterList?currentPage=${i}&pageSize=${pageSize}">
                                                     ${i}
                                                 </a>
                                             </li>
                                         </c:forEach>

                                         <!-- 다음 그룹으로 이동 -->
                                         <c:if test="${endPage < totalPages}">
                                             <li class="page-item">
                                                 <a class="page-link" href="/master/noticeMasterList?currentPage=${endPage + 1}&pageSize=${pageSize}">
                                                     &rsaquo;
                                                 </a>
                                             </li>
                                         </c:if>

                                         <!-- 마지막 페이지로 이동 -->
                                         <c:if test="${endPage < totalPages}">
                                             <li class="page-item">
                                                 <a class="page-link" href="/master/noticeMasterList?currentPage=${totalPages}&pageSize=${pageSize}">
                                                     &raquo;&raquo;
                                                 </a>
                                             </li>
                                         </c:if>
                                     </ul>
                                 </nav>
        </div>

