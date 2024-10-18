<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 애니리스트</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css"></link>
<script src="/js/Master.js"></script>
 <div class="anime-list-container">
         <h2>애니 목록</h2>

         <!-- 애니 요약 정보 -->
         <div class="summary">
             <div>
                 <strong>총 애니 수</strong>
                 <p id="totalAnime">3 개</p>
             </div>
             <div>
                 <strong>장르별</strong>
                 <p id="categorySummary">액션: 1, 코미디: 1, 판타지: 1</p>
             </div>
         </div>

         <!-- 검색 및 필터 기능 -->
         <div class="search-bar">
             <div class="d-flex">
                 <input type="text" id="searchInput" class="form-control" placeholder="작품명, 카테고리 검색">
                 <button class="btn btn-primary ms-2" onclick="searchTable1()">검색</button>
             </div>
             <div>
                 <select class="form-select w-auto" id="filterSelect">
                     <option value="">모든 카테고리</option>
                     <option value="액션">액션</option>
                     <option value="코미디">코미디</option>
                     <option value="판타지">판타지</option>
                 </select>
             </div>
         </div>

         <!-- 애니 정보 테이블 -->
         <table class="anime-list table table-hover table-bordered">
             <thead>
                 <tr>
                     <th style="width:2%"><input type="checkbox" name="selectAll" id="selectAll"/></th>
                     <th style="width:6%" class="sortable" onclick="sortTable1(1)">애니번호</th>
                     <th style="width:6%" class="sortable" onclick="sortTable1(2)">카테고리</th>
                     <th style="width:35%" class="sortable" onclick="sortTable1(3)">작품명</th>
                     <th style="width:9%" class="sortable" onclick="sortTable1(4)">감독</th>
                     <th style="width:6%" class="sortable" onclick="sortTable1(5)">등급</th>
                     <th style="width:15%">관리<a href="/master/aniAddMaster" class="btn btn-outline-success btn-sm">추가</a></th>
                 </tr>
             </thead>
             <tbody id="animeTableBody">
                   <c:forEach var="ani" items="${aniList}">
                                  <tr>
                                      <td><input type="checkbox" name="select" id="select"/></td>
                                      <td>${ani.idx}</td>
                                      <td>
                                          <c:choose>
                                              <c:when test="${ani.anitype == 1}">드라마</c:when>
                                              <c:when test="${ani.anitype == 2}">미스터리</c:when>
                                              <c:when test="${ani.anitype == 3}">스포츠</c:when>
                                              <c:when test="${ani.anitype == 4}">코미디</c:when>
                                              <c:when test="${ani.anitype == 5}">판타지</c:when>
                                              <c:when test="${ani.anitype == 6}">SF</c:when>
                                              <c:otherwise>기타</c:otherwise>
                                          </c:choose>
                                      </td>
                                      <td>${ani.title}</td>
                                      <td>${ani.director}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${ani.agetype == 1}">전체</c:when>
                                            <c:when test="${ani.agetype == 2}">12세</c:when>
                                            <c:when test="${ani.agetype == 3}">15세</c:when>
                                            <c:when test="${ani.agetype == 4}">19세</c:when>
                                            <c:otherwise>알 수 없음</c:otherwise>
                                        </c:choose>
                                    </td>
                     <td>
                         <button class="btn btn-outline-secondary btn-sm"><a href="/master/aniEditMaster/${ani.idx}">수정</a></button>
                         <button class="btn btn-outline-danger btn-sm" data-idx="${ani.idx}">삭제</button>
                     </td>
                 </tr>
                 </c:forEach>
             </tbody>
         </table>

         <!-- 페이지네이션 -->
        <nav>
            <ul class="pagination">
                <c:set var="pageGroupSize" value="10" />
                <c:set var="startPage" value="${((currentPage - 1) / pageGroupSize) * pageGroupSize + 1}" />
                <c:set var="endPage" value="${startPage + pageGroupSize - 1 > totalPages ? totalPages : startPage + pageGroupSize - 1}" />

                <!-- 이전 그룹으로 이동 -->
                <c:if test="${startPage > 1}">
                    <li class="page-item">
                        <a class="page-link" href="/master/aniMasterList?currentPage=${startPage - 1}&pageSize=${pageSize}">&laquo; 이전</a>
                    </li>
                </c:if>

                <!-- 페이지 번호 -->
                <c:forEach var="i" begin="${startPage}" end="${endPage}">
                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                        <a class="page-link" href="/master/aniMasterList?currentPage=${i}&pageSize=${pageSize}">${i}</a>
                    </li>
                </c:forEach>

                <!-- 다음 그룹으로 이동 -->
                <c:if test="${endPage < totalPages}">
                    <li class="page-item">
                        <a class="page-link" href="/master/aniMasterList?currentPage=${endPage + 1}&pageSize=${pageSize}">다음 &raquo;</a>
                    </li>
                </c:if>
            </ul>
        </nav>
