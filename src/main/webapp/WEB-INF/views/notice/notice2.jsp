<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf"%>

<!DOCTYPE html>
<html lang="ko">

<head>
<meta charset="UTF-8">
<title>JSP Template</title>
<meta name="viewport" content="width=device-width, initial-scale=1">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="/ckeditor/ckeditor.css"/>
<script src="https://cdn.ckeditor.com/ckeditor5/39.0.1/super-build/ckeditor.js"></script>
<script src="/ckeditor/ckeditor.js"></script>
<link href="/css/notice2.css" rel="stylesheet" type="text/css">

</link>
</head>

<body>





<div class="all">

<section class="notice_info">
    <div class="notice_top">
        <h1 class="customer">고객센터</h1>
        <div class="aaa">
            <div class="notice_tab">
                <ul class="list">
                    <li class="selected">
                        <a href="#tap1" class="btn" id="resetFilters" style="cursor:pointer;">공지사항</a>
                    </li>
                    <li class="selected">
                        <a href="#tap2" class="btn">자주 묻는 질문</a>
                    </li>
                    <li class="selected">
                        <a href="#tap3" class="btn">1:1 문의</a>
                    </li>
                    <li class="selected">
                        <a href="#tap4" class="btn">이용약관</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</section>

    <!-- 공지사항 모달 -->
    <div class="detail_layer_pop" style="display:none;">
        <div class="detail_view">
            <div class="detail_layer_close" style="cursor:pointer;">
            <img src="/img/notice/close_1.png" alt="Close">
            </div>
            <div class="detail_view_layer">
                <div class="detil_new">

                    <div class="titarea">
                        <div class="tit">
                            <span id="modalDate"></span>
                            <div id="modalTitle"></div>
                        </div>
                    </div>

                    <div class="detail_content">
                        <div class="sample_txt">
                        <div id="modalContent"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <div class="con_width">
            <div class="notice" id="content">
                <div class="tools">
                    <div class="search">
                        <select>
                            <option>제목</option>
                        </select>
                        <input type="text" id="textSearch" value="${keyword}" placeholder="검색어를 입력하세요." />
                        <button type="button" id="btnSearch">검색</button>
                    </div>
                </div>
            </div>

            <!-- 공지사항 리스트 -->
            <div class="bbb">
                <div class="content active" id="tap1"> <!-- 기본적으로 active 클래스를 추가 -->
                    <div class="noticeList" id="noticeList">
                        <div class="row header">
                            <div class="col-sm-1 p-2">번호</div>
                            <div class="col-sm-9 p-2">제목</div>
                            <div class="col-sm-2 p-2">등록일</div>
                        </div>

                            <!-- 공지사항 리스트 반복 출력 -->
                            <c:forEach var="notice" items="${list}">
                                <div class="row">
                                    <div class="col-sm-1 p-2">${notice.idx}</div>
                                    <div class="col-sm-9 p-2">
                                        <a href="#" class="noticeTitle" data-date="${notice.regDT}" data-title="${notice.title}" data-content="${notice.content}">
                                            ${notice.title}
                                        </a>
                                    </div>
                                    <div class="col-sm-2 p-2">${notice.regDT}</div>
                                </div>
                            </c:forEach>
                    </div>

                    <!-- 페이지네이션 -->
                        <div class="pagination">
                            <ul class="paging">

                                <!-- 이전 페이지 버튼 -->
                                <c:if test="${pVO.hasPrevious}">
                                    <li class="pre">
                                        <a class="page-link" href="javascript:reloadPage(${pVO.nowPage - 1});">
                                            이전
                                        </a>
                                    </li>
                                </c:if>
                                <c:if test="${!pVO.hasPrevious}">
                                    <li class="pre disabled">
                                        <a class="page-link" href="javascript:void(0);">
                                            이전
                                        </a>
                                    </li>
                                </c:if>

                                <!-- 페이지 번호 -->
                                <c:forEach var="p" begin="${pVO.startPage}" end="${pVO.endPage}">
                                    <c:if test="${p <= pVO.totalPage}">
                                        <li class="${p == pVO.nowPage ? 'page active' : 'page'} ">
                                            <a class="page-link" href="javascript:reloadPage(${p});">${p}</a>
                                        </li>
                                    </c:if>
                                </c:forEach>

                                <!-- 다음 페이지 버튼 -->
                                <c:if test="${pVO.hasNext}">
                                    <li class="next">
                                        <a class="page-link" href="javascript:reloadPage(${pVO.nowPage + 1});">
                                            다음
                                        </a>
                                    </li>
                                </c:if>
                                <c:if test="${!pVO.hasNext}">
                                    <li class="next disabled">
                                        <a class="page-link" href="javascript:void(0);">
                                            다음
                                        </a>
                                    </li>
                                </c:if>

                            </ul>
                        </div>
                </div>

                <!-- 자주 묻는 질문 -->
                <div class="content" id="tap2">
                    <div class="FAQList">
                        <h2 class="search-tags">
                            <a class="${selectedTag == '' ? 'on' : ''}" style="cursor:pointer;" onclick="filterByTag('')">#전체</a>
                            <a class="${selectedTag == '쇼핑' ? 'on' : ''}" style="cursor:pointer;" onclick="filterByTag('쇼핑')">#쇼핑</a>
                            <a class="${selectedTag == '계정' ? 'on' : ''}" style="cursor:pointer;" onclick="filterByTag('계정')">#계정</a>
                            <a class="${selectedTag == '포인트' ? 'on' : ''}" style="cursor:pointer;" onclick="filterByTag('포인트')">#포인트</a>
                            <a class="${selectedTag == '애니' ? 'on' : ''}" style="cursor:pointer;" onclick="filterByTag('애니')">#애니</a>
                            <a class="${selectedTag == '서비스' ? 'on' : ''}" style="cursor:pointer;" onclick="filterByTag('서비스')">#서비스</a>
                        </h2>
                            <div class="f_header" style="height:auto; overflow:hidden; text-align:center;">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0" class="customer_table2">
                                    <colgroup>
                                        <col width="20">
                                        <col width="*">
                                    </colgroup>
                                    <tbody id="faqtag">
                                        <!-- FAQ 리스트 반복 출력 -->
                                        <c:forEach var="faq" items="${faqs}">
                                            <tr class="faq-question" style="cursor:pointer;">
                                                <td>Q</td>
                                                <td>
                                                    ${faq.question}  <!-- 질문 내용 -->
                                                    <img src="img/notice/down.png" class="toggle-icon" style="width: 16px; height: 16px; vertical-align: middle;">
                                                </td>
                                            </tr>

                                            <!-- 답변 항목 (초기에는 숨김 처리) -->
                                            <tr class="faq-answer" style="display: none;">
                                                <td colspan="2">
                                                    <div class="answer-text" style="padding: 15px; background: #333; color: #ddd;">
                                                        <p>A. ${faq.answer}</p> <!-- 답변 내용 -->
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                    </div>
                </div>


                <div class="content" id="tap3">
                    <div class="inquiry-container">
                        <!-- 카테고리 및 문의 영역 -->
                        <form class="inquiry-form" method="post" onsubmit = "return submitInquiry()" enctype="multipart/form-data">
                            <table class="inquiry-table">
                                <!-- 구매 관련 문의 -->
                                <tr>
                                    <th>구매 관련 문의</th>
                                    <td class="category-options">
                                        <label><input type="radio" name="qnatype" value="1"> 배송문의</label>
                                        <label><input type="radio" name="qnatype" value="2"> 주문문의</label>
                                        <label><input type="radio" name="qnatype" value="3"> 취소문의</label>
                                        <label><input type="radio" name="qnatype" value="4"> 반품문의</label>
                                        <label><input type="radio" name="qnatype" value="5"> 교환문의</label>
                                        <label><input type="radio" name="qnatype" value="6"> 환불문의</label>
                                    </td>
                                </tr>
                                <tr class="line"><td colspan="2"></td></tr>

                                <!-- 일반 상담 문의 -->
                                <tr>
                                    <th>일반 상담 문의</th>
                                    <td class="category-options">
                                        <label><input type="radio" name="qnatype" value="7"> 회원정보문의</label>
                                        <label><input type="radio" name="qnatype" value="8"> 회원제도문의</label>
                                        <label><input type="radio" name="qnatype" value="9"> 결제방법문의</label>
                                        <label><input type="radio" name="qnatype" value="10"> 상품문의</label>
                                    </td>
                                </tr>
                                <tr class="line"><td colspan="2"></td></tr>

                                <!-- 기타 문의 -->
                                <tr>
                                    <th>기타 문의</th>
                                    <td class="category-options">
                                        <label><input type="radio" name="qnatype" value="11"> 기타</label>
                                    </td>
                                </tr>
                                <tr class="line"><td colspan="2"></td></tr>

                                <!-- 아이디는 로그인된 사용자 아이디를 서버에서 불러와 표시 -->
                                <tr>
                                    <th>아이디</th>
                                    <td><span class="user-id" id="userid">{userid}</span></td>
                                </tr>
                                <tr class="line"><td colspan="2"></td></tr>

                                <!-- 제목 입력 -->
                                <tr>
                                    <th>제목</th>
                                    <td><input type="text" name="title" id="title" placeholder="제목을 입력하세요." class="inquiry-title"></td>
                                </tr>
                                <tr class="line"><td colspan="2"></td></tr>

                                <!-- 내용 입력 -->
                                <tr>
                                    <th>내용</th>
                                    <td><textarea name="content" id="content" placeholder="내용을 입력하세요." class="inquiry-content"></textarea></td>
                                </tr>
                                <tr class="line"><td colspan="2"></td></tr>

                                <!-- 사진 첨부 -->
                                <tr>
                                    <th>사진 첨부</th>
                                    <td>
                                        <input type="file" name="file" id="file" class="inquiry-attachment" multiple>
                                        <p class="attachment-info">파일용량은 최대 10MB로 제한되며, 1개의 파일을 첨부할 수 있습니다.</p>
                                    </td>
                                </tr>
                            </table>

                            <!-- 등록 버튼 -->
                            <div class="inquiry-submit">
                                <button type="submit" class="submit-btn">1:1 문의하기</button>
                            </div>
                        </form>
                    </div>
                </div>

                <div class="content" id="tap4">
                    <h2 class="tap4SelectTag">
                        <a class="tab-link active" data-content="terms" style="cursor:pointer">이용약관</a>
                        <a class="tab-link" data-content="privacy" style="cursor:pointer">개인정보 처리방침</a>
                    </h2>

                    <h2 id="termsTitle">서비스 이용약관</h2>
                    <div class="terms-content" id="termsContent">
                    <img src="/img/notice/tap4_1.png" alt="이용약관 이미지">
                    </div>
                </div>

            </div>
        </div>

    </div>
