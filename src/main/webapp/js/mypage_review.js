// 리뷰 탭메뉴 움직이기
function reviewTab(tabId) {
    // 모든 탭을 숨김
    document.getElementById("tab1").classList.remove("on");
    document.getElementById("tab1").classList.add("off");
    document.getElementById("tab2").classList.remove("on");
    document.getElementById("tab2").classList.add("off");

    // 클릭한 탭만 보여줌
    document.getElementById(tabId).classList.remove("off");
    document.getElementById(tabId).classList.add("on");
}

var tag = ``;

// 리뷰 모달창 삭제(닫기)
function review_write_exit(){
    $(".review_modal_body").remove();
    deletedFiles = [];  // 삭제된 파일 목록 초기화
    allFiles = [];  // 업로드된 파일 목록 초기화
}

// 리뷰 작성 전 데이터 전역변수 선언
var reviewbeforeData;
var reviewCompletedData;
var allFiles = [];

//리뷰 작성 버튼 누르면 나오는 모달
function review_writeModal(index){
    tag = ``;
    const reviewbefore = reviewbeforeData[index];
    tag += `<div class="review_modal_body">
              <div class="review_modal_div">
                <button class="exit_btn" type="button" onclick="review_write_exit()">
                  <i class="fa-solid fa-xmark"></i>
                </button>
                <div class="modal_div_size">
                  <div class="review_write_modal">
                    <header class="review_modal_header">
                      <h3>리뷰 작성</h3>
                    </header>
                    <div class="modal_write_ele">
                      <div class="modal_write_ele_top">
                        <div class="review_modal_proinfo">
                          <img
                            src=""
                          />
                          <div class="review_modal_protitle">
                          </div>
                        </div>
                        <div class="review_modal_grade">
                          <span>상품은 어떠셨나요? </span>
                          <div class="review_modal_grade_check">
                            <span value="1"><i class="fa-regular fa-star"></i></span>
                            <span value="2"><i class="fa-regular fa-star"></i></span>
                            <span value="3"><i class="fa-regular fa-star"></i></span>
                            <span value="4"><i class="fa-regular fa-star"></i></span>
                            <span value="5"><i class="fa-regular fa-star"></i></span>
                          </div>
                          <input type="hidden" name="grade" id="grade" value="0"/>
                          <input type="hidden" name="orderList_idx" id="orderList_idx" value="${reviewbefore.orderList_idx}"/>
                        </div>
                        <div class="review_modal_file">
                          <div>
                            <p>이미지는 최대 2개까지 첨부가 가능합니다.</p>
                            <div>
                                <div id="fileimg_preview" class="fileimg_preview"></div>
                                <label for="fileInput" class="review_modal_file_btn">이미지 업로드</label>
                                <input
                                  type="file"
                                  id="fileInput"
                                  style="display:none;"
                                />
                            </div>
                          </div>
                        </div>
                        <div class="review_modal_txt_div">
                          <div class="review_modal_txt">
                            <textarea name="content" id="content"
                              placeholder="최소 15자 이상 작성해주세요."
                            ></textarea>
                          </div>
                        </div>
                        <ul class="review_modal_notice">
                          <li>
                            상품과 무관하거나 비속어가 포함된 리뷰, 그 외 리뷰
                            운영정책에 위배되는 리뷰는 고지 없이 블라인드 후 경고
                            조치됩니다.
                          </li>
                          <li>경고 누적 시 리뷰 작성이 제한될 수 있습니다.</li>
                        </ul>
                      </div>
                      <div class="modal_write_ele_bottom">
                        <button class="review_write_btn" type="button">등록하기</button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>`;
    $("body").append(tag);

    $(".review_modal_proinfo img").attr("src","http://192.168.1.180:8000/" + reviewbefore.thumImg);
    console.log(reviewbefore.detail_img);
    $(".review_modal_protitle").html(`
            <p>${reviewbefore.title}</p>
            <p>${reviewbefore.price}원 / 수량 ${reviewbefore.amount}개</p>
    `);
};

