<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf"%>

<link rel="stylesheet" href="/css/mypage.css" type="text/css" />
<link rel="stylesheet" href="/css/mypage_userDelReason.css" type="text/css" />
<script src="/js/mypage_common.js"></script>
<script src="/js/mypage_userDelReason.js"></script>

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
              <a href="/user/mypage_userEdit" class="option_active">회원정보수정</a>
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
        <h4 class="my_tit_userdel">회원탈퇴</h4>
        <div class="userdel_top">
          <div>
            <span
              >의견을 남겨주시면 소중히 새겨듣고 오래 이용할 수 있는
              서비스로 개선하겠습니다.</span
            >
            <span
              >궁금하신 점은 고객센터 1:1 문의로 연락주시면 처리를
              도와드립니다.</span
            >
          </div>
        </div>
        <div>
          <h4>탈퇴 사유 선택</h4>
          <form class="userDel_form">
            <select name="delReason" id="delReason">
              <option value="1">
                탈퇴 후 재가입을 위해서
              </option>
              <option value="2">
                사고 싶은 상품이 없어서
              </option>
              <option value="3">
                자주 이용하지 않아서
              </option>
              <option value="4">
                원하는 애니가 없어서
              </option>
              <option value="4">
                서비스 및 고객지원이 만족스럽지 않아서
              </option>
            </select>
            <textarea id="delReasonDetail"
              placeholder="이 외에 불편하셨던 점을 알려주세요(선택)"
            ></textarea>
            <div>
              <input type="button" value="취소하기" onclick="location.href='/user/mypage_userEdit'"/>
              <input type="button" value="다음으로" id="userDel_next"/>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</div>

<%@include file="/WEB-INF/inc/footer.jspf"%>