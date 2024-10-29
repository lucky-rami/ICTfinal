<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf"%>

<link rel="stylesheet" href="/css/mypage.css" type="text/css" />
<link rel="stylesheet" href="/css/mypage_qna.css" type="text/css" />
<script src="/js/mypage_common.js"></script>
<script src="/js/mypage_qna.js"></script>

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
              <a href="/user/mypage_qna" class="option_active">1:1 문의</a>
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
        <h4 class="my_tit_qna">1:1 문의내역</h4>
        <div class="qna_top">
          <div>
            <span>한번 등록한 상담 내용은 수정이 불가능합니다.</span>
            <span
              >문의와 무관하거나 운영정책에 위배되는 경우 고지없이 경고
              조치됩니다.</span
            >
          </div>
          <div>
            <button type="button" class="qna_write_btn">
              1:1 문의 쓰기
            </button>
          </div>
        </div>

        <div class="myqna_list">
          <ul>
            <li class="myqna_list_li">
              <div class="qna_type">
                <span>상담구분</span>
              </div>
              <div class="qna_title">
                <span>상담제목</span>
              </div>
              <div class="qna_regDT">
                <span>작성일</span>
              </div>
              <div class="qna_replyState">
                <span>답변유무</span>
              </div>
              <div class="qna_delBtn_div"></div>
            </li>
            <div>
              <ul class="myqna_write_list_ul">

              </ul>
            </div>
          </ul>
        </div>
        <div id="pagination">
          <div class="custom-pagination">

          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<%@include file="/WEB-INF/inc/footer.jspf"%>