//리뷰 수정 버튼 누르면 나오는 모달
function review_EditModal(index){
    tag = ``;
    const reviewCompleted = reviewCompletedData[index];
    let starHtml = '';
    let fileCount = 0; // 서버에서 받아온 파일 갯수를 카운트하기 위한 변수
    allFiles = []; // 배열 초기화

    // grade 값만큼 채워진 별 추가
    for (let i = 1; i <= reviewCompleted.grade; i++) {
        starHtml += '<span><i class="fa-solid fa-star"></i></span>';
    }
    // 나머지 빈 별 추가 (5 - grade)
    for (let i = 1; i <= 5-reviewCompleted.grade; i++) {
        starHtml += '<span><i class="fa-regular fa-star"></i></span>';
    }

    // 서버에서 받아온 파일 처리
        if (reviewCompleted.imgfile1) {
            allFiles.push(new File([], reviewCompleted.imgfile1));
            fileCount++;
        }
        if (reviewCompleted.imgfile2) {
            allFiles.push(new File([], reviewCompleted.imgfile2));
            fileCount++;
        }

        console.log("fileCount",fileCount);

    // 이미지 파일 있으면 보여주기
    let filePreviewHtml = '';
    if (reviewCompleted.imgfile1) {
        filePreviewHtml += `<div class="image-container"><img src="http://192.168.1.180:8000/${reviewCompleted.imgfile1}" style="width: 76px; height: 76px; margin-right:10px; position:relative"><button class="file-delete-btn">X</button></div>`;
    }
    if (reviewCompleted.imgfile2) {
        filePreviewHtml += `<div class="image-container"><img src="http://192.168.1.180:8000/${reviewCompleted.imgfile2}" style="width: 76px; height: 76px;  margin-right:10px; position:relative"><button class="file-delete-btn">X</button></div>`;
    }

    tag += `<div class="review_modal_body">
              <div class="review_modal_div">
                <button class="exit_btn" type="button" onclick="review_write_exit()">
                  <i class="fa-solid fa-xmark"></i>
                </button>
                <div class="modal_div_size">
                  <div class="review_write_modal">
                    <header class="review_modal_header">
                      <h3>리뷰 수정</h3>
                    </header>
                    <div class="modal_write_ele">
                      <div class="modal_write_ele_top">
                        <div class="review_modal_proinfo">
                          <img
                            src=""
                          />
                          <div class="review_modal_protitle">
                          </div>
                        </div>
                        <div class="review_modal_grade">
                          <span>상품은 어떠셨나요? </span>
                          <div class="review_modal_grade_check">
                            ${starHtml}
                          </div>
                          <input type="hidden" name="grade" id="grade" value="${reviewCompleted.grade}"/>
                          <input type="hidden" name="orderList_idx" id="orderList_idx" value="${reviewCompleted.orderList_idx}"/>
                        </div>
                        <div class="review_modal_file">
                          <div>
                            <p>이미지는 최대 2개까지 첨부가 가능합니다.</p>
                            <div>
                                <div id="fileimg_preview" class="fileimg_preview">
                                    ${filePreviewHtml}
                                </div>
                                <label for="fileInput" class="review_modal_file_btn">이미지 업로드</label>
                                <input
                                  type="file"
                                  id="fileInput"
                                  style="display:none;"
                                />
                            </div>
                          </div>
                        </div>
                        <div class="review_modal_txt_div">
                          <div class="review_modal_txt">
                            <textarea name="content" id="content"
                              placeholder="최소 15자 이상 작성해주세요."
                            ></textarea>
                          </div>
                        </div>
                        <ul class="review_modal_notice">
                          <li>
                            상품과 무관하거나 비속어가 포함된 리뷰, 그 외 리뷰
                            운영정책에 위배되는 리뷰는 고지 없이 블라인드 후 경고
                            조치됩니다.
                          </li>
                          <li>경고 누적 시 리뷰 작성이 제한될 수 있습니다.</li>
                        </ul>
                      </div>
                      <div class="modal_write_ele_bottom">
                        <button class="review_edit_btn" type="button">수정하기</button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>`;
    $("body").append(tag);

    $(".review_modal_proinfo img").attr("src","http://192.168.1.180:8000/" + reviewCompleted.pro_thumImg);
    $(".review_modal_protitle").html(`
            <p>${reviewCompleted.pro_title}</p>
            <p>${reviewCompleted.pro_price}원 / 수량 ${reviewCompleted.order_amount}개</p>
    `);
    $("#content").val(reviewCompleted.content);

    // 이미지가 2개면 + 버튼 숨김
    if (fileCount >= 2) {
        $('.review_modal_file_btn').hide();
    }
};

