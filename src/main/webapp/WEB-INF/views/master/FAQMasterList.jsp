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
                         <th style="width:5%">No.</th>
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
                         <td>${faq.faqtype}</td>
                         <td>${faq.question}</td>
                         <td>${faq.regDT}</td>
                         <td>
                             <button class="btn btn-outline-secondary btn-sm" data-idx><a href="/master/FAQEditMaster/${faq.idx}">수정</a></button>
                             <button class="btn btn-outline-danger btn-sm">삭제</button>
                         </td>
                     </tr>
                     </c:forEach>
                 </tbody>
             </table>
         </div>

         <!-- 페이징 영역 -->
         <div class="FAQPageing">
             <nav aria-label="Page navigation example">
                 <ul class="pagination justify-content-center">
                     <li class="page-item disabled">
                         <a class="page-link" href="#" tabindex="-1">이전</a>
                     </li>
                     <li class="page-item"><a class="page-link" href="#">1</a></li>
                     <li class="page-item"><a class="page-link" href="#">2</a></li>
                     <li class="page-item"><a class="page-link" href="#">3</a></li>
                     <li class="page-item">
                         <a class="page-link" href="#">다음</a>
                     </li>
                 </ul>
             </nav>
         </div>