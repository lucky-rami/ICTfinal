function formatNumber(number) {
    return number.toLocaleString();
}

let orderDetail = null;
let orderer = null;
let cancelData = null;

$(function(){
    const token = localStorage.getItem("token");
    console.log("order_idx : ",order_idx);
    $.ajax({
        url: '/user/getOrderDetailOk',
        type: 'POST',
        data: { order_idx:order_idx },
        headers: {
            "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
        },
        success: function(response) {
            console.log(response);
            orderDetail = response.orderDatail;
            orderer = response.orderer;
            cancelData = response.cancelData;
            console.log(cancelData);
            displayOrderDetail(orderDetail,orderer,cancelData);
        },
        error: function(error) {
            console.log('Error:', error);
        }
    });
})

function displayOrderDetail(orderDetail,orderer,cancelData){
    const orderContainer = $(".order_detail_all_container");
    orderContainer.empty();

    // 주문한 상품들의 총액 계산
    const totalAmount = orderDetail.products.reduce((total, product) => {
        return total + (product.pro_price * product.order_pro_amount);
    }, 0);
    // 배송비 계산
    let fee = totalAmount >= 150000 ? 0 : 2500;

    const formattedTotalAmount = formatNumber(totalAmount) + "원";
    const formattedFee = formatNumber(fee) + "원";

    // 결제완료 또는 부분취소 완료 상태인 상품이 있는지 확인
    const hasCancelableProduct = orderDetail.products.some(product => product.orderState === 1 || product.orderState === 8);

    const cancelButtonHTML = hasCancelableProduct
        ? `<button id="payCancel_btn">상품 결제 취소하기</button>`
        : ''; // 조건에 맞는 상품이 있으면 취소하기 버튼 추가

    let orderHTML = `
        <div class="order_detail_all">
            <input type="hidden" id="order_idx" value="${orderDetail.order_idx}"/>
            <div class="order_detail_state">
              <h4 class="my_tit">주문상세내역</h4>
              <div class="order_detail_inform">
                <div>
                  <div class="tb_header">
                    <div class="tb_header_proinform">상품정보</div>
                    <div class="tb_header_fee">구매수량</div>
                    <div class="tb_header_state">진행상태</div>
                  </div>
                  <div class="my_data">
                      <p><span>주문일자  </span>${orderDetail.order_date}</p>
                      <p><span>주문번호  </span>${orderDetail.orderId}</p>
                      ${cancelButtonHTML}
                  </div>
                  <ul class="tb_body_ul">
                    ${orderDetail.products.map(product => {
                      const formattedAmount = formatNumber(product.pro_price) + "원";
                      const packageButton = product.orderState >= 3 && product.orderState <=6
                          ? `<button class="order_package">CJ대한통운 <span id="deli_tracking">${orderDetail.trackingNum}</span></button>`
                          : '';
                      const orderConfirmButton = product.orderState ==5
                          ? `<button class="order_package orderConfirm">구매확정 하기</button>`
                          : '';
                      return `
                      <li>
                          <ul>
                            <li class="order_data_list_one">
                              <input type="hidden" id="pro_idx" value="${product.pro_idx}"/>
                              <a href="/storeDetail/${product.pro_idx}">
                                <div class="order_data_inform">
                                  <div class="order_data_img"><img src="http://192.168.1.180:8000/${product.pro_image}" /></div>
                                  <div>
                                    <p class="order_aniTitle">${product.pro_anititle}</p>
                                    <p class="order_pro_name">${product.pro_title}</p>
                                    <ul class="order_pro_option">
                                      <li>${formattedAmount}</li>
                                    </ul>
                                  </div>
                                </div>
                              </a>
                              <div class="order_delivery_fee_data">${product.order_pro_amount}</div>
                              <div class="order_current_state_div">
                                <div>
                                  <div class="order_state_all">
                                    <p class="order_state_data">${getOrderStateText(product.orderState)}</p>
                                  </div>
                                  ${packageButton}
                                  ${orderConfirmButton}
                                </div>
                              </div>
                            </li>
                          </ul>
                      </li>
                    `;}).join('')}
                  </ul>
                </div>
              </div>
            </div>
            <div>
              <div>
                <h4 class="my_tit">구매자정보</h4>
              </div>
              <table class="buyer_inform">
                <colgroup>
                  <col width="140" />
                  <col />
                  <col width="140" />
                  <col />
                </colgroup>
                <tbody>
                  <tr>
                    <th scope="row" class="th_style">주문자</th>
                    <td class="td_style">${orderer.username}</td>
                    <th scope="row" class="th_style">아이디</th>
                    <td class="td_style">${orderer.userid}</td>
                  </tr>
                  <tr>
                    <th scope="row" class="th_style">이메일주소</th>
                    <td class="td_style">${orderer.email}</td>
                    <th scope="row" class="th_style">휴대폰번호</th>
                    <td class="td_style">${orderer.tel}</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div>
              <h4 class="my_tit">결제정보</h4>
              <div>
                <ol class="pay_inform_ol">
                  <li class="pay_inform_li">
                    <ol>
                      <li class="li_top"><span>주문금액</span><strong>${formatNumber(orderDetail.total_price)}원</strong></li>
                      <li class="li_bottom"><span>상품금액</span><span>${formattedTotalAmount}</span></li>
                      <li class="li_bottom"><span>배송비</span><span>${formattedFee}</span></li>
                    </ol>
                  </li>
                  <li class="pay_inform_li">
                    <ol>
                      <li class="li_top li_hopstyle"><span>할인금액</span><strong>${formatNumber(orderDetail.use_point)}원</strong></li>
                      <li class="li_bottom"><span>적립금</span><span>${formatNumber(orderDetail.use_point)}원</span></li>
                    </ol>
                  </li>
                  <li class="pay_inform_li_toss">
                    <ol>
                      <li class="li_top" style="color: var(--primary)"><strong>총 결제금액</strong><strong>${formatNumber(orderDetail.amount)}원</strong></li>
                      <li class="li_bottom"><span>${orderDetail.paytype}</span><span>${formatNumber(orderDetail.amount)}원</span></li>
                      <li class="li_bottom"><span style="color: #a0a0a0">결제일시 ${orderDetail.payDT}</span></li>
                    </ol>
                  </li>
                </ol>
              </div>
            </div>`;

            // cancelData가 존재하면 환불 정보 추가
            if (cancelData && Object.keys(cancelData).length > 0) {
            orderHTML += `
                <div>
                  <h4 class="my_tit">환불정보</h4>
                  <div>
                    <ol class="pay_inform_ol">
                      <li class="pay_inform_li">
                        <ol>
                          <li class="li_top"><span>환불금액</span><strong>${formatNumber(cancelData.cancelAmount+cancelData.refundUsePoint)}원</strong></li>
                          <li class="li_bottom"><span>결제금액</span><span>${formatNumber(cancelData.cancelAmount)}원</span></li>
                          <li class="li_bottom"><span>적립금</span><span>${formatNumber(cancelData.refundUsePoint)}원</span></li>
                        </ol>
                      </li>
                      <li class="pay_inform_li">
                        <ol>
                          <li class="li_top li_hopstyle"><span>반환금액</span><strong>${formatNumber(cancelData.refundUsePoint)}원</strong></li>
                          <li class="li_bottom"><span>적립금</span><span>${formatNumber(cancelData.refundUsePoint)}원</span></li>
                        </ol>
                      </li>
                      <li class="pay_inform_li_toss">
                        <ol>
                          <li class="li_top" style="color: #f16731"><strong>총 환불금액</strong><strong>${formatNumber(cancelData.cancelAmount)}원</strong></li>
                          <li class="li_bottom"><span>${orderDetail.paytype}</span><span>${formatNumber(cancelData.cancelAmount)}원</span></li>
                          <li class="li_bottom"><span style="color: #a0a0a0">취소일시 ${cancelData.cancelDT}</span></li>
                        </ol>
                      </li>
                    </ol>
                  </div>
                </div>
                `;
            }

            orderHTML += `
              <div style="margin-bottom: 50px;">
                  <div>
                    <h4 class="my_tit">배송지정보</h4>
                  </div>
                  <table class="delivery_inform">
                    <colgroup>
                      <col width="140" />
                      <col />
                      <col width="140" />
                      <col />
                    </colgroup>
                    <tbody>
                      <tr>
                        <th scope="row" class="th_style">받는사람</th>
                        <td colspan="3" class="td_style">${orderDetail.recipient}</td>
                      </tr>
                      <tr>
                        <th scope="row" class="th_style">휴대폰번호</th>
                        <td class="td_style">${orderDetail.tel}</td>
                        <th scope="row" class="th_style">전화번호</th>
                        <td class="td_style">-</td>

                      </tr>
                      <tr>
                        <th scope="row" class="th_style">주소</th>
                        <td colspan="3" class="td_style">[${orderDetail.zipcode}] ${orderDetail.addr} ${orderDetail.addrdetail}</td>
                      </tr>
                      <tr>
                        <th scope="row" class="th_style">배송요청사항</th>
                        <td colspan="3" class="td_style">${orderDetail.request_memo}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            `;
    orderContainer.append(orderHTML);
}

