// 굿즈 상품 테이블 대분류 중분류 내용 스크립트
// 대분류와 중분류의 매핑 정보
document.addEventListener('DOMContentLoaded', function() {
    const mainCategorySelect = document.getElementById('code');
    const subCategorySelect = document.getElementById('second_category');

    if (!mainCategorySelect || !subCategorySelect) {
        console.error('대분류 또는 중분류 select 요소가 존재하지 않습니다.');
        return; // 요소가 없을 경우 함수 종료
    }

    // 대분류가 변경될 때 중분류 업데이트
    mainCategorySelect.addEventListener('change', function() {
        const selectedValue = mainCategorySelect.value;

        // 중분류 초기화
        subCategorySelect.innerHTML = '<option value="">중분류 선택</option>'; // 기본 옵션 추가
        subCategorySelect.disabled = true; // 기본적으로 비활성화

        // 대분류에 따라 중분류 추가
        let subOptions = [];
        switch (selectedValue) {
            case '1': // 의류
                subOptions = [
                    { value: '10', text: '아우터' },
                    { value: '11', text: '상의' },
                    { value: '12', text: '하의' },
                    { value: '13', text: '잡화' }
                ];
                break;
            case '2': // 완구/취미
                subOptions = [
                    { value: '20', text: '아크릴' },
                    { value: '21', text: '피규어' },
                    { value: '22', text: '캔뱃지' },
                    { value: '23', text: '슬로건' },
                    { value: '24', text: '포스터' },
                    { value: '25', text: '기타' }
                ];
                break;
            case '3': // 문구/오피스
                subOptions = [
                    { value: '30', text: '필기류' },
                    { value: '31', text: '노트&메모지' },
                    { value: '32', text: '파일' },
                    { value: '33', text: '스티커' },
                    { value: '34', text: '달력' },
                    { value: '35', text: '기타' }
                ];
                break;
            case '4': // 생활용품
                subOptions = [
                    { value: '40', text: '컵&텀블러' },
                    { value: '41', text: '쿠션' },
                    { value: '42', text: '담요' },
                    { value: '43', text: '기타' }
                ];
                break;
            default:
                break;
        }

        // 중분류 옵션 추가
        if (subOptions.length > 0) {
            subOptions.forEach(option => {
                const newOption = document.createElement('option');
                newOption.value = option.value;
                newOption.textContent = option.text;
                subCategorySelect.appendChild(newOption);
            });
            subCategorySelect.disabled = false; // 중분류 활성화
        }
    });
});


// ---------------------------------------------------------
$(document).ready(function() {
    // 신고내역추가 버튼 클릭 시 이벤트
    $('.addReportBtn').click(function() {
        // 버튼에서 data-userid와 해당 행의 idx 가져오기
        const userid = $(this).data('userid');
        const idx = $(this).closest('tr').find('td:eq(1)').text();  // No 컬럼에서 idx 가져오기
        const commentIdx = $(this).data('comment-idx'); // 버튼에서 data-comment-idx 가져오기
        const reviewIdx = $(this).data('review-idx'); // 버튼에서 data-review-idx 가져오기
        const comunityIdx = $(this).data('comunity-idx'); // 버튼에서 data-comunity-idx 가져오기
        const reportType = $(this).data('report-type'); // 버튼에서 data-report-type 가져오기

        // 모달의 hidden input에 값 설정
        $('#userid').val(userid);
        $('#idx').val(idx);
        $('#comment_idx').val(commentIdx); // comment_idx 설정
        $('#review_idx').val(reviewIdx); // review_idx 설정
        $('#comunity_idx').val(comunityIdx); // comunity_idx 설정

        // report_type이 없거나 빈 경우 기본값을 설정
        if (reportType !== undefined && reportType !== "") {
            $('#report_type').val(reportType); // report_type 설정
        } else {
            $('#report_type').val(1); // 기본값 설정 (예: 1)
        }

        // 모달창 띄우기
        $('#reportModal').modal('show');
    });

    // 폼 제출 이벤트
    $('#reportForm').submit(function(event) {
        event.preventDefault(); // 폼의 기본 제출 동작 막기

        const token = localStorage.getItem('token'); // 로컬스토리지에서 토큰 가져오기
        if (!token) {
            alert("로그인 토큰이 없습니다.");
            return;
        }

        const formData = $(this).serialize(); // 폼 데이터 직렬화
        console.log("전송할 데이터:", formData); // 전송할 데이터 로그 확인

        $.ajax({
            type: 'POST',
            url: '/master/reportinguserOK',
            headers: {
                'Authorization': `Bearer ${token}` // Authorization 헤더에 토큰 추가
            },
            data: formData,
            success: function(response) {
                alert('신고가 성공적으로 처리되었습니다.');
                location.reload(); // 페이지 새로고침
            },
            error: function(xhr, status, error) {
                console.error("신고 처리 중 오류 발생:", error);
                console.log("서버 응답:", xhr.responseText); // 서버에서 반환된 오류 메시지 출력
                alert('신고 처리 중 오류가 발생했습니다.');
            }
        });
    });
});



