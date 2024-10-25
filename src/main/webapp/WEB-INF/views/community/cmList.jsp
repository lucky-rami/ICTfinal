<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf" %>



<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<link href="/css/cmList.css" rel="stylesheet" type="text/css">
</link>




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
    <div class="cummunity" id="content">
        <form id="searchForm" action="/cmList" method="get">
         <input type="hidden" id="commtype" name="commtype" value="${commtype}" />  <!-- Hidden 필드 추가 -->
            <!-- 커뮤니티 타입 선택 드롭다운 (예: 자랑, 친목) -->
            <div class="search-area">
                <div class="left-area">


                    <!-- 정렬 조건 선택 -->
                    <div class="filter-box">
                        <select id="selectDirection" name="orderBy" onchange="submitSearchForm()">
                            <option value="DEFAULT" ${orderBy == 'DEFAULT' ? 'selected="selected"' : ''}>최신글순</option>
                            <option value="NEW_REPLY" ${orderBy == 'NEW_REPLY' ? 'selected="selected"' : ''}>최신댓글순</option>
                            <option value="REPLY" ${orderBy == 'REPLY' ? 'selected="selected"' : ''}>많은댓글순</option>
                            <option value="LIKE" ${orderBy == 'LIKE' ? 'selected="selected"' : ''}>조회수순</option>
                        </select>
                    </div>
                </div>

                <!-- 검색 조건 -->
                <div class="right-area">
                    <!-- 검색 카테고리 선택 -->
                    <div class="select-box">
                        <select id="selectCategory" name="searchCategory">
                            <option value="TITLE_AND_CONTENT" ${searchCategory == 'TITLE_AND_CONTENT' ? 'selected' : ''}>제목 + 내용</option>
                            <option value="TITLE" ${searchCategory == 'TITLE' ? 'selected' : ''}>제목</option>
                            <option value="CONTENT" ${searchCategory == 'CONTENT' ? 'selected' : ''}>내용</option>
                        </select>
                    </div>

                    <!-- 검색어 입력 -->
                    <div class="search">
                        <input type="text" id="searchKeyword" name="searchKeyword" value="${searchKeyword}" placeholder="검색어를 입력하세요." />
                        <button type="button" id="btnSearch" onclick="submitSearchForm()">검색</button>
                    </div>
                </div>
            </div>
        </form>

        <!-- 공통 커뮤니티 리스트 -->
        <div class="content active" id="contentList">
            <div class="allList">
                <div class="row header">
                    <div class="col-sm-1 p-2">번호</div>
                    <div class="col-sm-1 p-2">카테고리</div>
                    <div class="col-sm-7 p-2">제목</div>
                    <div class="col-sm-2 p-2">날짜</div>
                    <div class="col-sm-1 p-2">조회</div>
                </div>

                <!-- 통합된 리스트 출력 -->
                <c:forEach var="vo" items="${list}">
                    <!-- commtype을 문자열로 변환 후 비교 -->
                    <c:if test="${commtype eq 'all' || (vo.commtype eq '자랑' && commtype eq 10) || (vo.commtype eq '친목' && commtype eq 30) || (vo.commtype eq '팬아트' && commtype eq 40)}">
                        <div class="row">
                            <div class="col-sm-1 p-2">${vo.idx}</div>
                            <div class="col-sm-1 p-2">${vo.commtype}</div>
                            <div class="col-sm-7 p-2"><a href="/cmView/${vo.idx}">${vo.title}</a></div>
                            <div class="col-sm-2 p-2">${vo.regDT}</div>
                            <div class="col-sm-1 p-2">${vo.hit}</div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>

        <div class="new_write" style="display:none;">
            <a href="/cmWrite">글쓰기</a>
        </div>
    </div>
    <div class="pagination">
        <ul class="paging">
        <c:out value="현재 페이지: ${pagingVO.nowPage}, 전체 페이지 수: ${pagingVO.totalPage}, 시작 페이지: ${pagingVO.startPage}, 끝 페이지: ${pagingVO.endPage}" />


            <!-- 이전 페이지 버튼 -->
            <c:if test="${pagingVO.hasPrevious}">
                <li class="pre">
                    <a class="page-link" href="javascript:reloadPage(${pagingVO.nowPage - 1});">
                        <img src="/img/cm/left-chevron.png" style="width:20px; height:18px;" />
                    </a>
                </li>
            </c:if>
            <c:if test="${!pagingVO.hasPrevious}">
                <li class="pre disabled">
                    <a class="page-link" href="javascript:void(0);">
                        <img src="/img/cm/left-chevron.png" style="width:20px; height:18px;" />
                    </a>
                </li>
            </c:if>

            <!-- 페이지 번호 -->
            <c:forEach var="p" begin="${pagingVO.startPage}" end="${pagingVO.endPage}">
                <c:if test="${p <= pagingVO.totalPage}">
                    <li class="${p == pagingVO.nowPage ? 'page active' : 'page'}">
                        <a class="page-link" href="javascript:reloadPage(${p});">${p}</a>
                    </li>
                </c:if>
            </c:forEach>

            <!-- 다음 페이지 버튼 -->
            <c:if test="${pagingVO.hasNext}">
                <li class="next">
                    <a class="page-link" href="javascript:reloadPage(${pagingVO.nowPage + 1});">
                        <img src="/img/cm/right-chevron.png" style="width:20px; height:18px;" />
                    </a>
                </li>
            </c:if>
            <c:if test="${!pagingVO.hasNext}">
                <li class="next disabled">
                    <a class="page-link" href="javascript:void(0);">
                        <img src="/img/cm/right-chevron.png" style="width:20px; height:18px;" />
                    </a>
                </li>
            </c:if>

        </ul>
    </div>

