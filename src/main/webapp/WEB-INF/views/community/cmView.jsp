<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf" %>


<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
<link href="/css/reportModal.css" rel="stylesheet" type="text/css">
<link href="/css/cmView.css" rel="stylesheet" type="text/css">




    <section class="top_info">
        <div class="cm_tab">
            <h1>커뮤니티</h1>
            <div class="cm_opt">
                <ul class="list">
                    <li class="${commtype == 'all' ? 'selected' : ''}">
                        <a href="javascript:void(0);" class="btn" onclick="showTab('all')">최신글보기</a>
                    </li>
                    <li class="${commtype == '10' ? 'selected' : ''}">
                        <a href="javascript:void(0);" class="btn" onclick="showTab('10')">자랑</a>
                    </li>
                    <li class="${commtype == '20' ? 'selected' : ''}">
                        <a href="javascript:void(0);" class="btn" onclick="showTab('20')">덕질</a>
                    </li>
                    <li class="${commtype == '30' ? 'selected' : ''}">
                        <a href="javascript:void(0);" class="btn" onclick="showTab('30')">친목</a>
                    </li>
                    <li class="${commtype == '40' ? 'selected' : ''}">
                        <a href="javascript:void(0);" class="btn" onclick="showTab('40')">팬아트</a>
                    </li>
                    <li>
                        <a href="/notice2" class="btn">통합공지</a>
                    </li>
                </ul>
            </div>
        </div>
    </section>

    <div class="container">
        <div class="cmView">
            <div class="viewInfo">
                <div class="title-box">
                    <p>${vo.commtype}</p>
                    <div>${vo.title}</div>
                </div>

                <div class="content-box">
                    <div class="author-id">${vo.userid}</div>
                    <span class="date">${vo.regDT}</span>
                    <span class="hit"><img src="/img/cm/ico_view.png">${vo.hit}</span>
                    <span class="post_report"><button id="reportPostBtn" class="btn btn-link">
                    <img src="http://192.168.1.180:8000/emergency.svg" class="emergency_icon">
                    </button></span>
                </div>
            </div>

            <div class="cmContent post-content">${vo.content}</div>
        </div>

        <!-- 댓 글 -->
        <div id="replyArea" style="margin-top:30px;">
            <form method="post" id="replyForm">
                <div class="review-writer">
                    <div class="reply_input">
                        <input type="text" id="textSearch" placeholder="정책위반 댓글은 삭제될 수 있습니다." />
                        <button type="button" id="btnSearch" onclick="regiComm()">등록</button>
                    </div>
                    <input type="hidden" name="no" value="${vo.idx}" />
                </div>
            </form>
            <div></div>
            <div id="replyList"></div>
        </div>
    </div>

        <!-- 리스트 목록 -->
           <div class="list_section">
            <!-- 이전 페이지 처리 -->
                <c:if test="${go != null}">
                    <a href="/cmView/${go.idx}">
                        <div class="list_pre">
                            <i class="bi bi-chevron-up"></i>이전페이지
                        </div>
                    </a>
                </c:if>
                <c:if test="${go == null}">
                    <a href="javascript:void(0);" onclick="alert('첫번째 페이지입니다.');">
                        <div class="list_pre">
                            <i class="bi bi-chevron-up"></i>이전페이지
                        </div>
                    </a>
                </c:if>

                <!-- 다음 페이지 처리 -->
                <c:if test="${tun != null}">
                    <a href="/cmView/${tun.idx}">
                        <div class="list_next">
                            <i class="bi bi-chevron-down"></i>다음페이지
                        </div>
                    </a>
                </c:if>
                <c:if test="${tun == null}">
                    <a href="javascript:void(0);" onclick="alert('마지막 페이지입니다.');">
                        <div class="list_next">
                            <i class="bi bi-chevron-down"></i>다음페이지
                        </div>
                    </a>
                </c:if>
           </div>


            <div class="listBt">
                <a class="btn btn-secondary btn-sm btn-edit" href="/cmEdit/${vo.idx}" role="button" style="display: none;">
                    수정
                </a>
                <a class="btn btn-secondary btn-sm" href="/cmList" role="button">
                    목록
                </a>
                <a class="btn btn-secondary btn-sm btn-delete" role="button" onclick="confirmDelete(${vo.idx});" style="display: none;">
                    삭제
                </a>
            </div>

        </div>

        <!-- 신고 모달 -->
                <div class="reportModal_body" id="reportModal" style="visibility: hidden;">
                    <div class="reportModal_background"></div>
                    <div class="reportModal_container">
                        <div class="reportModal_layer">
                            <div class="reportModal_top">
                                <p>작성 글 신고하기</p>
                                <button id="closeModal"><i class="fa-solid fa-x"></i></button>
                            </div>
                            <div class="reportModal_bottom">
                                <strong>신고대상 ID 및 내용</strong>
                                <div class="report_info">
                                    <span>sing2727</span>
                                    <p>제 첫 키보드인데 굉장히 만족합니다!...</p>
                                </div>
                                <strong>신고사유</strong>
                                <ul class="report_reason_ul">
                                    <li>
                                        <input type="radio" id="not_relevant_img" name="report_reason" value="1" />
                                        <label for="not_relevant_img">관련없는 이미지</label>
                                    </li>
                                    <li>
                                        <input type="radio" id="not_relevant_content" name="report_reason" value="2" />
                                        <label for="not_relevant_content">관련없는 내용</label>
                                    </li>
                                    <li>
                                        <input type="radio" id="abuse" name="report_reason" value="3" />
                                        <label for="abuse">욕설/비방</label>
                                    </li>
                                    <li>
                                        <input type="radio" id="PromotionalPost" name="report_reason" value="4" />
                                        <label for="PromotionalPost">광고/홍보글</label>
                                    </li>
                                    <li>
                                        <input type="radio" id="perPersonal_info_leak" name="report_reason" value="5" />
                                        <label for="perPersonal_info_leak">개인정보 유출</label>
                                    </li>
                                    <li>
                                        <input type="radio" id="over_post" name="report_reason" value="6" />
                                        <label for="over_post">게시글 도배</label>
                                    </li>
                                    <li>
                                        <input type="radio" id="adult" name="report_reason" value="7" />
                                        <label for="adult">음란/선정성</label>
                                    </li>
                                    <li>
                                        <input type="radio" id="etc" name="report_reason" value="8" />
                                        <label for="etc">기타</label>
                                    </li>
                                </ul>
                                <div class="report_reason_text">
                                    <textarea placeholder="이 외 사유를 적어주세요.(100자까지 작성가능)" maxlength="100"></textarea>
                                </div>
                                <div class="report_reason_bottom">
                                    <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    width="20"
                                    height="20"
                                    viewBox="-4 -4 38 38"
                                    id="icon_exclamation_30x30_gray"
                                    x="740"
                                    y="77"
                                    >
                                    <path
                                      fill="#888"
                                      fill-rule="evenodd"
                                      d="M15 30C6.716 30 0 23.284 0 15 0 6.716 6.716 0 15 0c8.284 0 15 6.716 15 15 0 8.284-6.716 15-15 15zm0-28.72C7.423 1.28 1.28 7.423 1.28 15c0 7.577 6.143 13.72 13.72 13.72 7.577 0 13.72-6.143 13.72-13.72 0-7.577-6.143-13.72-13.72-13.72zM14 19h2v2h-2v-2zm0-10h2v8h-2V9z"
                                    />
                                    </svg>
                                    <p>신고해주신 내용은 관리자 검토 후 내부정책에 의거 조치가 진행됩니다.</p>
                                </div>
                                <div class="report_btn_div">
                                    <button id="cancelReport">취소</button>
                                    <button id="submitReport">신고</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>



