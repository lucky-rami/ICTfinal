
/*@@@@@@@@@@@@@장르@@@@@@@@@@*/
$(document).ready(function () {
    // 애니메이션 카드 클릭 시 첫 번째 모달 열기
    $('.list_img_bg').on('click', function (e) {
        e.preventDefault(); // 기본 동작 방지 (필요 시)
        const title = $(this).data('title'); // 클릭한 카드의 제목 가져오기
        const imageSrc = $(this).find('img').attr('src'); // 클릭한 카드의 이미지 경로 가져오기
        const genre = $(this).find('.genre span').first().text(); // 장르 텍스트 가져오기
        const age = $(this).find('.genre span').last().text(); // 나이 관람 텍스트 가져오기
        const director = $(this).data('director'); // 감독 정보 가져오기
        const idx = $(this).data('idx');
        const grade = $(this).data('grade');

        // AJAX 요청으로 줄거리 및 비슷한 애니메이션 가져오기
        $.ajax({
            url: '/aniDetail', // 줄거리를 가져올 API URL
            method: 'GET',
            data: { title:title,
            idx:idx
            }, // 제목을 요청 파라미터로 전달
            success: function (response) {
                console.log("AJAX 응답:", response); // 응답 데이터 확인
                $(".modal-grade").text(response.grade);
            const summary = response.outline; // 줄거리
            const registrationDate = response.registrationDate; // 등록일

            // 줄거리 길이 조절
            const shortSummary = summary.length > 1000 ? summary.substring(0, 100) + '...' : summary;
            /*alert(idx);*/


    console.log(idx);
    // 모달 내용 업데이트
    $('.animodal_usergrade').attr('data-idx',idx)
    $('.animodal_item_infoDiv h1').text(title);
    $('.animodal_item_imgBack img').attr('src', imageSrc);
    $('.animodal_item_imgDiv img').attr('src', imageSrc);
    $('.animodal_item_infoDiv .genre').text(` 장르: ${response.anitype}`);
    $('.animodal_item_infoDiv .age').text(` 등급: ${response.agetype}관람  `);
    $('.animodal_item_infoDiv .director').text(` 감독: ${director}`);
    $('.animodal_item_infoDiv .ani_outline .summary').text(shortSummary);
    $('.animodal_item_infoDiv .ani_outline .ouline_more').data('full-summary', summary);


    // 비슷한 애니메이션 목록 업데이트
    const similarAnis = response.randomSimilarAnis || []; // 비슷한 애니메이션 목록 가져오기
    const randomSimilarAnis = similarAnis.sort(() => 0.5 - Math.random()).slice(0, 5);
const similarAniList = similarAnis.map(ani => `
    <li>
        <img src="${pageContext.request.contextPath}/img/ani_img/${ani.post_img}" alt="${ani.title}">
        <p>${ani.title}</p>
    </li>
`).join('');
$('.bottom_ani_content ul').html(similarAniList); // 비슷한 애니메이션 목록 추가

 // 제작 정보 섹션 업데이트 (감독, 등록일)
                $('.director-name').text(director); // 감독 정보
                $('.registration-date').text(response.regDT); // 등록일 정보

                const token = localStorage.getItem("token");

                $.ajax({
                    url: '/checkLikeStatus', // 좋아요 상태 확인 API
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + token // 토큰 추가
                    },
                    data: { ani_idx: idx},
                    success: function (isLiked) {
                        // 하트 아이콘 상태 업데이트
                        const likeIcon = document.getElementById("likeIcon");
                        if (isLiked) {
                            likeIcon.classList.remove('fa-regular');
                            likeIcon.classList.add('fa-solid');
                        } else {
                            likeIcon.classList.remove('fa-solid');
                            likeIcon.classList.add('fa-regular');
                        }
                    },
                    error: function (error) {
                        console.error('좋아요 상태 확인 오류:', error);
                    }
                });



// 모달 열기
$('.animodal_body').fadeIn();
},
            error: function (jqXHR, textStatus, errorThrown) {
                console.error('Error status:', textStatus);
                console.error('Error thrown:', errorThrown);
                alert('줄거리를 가져오는 데 실패했습니다. 오류: ' + textStatus);
            }
        });
    });



    // 첫 번째 모달 닫기 (X 버튼 클릭 시)
    $('.animodal_body').on('click', '.fa-x', function (e) {
        e.stopPropagation(); // 이벤트 전파 중지
        $('.animodal_body').fadeOut(); // 첫 번째 모달을 닫기 위해 fadeOut 효과를 사용
        setRating(0);

            // 좋아요 상태 초기화
/*    isLiked = false; // 상태 초기화
    const likeIcon = document.getElementById("likeIcon");
    updateLikeIcon(likeIcon, isLiked); // 아이콘 초기화*/
    });


    // 첫 번째 모달 닫기 (배경 클릭 시)
    $('.animodal_body').on('click', '.animodal_background', function () {
        $('.animodal_body').fadeOut(); // 첫 번째 모달을 닫기 위해 fadeOut 효과를 사용
    });

    // "더 보기" 클릭 시 두 번째 모달 열기 및 전체 줄거리 표시
    $('.animodal_body').on('click', '.ouline_more', function (e) {
        e.stopPropagation(); // 이벤트 전파 중지

        const fullSummary = $(this).data('full-summary'); // data 속성에서 전체 줄거리 가져오기

        if (!fullSummary) {
            console.error('Full summary is not available.');
            return;
        }

        console.log("전체 줄거리:", fullSummary); // 전체 줄거리 확인

        // 두 번째 모달의 줄거리 업데이트
        $('.aniInfo_modal_body .ani-outline').text(fullSummary); // 두 번째 모달의 줄거리 업데이트

        // 두 번째 모달 열기
        $('.aniInfo_modal_body').fadeIn();
    });

    // 두 번째 모달 닫기 (X 버튼 클릭 시)
    $('.aniInfo_modal_body').on('click', '.fa-x', function (e) {
        e.stopPropagation(); // 이벤트 전파 중지
        $('.aniInfo_modal_body').fadeOut(); // 두 번째 모달만 닫기
    });

    // 두 번째 모달 닫기 (배경 클릭 시)
    $('.aniInfo_modal_body').on('click', function (e) {
        if ($(e.target).is('.aniInfo_modal_body > div:first-child')) {
            $('.aniInfo_modal_body').fadeOut(); // 두 번째 모달만 닫기
        }
    });
});

