<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="/WEB-INF/inc/store_header.jspf"%>

<link href="/css/storeDetail.css" rel="stylesheet" type="text/css">
<link href="https://getbootstrap.com/docs/5.3/components/buttons/" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script type="text/javascript">
    var storeDetail = {
        price: "${storeDetail.price}",  // 서버에서 전달된 상품 가격
        stock: "${storeDetail.stock}"   // 서버에서 전달된 재고 수량
    };
</script>
<link rel="stylesheet" href="/css/basketmodal.css" type="text/css" />
<script src="/js/storeDetail_input.js"></script>

<!-- 상품 상세 페이지 컨테이너 -->
<div class="product-container">
    <div class="product-wrapper">
        <!-- 상품 이미지 섹션 -->
        <div class="product-image-section">
            <img src="${storeDetail.thumImg}" alt="${storeDetail.title}" class="main-image">
        </div>
    
        <!-- 상품 정보 섹션 -->
        <div class="product-info-section">
            <input type="hidden" name="pro_idx" id="pro_idx" value="${storeDetail.idx}">
            <h1 class="product-title">${storeDetail.title}</h1>
            <p class="product-price"><fmt:formatNumber value="${storeDetail.price}" type="number" pattern="#,###"/> 원</p>
    
            <div class="product-meta-info">
                <p>발매일: ${storeDetail.relDT}</p>
                <p>적립포인트: ${storeDetail.price/100}원</p> <!-- 가격의 1%를 적립 포인트로 표시 -->
                <p>배송비: <fmt:formatNumber value="${storeDetail.fee}" type="number" pattern="#,###"/> 원</p>

               <div class="like-section">
                                  <div class="like-icon" data-product-id="${storeDetail.idx}">
                                      <i class="like-heart ${storeDetail.liked != null && storeDetail.liked == 1 ? 'fa-solid fa-heart' : 'fa-regular fa-heart'}"></i>
                                  </div>
                                  <span class="like-count">${storeDetail.likeCount}</span>
                              </div>
                           </div>

    
            <!-- 상품 선택 옵션 -->
            <div class="product-selection">
                <div class="selection-box">
                    <span class="product-name">상품명</span>
                         <p>${storeDetail.title}</p>
                    <span class="product-quantity-control">
                        <button class="quantity-button" onclick="decreaseQuantity()">-</button>
                        <input type="number" name="amount" id="amount" value="1" min="1" max="5000" class="quantity-input">
                        <button class="quantity-button" onclick="increaseQuantity()">+</button>
                    </span>
                    <span class="product-price-info">  <fmt:formatNumber value="${storeDetail.price}" type="number" pattern="#,###"/> 원</span>
                </div>
    
                <div class="total-price">총 결제금액:
                    <span id="total-price">
                        <fmt:formatNumber value="${storeDetail.price}" type="number" pattern="#,###"/>
                    </span>원
                    <input type="hidden" id="total" name="total" value="">
                </div>
                    
            </div>
                    <!-- 구매 버튼 -->
                    <div class="buy-buttons">
                        <button class="add-to-cart" id="basket_in_btn">장바구니</button>
                        <button class="buy-now" id="order_buy_btn">바로 구매</button>
                    </div>
        </div>
        
    </div>



    <!-- 상품 정보 및 기타 탭 -->
    <div class="product-tabs">
        <button class="tab active">상품정보</button>
        <button class="tab" onclick="scrollToShippingSection()">배송/교환/반품</button>
        <button class="tab" onclick="window.location.href='/notice2'">1:1 상품문의</button>
        <button class="tab" onclick ="scrollToReviewSection()">리뷰</button>
    </div>

    <!-- 상품 정보 섹션 추가 -->
    <div class="product-info-details-section">
        <h2>상품 정보</h2>
        <table class="product-info-table">
            <tr>
                <th>출시일</th>
                <td>${storeDetail.relDT}</td>
                <th>정가</th>
                <td> <fmt:formatNumber value="${storeDetail.price}" type="number" pattern="#,###"/> 원</td>
            </tr>
            <tr>
                <th>상품명</th>
                <td colspan="3">${storeDetail.title}</td>
            </tr>
            <tr>
                <th>작품명</th>
                <td><a href="#">#${storeDetail.ani_title}</a></td>
                <th>브랜드</th>
                <td><a href="#">#${storeDetail.brand}</a></td>
            </tr>
            <tr>
                <th>카테고리</th>
                <td colspan="3"><a href="#">#${categoryType}</a></td>
            </tr>
            <tr>
                <th>상품설명</th>
                <td colspan="3">${storeDetail.pro_detail}</td>
            </tr>
        </table>
    </div>
            <!-- 주의 사항 섹션 -->
            <div class="caution-section">
                <h3>주문 전 유의사항</h3>
                <p>오프라인 매장과 동시에 판매되는 상품의 경우, 결제 완료 후에도 품절/결품이 발생할 수 있습니다. 이로 인한 환불 처리는 고객님께 문자로 안내드리며 빠른 처리 진행해드리겠습니다.</p>
            </div>

        <!-- 텍스트와 이미지 섹션 -->
        <div class="product-details-section">
            <h2>${storeDetail.title}</h2>
            <div class="product-details-wrapper">
                <!-- 이미지 섹션 -->
                <div class="product-image">
                    <img src="${storeDetail.thumImg}" alt="상품 상세 이미지" />
                </div>


                          <!-- 상품 설명 섹션 -->
                <div class="product-description-section">
                    <button class="toggle-description btn btn-secondary btn-lg" onclick="toggleDescription()">상품정보 더보기▼</button>
            </div>


        <!-- 숨겨진 상품 설명 및 추가 이미지 -->
        <div id="hidden-description" class="hidden-description">
            <c:forEach var="image" items="${storeDetail.detailImages}">
                  <img src="${image}" alt="Additional Image" class="additional-image">
              </c:forEach>
        </div>
    </div>
   

        <!-- 배송/교환/반품 섹션 추가 -->
    <div class="shipping-exchange-return-section">
        <h2>배송/교환/반품</h2>
        <div class="shipping-info">
            <h3>배송정보</h3>
            <ul>
                <li>발송 시기는 예고 없이 변경될 수 있습니다.</li>
                <li>입고 상품의 배송 기간은 2~7일이 소요될 수 있습니다.</li>
                <li>운송장 번호는 "마이 쇼핑 - 주문 내역 - 배송 정보"에서 확인 가능합니다.</li>
            </ul>
        </div>
        <div class="exchange-info">
            <h3>교환</h3>
            <ul>
                <li>상품을 수령하신 후 교환이 필요할 시 교환 조건에 따라 가능합니다.</li>
                <li>상품 수령 후 7일 이내에 교환이 가능합니다.</li>
            </ul>
        </div>
        <div class="return-info">
            <h3>반품 및 환불</h3>
            <ul>
                <li>반품은 배송 완료 후 7일 이내에 가능하며, 제품에 손상이 없는 경우에 한해 환불이 진행됩니다.</li>
                <li>반품 시 택배비는 고객님께서 부담하셔야 합니다.</li>
            </ul>
        </div>
    </div>





    <script src="/js/storeDetail.js"></script>
    <script src="../../../js/store_header.js"></script>

    <!-- 상품 리뷰 제목 -->
    <div class="review-title">
        <h2>상품리뷰</h2>
        <p>상품을 구매하신 분들만 본인이 작성하신 리뷰만 있습니다. 리뷰 작성시 아래 금액만큼 포인트가 적립됩니다.</p>
        <div class="review-stats">
            <span>텍스트 리뷰: 50원</span>
            <span>포토/동영상 리뷰: 150원</span>
        </div>
    </div>

    <!-- 사용자 총 평점과 전체 리뷰수 섹션 -->
    <div class="review-summary">
       <div class="average-rating">
           <h3>사용자 총 평점</h3>
           <div class="stars">
               <c:forEach var="i" begin="1" end="5">
                   <span class="${i <= averageRating ? 'filled-star' : 'empty-star'}">★</span>
               </c:forEach>
           </div>
           <div class="rating-number">
               ${averageRating} <small>/5</small>
           </div>
       </div>
        <div class="total-reviews">
            <h3>전체 리뷰수</h3>
            <div class="review-count">${reviewCount}</div>
        </div>
    </div>

    <!-- 리뷰 탭과 필터 -->
    <div class="review-filter">

        <div class="review-filter1">
            <span onclick="filterReviews('text')">텍스트 리뷰</span>
            <span onclick="filterReviews('photo')">포토/동영상 리뷰</span>
        </div>
        <div class="review-filter2">
            <span class="active" onclick="filterReviews('latest')">최신순</span>
            <span onclick="filterReviews('highest')">평점 높은순</span>
            <span onclick="filterReviews('lowest')">평점 낮은순</span>
          </div>
    </div>
            <!-- 리뷰 리스트 -->
      <div id="review-list">
          <c:forEach var="review" items="${reviews}">
               <div class="review-item ${not empty review.imgfile1 || not empty review.imgfile2 ? 'photo' : 'text'}"
                           data-rating="${review.grade}" data-date="${review.regDT}">
                  <div class="review-nickname">
                      <span class="review-rating">
                          <!-- 별점 출력 -->
                          <c:forEach var="i" begin="1" end="5">
                              <span class="${i <= review.grade ? 'filled-star' : 'empty-star'}">★</span>
                          </c:forEach>
                      </span>
                      <span class="reviewer-name">
                          <!-- 아이디 중간에 마스킹 처리 -->
                          <c:out value="${fn:substring(review.useridx, 0, 3)}"/>*****
                      </span>
                      <span class="review-date">${review.regDT}</span>
                  </div>

                  <div class="review-content">${review.content}</div>

                  <!-- 이미지 파일1이 있을 경우 출력 -->
                  <c:if test="${not empty review.imgfile1}">
                      <div class="review-image">
                          <img src="http://192.168.1.180:8000/${review.imgfile1}" alt="리뷰 이미지">
                      </div>
                  </c:if>

                  <!-- 이미지 파일2가 있을 경우 출력 -->
                  <c:if test="${not empty review.imgfile2}">
                      <div class="review-image">
                          <img src="http://192.168.1.180:8000/${review.imgfile2}" alt="리뷰 이미지 2">
                      </div>
                  </c:if>
              </div>
          </c:forEach>
      </div>


    
        <!-- 페이지 네이션 -->
        <div class="pagination">

        </div>

    <!-- Sticky Footer -->
    <div class="sticky-footer">
        <div class="d-flex justify-content-between sticky-footer_div">
            <div class="sticky-left">
                <div class="fw-bold">${storeDetail.title}
                    <p><fmt:formatNumber value="${storeDetail.price}" type="number" pattern="#,###"/> 원</p>
                </div>
                
            </div>
            <div class="sticky-right">
                <div class="price">  <fmt:formatNumber value="${storeDetail.price}" type="number" pattern="#,###"/> 원</div>
                <button class="sticky_basket_btn" id="basket_in_btn">장바구니</button>
                <button class="sticky_buy_btn" id="order_buy_btn">바로구매</button>
            </div>
        </div>
    </div>

  </div>
</div>

 <%@include file="/WEB-INF/inc/store_footer.jspf"%> 