<script>
var useridx; // 해당 페이지에서 모두 사용 가능하도록! 전역변수로 선언
var userid;
var currentCommIdx;// 현재 페이지의 comm_idx를 전역 변수로 설정

setTimeout(function() {


    // 커뮤니티 페이지 전용 로그인 상태 확인 함수 호출
    checkLoginStatusForCommunity();
}, 400);

function checkLoginStatusForCommunity() {
    console.log("호출");

    var token = localStorage.getItem("token"); // 토큰 값 가져오기
    if (token != "" && token != null) {
        $.ajax({
            url: "/getuser",
            type: "get",
            data: { Authorization: token },
            success: function(vo) {
                console.log("로그인된 사용자 ID: " + vo.userid);

                userid = vo.userid;
                useridx = vo.idx;

                console.log(userid);
                console.log(useridx);

                // useridx 설정 후 댓글 목록 불러오기
                let comm_idx = $('[name=no]').val();
                loadComments(comm_idx);

                // 게시물 작성자의 아이디와 로그인된 사용자의 아이디가 같은지 확인
                var postAuthorId = "${vo.userid}";

                if (userid === postAuthorId) {
                    // 로그인된 사용자가 게시물 작성자인 경우 수정, 삭제 버튼을 표시
                    document.querySelector(".btn-edit").style.display = "block";
                    document.querySelector(".btn-delete").style.display = "block";
                } else {
                    // 게시물 작성자가 아닌 경우 수정, 삭제 버튼을 숨김
                    document.querySelector(".btn-edit").style.display = "none";
                    document.querySelector(".btn-delete").style.display = "none";
                }
            },
            error: function() {
                console.error("로그인 여부 확인 실패");
                // 로그인되지 않은 경우 수정, 삭제 버튼 숨김
                document.querySelector(".btn-edit").style.display = "none";
                document.querySelector(".btn-delete").style.display = "none";
            }
        });
    } else {
        // 토큰이 없거나 유효하지 않은 경우 수정, 삭제 버튼 숨김
        document.querySelector(".btn-edit").style.display = "none";
        document.querySelector(".btn-delete").style.display = "none";
    }
}


    <!-- 삭제 확인 및 처리 -->
    //<script type="text/javascript">
        function confirmDelete(idx) {
            if (confirm('정말로 이 게시글을 삭제하시겠습니까?')) {
                // 삭제 확인 시 삭제 URL로 이동
                location.href = '/cmDelete/' + idx;
            }
        }




