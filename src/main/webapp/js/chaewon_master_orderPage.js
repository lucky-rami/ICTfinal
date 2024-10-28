let currentStartDate = null;
let currentEndDate = null;
let currentSearchType = null;
let currentSearchKeyword = null;

$(function(){
    // 날짜 필터 버튼 클릭 시
    $(".date-btn").on('click', function() {
        const filterType = $(this).data('date');
        let startDate, endDate;

        endDate = new Date().toISOString().split('T')[0];
        // 날짜 범위 설정
        switch (filterType) {
            case 'today':
                startDate = new Date().toISOString().split('T')[0];
                break;
            case 'week':
                startDate = new Date(new Date().setDate(new Date().getDate() - 7)).toISOString().split('T')[0];
                break;
            case 'month1':
                startDate = new Date(new Date().setMonth(new Date().getMonth() - 1)).toISOString().split('T')[0];
                break;
            case 'month3':
                startDate = new Date(new Date().setMonth(new Date().getMonth() - 3)).toISOString().split('T')[0];
                break;
            case 'month6':
                startDate = new Date(new Date().setMonth(new Date().getMonth() - 6)).toISOString().split('T')[0];
                break;
        }
        // 필터링 된 날짜를 전역 변수에 저장
        currentStartDate = startDate;
        currentEndDate = endDate;
        currentSearchType = null;
        currentSearchKeyword = null;

        // 필터링 된 날짜로 데이터 요청
        getOrderList(1, startDate, endDate);
    });

    // 검색 버튼 클릭 시
    $('#search-btn').on('click', function() {
        search_list()
    });

    function search_list(){
        const searchType = $('#search-type').val(); // 검색 유형 (주문번호 또는 주문자 아이디)
        const searchKeyword = $('#search-keyword').val(); // 검색어

        if (searchKeyword == "") {
            alert('검색어를 입력하세요.');
            return false;
        }

        currentSearchType = searchType;
        currentSearchKeyword = searchKeyword;
        currentStartDate = null;
        currentEndDate = null;


        getOrderList(1, null, null, searchType, searchKeyword); // 검색 값 전달
    }

    $("#search-keyword").keyup(function (event) {
        if (event.key == "Enter") {
            search_list();
          }
        });

    //주문 내역 데이터 가져오기(가장 기본:아무런 필터,검색 조건 없는 모든 데이터)
    getOrderList(1);
})

function formatNumber(number) {
    return number.toLocaleString();
}

function createPaginationOrder(totalPages, currentPage) {
    $(".pagination").empty();

    const pagesPerGroup = 5;
    const currentGroup = Math.ceil(currentPage / pagesPerGroup);
    const startPage = (currentGroup - 1) * pagesPerGroup + 1;
    const endPage = Math.min(currentGroup * pagesPerGroup, totalPages);

    // << 버튼 추가 (첫 그룹이 아닐 때만 표시)
    if (currentGroup > 1) {
        $(".pagination").append(`
            <li class="page-item"><a class="page-link" data-page="${startPage - 1}"><<</a></li>
        `);
    }

    // 이전 페이지 버튼 추가 (첫 페이지가 아닐 때만 표시)
    if (currentPage > 1) {
        $(".pagination").append(`
            <li class="page-item"><a class="page-link" data-page="${currentPage - 1}"><</a></li>
        `);
    }

    // 페이지 번호 버튼 생성
    for (let i = startPage; i <= endPage; i++) {
        $(".pagination").append(`
            <li class="page-item"><a class="page-link ${i === currentPage ? 'clicked' : ''}" data-page="${i}">${i}</a></li>
        `);
    }

    // 다음 페이지 버튼 추가 (마지막 페이지가 아닐 때만 표시)
    if (currentPage < totalPages) {
        $(".pagination").append(`
            <li class="page-item"><a class="page-link" data-page="${currentPage + 1}">></a></li>
        `);
    }

    // >> 버튼 추가 (마지막 그룹이 아닐 때만 표시)
    if (currentGroup < Math.ceil(totalPages / pagesPerGroup)) {
        $(".pagination").append(`
            <li class="page-item"><a class="page-link" data-page="${endPage + 1}">>></a></li>
        `);
    }

    // 현재 페이지에 'clicked' 클래스를 추가
    $(`.pagination .page-item a[data-page='${currentPage}']`).addClass("clicked");
    // 페이지네이션 버튼 클릭 이벤트 설정
    $(".pagination .page-item a").on('click', function() {
        const page = $(this).data('page');
        $(".pagination .page-item a").removeClass("clicked");
        $(this).addClass("clicked");

        // 기존에 필터된 날짜나 검색어가 있다면 유지, 없다면 전체 데이터 가져오기
        getOrderList(page, currentStartDate, currentEndDate, currentSearchType, currentSearchKeyword);
    });
}

function getOrderList(page, startDate = null, endDate = null, searchType = null, searchKeyword = null){
    const pageSize = 6; //한 페이지에 보여줄 항목 수
    $(".user_orderList").empty();

    let requestData = { page: page, pageSize: pageSize };

    // 필터링 값이 있으면 requestData에 추가
    if (startDate && endDate) {
        requestData.startDate = startDate;
        requestData.endDate = endDate;
    }

    // 검색 값이 있으면 추가
    if (searchType && searchKeyword) {
        requestData.searchType = searchType;
        requestData.searchKeyword = searchKeyword;
    }

    $.ajax({
        url: '/master/getOrderList',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(requestData),
        success: function(response) {
            const userOrderList = response.userOrderList;
            const totalPages = response.totalPages;

            console.log(userOrderList);

            if (userOrderList && userOrderList.length > 0) {
                userOrderList.forEach(userOrderList => {
                    const formattedAmount = formatNumber(userOrderList.amount); // 여기서 형식화된 금액을 변수로 저장
                    $(".user_orderList").append(`
                        <tr class="user_orderList_tr">
                            <td>${userOrderList.order_idx}</td>
                            <input type="hidden" id="order_idx" value="${userOrderList.order_idx}">
                            <td>${userOrderList.orderId}</td>
                            <td style="width:10%"><span class="orderName_td">${userOrderList.orderName}<span></td>
                            <td>${userOrderList.customerName}</td>
                            <td>${formattedAmount}</td>
                            <td>${userOrderList.order_date}</td>
                            <td>
                                <button class="btn btn-secondary" id="order_detail_btn">상세정보</button>
                            </td>
                        </tr>
                    `);
                });
                createPaginationOrder(totalPages, page);
            }else{
                $(".user_orderList").empty();
                $(".user_orderList").append(`
                    <tr>
                        <td colspan="7" style="text-align:center;vertical-align: middle;">주문 내역이 없습니다.</td>
                    </tr>
                `); // 기본 데이터가 없을 때를 처리

                $(".pagination").empty(); // 페이지네이션을 비움
            }
        },
        error: function(error) {
            console.log('Error:', error);
        }
    });
}

$(document).on('click', '#order_detail_btn', function () {
    var order_idx= $(this).closest(".user_orderList_tr").find('#order_idx').val();
    console.log(order_idx);

    location.href = '/master/orderDetail?order_idx=' + order_idx;
});

$(document).on('click', '#ListAll_btn', function() {
    currentStartDate = null;
    currentEndDate = null;
    currentSearchType = null;
    currentSearchKeyword = null;
    getOrderList(1);
});
