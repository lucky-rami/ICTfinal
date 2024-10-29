<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 자주 묻는 질문</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css"></link>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/Master.js"></script>
 <div class="FAQ-list-container">
             <h2>자주 묻는 질문 리스트</h2>
             <table class="FAQ-list table table-hover table-bordered">
                 <thead class="table-light">
                     <tr>
                         <th style="width:5%">NO</th>
                         <th style="width:6%">카테고리</th>
                         <th style="width:60%">제목</th>
                         <th style="width:15%">작성일</th>
                         <th style="width:20%">관리<a href="/master/FAQAddMaster" class="btn btn-outline-success btn-sm">추가</a></th>
                     </tr>
                 </thead>
                 <tbody>
                 <c:forEach var="faq" items="${faqList}">
                     <tr>
                         <td>${faq.idx}</td>
                         <td>
                             <c:choose>
                                 <c:when test="${faq.faqtype == 1}">쇼핑</c:when>
                                 <c:when test="${faq.faqtype == 2}">계정</c:when>
                                 <c:when test="${faq.faqtype == 3}">포인트</c:when>
                                 <c:when test="${faq.faqtype == 4}">애니</c:when>
                                 <c:when test="${faq.faqtype == 5}">서비스</c:when>
                                 <c:otherwise>알 수 없음</c:otherwise>
                             </c:choose>
                         </td>
                         <td>${faq.question}</td>
                         <td>${faq.regDT}</td>
                         <td>
                             <button class="btn btn-outline-secondary btn-sm" data-idx><a href="/master/FAQEditMaster/${faq.idx}">수정</a></button>
                             <button class="btn btn-outline-danger btn-sm deleteFaqBtn" data-idx="${faq.idx}">삭제</button>
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
                                                  <c:set var="startPage" value="${((currentPage - 1) / pageGroupSize) * pageGroupSize + 1}" />
                                                  <c:set var="endPage" value="${startPage + pageGroupSize - 1 > totalPages ? totalPages : startPage + pageGroupSize - 1}" />

                                                  <!-- 첫 번째 페이지로 이동 -->
                                                  <c:if test="${startPage > 1}">
                                                      <li class="page-item">
                                                          <a class="page-link" href="/master/FAQMasterList?currentPage=1&pageSize=${pageSize}">
                                                              &laquo;&laquo;
                                                          </a>
                                                      </li>
                                                  </c:if>

                                                  <!-- 이전 그룹으로 이동 -->
                                                  <c:if test="${startPage > 1}">
                                                      <li class="page-item">
                                                          <a class="page-link" href="/master/FAQMasterList?currentPage=${startPage - 1}&pageSize=${pageSize}">
                                                              &lsaquo;
                                                          </a>
                                                      </li>
                                                  </c:if>

                                                  <!-- 페이지 번호 -->
                                                  <c:forEach var="i" begin="${startPage}" end="${endPage}">
                                                      <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                          <a class="page-link" href="/master/FAQMasterList?currentPage=${i}&pageSize=${pageSize}">
                                                              ${i}
                                                          </a>
                                                      </li>
                                                  </c:forEach>

                                                  <!-- 다음 그룹으로 이동 -->
                                                  <c:if test="${endPage < totalPages}">
                                                      <li class="page-item">
                                                          <a class="page-link" href="/master/FAQMasterList?currentPage=${endPage + 1}&pageSize=${pageSize}">
                                                              &rsaquo;
                                                          </a>
                                                      </li>
                                                  </c:if>

                                                  <!-- 마지막 페이지로 이동 -->
                                                  <c:if test="${endPage < totalPages}">
                                                      <li class="page-item">
                                                          <a class="page-link" href="/master/FAQMasterList?currentPage=${totalPages}&pageSize=${pageSize}">
                                                              &raquo;&raquo;
                                                          </a>
                                                      </li>
                                                  </c:if>
                                              </ul>
                                          </nav>