/*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
$(document).ready(function() {
    $('.div_li').each(function() {
        const aniType = $(this).data('anitype'); // 장르 가져오기
        const ageType = $(this).data('agetype'); // 나이 관람 가져오기

        // 콘솔 로그 추가
        console.log('장르:', aniType);
        console.log('나이 관람:', ageType);

        // HTML 업데이트
        $(this).find('.genre').html(`
            <span>${aniType}</span> <!-- 장르 -->
            <span>${ageType} 관람</span> <!-- 나이 관람 -->
        `);
    });
});
/*@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@검색@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
function searchTable() {
    var input, filter, aniList, divLi, title, i, txtValue;
    input = document.getElementById("searchInput"); // 검색 입력 필드
    filter = input.value.toLowerCase(); // 입력된 검색어
    aniList = document.querySelectorAll(".ani_viewList .div_li"); // 애니메이션 목록

    for (i = 0; i < aniList.length; i++) {
        divLi = aniList[i];
        title = divLi.querySelector("p").textContent || divLi.querySelector("p").innerText; // 제목 텍스트 가져오기
        if (title.toLowerCase().indexOf(filter) > -1) {
            divLi.style.display = ""; // 검색어와 일치하면 보여줌
        } else {
            divLi.style.display = "none"; // 검색어와 일치하지 않으면 숨김
        }
    }
}
 $(document).ready(function () {
            $('.filter').on('change', function () {
                var selectedGenres = $('.filter:checked').map(function () {
                    return $(this).val();
                }).get();

                $('.div_li').show(); // 모든 애니메이션 항목 보여주기

                if (selectedGenres.length > 0) {
                    $('.div_li').filter(function () {
                        return !selectedGenres.includes($(this).data('anitype')); // data-anitype을 사용하여 필터링
                    }).hide();
                }
            });
        });
        /*@@@@@@@@@@@@@@@@@@@@@@@@@왼쪽 필터링@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
$(document).ready(function () {
    $('.filter').change(function () {
        const selectedGenres = [];
        const selectedTags = [];
        const selectedAges = [];

        // 장르 필터링
        $('input[type="checkbox"][value]').each(function () {
            if ($(this).is(':checked')) {
                selectedGenres.push($(this).val());
            }
        });

        // 태그 필터링
        $('input[type="checkbox"][data-tag]').each(function () {
            if ($(this).is(':checked')) {
                selectedTags.push($(this).data('tag'));
            }
        });

        // 연령 필터링
        $('input[type="checkbox"][data-age]').each(function () {
            if ($(this).is(':checked')) {
                selectedAges.push($(this).data('age'));
            }
        });

        $('.div_li').each(function () {
            const genreMatch = selectedGenres.length ? selectedGenres.includes($(this).find('.genre span:first-child').text().trim()) : true;
            const tagMatch = selectedTags.length ? selectedTags.some(tag => $(this).data('tags') && $(this).data('tags').includes(tag)) : true;

            // 수정된 ageMatch
            const ageType = $(this).data('agetype'); // 애니메이션 항목의 연령 데이터 가져오기
            const ageMatch = selectedAges.length ? selectedAges.includes(ageType) : true;

            if (genreMatch && tagMatch && ageMatch) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });
});
/*@@@@@@@@@@@@@@@@@@@@@오른쪽 필터@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
// 애니메이션 목록을 서버에서 받아오는 함수
function fetchAniList() {
    $.ajax({
        url: "/aniList", // 모든 애니메이션 목록을 가져오는 URL
        type: "GET",
        success: function(data) {
            aniList = data; // 전체 애니메이션 목록을 저장
            renderAniList(aniList); // 목록을 렌더링
        },
        error: function(xhr, status, error) {
            console.error("Error fetching ani list: ", error);
        }
    });
}

// 정렬 함수
function sortAniList(sortCriteria) {
    let sortedList = aniList;

    switch (sortCriteria) {
        case 'title':
            sortedList = aniList.sort((a, b) => a.grade - b.grade); // 별점 기준 정렬
            break;
        case 'new':
            sortedList = aniList.sort((a, b) => new Date(b.regDT) - new Date(a.regDT)); // 등록일 기준 정렬
            break;
        case 'popular':
            sortedList = aniList.sort((a, b) => b.anilike - a.anilike); // 좋아요 수 기준 정렬
            break;
        default:
            sortedList = aniList; // 기본 정렬 (변경 없음)
    }

    renderAniList(sortedList); // 정렬된 리스트 렌더링
}

// 드롭다운의 값이 변경될 때 호출되는 함수
function onSortChange() {
    const sortBy = document.querySelector('.vod_select').value; // 선택된 정렬 기준 가져오기

    $.ajax({
        url: "/sort", // 정렬된 애니메이션 목록을 가져오는 URL
        type: "GET",
        data: { sort: sortBy },
        success: function(data) {
            aniList = data; // 정렬된 목록 저장
            renderAniList(aniList); // 목록을 렌더링
            const aniListString = JSON.stringify(aniList)
        },
        error: function(xhr, status, error) {
            console.error("Error fetching ani list: ", error);
        }
    });
}

// 애니메이션 목록을 화면에 렌더링하는 함수
function renderAniList(list) {
    let tag = '';

    list.forEach(function (el) {
        tag += `
        <div class="div_li" data-anitype="`+el.anitype+`" data-agetype="`+el.agetype+`">
            <div class="list_img_bg" data-title="`+el.title+`" data-director="`+el.director+`" data-idx="`+el.idx+`">
                <img src="http://192.168.1.92:8000/`+el.post_img+`" alt="`+el.title+`">
                <div class="overlay">상세 보기</div>
            </div>
            <p>${el.title}</p>
            <p class="genre">
                <span>`+el.anitype+`</span> <!-- 장르 문자열 -->
                <span>`+el.agetype+`관람</span> <!-- 나이 관람 문자열 -->
            </p>
        </div>`;
    });

    document.querySelector(".ani_viewList").innerHTML = tag;

    // 렌더링 후 모달 이벤트를 다시 바인딩해야 함
    bindModalEvents();
}

// 모달을 열고 정보를 표시하는 함수
function bindModalEvents() {
    $('.list_img_bg').on('click', function (e) {
        e.preventDefault(); // 기본 동작 방지
        const title = $(this).data('title');
        const imageSrc = $(this).find('img').attr('src');
        const director = $(this).data('director');
        const idx = $(this).data('idx');
        console.log("테스트"+idx)
        // AJAX 요청으로 줄거리 및 비슷한 애니메이션 가져오기
        $.ajax({
            url: '/aniDetail',
            method: 'GET',
            data: { title: title },
            success: function (response) {
                const summary = response.outline;
                const shortSummary = summary.length > 1000 ? summary.substring(0, 100) + '...' : summary;
/*별점 초기화 시킬곳*/
                // 모달 내용 업데이트
                $('.animodal_usergrade').attr('data-idx', idx);
                $('.animodal_item_infoDiv h1').text(title);
                $('.animodal_item_imgBack img').attr('src', imageSrc);
                $('.animodal_item_imgDiv img').attr('src', imageSrc);
                $('.animodal_item_infoDiv .genre').text(` 장르: ${response.anitype}`);
                $('.animodal_item_infoDiv .age').text(` 등급: ${response.agetype}관람  `);
                $('.animodal_item_infoDiv .director').text(` 감독: ${director}`);
                $('.animodal_item_infoDiv .ani_outline .summary').text(shortSummary);
                $('.animodal_item_infoDiv .ani_outline .ouline_more').data('full-summary', summary);


                // 비슷한 애니메이션 목록 업데이트
                const similarAnis = response.randomSimilarAnis || [];
                const similarAniList = similarAnis.map(ani => `
                    <li>
                        <img src="${pageContext.request.contextPath}/img/ani_img/${ani.post_img}" alt="${ani.title}">
                        <p>${ani.title}</p>
                    </li>
                `).join('');
                $('.bottom_ani_content ul').html(similarAniList);

                // 모달 열기
                $('.animodal_body').fadeIn();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error('Error fetching ani detail:', textStatus, errorThrown);
                alert('줄거리를 가져오는 데 실패했습니다.');
            }
        });
    });

    // 모달 닫기 이벤트
    $('.animodal_body').on('click', '.fa-x, .animodal_background', function (e) {
        e.stopPropagation();
        $('.animodal_body').fadeOut();
    });
}

