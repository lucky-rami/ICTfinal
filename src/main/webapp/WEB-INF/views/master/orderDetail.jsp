<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/Masterheader.jspf" %>
<title>DashBoard - 주문 내역 상세정보</title>
<link href="/css/masterStyle.css" rel="stylesheet" type="text/css" />
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="/js/MasterPage.js"></script>
<!-- 채원 -->
<link href="/css/chaewon_master.css" rel="stylesheet" type="text/css" />
<script src="/js/chaewon_master_orderDetail.js"></script>

<h2 class="order_list_h2">주문 상세정보</h2>

<div class="order_detatil_top">
    <h4>주문정보</h4>
    <table class="order-list table table-hover table-bordered">
        <thead>
            <tr>
                <th style="width:3%">주문코드</th>
                <th style="width:10%">주문번호</th>
                <th style="width:3%">주문자 아이디</th>
                <th style="width:3%">결제타입</th>
                <th style="width:3%">결제금액</th>
                <th style="width:3%">사용 적립금</th>
                <th style="width:8%">결제일시</th>
            </tr>
        </thead>
        <tbody class="user_orderDetail">
            <tr class="user_orderDetail_tr">
                <td>${orderDetailInfo.order_idx}</td>
                <td>${orderDetailInfo.orderId}</td>
                <td>${orderDetailInfo.customerName}</td>
                <td>${orderDetailInfo.paytype}</td>
                <td><span class="order_orderPrice">${orderDetailInfo.amount}<span></td>
                <td><span class="order_usePoint">${orderDetailInfo.use_point}<span></td>
                <td>${orderDetailInfo.payDT}</td>
            </tr>
        </tbody>
    </table>

    <h4>배송정보</h4>
    <table class="order-list table table-hover table-bordered">
        <thead>
            <tr>
                <th style="width:2%">수령인</th>
                <th style="width:3%">전화번호</th>
                <th style="width:10%">주소</th>
                <th style="width:6%">요청사항</th>
                <th style="width:4%">운송장번호
                    <!-- 상품준비중인 상품이 있을 때만 버튼 표시 -->
                    <c:if test="${hasPreparingProducts}">
                        <button class="btn btn-secondary" id="order_trackingNum_btn">
                            <c:choose>
                                <c:when test="${not empty orderDetailInfo.trackingNum}">
                                    수정
                                </c:when>
                                <c:otherwise>
                                    저장
                                </c:otherwise>
                            </c:choose>
                        </button>
                    </c:if>
                </th>
            </tr>
        </thead>
        <tbody class="user_orderList">
            <tr class="user_orderDeli_tr">
                <td>${orderDetailInfo.recipient}</td>
                <td>${orderDetailInfo.tel}</td>
                <td>[${orderDetailInfo.zipcode}] ${orderDetailInfo.addr} ${orderDetailInfo.addrdetail}</td>
                <td>${orderDetailInfo.request_memo}</td>
                <td id="trackingNumCell">${orderDetailInfo.trackingNum}</td>
            </tr>
        </tbody>
    </table>
</div>


<!-- 주문 관리 리스트 -->
<div class="order-list-container">
    <h4>주문 상품 정보</h4>
    <table class="order-list table table-hover table-bordered">
        <thead>
            <tr>
                <th style="width:3%">상품코드</th>
                <th style="width:12%">상품명</th>
                <th style="width:3%">구매갯수</th>
                <th style="width:3%">취소갯수</th>
                <th style="width:3%">상품결제가격<br>(배송비, 할인제외)</th>
                <th style="width:4%">상태</th>
            </tr>
        </thead>
        <tbody class="user_orderList">
            <c:forEach var="orderDetailProduct" items="${orderDetailProducts}">
                <tr class="user_orderList_tr">
                    <td>${orderDetailProduct.pro_idx}</td>
                    <input type="hidden" id="idx" value="${orderDetailProduct.idx}">
                    <input type="hidden" id="order_idx" value="${orderDetailProduct.order_idx}">
                    <td>${orderDetailProduct.title}</td>
                    <td><span>${orderDetailProduct.amount}개</span></td>
                    <td><span>${orderDetailProduct.cancelCount}개</span></td>
                    <td><span class="order_productPrice"></span></td>
                    <td>
                        <span class="order_productState"></span>
                        <button class="btn btn-secondary productBtn"></button>
                        <div class="button-group"></div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>


<script>
    let prices = [];
    let amounts = [];
    let orderStates = [];

    <c:forEach var="product" items="${orderDetailProducts}" varStatus="status">
        prices.push(${product.price});
        amounts.push(${product.amount - product.cancelCount});
        orderStates.push(${product.orderState});
    </c:forEach>

    let order_amount=${orderDetailInfo.amount};
    let use_point = ${orderDetailInfo.use_point};

</script>