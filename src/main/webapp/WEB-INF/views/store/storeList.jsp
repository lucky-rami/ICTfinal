<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="/WEB-INF/inc/store_header.jspf"%>

<link href="/css/storeList.css" rel="stylesheet" type="text/css">
<script src="/js/storeList.js"></script>


<script>

</script>







<div class="storeList_container">
        <h2>#hololive GAMERS 콜라보 굿즈 예약 판매 (~12.15)</h2>
            <section class="list-banners" id="banner">
                <div class="list-banner-images" id="List-div">
                    <div class="list-banner">
                            <img src="img/store/list-banner.png" alt="List Banner1">
                    </div>
                </div>
                </section>

                    <!-- 좌측 필터 -->
                    <section class="filter-section">
                        <div class="left-con">
                            <h3>필터</h3>
                            <div class="filter-header">
                                <span>판매종료 포함</span>
                                <label class="switch">
                                    <input type="checkbox" id="stockFilter" onclick="applyFilters(this)">
                                    <span class="slider round"></span>
                                </label>
                            </div>
                            <hr>
                            <div class="filter-type-title">
                                  <ul class="filter-list">
                                    <!-- firstCategoryList를 순회하면서 카테고리 목록을 출력 -->
                                  <!-- <c:forEach var="category" items="${firstCategoryList}">
                                        <li class="filter-item" onclick="loadSubcategories(${category.code})">
                                       <span class="filter-text">${category.type}</span>
                                        </li>
                                    </c:forEach> -->
                                </ul>

                                  <!-- 하위 카테고리를 표시할 영역 -->
                                   <h3>하위 카테고리</h3>
                                   <!-- mapper에서 커리문을 들고와서 해당하는 서브카테고리가 있으면 display : block, default는 none -->
                                   <ul id="subcategory-list" class="filter-list">
                                       <li class="filter-item" onclick="applyFilter(null,'아우터')"><span class="filter-text">아우터</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'상의')"><span class="filter-text">상의</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'하의')"><span class="filter-text">하의</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'잡화')"><span class="filter-text">잡화</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'아크릴')"><span class="filter-text">아크릴</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'피규어')"><span class="filter-text">피규어</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'캔뱃지')"><span class="filter-text">캔뱃지</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'슬로건')"><span class="filter-text">슬로건</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'포스터')"><span class="filter-text">포스터</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'기타')"><span class="filter-text">기타</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'필기류')"><span class="filter-text">필기류</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'노트&메모지')"><span class="filter-text">노트&메모지</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'파일')"><span class="filter-text">파일</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'스티커')"><span class="filter-text">스티커</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'달력')"><span class="filter-text">달력</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'컵&텀블러')"><span class="filter-text">컵&텀블러</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'쿠션')"><span class="filter-text">쿠션</span></li>
                                       <li class="filter-item" onclick="applyFilter(null,'담요')"><span class="filter-text">담요</span></li>
                                   </ul>

                            </div>

                            </ul>
                            <hr>
                        </div>
                    </section>


                <!-- 상품 섹션 위쪽에 필터 추가 -->
                        <section class="product-filter">
                            <div class="filter-options">
                                <!-- 검색창 추가 -->
                                <div class="search-section">
                                    <div class="search_window">
                                    <input type="text" id="productSearch" class="search-input" placeholder="검색어를 입력하세요..." onkeyup="searchProducts()" />
                                    </div>
                                    <div class="search-icon">
                                        <img src="img/store/search_box_btn.png">
                                    </div>
                                </div>
                                <div class="filter-option-keyword">
                                    <span class="filter-option" onclick="filterProductsByType('latest')">최신순</span>
                                    <span class="filter-option" onclick="filterProductsByType('popular')">인기순</span>
                                    <span class="filter-option" onclick="filterProductsByType('high-price')">높은 가격순</span>
                                    <span class="filter-option" onclick="filterProductsByType('low-price')">낮은 가격순</span>

                                </div>
                            </div>
                        </section>


                <!--상품섹션-->
                <section class="list-products">
                    <div class="right-con">
                        <ul class="list-carousel">
                            <li class="list-carousel-wrapper">
                                <ul class="list-carousel-images">
                                  <!--db에서 가져온 상품목록-->
                                  <c:forEach var="product" items="${pagedProducts}">
                                    <li class="list-product">
                                        <!-- idx 값을 사용하여 링크 생성 -->
                                     <a href="<c:url value='/storeDetail/${product.idx}' />">
                                         <img src="http://192.168.1.180:8000/${product.thumImg}" alt="${product.title}">
                                     </a>

                                        <p>${product.title}</p>
                                        <p>    <fmt:formatNumber value="${product.price}" type="number" pattern="#,###"/> 원</p>
                                    </li>
                                </c:forEach>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </section>
<div class="pagination">
     <!-- 이전 페이지 링크 -->
     <c:if test="${currentPage > 1}">
         <a href="/storeList?pageNum=${currentPage - 1}
            <c:if test='${selectedCategory != null}'>&category=${selectedCategory}</c:if>
            <c:if test='${selectedFilterType != null}'>&filterType=${selectedFilterType}</c:if>">&laquo; 이전</a>
     </c:if>

     <!-- 페이지 번호 링크 -->
     <c:forEach var="i" begin="${currentPage - 2 > 0 ? currentPage - 2 : 1}" end="${currentPage + 1 < totalPages ? currentPage + 1 : totalPages}">
         <c:choose>
             <c:when test="${i == currentPage}">
                 <span class="current">${i}</span>
             </c:when>
             <c:otherwise>
                 <a href="/storeList?pageNum=${i}
                    <c:if test='${selectedCategory != null}'>&category=${selectedCategory}</c:if>
                    <c:if test='${selectedFilterType != null}'>&filterType=${selectedFilterType}</c:if>">${i}</a>
             </c:otherwise>
         </c:choose>
     </c:forEach>

     <!-- 다음 페이지 링크 -->
     <c:if test="${currentPage < totalPages}">
         <a href="/storeList?pageNum=${currentPage + 1}
            <c:if test='${selectedCategory != null}'>&category=${selectedCategory}</c:if>
            <c:if test='${selectedFilterType != null}'>&filterType=${selectedFilterType}</c:if>">다음 &raquo;</a>
     </c:if>
 </div>

<!-- 카테고리 변경 시 페이지 넘버를 1로 설정 -->
<div class="category-filter">
    <c:forEach var="category" items="${categories}">
        <a href="/storeList?pageNum=1&category=${category.id}
            ${selectedFilterType != null ? '&filterType=' + selectedFilterType : ''}">
            ${category.name}
        </a>
    </c:forEach>
</div>





<script>
    // URL에서 파라미터를 추출하는 함수
    function getParameterByName(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }

    window.onload = function() {
        // URL에서 'category' 파라미터 값을 가져와서 함수 호출
        const categoryId = getParameterByName('category');  // 'category' 파라미터 추출
        if (categoryId) {
            showSubcategories(categoryId);  // 이미 정의된 함수 호출
        }
    };
</script>

<%@include file="/WEB-INF/inc/store_footer.jspf"%>