// 페이지 로드 시 애니메이션 목록 요청
$(document).ready(function() {
    fetchAniList();

    // 정렬 드롭다운 변경 시 처리
    $('.vod_select').on('change', onSortChange);
});
/*@@@@@@@@@@@@@@@@@@@@@@@@@@@@별점@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
let currentRating = 0;

// 별점 설정 함수
function setRating(rating) {
    currentRating = rating;
    const stars = document.querySelectorAll("#stars i");
    stars.forEach((star, index) => {
        if (index < rating) {
            star.classList.add("fas"); // 채워진 별 (fa-solid)
            star.classList.remove("fa-regular");
        } else {
            star.classList.remove("fas");
            star.classList.add("fa-regular"); // 빈 별 (fa-regular)
        }
    });
}

// 선택된 별점 가져오기
function getSelectedGrade() {
    return currentRating;
}

// 서버로 데이터 전송 함수
function submitRating() {
    const modal = document.getElementById('animodal_usergrade');
    const aniIdx = modal.getAttribute('data-idx'); // jQuery로 data-idx 값 가져오기
    /*console.log("모달에서 가져온 인덱스:", aniIdx);*/
    const grade = getSelectedGrade();
    const token = localStorage.getItem("token");

    // 유효성 검사
    if (!aniIdx) {
        alert("애니메이션 인덱스가 제공되지 않았습니다.");
        return;
    }
    if (grade === 0) {
        alert("별점을 선택해 주세요.");
        return;
    }
    if (!token) {
        alert("로그인이 필요합니다. 로그인 후 다시 시도해 주세요.");
        window.location.href = '/user/login';
        return;
    }

    const gradeData = {
        ani_idx: aniIdx,
        grade: grade
    };

    /*console.log("전송할 데이터:", gradeData);*/

    $.ajax({
        url: '/addGrade',
        type: 'POST',
        contentType: 'application/json',
        dataType: 'json',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        data: JSON.stringify(gradeData),
        success: function (response) {
            if (response.message) {
                alert(response.message);
            } else {
                alert("별점이 성공적으로 등록되었습니다.");
            }
        },
        error: function (xhr) {
            console.error(xhr);
            let errorMessage = '별점 등록에 실패했습니다.';
            if (xhr.responseJSON && xhr.responseJSON.error) {
                errorMessage = xhr.responseJSON.error;
            } else if (xhr.status === 401) {
                errorMessage = '인증 실패. 로그인 상태를 확인해주세요.';
                window.location.href = '/user/login';
            } else if (xhr.status === 404) {
                errorMessage = '요청한 경로를 찾을 수 없습니다.';
            } else if (xhr.status === 500) {
                errorMessage = '서버 내부 오류입니다.';
            }
            alert(errorMessage);
        }
    });
}

