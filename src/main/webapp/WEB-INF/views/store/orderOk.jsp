<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/store_header.jspf"%>

<script src="/js/orderOk.js"></script>
<link rel="stylesheet" href="/css/orderOK.css" type="text/css" />

<div class="payOk_container">
  <div class="payOk_progress">
    <ol>
      <li>
        01 SHOPPING BAG
        <i class="fa-solid fa-chevron-right"></i>
      </li>
      <li>
        02 ORDER
        <i class="fa-solid fa-chevron-right"></i>
      </li>
      <li>03 ORDER CONFIRMED</li>
    </ol>
  </div>
  <div class="payOk_ticketDiv">
    <div class="payOk_ticketDiv_left">
      <div class="payOk_ticketDiv_success">
        <h2>주문이 완료되었습니다.</h2>
      </div>
      <p>
        주문번호
        <strong>${paymentData.orderId}</strong>
      </p>
      <p>A GOODS SHOP FOR YOR "ANIWAVE"</p>
      <p>ANIWAVE.COM</p>
    </div>
    <div class="payOk_ticketDiv_right">
      <div class="ticketDiv_box_top_left"></div>
      <div class="ticketDiv_box_top_right"></div>
      <div class="ticketDiv_box_bottom_left"></div>
      <div class="ticketDiv_box_bottom_right"></div>
      <div></div>
    </div>
  </div>
  <div class="pay_product_section">
    <h3>주문상품정보</h3>
    <div class="pay_product_table">
      <ul>
        <li class="pay_product_table_head">
          <div>주문 상품</div>
          <div>수량</div>
          <div>진행상태</div>
        </li>
        <c:forEach var="orderListData" items="${orderListData}">
            <li class="pay_product_table_data">
              <div class="pay_product_table_data_info">
                <a>
                  <img src="http://192.168.1.180:8000/${orderListData.thumImg}" />
                  <div>
                    <p>${orderListData.ani_title}</p>
                    <h3>${orderListData.title}</h3>
                    <p class="order_product_price" data-price="${orderListData.price}"></p>
                  </div>
                </a>
              </div>
              <div class="pay_product_table_data_info"><span class="order_product_amount" data-amount="${orderListData.amount}">${orderListData.amount}<span></div>
              <div class="pay_product_table_data_info">
                ${orderListData.orderState == 1 ? '결제완료' : '결제실패'}
              </div>
            </li>
        </c:forEach>
      </ul>
    </div>
  </div>
  <div class="pay_info_section">
    <h3>결제정보</h3>
    <div class="pay_info_table">
      <table>
        <tbody>
          <tr>
            <th>결제방법</th>
            <td>${paymentData.paytype}</td>
          </tr>
          <tr>
            <th>주문상태</th>
            <td>${orderData.payState == 1 ? '결제완료' : '결제실패'}</td>
          </tr>
          <tr>
            <th>주문접수일시</th>
            <td>${orderData.regDT}</td>
          </tr>
          <tr>
            <th>결제완료일시</th>
            <td>${paymentData.createDT}</td>
          </tr>
          <tr>
            <th>배송비</th>
            <td class="order_fee"></td>
          </tr>
          <tr>
            <th>적립금 사용금액</th>
            <td class="order_usePoint" data-usePoint="${orderData.use_point}"></td>
          </tr>
          <tr>
            <th>결제금액</th>
            <td class="order_totalPrice" data-totalPrice="${paymentData.amount}"></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <div class="pay_delivery_section">
    <h3>배송정보</h3>
    <div class="pay_delivery_table">
      <table>
        <tbody>
          <tr>
            <th>받으시는 분</th>
            <td>${orderData.recipient}</td>
          </tr>
          <tr>
            <th>휴대폰번호</th>
            <td>${orderData.tel}</td>
          </tr>
          <tr>
            <th>주소</th>
            <td>[${orderData.zipcode}] ${orderData.addr} ${orderData.addrdetail}</td>
          </tr>
          <tr>
            <th>배송요청사항</th>
            <td>${orderData.request_memo}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <div class="page_info">
    <p>상세내역은 마이페이지에서 확인하실 수 있습니다.</p>
  </div>
  <div class="btn_div">
    <a href="/storeMain">계속 쇼핑하기</a>
    <button onclick="window.location.href='/user/mypage_order_detail/${orderData.idx}'">주문/배송조회</button>
  </div>
</div>

