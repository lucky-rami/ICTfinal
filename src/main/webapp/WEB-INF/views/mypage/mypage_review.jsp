<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf"%>

<link rel="stylesheet" href="/css/mypage.css" type="text/css" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" />
<link rel="stylesheet" href="/css/mypage_review.css" type="text/css" />
<link rel="stylesheet" href="/css/mypage_reviewmodal.css" type="text/css"/>
<script src="/js/mypage_common.js"></script>
<script src="/js/mypage_review.js"></script>

<div class="mypage_wrap">
  <div class="mypage_main_wrap">
    <div class="mypage_left">
      <a href="/user/mypage" class="mypage_home">MY PAGE</a>
      <section class="mypage_menu">
          <h3>나의 쇼핑정보</h3>
          <ul>
            <li>
              <a href="/user/mypage_order">주문배송조회</a>
            </li>
            <li>
              <a href="/user/mypage_review" class="option_active"> 상품 리뷰 </a>
            </li>
          </ul>
          <h3>나의 계정설정</h3>
          <ul>
            <li>
              <a href="/user/mypage_userEdit">회원정보수정</a>
            </li>
            <li>
              <a href="/user/mypage_comm">내가 쓴 글</a>
            </li>
            <li>
              <a href="/user/mypage_point">적립금</a>
            </li>
            <li>
              <a href="/user/mypage_heart">좋아요</a>
            </li>
          </ul>
          <h3>고객센터</h3>
          <ul>
            <li>
              <a href="/user/mypage_qna">1:1 문의</a>
            </li>
            <li>
              <a href="">공지사항</a>
            </li>
            <li>
              <a href="">FAQ</a>
            </li>
          </ul>
        </section>

      <section>
        <div class="btn_bx">
          <button type="button" class="logout_btn" onclick="logout()">LOGOUT</button>
        </div>
      </section>
    </div>
    <div class="mypage_right">
      <ul class="mypage_right_top">
          <li class="user_inform">
            <a class="atag_css" href="/user/mypage_userEdit"
              ><strong>회원정보 수정</strong><span class="currentID"></span></a
            >
          </li>
          <li class="user_retention_details">
            <a class="atag_css" href="/user/mypage_point"
              ><strong class="">적립금</strong><span class="reservePoint"></span></a
            >
          </li>
          <li class="user_retention_details">
            <a class="atag_css" href="/user/mypage_review"
              ><strong class="">작성한 리뷰</strong><span class="afterCountSpan"></span></a
            >
          </li>
        </ul>

      <div class="mypage_right_element">
        <!-- 여기에 페이지에 맞는 요소 넣으면 됨 -->
        <div class="on" id="tab1">
          <ul class="review_tabmenu">
            <li class="tab_activate" onclick="reviewTab('tab1')">작성 가능한 리뷰 (<span class="beforeCountSpan">0</span>)</li>
            <li class="tab_deactivate" onclick="reviewTab('tab2')">내 리뷰 (<span class="afterCountSpan"></span>)</li>
          </ul>
          <div class="review_list_all">
            <div>
              <ul class="review_list_ul" id="review_list_ul">

              </ul>
            </div>
          </div>
        </div>
        <div class="off" id="tab2">
          <ul class="review_tabmenu">
            <li class="tab_deactivate" onclick="reviewTab('tab1')">작성 가능한 리뷰 (<span class="beforeCountSpan">0</span>)</li>
            <li class="tab_activate" onclick="reviewTab('tab2')">내 리뷰 (<span class="afterCountSpan"></span>)</li>
          </ul>
          <div class="review_list_all">
            <div>
              <ul class="review_list_ul" id="review_list_ul2">

              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<%@include file="/WEB-INF/inc/footer.jspf"%>