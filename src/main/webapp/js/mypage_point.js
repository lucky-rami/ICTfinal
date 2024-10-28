function formatNumber(number) {
    return number.toLocaleString();
}

function createPaginationPoint(totalPages, currentPage) {
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
        getPointList(page);
    });
}

function getPointList(page){
    const pageSize = 10; //한 페이지에 보여줄 항목 수
    $(".point_list_ul").empty();
    $.ajax({
        url: '/user/getPointList',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ page: page, pageSize: pageSize }),
        headers: {
            "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
        },
        success: function(response) {
            const pointList = response.pointList;
            const totalPages = response.totalPages;

            console.log(pointList);

            if (pointList && pointList.length > 0) {
                pointList.forEach(pointList => {
                    const formattedPoint = formatNumber(pointList.point); // 여기서 형식화된 금액을 변수로 저장
                    const getPointType = getPoinType(pointList.type);
                    const pointClass = pointList.type !=3
                        ? `<span style="color:var(--primary); font-weight: bold;">+${formattedPoint}</span>`
                        : `<span style="color:#a0a0a0; font-weight: bold;">${formattedPoint}</span>`;
                    $(".point_list_ul").append(`
                        <li class="point_list_li">
                          <div class="point_date"><span>${pointList.formatted_regDT}</span></div>
                          <div class="point_inform">
                            <div class="point_inform_detail">
                                <span>${getPointType}</span>
                                ${pointClass}
                            </div>
                          </div>
                        </li>
                    `);
                });
            createPaginationPoint(totalPages, page);
            }else{
                $(".point_list_all").empty();
                $(".point_list_all").append(`
                    <div class="order_list_none">
                        <p style="margin-top:100px">적립 내역이 없습니다.</p>
                    </div>
                `);
            }
        },
        error: function(error) {
            console.log('Error:', error);
        }
    });
}
function getPoinType(type) {
    switch(type) {
        case 1:
            return "상품 구매확정 적립";
        case 2:
            return "리뷰 작성 적립";
        case 3:
            return "상품 구매시 사용";
        case 4:
            return "상품 취소 적립금 환불";
        case 5:
            return "상품 환불로 인한 적립금 반환";
        default:
            return "타입값 오류";
    }
}


$(function(){
    getPointList(1);
})