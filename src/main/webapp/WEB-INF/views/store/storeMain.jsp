<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@include file="/WEB-INF/inc/store_header.jspf"%>


<!-- jQuery 로드 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<!-- jQuery Migrate 로드 (필요할 경우) -->
<script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>

<!-- Slick CSS 로드 -->
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick.min.css"/>
<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick-theme.min.css"/>

<!-- Slick JS 로드 -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick.min.js"></script>

<!-- Custom CSS -->
<link href="/css/storeMain.css" rel="stylesheet" type="text/css">

<!-- jQuery noConflict 설정 -->
<script type="text/javascript">
    var $371 = $.noConflict();
</script>

<!-- 기존 Custom JS가 있는 경우 -->
<script src = "../../../js/storeMain.js"></script>

<div class="storeMain_container">
    <!-- 배너 -->
        <section class="banner" id="banner">
            <div class="carousel-banner-images" id="slider-div">
                <div class="slide">
                    <a href="/storeList">
                        <img src="img/store/banner1.png" alt="Main Banner1">
                    </a>
                </div>
                <div class="slide">
                    <a href="/storeList">
                        <img src="img/store/banner2.png" alt="Main Banner2">
                    </a>
                </div>
                <div class="slide">
                    <a href="/storeList">
                        <img src="img/store/banner3.png" alt="Main Banner3">
                    </a>
                </div>
            </div>
        </section>

        <!--Notification-->
        <section>
            <div class="notification" onclick="goToNoticePage()">
                <div id="noticeList">

                    <!-- 제목과 등록일자가 여기에 표시됩니다 -->
                </div>
            </div>
        </section>

            <!-- 인기굿즈 -->
        <section class="products">
          <a href="/storeList">
              <h2>Hot #인기굿즈</h2>
          </a>
            <p>[25/1월 발매] hololive GAMERS 콜라보 굿즈 예약 판매(~10.29)</p>
                <div class="pop-carousel">
                    <div class="pop-carousel-wrapper">
                        <div class="pop-carousel-images">
                            <!-- 서버에서 불러온 데이터를 사용한 이미지 리스트 -->
                            <c:forEach var="product" items="${recentProducts}">
                                <div class="pop-product">
                                      <a href="/storeDetail/${product.idx}">
                                        <!-- 서버에서 불러온 이미지 경로 사용 -->
                                        <img src="http://192.168.1.180:8000/${product.thumImg}" alt="${product.title}">
                                        <!-- <span class="rcont"></span> -->
                                        <div class="tit">${product.title}</div>
                                        <span class="price">
                                          <span class="discount"><fmt:formatNumber value="${product.price}" type="number" pattern="#,###"/> 원</span>
                                        </span>
                                        <span class="tag" style="cursor: default;">
                                            <span>${product.brand}</span>
                                            <span>${product.ani_title}</span>
                                        </span>
                                    </a>
                                </div>
                            </c:forEach>

                    </div>
                </div>            
            </div>       
        </section>


        <section class="origin-goods">
            <h2>오리지널 굿즈</h2>
                <div class="origin-carousel">
                    <div class="origin-carousel-wrapper">
                        <div class="origin-carousel-images">
                   <!-- Origin 이미지 섹션 -->
                   <c:forEach var="originImageInfo" items="${originImageInfoList}">
                       <div class="origin-item">
                           <div class="origin-item-img">
                               <a href="/storeList">
                                   <img src="/img/store/origin/${originImageInfo.imageNameWithExt}" alt="${originImageInfo.imageName}">
                               </a>
                           </div>
                           <div class="origin-tag">${originImageInfo.imageName}</div>
                       </div>
                   </c:forEach>
                        </div>    
                    </div>
                </div>
        </section>

        <!-- 신규굿즈 -->
        <section class="new-products">
            <a href="/storeList">
            <h2>Hot #신규굿즈</h2>
            </a>
            <p>[25/1월 발매] hololive GAMERS 콜라보 굿즈 예약 판매(~10.29)</p>
                <div class="new-carousel">
                    <div class="new-carousel-wrapper">
                        <div class="new-carousel-images">
                             <!-- 3개월 이내의 신규 굿즈를 DB에서 가져와 출력 -->

                                            <c:forEach var="product" items="${recentProducts}">
                                                    <div class="new-product">
                                                    <a href="/storeDetail/${product.idx}">
                                                        <img src="http://192.168.1.180:8000/${product.thumImg}" alt="${product.title}">
                                                        <!-- <span class="rcont"></span> -->
                                                        <div class="tit">${product.title}</div>
                                                        <span class="price">
                                                            <span class="discount"><fmt:formatNumber value="${product.price}" type="number" pattern="#,###"/> 원</span>
                                                        </span>
                                                        <span class="tag" style="cursor: default;" >
                                                            <span>${product.brand}</span>
                                                            <span>${product.ani_title}</span>
                                                        </span>
                                                    </a>
                                                </div>
                                            </c:forEach>
                        </div>
                    </div>            
                </div>       
        </section>

            <!--event-->
            <section class="md">
                <h2>이벤트#신규이벤트 업데이트</h2>
                    <div class="md-carousel">
                        <div class="md-carousel-wrapper">
                            <div class="md-carousel-images">

                      <!-- Event 이미지 섹션 -->
                      <c:forEach var="eventImageInfo" items="${eventImageInfoList}">
                          <div class="md-item">
                              <div class="md-item-img">
                                  <a href="/Event">
                                      <img src="/img/store/event/${eventImageInfo.imageNameWithExt}" alt="${eventImageInfo.imageName}">
                                  </a>
                              </div>
                              <div class="md-tag">${eventImageInfo.imageName}</div>
                          </div>
                      </c:forEach>
    
                            </div>    
                        </div>
                    </div>
            </section>
            
     
</div>

<%@include file="/WEB-INF/inc/store_footer.jspf"%>