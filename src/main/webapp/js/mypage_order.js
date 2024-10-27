function formatNumber(number) {
    return number.toLocaleString();
}

function getOrderListAll(page){
    const pageSize = 5; //한 페이지에 보여줄 항목 수

    $.ajax({
        url: '/user/getOrderListAll',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ page: page, pageSize: pageSize }),
        headers: {
            "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
        },
        success: function(response) {
            displayOrderList(response.content);
            setupPagination(response.totalPages,response.page);
        },
        error: function(error) {
            console.log('Error:', error);
        }
    });
}

$(function(){
    getOrderListAll(1);
})

function displayOrderList(orderList) {
    const orderContainer = $(".order_list_bottom"); // 주문 목록이 출력될 컨테이너

    orderContainer.empty(); // 이전 목록을 지우고 새로 업데이트

    if (orderList.length === 0) {
        $(".mypage_order_list").empty();
        $(".mypage_order_list").append(`
            <div class="order_list_none">
                <p style="margin-top:100px">주문 내역이 없습니다.</p>
            </div>
        `);
        return;
    }

    // 주문 리스트 순회
    orderList.forEach(order => {
        // 결제완료 또는 부분취소 완료 상태인 상품이 있는지 확인
        const hasCancelableProduct = order.products.some(product => product.orderState === 1 || product.orderState === 8);

        const cancelButtonHTML = hasCancelableProduct
            ? `<button id="payCancel_btn">상품 결제 취소하기</button>`
            : ''; // 조건에 맞는 상품이 있으면 취소하기 버튼 추가
        const orderHTML = `
            <li class="order_list_li">
              <input type="hidden" id="order_idx" value="${order.order_idx}"/>
              <div class="order_date_num">
                <a href="/user/mypage_order_detail/${order.order_idx}"><span>주문일자  </span>${order.order_date}</a>
                <a href="/user/mypage_order_detail/${order.order_idx}"><span>주문번호  </span>${order.orderId}</a>
                ${cancelButtonHTML}
              </div>
              <ul>
                  ${order.products.map(product => {
                    const formattedAmount = formatNumber(product.pro_price) + "원";
                    // orderState에 따라 버튼 출력 여부 결정
                    const packageButton = product.orderState >= 3 && product.orderState <=6
                        ? `<button class="order_package">CJ대한통운 <span id="deli_tracking">${order.trackingNum}</span></button>`
                        : '';
                    const orderConfirmButton = product.orderState ==5
                        ? `<button class="order_package orderConfirm">구매확정 하기</button>`
                        : '';
                    return `
                    <li>
                      <ul class="order_data_ulStyle">
                        <input type="hidden" id="pro_idx" value="${product.pro_idx}"/>
                        <li class="order_data_list_one">
                          <a href="/user/mypage_order_detail/${order.order_idx}">
                            <div class="order_data_inform">
                              <div class="order_data_img"><img src="http://192.168.1.180:8000/${product.pro_image}"/></div>
                              <div style="padding: 5px 0;">
                                <p class="order_aniTitle">${product.pro_anititle}</p>
                                <p class="order_pro_name">${product.pro_title}</p>
                                <ul class="order_pro_option">
                                  <li>${formattedAmount}</li>
                                </ul>
                              </div>
                            </div>
                          </a>
                          <div class="order_delivery_fee_data">${product.order_pro_amount}</div>
                          <div class="order_state_div">
                            <div>
                              <div class="order_state_all">
                                <p class="order_state_data">${getOrderStateText(product.orderState)}</p>
                              </div>
                              <div>
                                  ${packageButton} <!-- 버튼 삽입 -->
                                  ${orderConfirmButton}
                              </div>
                            </div>
                          </div>
                        </li>
                      </ul>
                    </li>
                  `;}).join('')}
              </ul>
            </li>
        `;
        orderContainer.append(orderHTML);
    });
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

function setupPagination(totalPages, currentPage) {
    $(".custom-pagination").empty();

    // 이전 페이지 버튼 추가 (첫 페이지가 아닐 때만 표시)
    if (currentPage > 1) {
        $(".custom-pagination").append(`
            <span class="pageNumber_span">
                <span data-page="${currentPage - 1}" class="prev-page">&lt;</span>
            </span>
        `);
    }

    // 페이지 번호 버튼 생성
    for (let i = 1; i <= totalPages; i++) {
        $(".custom-pagination").append(`
            <span class="pageNumber_span">
                <span data-page="${i}">${i}</span>
            </span>
        `);
    }

    // 다음 페이지 버튼 추가 (마지막 페이지가 아닐 때만 표시)
    if (currentPage < totalPages) {
        $(".custom-pagination").append(`
            <span class="pageNumber_span">
                <span data-page="${currentPage + 1}" class="next-page">&gt;</span>
            </span>
        `);
    }

    // 현재 페이지에 'clicked' 클래스를 추가
    $(`.custom-pagination .pageNumber_span span[data-page='${currentPage}']`).addClass("clicked");

    // 페이지네이션 버튼 클릭 이벤트 설정
    $(".custom-pagination .pageNumber_span span").on('click', function() {
        const page = $(this).data('page');
        $(".custom-pagination .pageNumber_span span").removeClass("clicked");
        $(this).addClass("clicked");
        getOrderListAll(page);
    });
}

//결제취소
$(document).on('click', '#payCancel_btn', function() {
    var order_idx= $(this).closest(".order_list_li").find('#order_idx').val();
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
    var pro_idx= $(this).closest(".order_data_ulStyle").find('#pro_idx').val();
    console.log("pro_idx",pro_idx);
    var order_idx= $(this).closest(".order_list_li").find('#order_idx').val();
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
                getOrderListAll(1);
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