</div>


<script>

// 전체 탭을 클릭하면 필터를 초기화하고 탭 필터링을 수행
function resetFilterAndShowTab(commtype) {
    document.getElementById("commtype").value = "all"; // 드롭다운 초기화
    document.getElementById("searchKeyword").value = ''; // 검색어 초기화
    document.getElementById("selectDirection").value = "DEFAULT"; // 정렬 조건 초기화
    document.getElementById("selectCategory").selectedIndex = 0; // 검색 카테고리 초기화
    showTab(commtype); // 탭 전환
}

// showTab 함수 정의(탭 전환)
function showTab(commtype) {
    // 검색 폼에 commtype 값을 설정
    document.getElementById("commtype").value = commtype;

    // 검색 조건 초기화
    document.getElementById("searchKeyword").value = '';

    // 드롭다운 요소가 존재하는지 확인하고 정렬 조건 초기화
    var orderByElement = document.getElementById("selectDirection");
    if (orderByElement) {
        orderByElement.value = "DEFAULT"; // 정렬 조건 초기화
    }

    // 검색 카테고리 초기화
    var searchCategoryElement = document.getElementById("selectCategory");
    if (searchCategoryElement) {
        searchCategoryElement.selectedIndex = 0; // 검색 카테고리 초기화
    }

    // 폼 제출
    document.getElementById("searchForm").submit();
}

// 검색 폼 제출 후 검색어 입력 필드를 초기화하는 함수
function submitSearchForm() {
    // 검색어 필드 초기화
    document.getElementById("searchKeyword").value = '';

    // 검색 카테고리 초기화
    var searchCategoryElement = document.getElementById("selectCategory");
    if (searchCategoryElement) {
        searchCategoryElement.selectedIndex = 0; // 검색 카테고리 초기화
    }

    // 폼을 제출합니다.
    document.getElementById("searchForm").submit();
}

// 페이지 번호를 클릭했을 때 페이지 이동 함수
function reloadPage(page) {
    console.log("선택한 페이지 번호: " + page);
    const commtype = document.getElementById("commtype").value || 'all';
    const orderBy = document.getElementById("selectDirection").value || 'DEFAULT';
    const searchCategory = document.getElementById("selectCategory").value || 'TITLE_AND_CONTENT';
    const searchKeyword = document.getElementById("searchKeyword").value || '';

    // URL 파라미터에 초기화된 값을 사용하여 페이지를 이동
    window.location.href = `/cmList?commtype=${commtype}&page=${2}&orderBy=${orderBy}&searchCategory=${searchCategory}&searchKeyword=${searchKeyword}`;

}



var useridx; // 해당 페이지에서 모두 사용 가능하도록! 전역변수로 선언
var userid;

setTimeout(function() {


    // 커뮤니티 페이지 전용 로그인 상태 확인 함수 호출
    checkLoginStatusForCommunity();
}, 400);

// 커뮤니티 페이지 전용 로그인 상태 확인 함수
function checkLoginStatusForCommunity() {
    var token = localStorage.getItem("token"); // 토큰 값 가져오기
    if (token && token.trim() !== "") { // 토큰이 존재하는지 확인
        $.ajax({
            url: "/getuser",
            type: "get",
            data: { Authorization: token },
            success: function(vo) {
                userid = vo.userid;
                useridx = vo.idx;

                if (userid && userid.trim() !== "") {
                    // 로그인 상태일 때 글쓰기 버튼을 표시
                    document.querySelector(".new_write").style.display = "block";
                } else {
                    // 로그인되지 않은 상태일 때 글쓰기 버튼을 숨김
                    document.querySelector(".new_write").style.display = "none";
                }
            },
            error: function() {
                console.error("로그인 여부 확인 실패");
                document.querySelector(".new_write").style.display = "none";
            }
        });
    } else {
        // 토큰이 없을 때 글쓰기 버튼 숨김
        document.querySelector(".new_write").style.display = "none";
    }
}

</script>

</body>

</html>