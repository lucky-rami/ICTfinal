<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 게시판 리뷰 전체 목록</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css"></link>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/Master.js"></script>

<div class="boardReviewManagement">
    <h2>게시판 리뷰 목록</h2>
    <div class="summary">
        <div>
            <strong>총 리뷰 수</strong>
            <p id="totalBoard">${totalReviews} 개</p>
        </div>
        <div>
            <strong>오늘 작성된 리뷰 수 </strong>
            <p id="newUsers">${newUsers}  개</p>
        </div>
        <div>
            <strong>일주일간 작성된 리뷰 수</strong>
            <p id="newSignups">${newSignups}  개</p>
        </div>
    </div>
    <table class="table table-hover table-bordered">
        <thead class="table-light">
             <tr>
                                    <tr>
                                                    <th style="width:2%;">NO</th>
                                                    <th style="width:40%;">리뷰제목</th>
                                                    <th style="width:8%;">별점</th>
                                                    <th style="width:8%;">작성자</th>
                                                    <th style="width:8%;">작성일</th>
                                                    <th style="width:15%;">관리</th>
                                                </tr>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${reviewList}" var="review">
                                <tr>
                                <td>${review.idx}</td>
                                                    <td>${review.content}</td>
                                                    <td>
                                                        <div class="grade">
                                                            <c:forEach begin="1" end="5" var="i">
                                                                <c:choose>
                                                                    <c:when test="${i <= review.grade}">★</c:when>
                                                                    <c:otherwise>☆</c:otherwise>
                                                                </c:choose>
                                                            </c:forEach>
                                                        </div>
                                                    </td>
                                                    <td>${review.userid}</td>
                                                    <c:set var="formattedDate" value="${review.regDT}" />
                                                    <fmt:parseDate var="parsedDate" value="${formattedDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                                                    <td><fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd" /></td>
                                    <td>
                                        <a href="#" class="btn btn-outline-secondary btn-sm reviewDetailBtn" data-idx="${review.idx}">상세보기</a>
                                        <button class="btn btn-outline-danger btn-sm deleteReviewBtn" data-idx="${review.idx}">삭제</button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </div>
                    </td>
        </tbody>
    </table>
    <!-- 페이징 영역 -->
    <nav>
       <ul class="pagination justify-content-center">
           <c:set var="pageGroupSize" value="5" />
           <c:set var="startPage" value="${((currentPage - 1) / pageGroupSize) * pageGroupSize + 1}" />
           <c:set var="endPage" value="${startPage + pageGroupSize - 1 > totalPages ? totalPages : startPage + pageGroupSize - 1}" />

           <!-- 이전 그룹으로 이동 -->
           <c:if test="${startPage > 1}">
               <li class="page-item">
                   <a class="page-link" href="/master/boardMasterReviewAll?currentPage=${startPage - 1}&pageSize=${pageSize}">&laquo;</a>
               </li>
           </c:if>

           <!-- 페이지 번호 -->
           <c:forEach var="i" begin="${startPage}" end="${endPage}">
               <li class="page-item ${i == currentPage ? 'active' : ''}">
                   <a class="page-link" href="/master/boardMasterReviewAll?currentPage=${i}&pageSize=${pageSize}">${i}</a>
               </li>
           </c:forEach>

           <!-- 다음 그룹으로 이동 -->
           <c:if test="${endPage < totalPages}">
               <li class="page-item">
                   <a class="page-link" href="/master/boardMasterReviewAll?currentPage=${endPage + 1}&pageSize=${pageSize}">&raquo</a>
               </li>
           </c:if>
       </ul>
    </nav>
</div>



<!-- 모달창 -->
<div class="modal fade" id="detailModal" tabindex="-1" role="dialog" aria-labelledby="detailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document"> <!-- 모달 크기를 변경 -->
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="detailModalLabel">리뷰 상세보기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <!-- 리뷰 상세 정보가 들어갈 영역 -->
                <div id="reviewDetail">
                    <p><strong>OrderList_idx:</strong> <span id="orderListIdx"></span></p>
                    <p><strong>Image File 1:</strong> <img id="imgFile1" src="" alt="Image 1" style="width: 100%;" /></p> <!-- 전체 너비 사용 -->
                    <p><strong>Image File 2:</strong> <img id="imgFile2" src="" alt="Image 2" style="width: 100%;" /></p> <!-- 전체 너비 사용 -->
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button> <!-- 닫기 버튼 추가 -->
            </div>
        </div>
    </div>
</div>
