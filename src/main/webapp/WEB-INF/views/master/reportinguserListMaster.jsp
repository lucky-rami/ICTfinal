<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 신고 목록 리스트</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css"></link>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/Master.js"></script>

<div class="reportinguserList-list-container">
<!-- 사용자 요약 정보 -->
        <div class="summary">
            <div>
                <strong>총 신고 수</strong>
                <p id="totalUsers">3 명</p>
            </div>
            <div>
                <strong>접수중</strong>
                <p id="activeUsers">2 명</p>
            </div>
            <div>
                <strong>처리대기</strong>
                <p id="inactiveUsers">1 명</p>
            </div>
            <div>
                <strong>처리완료</strong>
                <p id="newUsers">1 명</p>
            </div>
        </div>
            <h2>신고 목록</h2>
            <!-- 신고 목록 테이블 -->
            <table class="reportinguserList-list table table-hover table-bordered">
                <thead class="table-light">
                    <tr>
                        <th style="width:2%"><input type="checkbox" name="selectAll" id="selectAll"/></th>
                        <th style="width:5%">No</th>
                        <th style="width:25%">내용</th>
                        <th style="width:10%">신고아이디</th>
                        <th style="width:10%">처리현황</th>
                        <th style="width:10%">처리날짜</th>
                        <th style="width:12%">관리</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="reportinguser" items="${reportinguserList}">
                    <tr>
                        <td><input type="checkbox" /></td>
                        <td>${reportinguser.idx}</td>
                        <td>${reportinguser.reason}</td>
                        <td>${reportinguser.reportedUser}</td>
                        <td><c:choose>
                                    <c:when test="${reportinguser.handleState == 1}">
                                        처리 완료
                                    </c:when>
                                    <c:when test="${reportinguser.handleState == 2}">
                                        처리 불가
                                    </c:when>
                                    <c:otherwise>
                                        처리 중
                                    </c:otherwise>
                                </c:choose></td>
                        <td>${reportinguser.handleDT}</td>
                        <td>
                            <!-- 신고내역추가 버튼 클릭 시 모달에 유저 ID 설정 -->
                            <button class="btn btn-outline-success btn-sm addReportBtn"
                                    data-userid="${reportinguser.reportedUser}"
                                    data-comment-idx="${reportinguser.comment_idx}">신고내역추가</button>
                            <button class="btn btn-outline-danger btn-sm deleteBtn" data-idx="${reportinguser.idx}">삭제</button>
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
                                <button type="submit" class="btn btn-primary">신고 제출</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>