function getOrderStateText(orderState) {
    switch(orderState) {
        case 1:
            return "결제완료";
        case 2:
            return "상품준비중";
        case 3:
            return "배송시작";
        case 4:
            return "배송중";
        case 5:
            return "배송완료";
        case 6:
            return "구매확정";
        case 7:
            return "전체취소 완료";
        case 8:
            return "부분취소 완료";
        case 9:
            return "교환접수 완료";
        case 10:
            return "교환 처리중";
        case 11:
            return "교환 처리완료";
        case 12:
            return "환불접수 완료";
        case 13:
            return "환불 처리중";
        case 14:
            return "환불 처리완료";
        default:
            return "상태오류";
    }
}

$(document).on('click', '#payCancel_btn', function() {
    var order_idx= $(this).closest(".order_detail_all").find('#order_idx').val();
    console.log("order_idx",order_idx);

    $.ajax({
        url: '/order/cancel_basicInfo',
        type: 'POST',
       data: JSON.stringify({
                order_idx: order_idx
        }),
        contentType: 'application/json',
        headers: {
                "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
        },
        success: function(response) {
            location.href = `/order/mypage_cancel1?order_idx=${order_idx}`;
        },
        error: function(error) {
            console.log('취소 페이지 이동중 에러 발생:', error);
        }
    });
});

