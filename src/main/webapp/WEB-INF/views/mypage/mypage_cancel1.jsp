<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf"%>

<link rel="stylesheet" href="/css/mypage.css" type="text/css" />
<link rel="stylesheet" href="/css/mypage_cancel1.css" type="text/css" />
<script src="/js/mypage_common.js"></script>
<script src="/js/mypage_cancel1.js"></script>

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
        <ol class="cancelProcess">
          <li class="cancel_step">
            <p class="cancel_stepOk">취소상품 선택</p>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              color="primary"
              class="nextSvg"
            >
              <g id="weight=light, fill=false">
                <path
                  id="vector"
                  fill-rule="evenodd"
                  clip-rule="evenodd"
                  d="M16.7071 12L8.35354 20.3536L7.64643 19.6464L15.2929 12L7.64643 4.35355L8.35354 3.64645L16.7071 12Z"
                  fill="black"
                ></path>
              </g>
            </svg>
          </li>
          <li class="cancel_step">
            <p class="cancel_stepNo">취소사유 작성</p>
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="24"
              height="24"
              viewBox="0 0 24 24"
              fill="none"
              color="primary"
              class="nextSvg"
            >
              <g id="weight=light, fill=false">
                <path
                  id="vector"
                  fill-rule="evenodd"
                  clip-rule="evenodd"
                  d="M16.7071 12L8.35354 20.3536L7.64643 19.6464L15.2929 12L7.64643 4.35355L8.35354 3.64645L16.7071 12Z"
                  fill="black"
                ></path>
              </g>
            </svg>
          </li>
          <li class="cancel_step">
            <p class="cancel_stepNo">취소정보 확인</p>
          </li>
        </ol>
        <div class="cancel_product_container">
          <section>
            <h2>취소 상품 선택</h2>
            <table>
              <thead class="productCancel_thead">
                <tr>
                  <th>상품 정보</th>
                  <th>진행 상태</th>
                  <th>수량 선택</th>
                </tr>
              </thead>
              <tbody class="productCancel_tbody">
                <c:forEach var="product" items="${cancelProductList}">
                    <tr>
                      <td class="productCancel_td">
                        <div class="cancel_list_chkbx">
                          <input type="checkbox" class="chk_product" id="chk_product_${product.pro_idx}" name="chk_product" value="${product.pro_idx}"/>
                          <label for="chk_product_${product.pro_idx}"></label>
                        </div>
                        <div class="cancel_product_info">
                          <img src="http://192.168.1.180:8000/${product.thumImg}" />
                          <div>
                            <p class="order_aniTitle">${product.ani_title}</p>
                            <strong>${product.title}</strong>
                            <p class="pro_price"></p>
                          </div>
                        </div>
                      </td>
                      <td class="productCancel_td">
                        <span>${product.orderState == 1 ? "결제완료" : "부분취소완료"}</span>
                      </td>
                      <td class="productCancel_td">
                        <div class="cancel_amount_count">
                          <div>
                            <button class="cancelAmount_minus">-</button>
                            <input
                              type="text"
                              class="cancel_amount_input"
                              id="cancel_amount"
                              value="${product.amount - product.cancelCount}"
                            />
                            <button class="cancelAmount_plus">+</button>
                          </div>
                        </div>
                      </td>
                    </tr>
                </c:forEach>

              </tbody>
            </table>
          </section>
          <div class="cancel_note">
            <div>
              <span>취소 접수 전 유의 사항 안내</span>
              <ul>
                <li>- 한 번에 한 주문끼리만 취소 접수 가능합니다.</li>
                <li>- 한번 취소한 상품은 상태변경이 불가합니다.</li>
                <li>- 상품에따라 취소가 불가능할 수 있습니다.</li>
              </ul>
            </div>
            <button id="next_cancelPage">다음으로</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
    let prices = [];
    let maxAmounts = [];

    <c:forEach var="product" items="${cancelProductList}" varStatus="status">
        prices.push(${product.price});
        maxAmounts.push(${product.amount - product.cancelCount});
    </c:forEach>
</script>