// showTab 함수 정의 (탭 전환)
function showTab(commtype) {
    // commtype 값에 따라 페이지 이동
    switch (commtype) {
        case 'all':
            window.location.href = '/cmList?commtype=all'; // 최신글보기
            break;
        case '10':
            window.location.href = '/cmList?commtype=10'; // 자랑
            break;
        case '20':
            window.location.href = '/cmList?commtype=20'; // 덕질
            break;
        case '30':
            window.location.href = '/cmList?commtype=30'; // 친목
            break;
        case '40':
            window.location.href = '/cmList?commtype=40'; // 팬아트
            break;
        default:
            console.error('잘못된 commtype 값입니다: ' + commtype);
    }
}

    <!-- 댓글 등록 -->
     function regiComm() {

         let comm_idx = $('[name=no]').val();
         let content =  $('[id=textSearch]').val();
         // 로컬 스토리지에서 JWT 토큰 가져오기
         const token = localStorage.getItem("token");
         if (!token) {
             alert('로그인이 필요합니다.');  // 토큰이 없을 경우 로그인 필요 메시지
             location.href = "/user/login";  // 로그인 페이지로 이동
             return false;
         }

         // 서버로 전송할 데이터를 FormData 객체에 추가
         const postData = new URLSearchParams();
         postData.append("content", content);
         postData.append("token", token);  // token 추가
         postData.append("comm_idx", comm_idx);

         // AJAX 요청 보내기
         $.ajax({
             url: "/regiComm",
             type: "POST",
             data: postData.toString(),
             contentType: "application/x-www-form-urlencoded",
             headers: {
                 "Authorization": `Bearer ${token}`  // Authorization 헤더에 JWT 토큰 추가
             },
             success: function(data) {
                 alert('댓글이 등록되었습니다.');  // 성공 메시지
                 loadComments(comm_idx);// 댓글 추가 후 댓글 목록 다시 불러오기
                 $('#textSearch').val('');// 댓글 입력창 비우기
             },
             error: function(xhr, status, error) {
                 if (xhr.status === 401) {
                     alert('인증에 실패했습니다. 다시 로그인하세요.');
                     location.href = "/user/login";  // 로그인 페이지로 이동
                 } else {
                     alert("요청 처리 중 오류가 발생했습니다.");
                 }
                 console.error("Error:", error);  // 오류 출력
             }
         });

         return false;  // 기본 폼 제출 방지
     }

     // 엔터키 이벤트 리스너 추가
     $(document).ready(function() {
         $('#textSearch').keydown(function(event) {
             if (event.key === 'Enter') {
                 event.preventDefault(); // 기본 엔터 동작 방지
                 regiComm(); // regiComm() 함수 호출
             }
         });
     });

     // 댓글 목록 로드 함수
          function loadComments(comm_idx) {
              currentCommIdx = comm_idx;
              $.ajax({
                  url: "/getComment",
                  type: "GET",
                  data: { comm_idx: comm_idx },
                  success: function(comments) {
                      let replyList = $('#replyList');
                      replyList.empty();

                      // 댓글을 렌더링
                      comments.forEach(comment => {
                          let indentLevel = comment.depth * 30;

                          let comm = '<div class="comment-item comment-' + comment.idx + '" style="margin-left: ' + indentLevel + 'px;">';

                          comm += '<div class="comment-header">';
                          comm += '<span class="comment-user"><strong>' + comment.userid + '</strong></span>';
                          comm += '<button id="reportCommentBtn-' + comment.idx + '" class="btn btn-link reportBtn" data-author-id="' + comment.userid + '" data-content="' + comment.content + '">' +
                                  '<img src="http://192.168.1.180:8000/emergency.svg" class="emergency_icon"></button>';
                          comm += '</div>';

                          comm += '<div class="comment-content">' + comment.content + '</div>';

                          comm += '<div class="comment-meta-wrapper">';
                          comm += '<span class="comment-meta">' + comment.regDT + '</span>';

                          // 댓글의 depth가 2 이상이면 답글 쓰기 비활성화 (자식 댓글은 depth가 2이므로)
                          // 답글 쓰기 버튼
                          if (comment.depth < 2) {// depth가 2 미만일 경우에만 답글 허용
                              comm += '<span class="reply-btn" onclick="toggleReplyInput(' + comment.idx + ')">답글쓰기</span>';
                          }

                          // 수정/삭제 버튼
                          if (parseInt(comment.useridx) === parseInt(useridx)) {
                              comm += '<span class="edit-btn" onclick="editComment(' + comment.idx + ')">수정</span>';
                              comm += '<span class="delete-btn" onclick="deleteComment(' + comment.idx + ')">삭제</span>';
                          }

                          // 댓글의 depth가 2 이상이면 답글 쓰기 비활성화 (자식 댓글은 depth가 2이므로)
                          // 답글 쓰기 폼
                          if (comment.depth < 2) {// depth가 2 미만일 경우에만 답글 허용
                              comm += '<div id="replyInput-' + comment.idx + '" class="reply_input" style="display:none;">' +
                                  '<input type="text" id="replyContent-' + comment.idx + '" placeholder="답글을 남겨보세요." />' +
                                  '<button onclick="regiReply(' + comment.idx + ',' + comment.comm_idx + ')">등록</button>' +
                                  '</div>';
                          }

                          // 수정 폼
                          if (parseInt(comment.useridx) === parseInt(useridx)) {
                              comm += '<div id="edit-form-' + comment.idx + '" class="reply_input" style="display:none;">' +
                                '<textarea id="edit-textarea-' + comment.idx + '">' + comment.content + '</textarea>' +
                                '<button id="test" onclick="updateComment(' + comment.idx + ',' + comment.comm_idx + ')">수정하기</button>' +
                                '</div>';
                          }

                          comm += '</div>'; // comment-meta-wrapper 종료

                          comm += '</div>'; // comment-item 종료
                          replyList.append(comm);
                      });

                  },
                  error: function(xhr, status, error) {
                      console.error("댓글 로드 오류:", error);
                  }
              });
          }


    function test(testidx){
    alert(testidx);
    }

     function regiReply(commentIdx, comm_idx) {//원댓글의 idx + 게시물의 idx
          //let contents =  $('[id=replyContent-${comment.idx}]').val();
          const content = document.querySelector("#replyContent-"+commentIdx).value;

          // 로컬 스토리지에서 JWT 토큰 가져오기
          const token = localStorage.getItem("token");
          if (!token) {
              alert('로그인이 필요합니다.');  // 토큰이 없을 경우 로그인 필요 메시지
              location.href = "/user/login";  // 로그인 페이지로 이동
              return false;
          }

          // 서버로 전송할 데이터를 FormData 객체에 추가
          const postData = new URLSearchParams();
          postData.append("content", content);
          postData.append("token", token);  // token 추가
          postData.append("commentIdx", commentIdx);
          postData.append("comm_idx", comm_idx);


              // AJAX 요청 보내기
              $.ajax({
                  url: "/regiReply",
                  type: "POST",
                  data: postData.toString(),
                  contentType: "application/x-www-form-urlencoded",
                  headers: {
                      "Authorization": `Bearer ${token}`  // Authorization 헤더에 JWT 토큰 추가
                  },
                  success: function(data) {
                      alert('답글이 등록되었습니다.');  // 성공 메시지

                      // comm_idx를 success 콜백 함수 내에서 사용 가능하게 함
                      loadComments(comm_idx);  // 댓글 추가 후 댓글 목록 다시 불러오기

                      // 입력 폼을 비웁니다.
                      const replyContentElement = document.querySelector("#replyContent-" + commentIdx);
                      if (replyContentElement) {
                          replyContentElement.value = '';  // 요소가 존재하는 경우에만 값을 비웁니다.
                      } else {
                          console.error("Element #replyContent-" + commentIdx + " not found.");
                      }
                  },
                  error: function(xhr, status, error) {
                      if (xhr.status === 401) {
                          alert('인증에 실패했습니다. 다시 로그인하세요.');
                          location.href = "/user/login";  // 로그인 페이지로 이동
                      } else {
                          alert("요청 처리 중 오류가 발생했습니다.");
                      }
                      console.error("Error:", error);  // 오류 출력
                  }
              });

              return false;  // 기본 폼 제출 방지
          }

        //댓글입력폼
       function toggleReplyInput(commentIdx) { //뭘쓰든 상관이 없어요! 본인만 알아부보세요!
        //alert(commentIdx);
           const replyInput = document.getElementById("replyInput-"+commentIdx);
           if (replyInput.style.display === 'none') {
               replyInput.style.display = 'block'; // 보여주기
           } else {
               replyInput.style.display = 'none'; // 숨기기
           }
       }


     // 댓글 수정
     function editComment(commentIdx) {
         const commentDiv = document.querySelector(".comment-" + commentIdx);
         if (commentDiv) {
             const commentContent = commentDiv.querySelector('.comment-content').textContent; // 수정된 부분
             const editTextarea = document.querySelector("#edit-textarea-" + commentIdx);
             if (editTextarea) {
                 editTextarea.value = commentContent;
             } else {
                 console.error("수정 textarea를 찾을 수 없습니다.");
             }

             const editForm = document.querySelector("#edit-form-" + commentIdx);
             if (editForm) {
                 editForm.style.display = editForm.style.display === 'none' ? 'flex' : 'none';
             } else {
                 console.error("수정 폼을 찾을 수 없습니다: edit-form-" + commentIdx);
             }
         } else {
             console.error("댓글 요소를 찾을 수 없습니다: comment-" + commentIdx);
         }
     }

     // 댓글 수정 처리
     function updateComment(commentIdx, comm_idx) {
         const editTextarea = document.querySelector("#edit-textarea-"+commentIdx);
         const updatedContent = editTextarea.value;

         // AJAX 요청으로 서버에 수정된 내용을 보냅니다.
         $.ajax({
             url: '/updateComment',
             type: 'POST',
             data: {
                 idx: commentIdx,
                 content: updatedContent,
                 comm_idx : comm_idx
             },
             success: function(response) {
                if (response.message === "댓글 수정 성공") {
                        alert('댓글이 수정되었습니다.');
                        loadComments(response.comm_idx); // 서버가 반환한 comm_idx로 댓글 목록을 다시 불러옵니다.
                 } else {
                        alert('댓글 수정에 실패했습니다.');
                  }
             },
             error: function(xhr, status, error) {
                 console.error("댓글 수정 오류:", error);
             }
         });
     }


     function deleteComment(idx) {
         console.log("삭제할 댓글의 idx: ", idx); // 여기서 idx 값을 로그로 확인
         $.ajax({
             url: '/deleteComment',
             type: 'POST',
             data: { idx: idx },
             success: function() {
                 alert('댓글이 삭제되었습니다.');
                 loadComments(currentCommIdx); // 댓글 목록 다시 불러오기
             },
             error: function(xhr, status, error) {
                 console.error("댓글 삭제 오류:", error);
             }
         });
     }

         // 페이지 로드 시 댓글 목록 가져오기
         $(document).ready(function() {
             let comm_idx = $('[name=no]').val();
             loadComments(comm_idx); // 댓글 목록 로드
         });


    //신고
    document.addEventListener("DOMContentLoaded", function () {
        const reportModal = document.getElementById("reportModal");
        const closeModal = document.getElementById("closeModal");
        const cancelReport = document.getElementById("cancelReport");

        // 신고 모달 열기 전 라디오 버튼 및 텍스트 초기화 함수
        function initializeReportModal() {
            // 텍스트 초기화
                document.querySelector(".report_reason_text textarea").value = "";

                // 라디오 버튼 초기화 (명시적으로 checked 해제 후 재할당)
                document.querySelectorAll('input[name="report_reason"]').forEach((radio) => {
                    radio.checked = false;  // 기존 체크 상태 해제
                    radio.removeAttribute("checked");  // checked 속성 제거
                });

                // 라벨 스타일 초기화
                document.querySelectorAll(".report_reason_ul li label").forEach((label) => {
                    label.classList.remove("checkedLabel"); // 라벨 클래스 제거
                });
        }

        // 게시물 신고 버튼 클릭 시 모달 띄우기
        $(document).on("click", "#reportPostBtn", function () {
            // 모달 초기화 함수 호출
            initializeReportModal();

            // 수집할 데이터: 신고 유형, 상세 사유, 신고 대상자의 아이디 및 작성 글 정보
            const authorId = document.querySelector(".author-id").textContent; // 글 작성자 아이디가 있는 요소
            const postContent = document.querySelector(".post-content").textContent; // 글 내용이 있는 요소

            // 모달 내 신고 정보 업데이트 (모달을 띄우기 전에 정보를 설정)
            document.querySelector("#reportModal .report_info span").textContent = authorId;
            document.querySelector("#reportModal .report_info p").textContent = postContent;

            // 모달 열기
            reportModal.style.visibility = "visible";
        });

        // 댓글 신고 버튼 클릭 시 모달 띄우기 (댓글마다 개별 이벤트 등록)
        $(document).on("click", "[id^='reportCommentBtn-']", function () {
            // 모달 초기화 함수 호출
            initializeReportModal();

            // 댓글 정보 가져오기
            const commentIdx = $(this).attr("id").split('-')[1];
            const authorId = $(this).data("author-id");
            const content = $(this).data("content");

            // 모달에 데이터 설정
            document.querySelector("#reportModal .report_info span").textContent = authorId;
            document.querySelector("#reportModal .report_info p").textContent = content;

            // 현재 신고하는 댓글의 idx 설정
            currentCommentIdx = commentIdx;

            // 모달 열기
            reportModal.style.visibility = "visible";
        });

        // 모달 열릴 때 초기화 처리
        $('#reportModal').on('show.bs.modal', function () {
            initializeReportModal();
        });

        // 신고 데이터 전송 버튼 클릭 시
        document.getElementById("submitReport").addEventListener("click", function () {
            // 신고 유형과 상세 사유 수집
            const reportType = document.querySelector('input[name="report_reason"]:checked');
            const reason = document.querySelector(".report_reason_text textarea").value.trim();

            // 신고 유형 선택 유효성 검사 (신고 유형만 필수로 유지)
            if (!reportType) {
                alert("신고 유형을 선택해 주세요.");
                return;
            }

            // 서버로 전송할 데이터 객체 생성
            const reportData = {
                report_type: reportType.value,
                reason: reason,
                comunity_idx: currentCommIdx,
                useridx: useridx,
                reportDT: new Date().toISOString()
            };

            // 댓글 신고 시에만 comment_idx 추가
            if (typeof currentCommentIdx !== 'undefined' && currentCommentIdx !== null) {
                reportData.comment_idx = currentCommentIdx;
            } else {
                reportData.comment_idx = null; // 게시글 신고 시 comment_idx를 null로 설정
            }

            // 데이터가 올바르게 수집되었는지 확인하기 위해 콘솔에 출력
            console.log("Report Data: ", reportData);

            // 서버로 전송
            sendReportData(reportData);
        });

            // 모달 닫기 버튼 클릭 시 모달 닫기
            closeModal.addEventListener("click", function () {
                reportModal.style.visibility = "hidden";
                initializeReportModal(); // 모달을 닫을 때도 초기화
            });

            // 취소 버튼 클릭 시 모달 닫기
            cancelReport.addEventListener("click", function () {
                reportModal.style.visibility = "hidden";
                initializeReportModal(); // 모달을 닫을 때도 초기화
            });


        // 신고 데이터 전송 함수
        function sendReportData(data) {
            $.ajax({
                url: "/submitReport",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(data),
                success: function (response) {
                    console.log("신고 성공:", response); // 요청이 성공했을 때 로그
                    alert("신고가 접수되었습니다.");
                    reportModal.style.visibility = "hidden";
                },
                error: function (jqXHR) {
                    console.log("신고 오류:", jqXHR.status, jqXHR.responseText); // 오류 세부 정보 출력
                    if (jqXHR.status === 409) {
                        const errorMessage = jqXHR.responseText;
                        if (errorMessage === "이미 신고된 게시물입니다.") {
                            alert("이미 신고된 게시물입니다.");
                        } else if (errorMessage === "이미 신고된 댓글입니다.") {
                            alert("이미 신고된 댓글입니다.");
                        }
                    } else {
                        alert("신고 처리 중 오류가 발생했습니다.");
                        console.error("신고 오류:", jqXHR);
                    }
                }
            });
        }

        // 라디오 버튼 변경 시 라벨 스타일 변경
        $(function () {
            $('input[type="radio"]').on("change", function () {
                // 모든 라벨에서 checkedLabel 클래스 제거
                $(".report_reason_ul li label").removeClass("checkedLabel");

                // 선택된 라디오 버튼의 라벨에 checkedLabel 클래스 추가
                $(this).next("label").addClass("checkedLabel");
            });
        });
    });


    </script>


