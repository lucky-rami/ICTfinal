<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf"%>


<link rel="stylesheet" href="/css/mypage.css" type="text/css" />
<link
  rel="stylesheet"
  href="/css/mypage_userDelAgree.css"
  type="text/css"
/>
<script src="/js/mypage_common.js"></script>
<script src="/js/mypage_userDelAgree.js"></script>

<div class="mypage_wrap">
  <div class="mypage_main_wrap">
    <div class="mypage_left">
      <p class="mypage_home">MY PAGE</p>
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
            ><strong>회원정보</strong><span class="currentID"></span></a
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
            <span>회원 탈퇴 신청 시 아래 내용을 반드시 확인해주세요.</span>
          </div>
        </div>
        <div class="userdel_bottom">
          <div>
            <div>
              <span>회원 아이디</span>
              <span class="delinfo_span delid"></span>
            </div>
            <div>
              <span>소멸 예정 적립금</span>
              <span class="delinfo_span delPoint"></span>
            </div>
          </div>
          <dl class="delAgree_info">
            <dt>탈퇴 시 삭제되는 내용</dt>
            <dd>
              탈퇴 시 고객님께서 보유하셨던 쿠폰과 마일리지는 모두 소멸되며
              환불할 수 없습니다. 또한 다른 계정으로 양도 또는 이관할 수
              없습니다.
            </dd>
            <dd>
              탈퇴한 계정 및 이용 내역은 복구할 수 없으니 탈퇴 시 유의하시기
              바랍니다.
            </dd>
            <dt>탈퇴 시 보관 또는 유지되는 항목</dt>
            <dd>
              탈퇴 시 법령에 따라 보관해야 하는 항목은 관련 법령에 따라 일정
              기간 보관하며 다른 목적으로 이용하지 않습니다. 전자상거래
              등에서의 소비자보호에 관한 법률에 의거하여 대금결제 및 재화
              등의 공급에 관한 기록 5년, 계약 또는 청약철회 등에 관한 기록
              5년, 소비자의 불만 또는 분쟁처리에 관한 기록은 3년동안
              보관됩니다.
            </dd>
            <dd>
              탈퇴 후에도 서비스에 등록한 게시물 및 댓글은 그대로 남아
              있습니다. 상품 리뷰, 29TV 게시글, 이벤트 댓글 등은 삭제되지
              않습니다. 탈퇴 후에는 회원정보가 삭제되어 본인 여부를 확인할
              수 없으므로 게시글을 임의로 삭제해드릴 수 없습니다. 먼저 해당
              게시물을 삭제하신 후 탈퇴를 신청하시기 바랍니다.
            </dd>
            <dt>탈퇴 불가 및 기타 유의사항</dt>
            <dd>
              진행 중인 주문이 있는 경우에는 탈퇴할 수 없습니다. 이미 결제가
              완료된 건은 탈퇴로 취소되지 않으므로 주문 취소 후 탈퇴를
              시도하시고, 주문 내역을 미리 확인해주세요.
            </dd>
          </dl>
          <dd class="check_wrap">
            <input type="checkbox" id="chk_agree" name="agreement" /><label
              for="chk_agree"
              >위 내용을 모두 확인했습니다. (필수)</label
            >
          </dd>
          <div class="delbtn_div">
            <button onclick="location.href='/user/mypage_userEdit'">취소하기</button>
            <button id="userDel_OK">탈퇴하기</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<%@include file="/WEB-INF/inc/footer.jspf"%>
