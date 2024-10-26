// 신고 모달창 닫기
$(document).on('click', '.fa-x, .modal_exit', function() {
    $(".reportModal_body").remove();
    $("input[type='radio']").prop("checked", false);
    selectedType = 0;
});

var tag=``;

// 리뷰 신고 클릭시 모달창 보이게
$(document).on('click', '.emergency_icon', function() {
    $(".reportModal_body").remove();
    // 1. 신고대상 id, 내용 가져오기
    let review_idx = $(this).closest(".review-item").data("idx");
    console.log("review_idx",review_idx);

    $.ajax({
        url: '/reviewReportInfo',
        type: 'POST',
        data: { review_idx: review_idx },
        success: function(response) {
            tag=``;
            tag+=`<div class="reportModal_body" data-idx="${response.reportedData.idx}">
                    <div class="reportModal_background"></div>
                    <div class="reportModal_container">
                      <div class="reportModal_layer">
                        <div class="reportModal_top">
                          <p>작성 글 신고하기</p>
                          <button><i class="fa-solid fa-x"></i></button>
                        </div>
                        <div class="reportModal_bottom">
                          <strong>신고대상 ID 및 내용</strong>
                          <div class="report_info">
                            <span>${response.reportedData.userid}</span>
                            <p>
                              ${response.reportedData.content}
                            </p>
                          </div>
                          <strong>신고사유</strong>
                          <ul class="report_reason_ul">
                            <li>
                              <input type="radio" id="not_relevant_img" value="1" />
                              <label for="not_relevant_img">관련없는 이미지</label>
                            </li>
                            <li>
                              <input type="radio" id="not_relevant_content" value="2" />
                              <label for="not_relevant_content">관련없는 내용</label>
                            </li>
                            <li>
                              <input type="radio" id="abuse" value="3" />
                              <label for="abuse">욕설/비방</label>
                            </li>
                            <li>
                              <input type="radio" id="PromotionalPost" value="4" />
                              <label for="PromotionalPost">광고/홍보글</label>
                            </li>
                            <li>
                              <input type="radio" id="perPersonal_info_leak" value="5" />
                              <label for="perPersonal_info_leak">개인정보 유출</label>
                            </li>
                            <li>
                              <input type="radio" id="over_post" value="6" />
                              <label for="over_post">게시글 도배</label>
                            </li>
                            <li>
                              <input type="radio" id="adult" value="7" />
                              <label for="adult">음란/선정성</label>
                            </li>
                            <li>
                              <input type="radio" id="etc" value="8" />
                              <label for="etc">기타</label>
                            </li>
                          </ul>
                          <div class="report_reason_text">
                            <textarea placeholder="이 외 사유를 적어주세요.(100자까지 작성가능)" maxlength="100" class="report_reason"></textarea>
                          </div>
                          <div class="report_reason_bottom">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="-4 -4 38 38" id="icon_exclamation_30x30_gray" x="740" y="77">
                              <path
                                fill="#888"
                                fill-rule="evenodd"
                                d="M15 30C6.716 30 0 23.284 0 15 0 6.716 6.716 0 15 0c8.284 0 15 6.716 15 15 0 8.284-6.716 15-15 15zm0-28.72C7.423 1.28 1.28 7.423 1.28 15c0 7.577 6.143 13.72 13.72 13.72 7.577 0 13.72-6.143 13.72-13.72 0-7.577-6.143-13.72-13.72-13.72zM14 19h2v2h-2v-2zm0-10h2v8h-2V9z"
                              />
                            </svg>
                            <p>신고해주신 내용은 관리자 검토 후 내부정책에 의거 조치가 진행됩니다.</p>
                          </div>
                          <div class="report_btn_div">
                            <button class="modal_exit">취소</button>
                            <button class="report_input">신고</button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
            `;
            $("body").append(tag);
        },
        error: function(error) {
            console.log('리뷰 신고 데이터 불러오는 중 오류 발생:', error);
        }
    });
})

let selectedType=0;
// 라디오버튼 스타일
$(document).on('change', 'input[type="radio"]', function() {
  // 모든 라벨에서 checkedLabel 클래스 제거
    $(".report_reason_ul li label").removeClass("checkedLabel");

  // 선택된 라디오 버튼의 라벨에 checkedLabel 클래스 추가
    $(this).next("label").addClass("checkedLabel");
    selectedType = $(this).val();
    console.log("Selected Radio Button Value on Change:", selectedType);
});

const token = localStorage.getItem("token");

// 리뷰 신고하기
$(document).on('click', '.report_input', function() {
    let review_idx = $(this).closest(".reportModal_body").data("idx");
    let reportReason = $(this).closest(".reportModal_body").find(".report_reason").val();

    if (selectedType==0) {
            alert("신고 사유를 선택해 주세요.");
            return; // 이후 코드 실행을 막음
    }

    console.log("review_idx",review_idx);
    console.log("selectedType", selectedType);
    console.log("reportReason", reportReason);

    var data = {
            review_idx : review_idx,
            report_type : selectedType,
            reason : reportReason
    }


    $.ajax({
        url: "/reviewReportOk",
        type: "POST",
        contentType: 'application/json',
        data: JSON.stringify(data),
        headers: {
            "Authorization": `Bearer ${token}`
        },
        success: function(response) {
            alert("신고가 접수되었습니다.");
            $(".reportModal_body").remove();
        },
        error: function(jqXHR, textStatus, error) {
            if (jqXHR.status === 409) {
                alert("이미 신고된 글입니다.");
            }else{
                console.log('리뷰 신고 접수중 에러 발생', error);
            }
        }
    });



});