// jquery 시작 -> 제일 먼저 실행됨
$(function(){
    const token = localStorage.getItem("token");
    console.log(token);

    function reviewList(){
        $("#review_list_ul").empty();
        $("#review_list_ul2").empty();
        // 리뷰 작성전, 작성완료 데이터 리스트 불러오기
        $.ajax({
            url: '/user/reviewList',
            type: 'POST',
            contentType: 'application/json',
            headers: {
                "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
            },
            success: function(response) {
                mypageCommInfo();
                console.log(response.reviewBefore);
                console.log(response.reviewBeforeAmount);
                console.log(response.reviewCompleted);
                console.log(response.reviewCompletedAmount);

                reviewbeforeData = response.reviewBefore;
                reviewCompletedData = response.reviewCompleted;
                const reviewBeforeAmount = response.reviewBeforeAmount;
                const reviewCompletedAmount = response.reviewCompletedAmount;

                // 작성 가능한 리뷰 수 업데이트
                $('.beforeCountSpan').text(reviewBeforeAmount);
                // 내 리뷰 수 업데이트
                $('.afterCountSpan').text(reviewCompletedAmount);

                // 리뷰 작성 전 for문
                response.reviewBefore.forEach(function(reviewbefore, index){
                    $("#review_list_ul").append(`
                        <li class="review_list_li">
                           <input type="hidden" name="orderList_idx" id="orderList_idx" value="${reviewbefore.orderList_idx}">
                           <div class="review_list_li_one">
                             <div class="review_list_li_one_detail">
                               <div class="review_product">
                                 <a href="">
                                   <img src="http://192.168.1.180:8000/${reviewbefore.thumImg}" class="review_product_img" />
                                 </a>
                                 <div class="review_product_inform">
                                   <a href="">
                                     <strong>${reviewbefore.title}</strong>
                                     <p>${reviewbefore.price}원 / 수량 ${reviewbefore.amount}개</p>
                                   </a>
                                 </div>
                               </div>
                               <span class="order_state_date"><span>구매확정</span></span>
                               <div><button class="review_write_modal_btn" type="button" onclick="review_writeModal(${index})">리뷰쓰기</button></div>
                             </div>
                           </div>
                         </li>
                    `)
                })// 리뷰 작성 전 for문 종료
                // 작성된 리뷰 for문
                response.reviewCompleted.forEach(function(reviewCompleted, index){
                    let starHtml = '';

                    // grade 값만큼 채워진 별 추가
                    for (let i = 0; i < reviewCompleted.grade; i++) {
                        starHtml += '<i class="fa-solid fa-star"></i>';
                    }
                    // 나머지 빈 별 추가 (5 - grade)
                    for (let i = reviewCompleted.grade; i < 5; i++) {
                        starHtml += '<i class="fa-regular fa-star"></i>';
                    }

                    $("#review_list_ul2").append(`
                    <li class="user_review_list_li">
                      <input type="hidden" name="orderList_idx" id="orderList_idx" value="${reviewCompleted.orderList_idx}">
                      <div class="review_write_list">
                        <a href="">
                          <img src="http://192.168.1.180:8000/${reviewCompleted.pro_thumImg}" class="css-1d5qj71 egc1z4c3" />
                          <div class="review_write_list_inform">
                            <strong>${reviewCompleted.pro_title}</strong>
                            <div>
                              ${starHtml}
                            </div>
                          </div>
                        </a>
                        <div class="review_txt">
                          <p><span>${reviewCompleted.content}</span></p>
                        </div>
                        ${reviewCompleted.imgfile1 || reviewCompleted.imgfile2 ? `
                        <div class="review_img">
                          ${reviewCompleted.imgfile1 ? `<img src="http://192.168.1.180:8000/${reviewCompleted.imgfile1}" />` : ''}
                          ${reviewCompleted.imgfile2 ? `<img src="http://192.168.1.180:8000/${reviewCompleted.imgfile2}" />` : ''}
                        </div>` : ''}
                      </div>
                      <div class="review_regDT">
                        <p>${reviewCompleted.formatted_regDT}</p>
                      </div>
                      <div class="review_modi_del_btn">
                        <div>
                          <button type="button" onclick="review_EditModal(${index})">수정</button>
                          <button type="button" id="review_detBtn">삭제</button>
                        </div>
                      </div>
                    </li>
                    `)
                })// 작성된 리뷰 for문 종료
            },
            error: function(xhr, status, error) {
                console.log('오류 발생: ' + error);  // 에러 메시지 자체 출력
            }
        });
    }
    reviewList();


    // reviewTab 함수에 tabId 전달
    reviewTab("tab1");  // tab1을 기본값으로 설정하여 호출

    // 리뷰 작성 모달 닫기
    review_write_exit();

    // 리뷰 별점 표시하기
    $(document).on('click', '.review_modal_grade_check span', function(){
        $(this).children('i').addClass('fa-solid').removeClass('fa-regular');
        $(this).prevAll('span').children('i').addClass('fa-solid').removeClass('fa-regular');

        // 현재 클릭한 별 이후 모든 별에 fa-regular 클래스를 추가
        $(this).nextAll('span').children('i').removeClass('fa-solid').addClass('fa-regular');

        // fa-solid 클래스를 가지는 i 요소의 개수 카운트
        const gradeCount = $(".review_modal_grade_check i.fa-solid").length;
        console.log("fa-solid 클래스를 가진 i 태그의 개수: ", gradeCount);

        $("#grade").val(gradeCount);
    });

    // 파일 추가 시
    $(document).on('change', '#fileInput', function(event) {
        var files = event.target.files;
        var preview = $('#fileimg_preview');
        var fileCount = $('#fileimg_preview .image-container').length; // DOM에서 이미지 개수 확인

        $.each(files, function(i, file) {
            if (file.type.startsWith('image/') && fileCount < 2) {
                var reader = new FileReader();
                reader.onload = function(e) {
                    var imgContainer = $('<div class="image-container"></div>');
                    var img = $('<img>').attr('src', e.target.result).css({
                        width: '76px',
                        height: '76px',
                        marginRight: '10px',
                        position: 'relative'
                    });
                    var deleteBtn = $('<button class="file-delete-btn">X</button>');
                    imgContainer.append(img).append(deleteBtn);
                    preview.append(imgContainer);
                    fileCount++;
                    allFiles.push(file);

                    if (fileCount >= 2) {
                        $('#fileInput').hide();
                        $('.review_modal_file_btn').hide();
                    }
                };
                reader.readAsDataURL(file);
            }
        });

        $(this).val('');
    });

    // 삭제된 파일을 담을 배열 선언
    let deletedFiles = [];

    // 이미지 삭제 시
    $(document).on('click', '.file-delete-btn', function() {
        const index = $(this).parent().index();
        const deletedFile = allFiles[index].name; // 삭제된 파일 이름 추출
        deletedFiles.push(deletedFile); // 삭제된 파일 이름 저장
        allFiles.splice(index, 1); // allFiles 배열에서 파일 제거
        $(this).parent().remove(); // DOM에서 파일 삭제
        var fileCount = $('#fileimg_preview .image-container').length;

        // 이미지가 2개 미만이면 + 버튼 다시 보이게 하기
        if (fileCount < 2) {
//            $('#fileInput').show();
            $('.review_modal_file_btn').show();
        }
    });

    // 리뷰 등록
    $(document).on('click', '.review_write_btn', function() {
        var grade = $('#grade').val();
        var orderListIdx = $(this).closest(".review_modal_body").find('#orderList_idx').val();
        var content = $('#content').val();
        const minLength = 15;
        const currentLength = $("#content").val().length;

        console.log("grade : ", grade);
        console.log("orderListIdx : ", orderListIdx);
        console.log("content : ", content);
        console.log("alldata", allFiles);

        // 별점 입력X 제한
        if(grade==0){
            alert("별점을 입력해주세요.");
            return false;
        }

        // textarea 글자수 제한
        if (currentLength < minLength) {
            alert("최소 15자 이상 입력해주세요.");
            return false;
        }

        var formData = new FormData();
        formData.append('grade', grade);
        formData.append('orderList_idx', orderListIdx);
        formData.append('content', content);

        for (var i = 0; i < allFiles.length; i++) {
        formData.append('file', allFiles[i]);
        }

        for (var pair of formData.entries()) {
            console.log(pair[0] + ', ' + pair[1]);
        }


        $.ajax({
            url: '/user/reviewWriteOK',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            headers: {
                        "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
            },
            success: function(response) {
                alert("리뷰가 성공적으로 등록되었습니다.");
                review_write_exit();
                reviewList();
            },
            error: function(error) {
                console.log('리뷰 등록 중 오류 발생:', error);
            }
        });
    });//리뷰등록 끝

    //리뷰 수정
    $(document).on('click', '.review_edit_btn', function() {
        var grade = $('#grade').val();
        var orderListIdx = $(this).closest(".review_modal_body").find('#orderList_idx').val();
        var content = $('#content').val();
        const minLength = 15;
        const currentLength = $("#content").val().length;

        console.log("grade : ", grade);
        console.log("orderListIdx : ", orderListIdx);
        console.log("content : ", content);
        console.log("alldata : ", allFiles);

        // textarea 글자수 제한
        if (currentLength < minLength) {
            alert("최소 15자 이상 입력해주세요.");
            return false;
        }

        var formData = new FormData();
        formData.append('grade', grade);
        formData.append('orderList_idx', orderListIdx);
        formData.append('content', content);

        for (var i = 0; i < allFiles.length; i++) {
        formData.append('file', allFiles[i]);
        }

        // 삭제된 파일 정보 추가
        formData.append('deletedFiles', JSON.stringify(deletedFiles));

        for (var pair of formData.entries()) {
            console.log(pair[0] + ', ' + pair[1]);
        }


        $.ajax({
            url: '/user/reviewEditOK',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            headers: {
                        "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
            },
            success: function(response) {
                alert("리뷰가 성공적으로 수정되었습니다.");
                review_write_exit();
                reviewList();
            },
            error: function(error) {
                console.log('리뷰 수정 중 오류 발생:', error);
            }
        });
    });

    //리뷰삭제
    $(document).on('click', '#review_detBtn', function() {
        if(confirm('이 리뷰를 삭제하시겠습니까?')){
            var orderListIdx = $(this).closest("li").find('#orderList_idx').val();
            console.log(orderListIdx);

            $.ajax({
                url: '/user/reviewDelOK',
                type: 'POST',
                data: { orderList_idx: orderListIdx },
                headers: {
                            "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
                },
                success: function(response) {
                    alert("리뷰가 성공적으로 삭제되었습니다.");
                    reviewList();
                },
                error: function(error) {
                    console.log('리뷰 삭제 중 오류 발생:', error);
                }
            });
        }
    })



}); // ajax 끝

