<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf"%>

<link rel="stylesheet" href="/css/mypage.css" type="text/css" />
<link rel="stylesheet" href="/css/mypage_point.css" type="text/css" />

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
              <a href="">내가 쓴 글</a>
            </li>
            <li>
              <a href="/user/mypage_point" class="option_active">적립금</a>
            </li>
            <li>
              <a href="/user/mypage_heart">좋아요</a>
            </li>
            <li>
              <a href="/user/mypage_userDel">회원탈퇴</a>
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
          <button type="button" class="logout_btn">LOGOUT</button>
        </div>
      </section>
    </div>
    <div class="mypage_right">
      <ul class="mypage_right_top">
          <li class="user_inform">
            <a class="atag_css" href="/user/mypage_userEdit"
              ><strong>회원정보 수정</strong><span>sing2727 님</span></a
            >
          </li>
          <li class="user_retention_details">
            <a class="atag_css" href="/user/mypage_point"
              ><strong class="">적립금</strong><span class="">360</span></a
            >
          </li>
          <li class="user_retention_details">
            <a class="atag_css" href="/user/mypage_review"
              ><strong class="">작성한 리뷰</strong><span class="">1</span></a
            >
          </li>
        </ul>

      <div class="mypage_right_element">
        <!-- 여기에 페이지에 맞는 요소 넣으면 됨 -->
        <div>
          <h4 class="my_tit">적립금 내역</h4>
          <div class="point_list_all">
            <div>
              <ul class="point_list_ul">
                <li class="point_list_li">
                  <div class="point_date"><span>2024.04.21</span></div>
                  <div class="point_inform">
                    <div class="point_inform_detail"><span>상품 구매확정 적립</span><span>+360</span></div>
                  </div>
                </li>
                <li class="point_list_li">
                  <div class="point_date"><span>2024.04.20</span></div>
                  <div class="point_inform">
                    <div class="point_inform_detail"><span>리뷰 작성 적립</span><span>+100</span></div>
                  </div>
                </li>
                <li class="point_list_li">
                  <div class="point_date"><span>2024.04.19</span></div>
                  <div class="point_inform">
                    <div class="point_inform_detail"><span>상품 구매 사용</span><span>-1000</span></div>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