$(document).ready(function() {
    // 리뷰 상세보기 버튼 클릭 시
    $(document).on('click', '.reviewDetailBtn', function(event) {
        event.preventDefault(); // 기본 링크 동작을 막음
        var reviewIdx = $(this).data('idx');  // 버튼에서 idx 값 가져오기

        // Ajax 요청으로 서버에서 리뷰 상세 정보를 가져옴
        $.ajax({
            type: 'GET',
            url: '/master/getReviewDetail',  // 리뷰 상세정보를 가져오는 URL
            data: { idx: reviewIdx },
            success: function(response) {
                if (response) {
                    // 기존 "리뷰 이미지 없음" 메시지 제거
                    $('#imgFile1').siblings('p').remove();
                    $('#imgFile2').siblings('p').remove();

                    // 이미지 파일이 존재할 경우만 이미지 경로 설정
                    if (response.imgfile1) {
                        const imgPath1 = "http://192.168.1.180:8000/" + encodeURIComponent(response.imgfile1);
                        $('#imgFile1').attr('src', imgPath1).show();
                    } else {
                        $('#imgFile1').hide();
                        $('#imgFile1').parent().append('<p>리뷰 이미지 없음</p>');
                    }

                    if (response.imgfile2) {
                        const imgPath2 = "http://192.168.1.180:8000/" + encodeURIComponent(response.imgfile2);
                        $('#imgFile2').attr('src', imgPath2).show();
                    } else {
                        $('#imgFile2').hide();
                        $('#imgFile2').parent().append('<p>리뷰 이미지 없음</p>');
                    }

                    // 서버로부터 받은 데이터를 모달에 표시
                    $('#orderListIdx').text(response.order_idx);

                    // 모달 창 띄우기
                    $('#detailModal').modal('show');
                } else {
                    alert('리뷰 상세 정보를 불러오지 못했습니다.');
                }
            },
            error: function() {
                alert('리뷰 상세 정보를 가져오는 데 실패했습니다.');
            }
        });
    });
});






