function createPaginationQna(totalPages, currentPage) {
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
        getQnAList(page);
    });
}

function getHandelType(type) {
    switch(type) {
        case 0:
            return "미완료";
        case 1:
            return "답변완료";
        default:
            return "타입값 오류";
    }
}

function getQnAType(type) {
    switch(type) {
        case 0:
            return "배송문의";
        case 1:
            return "주문문의";
        case 2:
            return "주문문의";
        case 3:
            return "취소문의";
        case 4:
            return "반품문의";
        case 5:
            return "교환문의";
        case 6:
            return "환불문의";
        case 7:
            return "회원정보문의";
        case 8:
            return "회원제도문의";
        case 9:
            return "결제방법문의";
        case 10:
            return "상품문의";
        case 11:
            return "기타문의";
        default:
            return "타입값 오류";
    }
}


function getQnAList(page){
    const pageSize = 5; //한 페이지에 보여줄 항목 수
    $(".myqna_write_list_ul").empty();
    $.ajax({
        url: '/user/getQnAList',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ page: page, pageSize: pageSize }),
        headers: {
            "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
        },
        success: function(response) {
            console.log(response);
            const qnAList = response.qnAList;
            const totalPages = response.totalPages;

            if (qnAList && qnAList.length > 0) {
                qnAList.forEach(qnAList => {
                    const qnaTypeText = getQnAType(qnAList.qnatype);
                    const handleStateText = getHandelType(qnAList.handleState);

                    $(".myqna_write_list_ul").append(`
                        <li class="myqna_write_list" data-qna-idx="${qnAList.idx}">
                          <div class="qna_type">
                            <span>${qnaTypeText}</span>
                          </div>
                          <div class="qna_title">
                              <span>${qnAList.title}</span>
                          </div>
                          <div class="qna_regDT">
                            <span>${qnAList.qnADT}</span>
                          </div>
                          <div class="qna_replyState">
                            <span>${handleStateText}</span>
                          </div>
                          <div class="qna_delBtn_div">
                            <button class="qna_delBtn">삭제</button>
                          </div>
                        </li>
                    `);
                });
            createPaginationQna(totalPages, page);
            }else{
                $(".myqna_list").empty();
                $(".myqna_list").append(`
                    <div class="order_list_none">
                        <p style="margin-top:100px">문의 내역이 없습니다.</p>
                    </div>
                `);
            }
        },
        error: function(error) {
            console.log('문의사항 내역 데이터 불러오는 중 에러 발생', error);
        }
    });
}

//디테일
$(document).on('click', '.myqna_write_list', function(e) {
    if ($(e.target).hasClass('qna_delBtn')) {
        return;
    }
    const qna_idx = $(this).data('qna-idx');
    // 이미 상세 정보가 열려 있는 경우에는 닫기
    if ($(this).next('.qna_info_div').length > 0) {
        $(this).next('.qna_info_div').toggle();
        return;
    }
    console.log(qna_idx);

    $.ajax({
        url: '/user/getQnADetail', // API 엔드포인트
        type: 'POST',
        data: JSON.stringify({ qna_idx: qna_idx }), // QnA ID를 보냄
        contentType: 'application/json',
        headers: {
                    "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
        },
        success: function(response) {
            console.log(response);
            const qnaDetail = response.qnaDetail; // 서버에서 받은 상세 데이터

            let qnaInfo=``;
            qnaInfo += `
                <div class="qna_info_div">
                    <div class="qna_q">
                        <span>Q.</span>
                        <p>${qnaDetail.content}</p>
            `;

            // imgfile1이 있을 경우에만 이미지 태그를 추가
            if (qnaDetail.imgfile1) {
                qnaInfo += `<img src="http://192.168.1.180:8000/${qnaDetail.imgfile1}" />`;
            }
            qnaInfo += `
                    </div>
                    <div class="qna_a">
                        <span>A.</span>
            `;

            // reply가 있을 경우에만 답변 내용을 추가
            if (qnaDetail.reply) {
                qnaInfo += `<p>${qnaDetail.reply}</p>
                            <p>답변일시 : ${qnaDetail.replyDT}</p>`;
            }else{
                qnaInfo += `<p>아직 답변이 등록되지 않았습니다.<br>조금만 기다려주시면 빠르게 답변하겠습니다.</p>`;
            }
            qnaInfo += `
                    </div>
                </div>
            `;
            $(this).after(qnaInfo);
        }.bind(this),
        error: function(error) {
            console.error('문의사항 디테일 불러오는 중 에러 발생', error);
        }
    });
});

//삭제 버튼
$(document).on('click', '.qna_delBtn', function(e) {
    e.stopPropagation();
    const qna_idx = $(this).closest(".myqna_write_list").data('qna-idx');

    if (confirm("문의내역을 삭제하시겠습니까?")) {
        $.ajax({
            url: '/user/qnaDelete', // API 엔드포인트
            type: 'POST',
            data: JSON.stringify({ qna_idx: qna_idx }), // QnA ID를 보냄
            contentType: 'application/json',
            headers: {
                    "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
            },
            success: function(response) {
                alert("문의내역이 삭제되었습니다.");
                getQnAList(1);
            },
            error: function(error) {
                console.error('문의사항 삭제중 에러 발생', error);
            }
        });
    }
});

//삭제 버튼
$(document).on('click', '.qna_write_btn', function(e) {
    $.ajax({
        url: '/user/qnaWrite',
        type: 'POST',
        headers: {
                "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
        },
        success: function(response) {
            location.href="/qnaWirte";
        },
        error: function(error) {
            console.error('문의사항 글쓰기 이동중 에러 발생', error);
        }
    });
});


$(function(){
    getQnAList(1);
})