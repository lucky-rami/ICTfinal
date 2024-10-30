<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf"%>

<link rel="stylesheet" href="/css/mypage.css" type="text/css" />
<link rel="stylesheet" href="/css/mypage_order.css" type="text/css" />
<script src="/js/mypage_common.js"></script>
<script src="/js/mypage_order.js"></script>

<div class="mypage_wrap">
  <div class="mypage_main_wrap">
    <div class="mypage_left">
      <a href="/user/mypage" class="mypage_home">MY PAGE</a>
      <section class="mypage_menu">
          <h3>나의 쇼핑정보</h3>
          <ul>
            <li>
              <a href="/user/mypage_order" class="option_active">주문배송조회</a>
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
        <div class="mypage_order_list_all">
          <h4 class="my_tit">주문배송조회</h4>
          <div>
            <div class="mypage_order_list">
              <div class="order_list_top">
                <div class="pro_inform">상품정보</div>
                <div class="delivery_fee">구매수량</div>
                <div class="progress_status">진행상태</div>
              </div>
              <ol class="order_list_bottom">

              </ol>
            </div>
          </div>
          <div id="pagination">
            <div class="custom-pagination">

            </div>
          </div>
          <div style="margin-top: 90px">
            <h4 class="my_tit">주문상태 및 취소기간 안내</h4>
            <ol class="order_notification">
              <li class="order_notification_li">
                <strong class="order_notification_strong">
                  <span>결제완료</span>
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 42 80">
                    <g fill="none" fill-rule="evenodd" stroke="#000000" stroke-width="3"><path d="M1 0l40 40.083L1.166 80"></path></g>
                  </svg>
                </strong>
                <p>입금(결제) 확인이 완료되었습니다</p>
              </li>
              <li class="order_notification_li">
                <strong class="order_notification_strong">
                  <span>상품준비중</span>
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 42 80">
                    <g fill="none" fill-rule="evenodd" stroke="#000000" stroke-width="3"><path d="M1 0l40 40.083L1.166 80"></path></g>
                  </svg>
                </strong>
                <p>주문하신 상품을 발송할 예정입니다</p>
              </li>
              <li class="order_notification_li">
                <strong class="order_notification_strong">
                  <span>배송시작</span>
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 42 80">
                    <g fill="none" fill-rule="evenodd" stroke="#000000" stroke-width="3"><path d="M1 0l40 40.083L1.166 80"></path></g>
                  </svg>
                </strong>
                <p>상품준비가 완료되어 곧 배송 될 예정입니다</p>
              </li>
              <li class="order_notification_li">
                <strong class="order_notification_strong">
                  <span>배송중</span>
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 42 80">
                    <g fill="none" fill-rule="evenodd" stroke="#000000" stroke-width="3"><path d="M1 0l40 40.083L1.166 80"></path></g>
                  </svg>
                </strong>
                <p>상품이 고객님께 배송중입니다</p>
              </li>
              <li class="order_notification_li">
                <strong class="order_notification_strong">
                  <span>배송완료</span>
                  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 42 80">
                    <g fill="none" fill-rule="evenodd" stroke="#000000" stroke-width="3"><path d="M1 0l40 40.083L1.166 80"></path></g>
                  </svg>
                </strong>
                <p>상품이 주문자에게 전달완료 되었습니다</p>
              </li>
              <li class="order_notification_li">
                <strong class="order_notification_strong">
                  <span>구매확정</span>
                </strong>
                <p>구매를 확정하여 적립금 및 리뷰작성을 할 수 있습니다</p>
              </li>
            </ol>
            <div class="order_delCheck">
              <p class="del_possible">취소가능</p>
              <p class="del_impossible">취소불가</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<%@include file="/WEB-INF/inc/footer.jspf"%>