$(document).ready(function() {
    // 답변 버튼 클릭 시
    $('.answerBtn').on('click', function() {
        const idx = $(this).data('idx');  // 문의 ID
        const title = $(this).data('title');  // 문의 제목
        const content = $(this).data('content');  // 문의 내용
        const attachment = $(this).data('attachment'); // 첨부파일 이름
        var attachmentUrl = "http://192.168.1.180:8000/" + attachment; // 동적 URL

        // 모달의 hidden input과 문의 제목, 내용 설정
        $('#idx').val(idx);
        $('#title').val(title);
        $('#content').val(content);

        // 첨부 파일 링크 설정
        const attachmentSection = $('#attachmentSection');
        if (attachment) {
            // 첨부파일이 있을 경우 다운로드 링크를 생성
            attachmentSection.html('<a href="' + attachmentUrl + '" target="_blank" download>파일 다운로드</a> (1)');
        } else {
            // 첨부파일이 없을 경우 적절한 메시지 표시
            attachmentSection.html('첨부된 파일이 없습니다. (0)');
        }

        // 모달 창 열기
        $('#answerModal').modal('show');
    });

    // 답변 폼 제출 시 처리
    $('#answerForm').submit(function(event) {
        event.preventDefault();  // 폼 자동 제출 방지

        // 로컬스토리지에서 토큰 가져오기
        const token = localStorage.getItem('token');
        if (!token) {
            alert("로그인 토큰이 없습니다.");
            return;
        }

        const formData = $(this).serialize();  // 폼 데이터를 직렬화

        $.ajax({
            type: 'POST',
            url: '/master/QNAanswerOK',
            headers: {
                'Authorization': `Bearer ${token}`  // Authorization 헤더에 토큰 추가
            },
            data: formData,
            success: function(response) {
                alert('답변이 성공적으로 처리되었습니다.');
                location.reload();  // 페이지 새로고침
            },
            error: function(xhr, status, error) {
                console.error('Error:', xhr.responseText);  // 에러 메시지를 콘솔에 출력
                alert('답변 처리 중 오류가 발생했습니다.');
            }
        });
    });
});

$(document).ready(function() {
    // 로컬 스토리지에서 토큰을 가져옵니다.
   var token = localStorage.getItem('token');
   console.log("저장된 토큰:", token); // 토큰 출력
   if (!token) {
       alert('토큰이 없습니다. 다시 로그인해 주세요.');
       return;
   }

    // 폼 제출 시 AJAX 처리
    $('#aniAddForm').submit(function(event) {
        event.preventDefault();  // 기본 폼 제출 방식을 방지

        // 폼 데이터를 객체로 직렬화
        var formData = new FormData(this);

        $.ajax({
            url: '/master/aniAddMasterOk',  // 서버의 애니 추가 처리 URL
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            headers: {
                'Authorization': 'Bearer ' + token // Authorization 헤더 추가
            },
            success: function(response) {
                alert('애니가 성공적으로 추가되었습니다.');
                window.location.href = '/master/aniMasterList';  // 성공 시 리다이렉트할 페이지
            },
            error: function(xhr, status, error) {
                console.error('에러:', xhr.responseText);
                alert('애니 추가 중 오류가 발생했습니다.');
            }
        });
    });
});






