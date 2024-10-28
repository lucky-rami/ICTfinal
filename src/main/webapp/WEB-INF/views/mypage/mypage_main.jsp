<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf"%>

<link rel="stylesheet" href="/css/mypage.css" type="text/css" />
<link rel="stylesheet" href="/css/mypage_main.css" type="text/css" />
<script src="/js/mypage_common.js"></script>
<script src="/js/mypage_main.js"></script>

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
            <a href="/user/mypage_review"> 상품 리뷰 </a>
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
        <div class="my_order">
          <h3 class="my_tit">최근 주문</h3>
          <ul class="my_order_tbl">
            <li class="my_order_tit">
              <div class="my_tbl">
                <div class="date">주문일</div>
                <div class="history history_top">주문내역</div>
                <div class="num">주문번호</div>
                <div class="amount">결제금액</div>
              </div>
            </li>
          </ul>
          <a class="btn_my_more" href="/user/mypage_order">더보기</a>
        </div>

        <div class="split_wrap">
          <div class="my_heart">
            <h3 class="my_tit">MY HEART</h3>

            <div class="heart_artwrap" id="heart_artwrap_goods">
              <h4 class="h_tit">GOODS</h4>
              <ul class="heart_lst" id="heart_lst_goods">

              </ul>
            </div>

            <div class="heart_artwrap" id="heart_artwrap_ani">
              <h4 class="h_tit">ANIMATION</h4>
              <ul class="heart_lst" id="heart_lst_ani">

              </ul>
            </div>
            <a class="btn_my_more" href="/user/mypage_heart">더보기</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>



