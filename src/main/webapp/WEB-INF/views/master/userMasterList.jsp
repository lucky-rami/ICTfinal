<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 회원 유저리스트</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css"></link>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/MasterPage.js"></script>
 <div class="user-list-container">
        <h2>사용자(회원) 정보 목록</h2>

        <!-- 사용자 요약 정보 -->
        <div class="summary">
            <div>
                <strong>총 유저 수</strong>
                <p id="totalUsers">${totalUser} 명</p>
            </div>
            <div>
                <strong>오늘 가입한 사용자 </strong>
                <p id="newUsers">${newUsers}  명</p>
            </div>
            <div>
                <strong>7일간 가입한 사용자 수</strong>
                <p id="newSignups">${newSignups}  명</p>
            </div>
        </div>

        <!-- 검색 및 필터 기능 -->
        <div class="search-bar">
            <div class="d-flex">
                <input type="text" id="searchInput" class="form-control" placeholder="검색어 입력 (아이디, 이름, 이메일)">
                <button class="btn btn-primary ms-2" onclick="searchTable()">검색</button>
            </div>
        </div>

        <!-- 사용자 정보 테이블 -->
        <table class="user-list table table-hover table-bordered">
            <thead>
                <tr>
                    <th style="width:2%"></th>
                    <th style="width:7%" class="sortable" onclick="sortTable(1)">유저번호</th>
                    <th style="width:8%" class="sortable" onclick="sortTable(2)">아이디</th>
                    <th style="width:8%" class="sortable" onclick="sortTable(3)">이름</th>
                    <th style="width:15%" class="sortable" onclick="sortTable(4)">이메일</th>
                    <th style="width:20%" class="sortable" onclick="sortTable(5)">가입일</th>
                    <th style="width:9%" class="sortable" onclick="sortTable(6)">포인트내역</th>
                </tr>
            </thead>
            <tbody id="userTableBody">
            <c:forEach var="member" items="${memberList}">
               <tr>
                    <td><input type="checkbox" name="select" id="select"/></td>
                    <td>${member.idx}</td>
                    <td>${member.userid}</td>
                    <td>${member.username}</td>
                    <td>${member.email}</td>
                    <td>${member.regDT}</td>
                    <td>${member.point}<span> point</span></td>
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
                              <a class="page-link" href="/master/userMasterList?currentPage=1&pageSize=${pageSize}">&laquo;&laquo;</a>
                          </li>
                      </c:if>

                      <!-- 이전 그룹으로 이동 -->
                      <c:if test="${startPage > 1}">
                          <li class="page-item">
                              <a class="page-link" href="/master/userMasterList?currentPage=${startPage - 1}&pageSize=${pageSize}">&lsaquo;</a>
                          </li>
                      </c:if>

               <c:if test="${not empty currentPage}">
                   <!-- 페이지 번호 -->
                   <c:forEach var="i" begin="${startPage}" end="${endPage}">
                       <li class="page-item ${i == currentPage ? 'active' : ''}">
                           <a class="page-link" href="/master/userMasterList?currentPage=${i}&pageSize=${pageSize}">${i}</a>
                       </li>
                   </c:forEach>
               </c:if>

                      <!-- 다음 그룹으로 이동 -->
                      <c:if test="${endPage < totalPages}">
                          <li class="page-item">
                              <a class="page-link" href="/master/userMasterList?currentPage=${endPage + 1}&pageSize=${pageSize}">&rsaquo;</a>
                          </li>
                      </c:if>

                      <!-- 마지막 페이지로 이동 -->
                      <c:if test="${endPage < totalPages}">
                          <li class="page-item">
                              <a class="page-link" href="/master/userMasterList?currentPage=${totalPages}&pageSize=${pageSize}">&raquo;&raquo;</a>
                          </li>
                      </c:if>
                  </ul>
              </nav>