// 애니 해당 게시글 삭제 --------------------------------------------------------------------------
$(document).ready(function() {
    // 애니 삭제 버튼 클릭 시
    $('.btn-outline-danger.ani-delete').on('click', function() {
        var idx = $(this).data('idx'); // 삭제할 애니의 idx 값

        if (confirm("정말로 삭제하시겠습니까?")) {
            $.ajax({
                type: 'POST',
                url: '/master/aniDeleteMaster/' + idx,
                success: function(response) {
                    if (response.success) {
                        alert('해당 ' + idx + ' 번호의 애니가 삭제되었습니다.');
                        window.location.href = '/master/aniMasterList';  // 성공 시 애니 리스트로 리다이렉트
                    } else {
                        alert(response.message);
                    }
                },
                error: function(xhr, status, error) {
                    console.error('삭제 중 에러 발생:', xhr.responseText);
                    alert('삭제 중 오류가 발생했습니다.');
                }
            });
        }
    });

    // 스토어 삭제 버튼 클릭 시
    $(document).ready(function() {
        // 삭제 버튼 클릭 이벤트 핸들러
        $('.btn-outline-danger.store-delete').on('click', function() {
            var idx = $(this).data('idx'); // 삭제할 스토어의 idx 값

            if (confirm("정말로 삭제하시겠습니까?")) {
                $.ajax({
                    type: 'POST',
                    url: '/master/storeDeleteMaster/' + idx,
                    success: function(response) {
                        // 서버에서 JSON 형식의 응답을 반환한다고 가정하고 처리
                        if (response.success) {
                            alert('해당 ' + idx + ' 번호의 스토어가 삭제되었습니다.');
                            window.location.href = '/master/storeMasterList';  // 성공 시 스토어 리스트로 리다이렉트
                        } else {
                            // 실패 시 서버에서 반환한 메시지를 표시
                            alert(response.message);
                        }
                    },
                    error: function(xhr, status, error) {
                        console.error('삭제 중 에러 발생:', xhr.responseText);
                        alert('삭제 중 오류가 발생했습니다.');
                    }
                });
            }
        });
    });

// 굿즈 상품 수정 --------------------------------------------------------------------------
 $(document).ready(function() {
     var token = localStorage.getItem('token');

     // 토큰이 없으면 경고 표시
     if (!token) {
         alert("토큰이 없습니다. 다시 로그인 해주세요.");
         return;
     }

     $('#storeEditForm').submit(function(event) {
         event.preventDefault(); // 기본 폼 제출 동작 중지

         var formData = new FormData(this);

         $.ajax({
             url: '/master/storeEditMasterOK',
             type: 'POST',
             headers: {
                 'Authorization': 'Bearer ' + token // Authorization 헤더로 토큰 전달
             },
             data: formData,
             contentType: false,
             processData: false,
             success: function(response) {
                 alert('굿즈 상품 수정 성공');
                 window.location.href = '/master/storeMasterList';
             },
             error: function(xhr, status, error) {
                 alert('굿즈 상품 수정 중 오류가 발생했습니다.');
             }
         });
     });
 });

 $(document).ready(function() {
     $('#code').change(function() {
         var categoryId = $(this).val(); // 선택된 대분류 ID

         if (categoryId) {
             $.ajax({
                 url: '/getSubCategories/' + categoryId,
                 type: 'GET',
                 success: function(data) {
                     var subCategorySelect = $('#second_category');
                     subCategorySelect.empty().append('<option value="">중분류 선택</option>');

                     // 중분류 목록을 동적으로 추가
                     $.each(data, function(index, item) {
                         subCategorySelect.append('<option value="' + item.second_category + '">' + item.name + '</option>');
                     });

                     subCategorySelect.prop('disabled', false); // 중분류 선택 활성화
                 },
                 error: function() {

                 }
             });
         } else {
             $('#second_category').empty().append('<option value="">중분류 선택</option>').prop('disabled', true);
         }
     });
 });
// 공지사항 수정 -------------------------------------------------------------------
 $(document).ready(function() {
     $('#noticeEditForm').submit(function(event) {
         event.preventDefault(); // 기본 폼 제출 동작을 막음

         var idx = $('#idx').val();
         var title = $('#title').val().trim();
         var content = $('#content').val().trim();
         var token = localStorage.getItem('token'); // 로컬 스토리지에서 토큰 가져오기

         // 폼 필드 검증
         if (!title) {
             alert("제목을 입력해 주세요.");
             return false;
         }

         if (!content) {
             alert("내용을 입력해 주세요.");
             return false;
         }

         if (!token) {
             alert("인증 토큰이 없습니다. 다시 로그인 해주세요.");
             return false;
         }

         // FormData 객체 생성
         var formData = new FormData(this);
         formData.append('token', token); // 토큰 추가

         // Ajax를 통해 서버로 데이터 전송
         $.ajax({
             type: "POST",
             url: "/master/noticeEditMasterOk",
             data: formData,
             headers: {
                 "Authorization": "Bearer " + token // Authorization 헤더 추가
             },
             processData: false,
             contentType: false,
             success: function(response) {
                 // 성공 시 리다이렉트 처리
                 alert(response);
                 window.location.href = "/master/noticeMasterList";
             },
             error: function(xhr) {
                 // 서버에서 반환된 오류 메시지를 로그로 확인
                 console.log("에러 메시지: " + xhr.responseText);
                 alert("등록 중 오류가 발생했습니다.");
             }
         });
     });
 });

 $(document).ready(function() {
     // 답변 보기 버튼 클릭 시 이벤트 처리
     $('.btn-outline-info').on('click', function() {
         // 버튼의 data-* 속성에서 값 가져오기
         var content = $(this).data('content');
         var reply = $(this).data('reply');

         // 모달 창에 내용 채우기
         $('#qnaContent').text(content);
         $('#qnaReply').text(reply);

         // 모달 창 띄우기
         $('#replyModal').modal('show');
     });
 });

 $(document).ready(function() {
     $("#storeAddForm").on("submit", function(event) {
         event.preventDefault(); // 기본 폼 제출 방지

         const form = $(this)[0];
         const formData = new FormData(form);

         // JWT 토큰을 로컬 스토리지에서 가져오기
         const token = localStorage.getItem("token");
         if (!token) {
             alert("로그인 정보가 없습니다. 다시 로그인해 주세요.");
             window.location.href = "/user/login"; // 로그인 페이지로 리디렉션
             return;
         }

         // 서버로 AJAX 요청 보내기
         $.ajax({
             url: "/master/storeAddMasterOk",
             type: "POST",
             headers: {
                 "Authorization": "Bearer " + token
             },
             data: formData,
             processData: false,
             contentType: false,
             success: function(data) {
                 alert("굿즈 상품이 성공적으로 등록되었습니다.");
                 window.location.href = '/master/storeMasterList';
             },
             error: function(xhr) { // 콤마 추가됨
                 if (xhr.status === 401) {
                     // 401 에러: 인증 오류 발생 시 로그인 페이지로 리디렉션
                     alert("세션이 만료되었습니다. 다시 로그인해 주세요.");
                     localStorage.removeItem("token"); // 만료된 토큰 삭제
                     window.location.href = "/user/login"; // 로그인 페이지로 리디렉션
                 } else {
                     console.error("에러:", xhr);
                     alert("굿즈 상품 등록 중 오류가 발생했습니다.");
                 }
             }
         });
     });
 });


 $(document).ready(function() {
     // 이벤트 추가 폼 제출 시
     $('#eventAddForm').on('submit', function(event) {
         event.preventDefault(); // 기본 폼 제출 방지

         // 폼 데이터 객체 생성
         var formData = new FormData(this);

         // JWT 토큰을 로컬 스토리지에서 가져오기
         var token = localStorage.getItem("token");
         if (!token) {
             alert("로그인 정보가 없습니다. 다시 로그인해 주세요.");
             window.location.href = "/user/login"; // 로그인 페이지로 리디렉션
             return;
         }

         // AJAX 요청 보내기
         $.ajax({
             url: '/master/EventAddMasterOk',
             type: 'POST',
             data: formData,
             headers: {
                 "Authorization": "Bearer " + token
             },
             processData: false, // 파일 업로드 시 필요
             contentType: false, // 파일 업로드 시 필요
             success: function(response) {
                 alert(response); // 서버의 응답 메시지 출력
                 window.location.href = "/master/EventMasterList"; // 이벤트 목록 페이지로 이동
             },
             error: function(xhr) {
                 if (xhr.status === 401) {
                     alert("세션이 만료되었습니다. 다시 로그인해 주세요.");
                     localStorage.removeItem("token");
                     window.location.href = "/user/login";
                 } else {
                     console.error("에러:", xhr);
                     alert("이벤트 등록 중 오류가 발생했습니다.");
                 }
             }
         });
     });
 });

 $(document).ready(function() {
     $('.viewDetails').on('click', function() {
         var eventIdx = $(this).data('idx'); // 이벤트 ID 가져오기

         // 서버에서 이벤트 상세 정보를 Ajax로 가져오기
         $.ajax({
             type: 'GET',
             url: '/master/getEventDetail',
             data: { idx: eventIdx },
             success: function(response) {
                 if (response) {
                     $('#eventTitle').text(response.title);
                     $('#eventDate').text(response.event_date);
                     $('#eventContent').html(response.content); // HTML 형식으로 내용 삽입

                     // 모달 창 열기
                     $('#eventDetailModal').modal('show');
                 } else {
                     alert('이벤트 상세 정보를 불러오지 못했습니다.');
                 }
             },
             error: function() {
                 alert('이벤트 상세 정보를 가져오는 데 실패했습니다.');
             }
         });
     });
 });

 $(document).ready(function() {
     $('#eventEditForm').on('submit', function(event) {
         event.preventDefault();

         var formData = new FormData(this);
         var token = localStorage.getItem("token");
         if (!token) {
             alert("로그인 정보가 없습니다. 다시 로그인해 주세요.");
             window.location.href = "/user/login";
             return;
         }

         $.ajax({
             url: '/master/EventEditMasterOk',
             type: 'POST',
             data: formData,
             headers: {
                 "Authorization": "Bearer " + token
             },
             processData: false,
             contentType: false,
             success: function(response) {
                 alert(response);
                 window.location.href = "/master/EventMasterList";
             },
             error: function(xhr) {
                 if (xhr.status === 401) {
                     alert("세션이 만료되었습니다. 다시 로그인해 주세요.");
                     localStorage.removeItem("token");
                     window.location.href = "/user/login";
                 } else {
                     alert("이벤트 수정 중 오류가 발생했습니다.");
                 }
             }
         });
     });
 });

 $(document).ready(function() {
     // 댓글 상세보기 버튼 클릭 시
     $(document).on('click', '.commentDetailBtn', function(event) {
         event.preventDefault();
         var commentIdx = $(this).data('idx');  // 버튼에서 idx 값 가져오기

         // Ajax 요청으로 서버에서 댓글 상세 정보를 가져옴
         $.ajax({
             type: 'GET',
             url: '/master/getCommentDetails',  // 댓글 상세정보를 가져오는 URL
             data: { idx: commentIdx },
             success: function(response) {
                 if (response && response.comment) {
                     // 서버로부터 받은 댓글 데이터를 모달에 표시
                     var comment = response.comment;
                     $('#commentContent').text(comment.content || '내용 없음');
                     $('#commentAuthor').text(comment.userid || '작성자 정보 없음'); // 댓글 작성자의 ID
                     $('#commentDate').text(comment.regDT || '작성일 정보 없음');

                     // 답글 목록 표시
                     var replies = response.replies;
                     var replyListHtml = '';
                     if (replies && replies.length > 0) {
                         replies.forEach(function(reply) {
                             replyListHtml += '<li class="list-group-item">';
                             replyListHtml += '<p><strong>' + reply.userid + '</strong> (' + reply.regDT + ')</p>';
                             replyListHtml += '<p>' + reply.content + '</p>';
                             replyListHtml += '</li>';
                         });
                     } else {
                         replyListHtml = '<li class="list-group-item">답글이 없습니다.</li>';
                     }
                     $('#replyList').html(replyListHtml);

                     // 모달 창 띄우기
                     $('#commentModal').modal('show');
                 } else {
                     alert('댓글 상세 정보를 불러오지 못했습니다.');
                 }
             },
             error: function() {
                 alert('댓글 상세 정보를 가져오는 데 실패했습니다.');
             }
         });
     });
 });



 $(document).ready(function() {
     $('#FAQEditForm').on('submit', function(event) {
         event.preventDefault(); // 기본 폼 제출 방지

         var formData = $(this).serialize(); // 폼 데이터 직렬화
         var token = localStorage.getItem("token");
         if (!token) {
             alert("로그인 정보가 없습니다. 다시 로그인해 주세요.");
             window.location.href = "/user/login";
             return;
         }

         $.ajax({
             url: '/master/FAQEditMasterOk',
             type: 'POST',
             data: formData,
             headers: {
                 "Authorization": "Bearer " + token
             },
             success: function(response) {
                 alert(response);
                 window.location.href = "/master/FAQMasterList"; // 수정 후 목록으로 이동
             },
             error: function(xhr) {
                 if (xhr.status === 401) {
                     alert("세션이 만료되었습니다. 다시 로그인해 주세요.");
                     localStorage.removeItem("token");
                     window.location.href = "/user/login";
                 } else {
                     alert("자주 묻는 질문 수정 중 오류가 발생했습니다.");
                 }
             }
         });
     });
 });

 $(document).ready(function() {
     $('.deleteBtn').on('click', function() {
         const idx = $(this).data('idx'); // data-idx 속성에서 idx 값 가져오기

         // idx가 정의되지 않았을 때 경고창 표시
         if (idx === undefined || idx === "") {
             alert("삭제할 항목의 ID가 정의되지 않았습니다.");
             return; // 이곳에서 함수 종료
         }

         // 삭제 여부 확인
         if (confirm("정말로 삭제하시겠습니까?")) {
             $.ajax({
                 type: 'POST',
                 url: '/master/reportingDeleteMaster/' + idx,
                 success: function(response) {
 alert('해당 ' + idx + ' 번호의 신고가 삭제되었습니다.');
                     window.location.reload(); // 페이지 새로고침
                 },
                 error: function(xhr) {
                     console.error("삭제 중 에러 발생:", xhr.responseText);
                     alert("삭제 중 오류가 발생했습니다.");
                 }
             });
         }
     });
 });
 });

 $(document).ready(function() {
     $('.deleteBoardBtn').on('click', function() {
         const idx = $(this).data('idx'); // data-idx 속성에서 idx 값 가져오기

         // idx가 정의되지 않았을 때 경고창 표시
         if (idx === undefined || idx === "") {
             alert("삭제할 항목의 ID가 정의되지 않았습니다.");
             return; // 이곳에서 함수 종료
         }

         // 삭제 여부 확인
         if (confirm("정말로 삭제하시겠습니까?")) {
             $.ajax({
                 type: 'POST', // HTTP 메서드를 POST로 설정
                 url: '/master/boardMasterAllDelete/' + idx, // 삭제 요청 URL
                 success: function(response) {
                     alert('해당 ' + idx + ' 번호의 게시글이 삭제되었습니다.'); // 성공 메시지
                     window.location.reload(); // 페이지 새로고침
                 },
                 error: function(xhr) {
                     console.error("삭제 중 에러 발생:", xhr.responseText); // 에러 로그
                     alert("삭제 중 오류가 발생했습니다."); // 오류 메시지
                 }
             });
         }
     });
 });

 $(document).ready(function() {
     $('.deleteFaqBtn').on('click', function() {
         const idx = $(this).data('idx'); // data-idx 속성에서 idx 값 가져오기

         // idx가 정의되지 않았을 때 경고창 표시
         if (idx === undefined || idx === "") {
             alert("삭제할 항목의 ID가 정의되지 않았습니다.");
             return; // 이곳에서 함수 종료
         }

         // 삭제 여부 확인
         if (confirm("정말로 삭제하시겠습니까?")) {
             $.ajax({
                 type: 'POST', // HTTP 메서드를 GET으로 설정
                 url: '/master/faqMasterDelete/' + idx, // 삭제 요청 URL
                 success: function(response) {
                     alert('해당 ' + idx + ' 번호의 FAQ가 삭제되었습니다.'); // 성공 메시지
                     window.location.reload(); // 페이지 새로고침
                 },
                 error: function(xhr) {
                     console.error("삭제 중 에러 발생:", xhr.responseText); // 에러 로그
                     alert("삭제 중 오류가 발생했습니다."); // 오류 메시지
                 }
             });
         }
     });
 });

 $(document).ready(function() {
     $('.deleteEventBtn').on('click', function() {
         const idx = $(this).data('idx'); // data-idx 속성에서 idx 값 가져오기

         // idx가 정의되지 않았을 때 경고창 표시
         if (idx === undefined || idx === "") {
             alert("삭제할 항목의 ID가 정의되지 않았습니다.");
             return; // 이곳에서 함수 종료
         }

         // 삭제 여부 확인
         if (confirm("정말로 삭제하시겠습니까?")) {
             $.ajax({
                 type: 'POST', // HTTP 메서드를 GET으로 설정
                 url: '/master/eventMasterDelete/' + idx, // 삭제 요청 URL
                 success: function(response) {
                     alert('해당 ' + idx + ' 번호의 EVENT가 삭제되었습니다.'); // 성공 메시지
                     window.location.reload(); // 페이지 새로고침
                 },
                 error: function(xhr) {
                     console.error("삭제 중 에러 발생:", xhr.responseText); // 에러 로그
                     alert("삭제 중 오류가 발생했습니다."); // 오류 메시지
                 }
             });
         }
     });
 });