<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 이벤트</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css"></link>


<style>
/* 모달 본문의 최대 높이 설정 및 스크롤 활성화 */
.modal-body {
    max-height: 80vh; /* 화면의 80% 높이 */
    max-width: 100%; /* ��면의 ��비 */
    overflow-y: auto; /* 세로 스크롤 활성화 */
}

/* 이미지가 모달 창에 맞춰 조정되도록 설정 */
.modal-body img {
    max-width: 100%; /* 모달 창의 너비에 맞춰 이미지 크기 조정 */
    width : 300px;
    height: auto; /* 이미지 비율 유지 */
    display: block; /* 블록 요소로 설정 */
    margin: 0 auto; /* 이미지를 중앙 정렬 */
}
</style>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
         <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<script src="/js/Master.js"></script>
<div class="Event-list-container">
    <h2>이벤트 리스트</h2>
    <table class="Event-list table table-hover table-bordered">
        <thead class="table-light">
            <tr>
                <th style="width:5%">NO</th>
                <th style="width:40%">제목</th>
                <th style="width:8%">행사일</th>
                <th style="width:8%">작성일</th>
                <th style="width:12%">관리<a href="/master/EventAddMaster" class="btn btn-outline-success btn-sm">추가</a></th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="event" items="${eventList}">
            <tr>
                <td>${event.idx}</td>
                <td>${event.title}</td>
                <td>${event.event_date}</td>
                <td>${event.regDT}</td>
                <td>
                    <button class="btn btn-outline-secondary btn-sm"><a href="/master/EventEditMaster/${event.idx}">수정</a></button>
                    <button class="btn btn-outline-success btn-sm viewDetails" data-idx="${event.idx}">상세보기</button>
                    <button class="btn btn-outline-danger btn-sm deleteEventBtn" data-idx="${event.idx}">삭제</a></button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- 모달 창 -->
<!-- 모달 구조 -->
<div class="modal fade" id="eventDetailModal" tabindex="-1" role="dialog" aria-labelledby="eventDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="eventDetailModalLabel">이벤트 상세보기</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <!-- 이벤트 내용이 표시될 영역 -->
                <p><strong>제목:</strong> <span id="eventTitle"></span></p>
                <p><strong>행사일:</strong> <span id="eventDate"></span></p>
                <div id="eventContent"></div> <!-- HTML 형식의 내용을 여기에서 렌더링 -->
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
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
                        <a class="page-link" href="/master/EventMasterList?currentPage=1&pageSize=${pageSize}">
                            &laquo;&laquo;
                        </a>
                    </li>
                </c:if>

                <!-- 이전 그룹으로 이동 -->
                <c:if test="${startPage > 1}">
                    <li class="page-item">
                        <a class="page-link" href="/master/EventMasterList?currentPage=${startPage - 1}&pageSize=${pageSize}">
                            &lsaquo;
                        </a>
                    </li>
                </c:if>

                <!-- 페이지 번호 -->
                   <c:if test="${not empty currentPage}">
                                   <!-- 페이지 번호 -->
                                   <c:forEach var="i" begin="${startPage}" end="${endPage}">
                                       <li class="page-item ${i == currentPage ? 'active' : ''}">
                                           <a class="page-link" href="/master/EventMasterList?currentPage=${i}&pageSize=${pageSize}">${i}</a>
                                       </li>
                                   </c:forEach>
                               </c:if>

                <!-- 다음 그룹으로 이동 -->
               <c:if test="${endPage < totalPages}">
                   <li class="page-item">
                       <a class="page-link" href="/master/EventMasterList?currentPage=${endPage + 1}&pageSize=${pageSize}">
                           &rsaquo;
                       </a>
                   </li>
               </c:if>

                <!-- 마지막 페이지로 이동 -->
                <c:if test="${endPage < totalPages}">
                    <li class="page-item">
                        <a class="page-link" href="/master/EventMasterList?currentPage=${totalPages}&pageSize=${pageSize}">
                            &raquo;&raquo;
                        </a>
                    </li>
                </c:if>
            </ul>
        </nav>