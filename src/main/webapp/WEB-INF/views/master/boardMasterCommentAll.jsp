<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 게시판 댓글 전체 목록</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css"></link>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/Master.js"></script>
<div class="boardReplyManagement">
            <h2>게시판 댓글 목록</h2>
            <table class="table table-hover table-bordered">
                <thead class="table-light">
                    <tr>
                        <th style="width:2%;">No.</th>
                        <th style="width:30%;">댓글내용</th>
                        <th style="width:8%;">작성자</th>
                        <th style="width:8%;">작성일</th>
                        <th style="width:15%;">관리</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${commentList}" var="comment">
                    <tr>
                        <td>${comment.idx}</td>
                        <td>${comment.content}</td>
                        <td>${comment.userid}</td>
                        <td>${comment.regDT}</td>
                        <td>
                           <a href="#" class="btn btn-outline-success btn-sm commentDetailBtn" data-idx="${comment.idx}">상세보기</a>
                            <a href="#" class="btn btn-outline-danger btn-sm" onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- 페이지네이션 -->
        <nav>
            <ul class="pagination justify-content-center">
                <c:set var="pageGroupSize" value="5" />
                <c:set var="totalPages" value="${totalPages}" />
                <c:set var="startPage" value="${((currentPage - 1) / pageGroupSize) * pageGroupSize + 1}" />
                <c:set var="endPage" value="${startPage + pageGroupSize - 1}" />

                <!-- endPage가 totalPages보다 크면 totalPages로 설정 -->
                <c:if test="${endPage > totalPages}">
                    <c:set var="endPage" value="${totalPages}" />
                </c:if>

                <!-- 첫 번째 페이지로 이동 -->
                <c:if test="${startPage > 1}">
                    <li class="page-item">
                        <a class="page-link" href="/master/boardMasterCommentAll?currentPage=1&pageSize=${pageSize}">
                            &laquo;&laquo;
                        </a>
                    </li>
                </c:if>

                <!-- 이전 그룹으로 이동 -->
                <c:if test="${startPage > 1}">
                    <li class="page-item">
                        <a class="page-link" href="/master/boardMasterCommentAll?currentPage=${startPage - 1}&pageSize=${pageSize}">
                            &lsaquo;
                        </a>
                    </li>
                </c:if>

                <!-- 페이지 번호 -->
                <c:forEach var="i" begin="${startPage}" end="${endPage}">
                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                        <a class="page-link" href="/master/boardMasterCommentAll?currentPage=${i}&pageSize=${pageSize}">
                            ${i}
                        </a>
                    </li>
                </c:forEach>

                <!-- 다음 그룹으로 이동 -->
                <c:if test="${endPage < totalPages}">
                    <li class="page-item">
                        <a class="page-link" href="/master/boardMasterCommentAll?currentPage=${endPage + 1}&pageSize=${pageSize}">
                            &rsaquo;
                        </a>
                    </li>
                </c:if>

                <!-- 마지막 페이지로 이동 -->
                <c:if test="${endPage < totalPages}">
                    <li class="page-item">
                        <a class="page-link" href="/master/boardMasterCommentAll?currentPage=${totalPages}&pageSize=${pageSize}">
                            &raquo;&raquo;
                        </a>
                    </li>
                </c:if>
            </ul>
        </nav>

       <div class="modal fade" id="commentModal" tabindex="-1" aria-labelledby="commentModalLabel" aria-hidden="true">
           <div class="modal-dialog">
               <div class="modal-content">
                   <div class="modal-header">
                       <h5 class="modal-title" id="commentModalLabel">댓글 상세 정보</h5>
                       <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                   </div>
                   <div class="modal-body">
                       <!-- 댓글 정보 -->
                       <div class="comment-section">
                           <h6>댓글 내용:</h6>
                           <div class="comment-content bg-light p-2 rounded">
                               <p id="commentContent" class="mb-1"></p>
                               <small class="text-muted">작성자: <span id="commentAuthor"></span> | 작성일: <span id="commentDate"></span></small>
                           </div>
                       </div>
                       <hr />
                       <!-- 답글 목록 -->
                       <div class="replies-section">
                           <h6>답글 목록</h6>
                           <div id="replyList" class="bg-white p-2 rounded" style="max-height: 300px; overflow-y: auto;">
                               답글이 없습니다.
                           </div>
                       </div>
                   </div>
                   <div class="modal-footer">
                       <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                   </div>
               </div>
           </div>
       </div>