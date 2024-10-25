<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf"%>


<link href="/css/search_all.css" rel="stylesheet" type="text/css" />
<script src="/js/search_all.js"></script>

<div class="search_all_container">
  <h1><span>"${searchKey}"</span> 에 대한 검색 결과 (${totalSearchCount})</h1>
  <div class="search_all_tabmenu">
    <ul>
      <li onclick="reviewTab('tab1')" class="tab_on">ANIMATION<span> (${aniSearchCount})</span></li>
      <li onclick="reviewTab('tab2')">GOODS<span> (${goodsSearchCount})</span></li>
    </ul>
  </div>
  <div class="search_all_animation on" id="tab1">
    <ul class="search_all_animation_grid">
      <c:forEach var="aniSearch" items="${aniSearch}">
        <li>
            <a>
              <div class="search_ani_img">
                <img src="http://192.168.1.180:8000/${aniSearch.post_img}" />
              </div>
              <div class="search_ani_title">
                <p>${aniSearch.title}</p>
              </div>
            </a>
        </li>
      </c:forEach>
    </ul>
  </div>
  <div class="search_all_goods off" id="tab2">
    <ul class="search_all_goods_grid">
        <c:forEach var="goodsSearch" items="${goodsSearch}">
            <li>
                <a>
                  <div class="search_ani_img">
                    <img src="http://192.168.1.180:8000/${goodsSearch.thumImg}" />
                  </div>
                  <div class="search_ani_title">
                    <p>${goodsSearch.title}</p>
                  </div>
                </a>
            </li>
        </c:forEach>
    </ul>
  </div>
</div>

