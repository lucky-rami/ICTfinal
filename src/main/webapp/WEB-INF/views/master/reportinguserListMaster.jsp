<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 신고 목록 리스트</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css"></link>
<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- Bootstrap JS -->
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
<script src="/js/Master.js"></script>

<div class="reportinguserList-list-container">
<!-- 사용자 요약 정보 -->
<h2>신고 목록</h2>
        <div class="summary">
            <div>
                <strong>총 신고 수</strong>
                <p id="totalReports">${totalReports} 개</p>
            </div>
            <div>
                <strong>접수중/처리중</strong>
                <p id="activeReports">${activeReports} 개</p>
            </div>
             <div>
                            <strong>처리불가</strong>
                            <p id="noncompletedReports">${noncompletedReports} 개</p>
                        </div>
            <div>
                <strong>처리완료</strong>
                <p id="completedReports">${completedReports} 개</p>
            </div>
        </div>

            <!-- 신고 목록 테이블 -->
            <table class="reportinguserList-list table table-hover table-bordered">
                <thead class="table-light">
                    <tr>
                        <th style="width:2%"></th>
                        <th style="width:5%">No</th>
                        <th style="width:25%">신고사유</th>
                        <th style="width:10%">신고아이디</th>
                        <th style="width:10%">처리현황</th>
                        <th style="width:10%">처리날짜</th>
                        <th style="width:12%"></th>
                    </tr>
                </thead>
                <tbody>
               <c:forEach var="reportinguser" items="${reportinguserList}">
                   <tr>
                       <td><input type="checkbox" /></td>
                       <td>${reportinguser.idx}</td>
                       <td>
                                                  <c:choose>
                                                      <c:when test="${reportinguser.report_type == 1}">관련없는 이미지</c:when>
                                                      <c:when test="${reportinguser.report_type == 2}">관련없는 내용</c:when>
                                                      <c:when test="${reportinguser.report_type == 3}">욕설/비방</c:when>
                                                      <c:when test="${reportinguser.report_type == 4}">광고/홍보글</c:when>
                                                      <c:when test="${reportinguser.report_type == 5}">개인정보 유출</c:when>
                                                      <c:when test="${reportinguser.report_type == 6}">게시글 도배</c:when>
                                                      <c:when test="${reportinguser.report_type == 7}">음란/선정성</c:when>
                                                      <c:when test="${reportinguser.report_type == 8}">기타</c:when>
                                                      <c:otherwise>알 수 없음</c:otherwise>
                                                  </c:choose>
                                              </td>
                       <td>
                                   <c:choose>
                                       <c:when test="${not empty reportinguser.commentAuthor}">
                                           ${reportinguser.commentAuthor}
                                       </c:when>
                                       <c:when test="${not empty reportinguser.reviewAuthor}">
                                           ${reportinguser.reviewAuthor}
                                       </c:when>
                                       <c:when test="${not empty reportinguser.communityAuthor}">
                                           ${reportinguser.communityAuthor}
                                       </c:when>
                                       <c:otherwise>
                                           알 수 없음
                                       </c:otherwise>
                                   </c:choose>
                               </td> <!-- 신고한 사용자의 ID -->
                       <td>
                           <c:choose>
                               <c:when test="${reportinguser.handleState == 1}">처리 완료</c:when>
                               <c:when test="${reportinguser.handleState == 2}">처리 불가</c:when>
                               <c:otherwise>처리 중</c:otherwise>
                           </c:choose>
                       </td>
                       <td>${reportinguser.handleDT}</td>
                       <td>
                           <button class="btn btn-outline-secondary btn-sm addReportBtn"
                                   data-userid="${reportinguser.userid}"
                                   data-comment-idx="${reportinguser.comment_idx}"
                                   data-review-idx="${reportinguser.review_idx}"
                                   data-comunity-idx="${reportinguser.comunity_idx}"
                                   data-report-type="${reportinguser.report_type}">
                               신고내역추가
                           </button>
                       </td>
                   </tr>
               </c:forEach>
                </tbody>
            </table>

            <!-- 신고 내역 추가 모달창 -->
            <div class="modal fade" id="reportModal" tabindex="-1" role="dialog" aria-labelledby="reportModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="reportModalLabel">신고 내역 추가/해제</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <form id="reportForm" action="/master/reportinguserOK" method="post">
                            <!-- 모달 내용 -->
                            <div class="modal-body">
                                <!-- 신고 사유 -->
                                <div class="form-group">
                                    <input type="hidden" id="userid" name="userid">
                                    <input type="hidden" id="idx" name="idx" value="">
                                    <input type="hidden" id="comment_idx" name="comment_idx" value="">
                                    <input type="hidden" id="review_idx" name="review_idx" value="">
                                    <input type="hidden" id="comunity_idx" name="comunity_idx" value="">
                                    <input type="hidden" id="report_type" name="report_type">
                                    <label for="reason">신고 사유</label>
                                    <textarea class="form-control" id="reason" name="reason" rows="3"></textarea>
                                </div>

                                <div class="form-group">
                                    <label for="handleState">처리 상태</label>
                                    <select class="form-control" id="handleState" name="handleState">
                                        <option value="0">처리중</option>
                                        <option value="1">처리완료</option>
                                        <option value="2">처리불가</option>
                                    </select>
                                </div>

                                <!-- 처리 날짜 -->
                                <div class="form-group">
                                    <label for="handleDT">처리 날짜</label>
                                    <input type="date" class="form-control" id="handleDT" name="handleDT">
                                </div>

                                <!-- 제재 종료 날짜 -->
                                <div class="form-group">
                                    <label for="endDT">제재 종료 날짜</label>
                                    <input type="date" class="form-control" id="endDT" name="endDT">
                                </div>
                            </div>

                            <!-- 모달 하단 버튼 -->
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
                                <button type="submit" class="btn btn-danger" style="background:var(--primary); border:0">신고 제출</button>
                            </div>
                        </form>
                    </div>
                </div>
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
                               <a class="page-link" href="/master/reportinguserListMaster?currentPage=${startPage - 1}&pageSize=${pageSize}">&laquo;</a>
                           </li>
                       </c:if>

                       <!-- 페이지 번호 -->
                       <c:forEach var="i" begin="${startPage}" end="${endPage}">
                           <li class="page-item ${i == currentPage ? 'active' : ''}">
                               <a class="page-link" href="/master/reportinguserListMaster?currentPage=${i}&pageSize=${pageSize}">${i}</a>
                           </li>
                       </c:forEach>

                       <!-- 다음 그룹으로 이동 -->
                       <c:if test="${endPage < totalPages}">
                           <li class="page-item">
                               <a class="page-link" href="/master/reportinguserListMaster?currentPage=${endPage + 1}&pageSize=${pageSize}">&raquo</a>
                           </li>
                       </c:if>
                   </ul>
                </nav>
            </div>