/*@@@@@@@@@@@@@@@@@@@@@좋아요@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
// 각 애니메이션의 좋아요 상태를 저장하기 위한 객체
const likeStatus = {};

// 좋아요 상태 업데이트 함수
function updateLikeIcon(icon, liked) {
    if (liked) {
        icon.classList.remove('fa-regular'); // 빈 하트
        icon.classList.add('fa-solid'); // 채운 하트
    } else {
        icon.classList.remove('fa-solid'); // 채운 하트
        icon.classList.add('fa-regular'); // 빈 하트
    }
}

// 좋아요 버튼 클릭 시 호출되는 함수
function toggleLike() {
    const ani_idx = document.querySelector('.animodal_usergrade').getAttribute('data-idx'); // 현재 애니메이션 인덱스 가져오기
    const token = localStorage.getItem("token"); // 사용자 토큰에서 가져오기

    if (!token) {
        alert("로그인이 필요합니다. 로그인 후 다시 시도해 주세요.");
        window.location.href = '/user/login';
        return;
    }

    const likeIcon = document.getElementById("likeIcon");

    // 좋아요 상태를 가져옵니다.
    const isLiked = likeStatus[ani_idx] || false; // 존재하지 않으면 기본값 false

    // 좋아요 상태 반전
    likeStatus[ani_idx] = !isLiked; // 상태 반전

    if (likeStatus[ani_idx]) {
        // 좋아요 추가
        updateLikeIcon(likeIcon, true);

        // DB에 좋아요 추가 요청
        fetch('/aniLike', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token // 토큰 추가
            },
            body: JSON.stringify({ ani_idx })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to add like');
            }
            console.log("좋아요가 추가되었습니다.");
            // 상태를 로컬 스토리지에 저장
            localStorage.setItem(`liked_${ani_idx}`, JSON.stringify(true));
        })
        .catch(error => {
            console.error("Error:", error);
            likeStatus[ani_idx] = false; // 상태 초기화
            updateLikeIcon(likeIcon, false); // 아이콘 업데이트
        });
    } else {
        // 좋아요 취소
        updateLikeIcon(likeIcon, false);

        // DB에서 좋아요 삭제 요청
        fetch('/removeLike', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token // 토큰 추가
            },
            body: JSON.stringify({ ani_idx })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to remove like');
            }
            console.log("좋아요가 취소되었습니다.");
            // 상태를 로컬 스토리지에 저장
            localStorage.setItem(`liked_${ani_idx}`, JSON.stringify(false));
        })
        .catch(error => {
            console.error("Error:", error);
            likeStatus[ani_idx] = true; // 상태 초기화
            updateLikeIcon(likeIcon, true); // 아이콘 업데이트
        });
    }
}

// 페이지 로드 시 초기 상태 설정
document.addEventListener('DOMContentLoaded', () => {
    const ani_idx = document.querySelector('.animodal_usergrade').getAttribute('data-idx');
    const token = localStorage.getItem("token");

    // 서버에 요청하여 초기 좋아요 상태를 가져옴
    if (token) {
        fetch(`/checkLikeStatus?ani_idx=${ani_idx}&useridx=${useridx}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token // 토큰 추가
            }
        })
        .then(response => response.json())
        .then(isLiked => {
            console.log("서버에서 받은 좋아요 상태:", isLiked); // 여기서 값 확인
            // 서버에서 받아온 상태에 따라 1은 true, 0은 false로 설정
            likeStatus[ani_idx] = (isLiked === 1); // 서버에서 받아온 상태로 초기화
            const likeIcon = document.getElementById("likeIcon");
            updateLikeIcon(likeIcon, likeStatus[ani_idx]); // UI 업데이트
        })
        .catch(error => console.error("Error:", error));
    }
});
/*@@@@@@@@@@@@@@@@@@@@@비슷한 영상@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/


$(document).ready(function() {
    // 상세보기 버튼 클릭 이벤트
    $(document).on('click', '.list_img_bg', function() {
        const aniIdx = $(this).data('idx'); // 클릭한 버튼의 data-idx 속성 값 가져오기
        console.log('Selected aniIdx:', aniIdx); // 선택된 aniIdx 로그 출력

        if (aniIdx === undefined) {
            console.error('aniIdx is undefined. Check your HTML structure.');
            return; // aniIdx가 undefined일 경우 요청하지 않음
        }

        // AJAX 요청으로 애니메이션 상세 정보 가져오기
        setTimeout(function() {
            // AJAX 요청으로 애니메이션 상세 정보 가져오기
            $.ajax({
                url: `/aniDetail/${aniIdx}`, // 해당 idx를 URL에 포함
                type: 'GET',
                success: function(data) {
                    console.log('Response data:', data); // 서버에서 반환된 데이터 로그 출력

                    // 랜덤 유사 애니메이션이 있는지 확인
                    if (data.randomSimilarAnis && data.randomSimilarAnis.length > 0) {
                        let similarAnisHtml = '';

                        // 랜덤 유사 애니메이션을 HTML로 변환
                        data.randomSimilarAnis.forEach(function(ani) {
                            similarAnisHtml += `
                                <div class="similar-ani">
                                    <img src="http://192.168.1.180:8000/${ani.post_img}" alt="${ani.title}" style="width: 185px;" />
                                    <h3 style="font-size: 17px; color:white;">${ani.title}</h3>
                                </div>
                            `;
                        });

                        $('.similar_ani_list').css({
                            display: 'flex',
                            padding: '0',
                            gap: '5px',
                            margin: '0',
                            listStyleType: 'none',
                        });

                        $('.similar-ani').hover(
                            function() {
                                $(this).find('img').css({
                                    transform: 'scale(1.1)', // 이미지 확대
                                    boxShadow: '0 4px 10px rgba(0, 0, 0, 0.5)' // 그림자 효과
                                });
                            },
                            function() {
                                $(this).find('img').css({
                                    transform: 'scale(1)', // 원래 크기로 돌아가기
                                    boxShadow: 'none' // 그림자 제거
                                });
                            }
                        );

                        // 생성한 HTML을 원하는 위치에 추가
                        $('.similar_ani_list').html(similarAnisHtml);
                    } else {
                        $('.similar_ani_list').html('<p>No similar animations found.</p>');
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.error('Error fetching anime details:', textStatus, errorThrown);
                }
            });
        }, 100);
    });
});