</div>


<script>
var token;
var userid;
var useridx;


//로그인한 아이디값 불러오기
setTimeout(function() {
    checkLoginStatus() ;
  // userid를 업데이트한 후에 DOM에 값을 설정
    document.getElementById("userid").innerText = userid;
},700);

//로그인 상태 확인 함수
function checkLoginStatus() {

    const token = localStorage.getItem("token");
        const loginDiv = document.querySelector(".sh_login");

        if (!loginDiv) {
            console.error(".sh_login 요소를 찾을 수 없습니다.");
            return;
        }

        if (!token) {
            loginDiv.innerHTML = `<button id="login_btn" onclick="location.href='/user/login'">로그인/가입</button>`;
        } else {
            $.ajax({
                url: "/getuser",
                type: "get",
                data: { Authorization: token },
                success: function(vo) {
                    userid = vo.userid;
                    useridx = vo.useridx;
                    loginDiv.innerHTML = `
                        <button id="login_btn" onclick="location.href='/user/mypage'">마이페이지</button>
                        <button id="login_btn" onclick="logout()">로그아웃</button>
                    `;
                    document.querySelector('.user-id').innerText = vo.userid; // 사용자 아이디 업데이트
                },
                error: function() {
                    console.error("로그인 여부 확인 실패");
                    loginDiv.innerHTML = `<button id="login_btn" onclick="location.href='/user/login'">로그인/가입</button>`;
                }
            });
        }
    };

    // tap3 탭 클릭 시 로그인 여부 확인
    document.querySelector('.notice_tab .list li:nth-child(3) .btn').addEventListener('click', function() {
        const token = localStorage.getItem("token"); // 토큰 값 가져오기

        if (!token) { // 토큰이 없으면
            alert('로그인이 필요합니다.');
            location.href = "/user/login"; // 로그인 페이지로 이동
            return; // 이후 코드 실행 중단
        }

        // 로그인된 사용자는 tap3의 콘텐츠에 접근 가능
        console.log('1:1 문의하기 탭에 접근했습니다.');
    });



    // 공지사항 탭 클릭 시 필터 초기화하고 전체 목록 요청
    document.getElementById('resetFilters').addEventListener('click', function() {
        // AJAX 요청으로 필터링 없이 전체 공지사항 리스트 요청
        $.ajax({
            url: '/notice2',
            type: 'GET',
            data: { keyword: '', tag: '' },  // 검색어와 태그를 초기화
            success: function (data) {
                // #noticeList 부분 업데이트
                const newNoticeList = $(data).find('#noticeList').html();
                document.getElementById('noticeList').innerHTML = newNoticeList;

                // 검색어 입력 필드 초기화
                document.getElementById('textSearch').value = '';
            },
            error: function (error) {
                console.log('Error:', error);
            }
        });
    });


    // 검색 실행 함수
    function triggerSearch() {
        const keyword = document.getElementById('textSearch').value;

        $.ajax({
            url: '/notice2',
            type: 'GET',
            data: { keyword },
            success: function (data) {
                document.getElementById('noticeList').innerHTML = $(data).find('#noticeList').html();
                document.getElementById('textSearch').value = '';  // 검색어 입력 필드 초기화
            },
            error: function (error) {
                console.log('Error:', error);
            }
        });
    }

    // 검색바 표시/숨기기 함수
    function toggleSearchBar(show) {
        document.querySelector('.tools').style.display = show ? 'block' : 'none';
    }

    // 탭 전환 시 검색바 처리
    document.querySelectorAll('.notice_tab .list li').forEach(tab => {
        tab.addEventListener('click', function() {
            const isNoticeTab = this.querySelector('.btn').innerText === '공지사항';
            toggleSearchBar(isNoticeTab);  // 공지사항 탭이면 검색바 표시, 아니면 숨김
        });
    });

    // 검색 버튼 클릭 또는 엔터 키 입력 시 검색 실행
    ['click', 'keypress'].forEach(eventType => {
        document.getElementById('textSearch').addEventListener(eventType, function(event) {
            if (eventType === 'click' || (eventType === 'keypress' && event.key === 'Enter')) {
                event.preventDefault();
                triggerSearch();
            }
        });
    });

    // 페이지 로드 시 검색바 고정 (공지사항 탭에서 시작할 경우)
    toggleSearchBar(true);  // 페이지 로드 시 항상 공지사항에는 검색바가 표시


    // 페이지 번호를 클릭했을 때 페이지 이동 함수
    function reloadPage(page) {
        console.log("선택한 페이지 번호: " + page);

        // 검색 키워드 값을 가져오기
        const searchKeyword = document.getElementById("textSearch").value || '';

        // URL 파라미터에 페이지와 검색 키워드를 추가하여 페이지 이동
        window.location.href = "/notice2?page=" + page + "&keyword=" + encodeURIComponent(searchKeyword);
    }

    //여기에 이벤트 펑션없이

    //자주 묻는 목록 항목에 대해 클릭 이벤트 추가
    function test(){
        document.querySelectorAll('.faq-question').forEach(question => {
            question.addEventListener('click', function () {
                // 현재 질문의 다음 요소(답변)를 가져옴
                const answer = this.nextElementSibling;

                console.log("list");
                // 모든 답변을 숨기고 아이콘을 초기화
                document.querySelectorAll('.faq-answer').forEach(ans => {
                    if (ans !== answer) ans.style.display = 'none';
                });

                document.querySelectorAll('.toggle-icon').forEach(icon => {
                    icon.src = "img/notice/down.png"; // 모든 아이콘을 down으로 초기화
                });

                // 현재 답변을 보이거나 숨기기
                answer.style.display = (answer.style.display === 'none' || answer.style.display === '') ? 'table-row' : 'none';

                // 아이콘 변경
                const icon = this.querySelector('.toggle-icon');
                icon.src = (answer.style.display === 'table-row') ? "img/notice/up.png" : "img/notice/down.png";
            });
        });
    }

    document.addEventListener('DOMContentLoaded', function () {
        // 기본값으로 #전체 태그에 'on' 클래스 추가
        const defaultTag = document.querySelector('.search-tags a');
        if (defaultTag) {
            defaultTag.classList.add('on');
        }

        test();
    });


    //자주 묻는 질문 태그로 검색
    function filterByTag(type){

        // 모든 태그에서 'on' 클래스를 제거
        document.querySelectorAll('.search-tags a').forEach(tag => {
            tag.classList.remove('on');
        });

        // 클릭한 태그에 'on' 클래스 추가
        const selectedTag = Array.from(document.querySelectorAll('.search-tags a')).find(tag => tag.textContent.includes(type) || (type === '' && tag.textContent.includes('전체')));
        if (selectedTag) {
            selectedTag.classList.add('on');
        }

        $.ajax({
            url:"/notice2/tap2",
            type:"post",
            data:{type:type},
            success:function(result){
            var list = result.list;
            var tag="";
            list.forEach(function(list) {
              tag += `<tr class="faq-question" style="cursor:pointer;">
                     <td>Q</td>
                     <td>
                         `+list.question+` <!-- 질문 내용 -->
                         <img src="img/notice/down.png" class="toggle-icon" style="width: 16px; height: 16px; vertical-align: middle;">
                     </td>
                 </tr>

                 <!-- 답변 항목 (초기에는 숨김 처리) -->
                 <tr class="faq-answer" style="display: none;">
                     <td colspan="2">
                         <div class="answer-text" style="padding: 15px; background: #333; color: #ddd;">
                             <p>A. `+list.answer+`</p> <!-- 답변 내용 -->
                         </div>
                     </td>
                 </tr>`
               });


               document.getElementById("faqtag").innerHTML = tag;
            test();
            }
        })
    }





    //탭1_공지사항_모달
    function setNoticeTitleClickEvents() {
        document.querySelectorAll('.noticeTitle').forEach(item => {
            item.addEventListener('click', function (e) {
                e.preventDefault(); // 링크 기본 동작 방지

                // 모달 내용 업데이트
                const date = this.getAttribute('data-date');
                const title = this.getAttribute('data-title');
                const content = this.getAttribute('data-content');

                document.getElementById('modalDate').innerHTML = date;
                document.getElementById('modalTitle').innerHTML = title;
                document.getElementById('modalContent').innerHTML = content;
                document.querySelector('.detail_layer_pop').style.display = 'flex'; // 모달 보이기

                console.log(date);
            });
        });
    }

    // 모달 닫기 이벤트 설정
    document.querySelector('.detail_layer_close').addEventListener('click', function () {
        document.querySelector('.detail_layer_pop').style.display = 'none'; // 모달 숨기기
    });

    // 초기 로드 시 공지사항의 모달 이벤트 설정
    setNoticeTitleClickEvents();

    // 탭 전환 시마다 이벤트 리스너 재설정 및 컨텐츠 갱신
    const tabList = document.querySelectorAll('.notice_tab .list li');
    const contentList = document.querySelectorAll('.content');

    tabList.forEach((tab, index) => {
        tab.querySelector('.btn').addEventListener('click', function (e) {
            e.preventDefault();
            // 모든 탭 비활성화 및 콘텐츠 숨김
            tabList.forEach(t => t.classList.remove('selected'));
            contentList.forEach(content => content.classList.remove('active'));

            // 현재 탭 활성화 및 해당 콘텐츠 보여줌
            tab.classList.add('selected');
            contentList[index].classList.add('active');

            // 공지사항 탭으로 돌아왔을 때 이벤트 리스너 재설정
            if (index === 0) { // 공지사항 탭인 경우
                setNoticeTitleClickEvents();
            }
        });
    });

    // 공지사항 리스트가 AJAX로 새로 로드될 때마다 이벤트 리스너 재설정
    $(document).ajaxComplete(function() {
        setNoticeTitleClickEvents();
    });



    // 1:1 문의 제출 함수
    function submitInquiry() {
        //let formData = new FormData(document.querySelector(".inquiry-form"));
        let formData = new FormData();

        // 입력 필드 값 가져오기
        let qnatype = document.querySelector('input[name="qnatype"]:checked').value;
        let title = document.querySelector('.inquiry-title').value;
        let content = document.querySelector('.inquiry-content').value;
        let files = document.querySelector('.inquiry-attachment').files;
         console.log("1", Array.from(formData.entries()));
            // FormData에 입력 값 추가
            formData.append("qnatype", qnatype);
            formData.append("title", title);
            formData.append("content", content);

            // 파일 추가
            for (let i = 0; i < files.length; i++) {
                formData.append('file', files[i]);
            }

            // 로컬 스토리지에서 JWT 토큰 가져오기
            const token = localStorage.getItem("token");
            console.log(token);
            if (!token) {
                alert('로그인이 필요합니다.');
                location.href = "/user/login";
                return false;
            }

            // 서버로 전송할 데이터를 FormData 객체에 추가
            // var formData = new FormData();
            //formData.append("file", file);

            console.log("2", Array.from(formData.entries()));
            $.ajax({
                url: '/inquirySubmit',
                type: 'POST',
                data: formData,
                processData: false, // 데이터를 기본 문자열로 처리하지 않음
                contentType: false, // "multipart/form-data"로 전송
                headers: {
                        "Authorization": "Bearer "+token   // JWT 토큰을 Authorization 헤더에 포함
                  },
                success: function(response) {
                    alert('1:1 문의가 등록되었습니다.');
                    location.reload(); // 성공 시 페이지 새로고침
                },
                error: function(xhr) {
                    if (xhr.status === 401) {
                        alert('인증에 실패했습니다. 다시 로그인하세요.');
                        location.href = "/user/login";  // 로그인 페이지로 이동
                    } else if (xhr.status === 404) {
                        alert('서버에서 요청을 찾을 수 없습니다.');
                    } else {
                        alert("요청 처리 중 오류가 발생했습니다.");
                    }
                    console.error("Error:", xhr);  // 오류 출력
                }
            });

        return false;  // 기본 폼 제출 방지
    }

    <!-- tap4 -->
    document.addEventListener("DOMContentLoaded", function () {
        // 기본으로 '이용약관'에 'on' 클래스 추가
        document.querySelector("#tap4 .tab-link[data-content='terms']").classList.add("on");

        // 탭 클릭 이벤트 설정
        document.querySelectorAll("#tap4 .tab-link").forEach(link => {
            link.addEventListener("click", function () {
                // 모든 탭에서 'on' 클래스 제거
                document.querySelectorAll("#tap4 .tab-link").forEach(l => l.classList.remove("on"));

                // 클릭한 탭에 'on' 클래스 추가
                this.classList.add("on");

                // 탭에 따라 내용 변경
                const contentType = this.getAttribute("data-content");
                const termsTitle = document.getElementById("termsTitle");
                const termsContent = document.getElementById("termsContent");

                if (contentType === "terms") {
                    termsTitle.textContent = "서비스 이용약관";
                    termsContent.innerHTML = '<img src="/img/notice/tap4_1.png" alt="이용약관 이미지">';
                } else if (contentType === "privacy") {
                    termsTitle.textContent = "개인정보 처리방침";
                    termsContent.innerHTML = '<img src="/img/notice/tap4_2.png" alt="개인정보 처리방침 이미지">';
                }
            });
        });
    });


$(function(){
    // URL에서 현재 페이지의 검색어를 확인
    const currentPath = window.location.pathname;

    if (currentPath.includes("qnaWirte")) {
        // 모든 탭에서 'selected' 클래스를 제거하고 'content' 클래스를 숨김
        $('.notice_tab .list li').removeClass('selected');
        $('.content').removeClass('active');

        // 1:1 문의 탭 활성화 및 관련 컨텐츠 보이기
        $('.notice_tab .list li:nth-child(3)').addClass('selected'); // 1:1 문의 탭
        $('#tap3').addClass('active'); // 1:1 문의 내용

        // 검색바 숨기기
        toggleSearchBar(false);

        // URL에 #tap3을 추가하는 대신 스크롤을 맨 위로 이동
        history.replaceState(null, null, " "); // URL에서 #tap3 제거
        window.scrollTo(0, 0); // 페이지 맨 위로 스크롤
    }
});


</script>

<%@include file="/WEB-INF/inc/footer.jspf"%>