// 구매확정
$(document).on('click', '.orderConfirm', function() {
    var pro_idx= $(this).closest(".order_data_list_one").find('#pro_idx').val();
    console.log("pro_idx",pro_idx);
    var order_idx= $(this).closest(".order_detail_all").find('#order_idx').val();
        console.log("order_idx",order_idx);

    if(confirm("구매확정을 하시면 교환/환불이 불가능합니다.\n정말 선택한 상품을 구매확정 하시겠습니까?")){
        $.ajax({
            url: '/order/orderConfirmOk',
            type: 'POST',
           data: JSON.stringify({
                    pro_idx: pro_idx,
                    order_idx:order_idx
            }),
            contentType: 'application/json',
            headers: {
                    "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
            },
            success: function(response) {
                alert("선택한 상품이 구매확정 되었습니다!");
                location.href = `/user/mypage_order_detail/${order_idx}`;
            },
            error: function(error) {
                console.log('구매확정 중 에러 발생:', error);
            }
        });
    }
});

// 운송장 조회
$(document).on('click', '#deli_tracking', function() {
    var trackingNum = $(this).text();
    console.log("trackingNum", trackingNum);

    var width = 486;
    var height = 780;
    var left = (screen.width - width) / 2;
    var top = (screen.height - height) / 2;

    // SweetTracker 템플릿 URL에 직접 쿼리스트링으로 파라미터 전달
    var trackingWindow = window.open(
        "https://info.sweettracker.co.kr/tracking/4?t_key=A1BfrxML1uWBvVcj1iVhWg&t_code=04&t_invoice=" + trackingNum,
        "배송 조회 결과",
        "width=" + width + ", height=" + height + ", top=" + top + ", left=" + left
    );
});