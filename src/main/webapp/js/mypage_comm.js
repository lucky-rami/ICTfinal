function createPaginationComm(totalPages, currentPage) {
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
        getCommList(page);
    });
}

function getCommType(type) {
    switch(type) {
        case 10:
            return "자랑";
        case 20:
            return "덕질";
        case 30:
            return "친목";
        case 40:
            return "팬아트";
        case 50:
            return "추천";
        default:
            return "타입값 오류";
    }
}

function getCommList(page){
    const pageSize = 5; //한 페이지에 보여줄 항목 수
    $(".mycm_write_list_ul").empty();
    $.ajax({
        url: '/user/getCommList',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ page: page, pageSize: pageSize }),
        headers: {
            "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
        },
        success: function(response) {
            console.log(response);
            const cmList = response.cmList;
            const totalPages = response.totalPages;

            if (cmList && cmList.length > 0) {
                cmList.forEach(cmList => {
                    const commTypeText = getCommType(parseInt(cmList.commtype));

                    $(".mycm_write_list_ul").append(`
                        <li class="mycm_write_list" data-comm-idx="${cmList.idx}">
                          <div class="cm_type">
                            <span>${commTypeText}</span>
                          </div>
                          <div class="cm_title">
                            <a href="/cmView/${cmList.idx}">
                                <span>${cmList.title}</span>
                            </a>
                          </div>
                          <div class="cm_regDT">
                            <span>${cmList.commDT}</span>
                          </div>
                          <div class="cm_replyState">
                            <span>${cmList.hit}</span>
                          </div>
                          <div class="cm_delBtn_div">
                            <button class="cm_editBtn" onclick="location.href='/cmEdit/${cmList.idx}'">수정</button>
                            <button class="cm_delBtn">삭제</button>
                          </div>
                        </li>
                    `);
                });
            createPaginationComm(totalPages, page);
            }else{
                $(".mycm_list").empty();
                $(".mycm_list").append(`
                    <div class="order_list_none">
                        <p style="margin-top:100px">작성한 커뮤니티 글이 없습니다.</p>
                    </div>
                `);
            }
        },
        error: function(error) {
            console.log('커뮤니티 내역 데이터 불러오는 중 에러 발생', error);
        }
    });
}

//삭제 버튼
$(document).on('click', '.cm_delBtn', function(e) {
    const comm_idx = $(this).closest(".mycm_write_list").data('comm-idx');

    if (confirm("커뮤니티 글을 삭제하시겠습니까?")) {
        $.ajax({
            url: '/user/cmDelete', // API 엔드포인트
            type: 'POST',
            data: JSON.stringify({ comm_idx: comm_idx }), // QnA ID를 보냄
            contentType: 'application/json',
            headers: {
                    "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
            },
            success: function(response) {
                alert("커뮤니티 글이 삭제되었습니다.");
               getCommList(1);
            },
            error: function(error) {
                console.error('커뮤니티 글 삭제중 에러 발생', error);
            }
        });
    }
});

$(function(){
    getCommList(1);
})