<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/store_header.jspf"%>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Store Main</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
        <!-- Bootstrap CSS 로드 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- jQuery 로드 -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    
        <!-- jQuery Migrate 로드 (필요할 경우) -->
        <script src="//code.jquery.com/jquery-migrate-1.2.1.min.js"></script>
    
        <!-- Slick CSS 로드 -->
        <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick.min.css"/>
        <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick-theme.min.css"/>
    
        <!-- Slick JS 로드 -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/slick-carousel/1.8.1/slick.min.js"></script>
    
        <!-- Bootstrap JS 로드 -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    
        <!-- Custom CSS -->
        <link href="/css/storeMain.css" rel="stylesheet" type="text/css">
    
        <!-- jQuery noConflict 설정 -->
        <script type="text/javascript">
            var $371 = $.noConflict();
        </script>
    
        <!-- 기존 Custom JS가 있는 경우 -->
        <script src="js/storeMain.js"></script>
</head>

<!-- 배너 -->
<section class="banner" id="banner">
    <div class="carousel-banner-images" id="slider-div">
        <div class="slide">
            <a href="bannerDetail1.jsp">
                <img src="img/store/banner1.png" alt="Main Banner1">
            </a>
        </div>
        <div class="slide">
            <a href="bannerDetail2.jsp">
                <img src="img/store/banner2.png" alt="Main Banner2">
            </a>   
        </div>
        <div class="slide">
            <a href="bannerDetail3.jsp">
                <img src="img/store/banner3.png" alt="Main Banner3">
            </a>
        </div>
    </div>
</section>




<!-- Featured Products Section -->
<section class="products">
    <h2>Hot #인기굿즈</h2>
    <div class="carousel">
        <button class="carousel-control prev" onclick="moveSlide('featured-products', -1)">&#10094;</button>
        <div class="carousel-wrapper">
            <div class="carousel-images">
                <div class="product">
                    <a href="productDetail1.jsp">
                        <img src="img/store/f1.png" alt="Product 1">
                    </a>
              
                </div>
                <div class="product">
                    <a href="productDetail2.jsp">
                        <img src="img/store/f2.png" alt="Product 2">
                    </a>
                            
                </div>
                <div class="product">
                    <a href="productDetail3.jsp">
                        <img src="img/store/f3.png" alt="Product 3">
                    </a>
                          
                </div>
                <div class="product">
                    <a href="productDetail4.jsp">
                        <img src="img/store/f4.png" alt="Product 4">
                    </a>
                  
                </div>
            </div>
        </div>
        <button class="carousel-control next" onclick="moveSlide('featured-products', 1)">&#10095;</button>
    </div>
</section>

<!-- Original Goods Section -->
<section class="original-goods">
    <h2>오리지널 굿즈</h2>
    <div class="carousel-original">
        <button class="carousel-control prev" onclick="moveSlide('original-goods', -1)">&#10094;</button>
        <div class="carousel-wrapper">
            <div class="carousel-goods-images">
                <div class="product-item">
                <a href="detail1.jsp">
                    <img src="img/store/origingoods1.png" alt="Product 1">
                    <p>러브라이브 굿즈 판매</p>
                </div>   
                <div class="product-item">
                </a>
                <a href="detail2.jsp">
                    <img src="img/store/origingoods2.png" alt="Product 2">
                    <p>hololive GAMERS 콜라보 굿즈 예약판매</p>
                </a>
                </div>
                <div class="product-item">
                <a href="detail3.jsp">
                    <img src="img/store/origingoods3.png" alt="Product 3">
                    <p>도쿄리벤저스 콜라보 굿즈 예약판매</p>
                </a>
                </div>
            </div>
        </div>
        <button class="carousel-control next" onclick="moveSlide('original-goods', 1)">&#10095;</button>
    </div>
</section>

<!-- More Products Section -->
<section class="products2">
    <h2>NEW #신상굿즈</h2>
    <div class="carousel">
        <button class="carousel-control prev" onclick="moveSlide('more-products', -1)">&#10094;</button>
        <div class="carousel-wrapper">
            <div class="carousel-images">
                <div class="product">
                    <a href="productDetail1.jsp">
                        <img src="img/store/f5.png" alt="Product 1">
                    </a>
         
                </div>
                <div class="product">
                    <a href="productDetail2.jsp">
                        <img src="img/store/f6.png" alt="Product 2">
                    </a>
         
                </div>
                <div class="product">
                    <a href="productDetail3.jsp">
                        <img src="img/store/f7.png" alt="Product 3">
                    </a>
                               
                </div>
                <div class="product">
                    <a href="productDetail4.jsp">
                        <img src="img/store/f8.png" alt="Product 4">
                    </a>
                       
                </div>
            </div>
        </div>
        <button class="carousel-control next" onclick="moveSlide('more-products', 1)">&#10095;</button>
    </div>
</section>



<!-- MD news Section -->
<section class="md_pick">
    <h2>MD's pick</h2>
    <div class="md">
        <button class="md-control prev" onclick="moveSlide('md_pick', -1)">&#10094;</button>
        <div class="md-wrapper">
            <div class="md-images">
                <a href="md1.jsp">
                    <img src="img/store/news1.png" alt="Product 1">
                   
                </a>
                <a href="md2.jsp">
                    <img src="img/store/news2.png" alt="Product 2">
                 
                </a>
                <a href="md3.jsp">
                    <img src="img/store/news3.png" alt="Product 3">
                 
                </a>
            </div>
        </div>
        <button class="md-control next" onclick="moveSlide('md_pick', 1)">&#10095;</button>
    </div>
</section>
