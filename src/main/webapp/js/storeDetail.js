
// 리뷰 탭 전환 함수
function showTab(tabType) {
  if (tabType === 'inquiry') {
        // 1:1 상품문의 탭을 누르면 /notice2로 이동
        window.location.href = '/notice2';
        return;  // 이후 동작을 막기 위해 함수 종료
    }
    activeTab = tabType;
    const allTabs = document.querySelectorAll('.review-tabs span');
    allTabs.forEach(tab => tab.classList.remove('active'));

    document.querySelector(`.review-tabs span[onclick="showTab('${tabType}')"]`).classList.add('active');
    applyFilters();  // 필터 적용
}

// 기본 설정
let activeTab = 'text';  // 기본적으로 텍스트 리뷰 탭이 활성화됨
let activeFilter = 'latest';  // 기본 정렬 필터는 최신순

// 필터 적용 함수
function applyFilters() {
    let reviews = Array.from(document.getElementsByClassName("review-item"));  // 모든 리뷰 아이템을 가져옴
    let filteredReviews = [...reviews];  // 필터링된 결과를 저장할 배열

    // 탭에 따른 필터링 (텍스트 리뷰 또는 사진/동영상 리뷰)
    if (activeTab === 'text') {

        filteredReviews = reviews.filter(review => !review.querySelector('review-image img'));  // 텍스트 리뷰만 필터링

    } else if (activeTab === 'photo') {
        filteredReviews = reviews.filter(review => review.querySelector('review-image img'));  // 사진/동영상 리뷰만 필터링

    }

    // 정렬 필터 적용 (최신순, 평점 높은순, 평점 낮은순)
    if (activeFilter === 'latest') {
        filteredReviews.sort((a, b) => new Date(b.getAttribute("data-date")) - new Date(a.getAttribute("data-date")));  // 최신순
    } else if (activeFilter === 'highest') {
        filteredReviews.sort((a, b) => b.getAttribute("data-rating") - a.getAttribute("data-rating"));  // 평점 높은순
    } else if (activeFilter === 'lowest') {
        filteredReviews.sort((a, b) => a.getAttribute("data-rating") - b.getAttribute("data-rating"));  // 평점 낮은순
    }

    // 필터링된 리뷰를 화면에 표시
    const reviewList = document.getElementById("review-list");
    reviewList.innerHTML = "";  // 기존 리뷰 삭제
    filteredReviews.forEach(review => reviewList.appendChild(review));  // 필터링된 리뷰 추가
}

// 리뷰 유형 필터 전환 함수 (텍스트 리뷰 또는 사진/동영상 리뷰)
function filterTab(type) {
    activeTab = type;

    // UI 업데이트 (리뷰 유형 필터 활성화)
    const allTabs = document.querySelectorAll('.review-filter1 span');
    allTabs.forEach(tab => tab.classList.remove('active'));
    document.querySelector(`.review-filter1 span[onclick="filterTab('${type}')"]`).classList.add('active');

    // 필터 적용
    applyFilters();
}

// 정렬 필터 전환 함수 (최신순, 평점 높은순, 평점 낮은순)
function filterReviews(type) {
    activeFilter = type;

    // UI 업데이트 (정렬 필터 활성화)
    const allFilters = document.querySelectorAll('.review-filter2 span');
    allFilters.forEach(filter => filter.classList.remove('active'));  // 기존에 'active' 클래스를 제거

    // 선택된 필터에 'active' 클래스를 추가
    const selectedFilter = document.querySelector(`.review-filter2 span[onclick="filterReviews('${type}')"]`);

    if (selectedFilter) {
        selectedFilter.classList.add('active');  // 선택된 필터에 'active' 클래스 추가
    } else {
        console.error(`해당 선택자에 대한 요소를 찾을 수 없습니다: filterReviews('${type}')`);
    }

    // 필터 적용
    applyFilters();
}

// 페이지 로드 시 기본 설정 적용 (최신순 정렬)
window.onload = function() {
    applyFilters();  // 초기화 시 필터 적용
}








function toggleDescription() {
    var description = document.getElementById("hidden-description");
    var button = document.querySelector(".toggle-description");

    if (description.style.display === "none" || description.style.display === "") {
        description.style.display = "block";
        button.innerHTML = "상품정보 접기▲";  // 버튼 텍스트 변경
    } else {
        description.style.display = "none";
        button.innerHTML = "상품정보 더보기▼";  // 버튼 텍스트 원래대로
    }
}

$(document).ready(function() {
    $(window).scroll(function() {
        console.log("Scroll event triggered");
        var scrollPosition = $(window).scrollTop();
        var windowHeight = $(window).height();
        var documentHeight = $(document).height();

        if (scrollPosition > (documentHeight - windowHeight) * 0.1) {
            $('.sticky-footer').addClass('show');
            console.log("Scroll event triggered");
        } else {
            $('.sticky-footer').removeClass('show');
        }
    });
});

function scrollToShippingSection() {
    const section = document.querySelector('.shipping-exchange-return-section');
    const sectionPosition = section.getBoundingClientRect().top + window.scrollY; // 섹션의 Y 위치 계산
    const offset = -160; // 원하는 만큼 위로 배치 (여기서는 100px 위로)

    window.scrollTo({
      top: sectionPosition + offset, // 섹션 위치에서 offset 만큼 위로
      behavior: 'smooth' // 부드러운 스크롤 애니메이션
    });
  }

  function scrollToReviewSection() {
    const section = document.querySelector('.review-title');
    const sectionPosition = section.getBoundingClientRect().top + window.scrollY; // 섹션의 Y 위치 계산
    const offset = -100; // 원하는 만큼 위로 배치 (여기서는 100px 위로)

    window.scrollTo({
      top: sectionPosition + offset, // 섹션 위치에서 offset 만큼 위로
      behavior: 'smooth' // 부드러운 스크롤 애니메이션
    });
  }

//상품정보 입력칸
  function decreaseQuantity() {
    let quantityInput = document.querySelector('.quantity-input');
    if (quantityInput.value > 1) {
        quantityInput.value = parseInt(quantityInput.value) - 1;
        updateTotalPrice();
    }
}

function increaseQuantity() {
    let quantityInput = document.querySelector('.quantity-input');
    let maxQuantity = parseInt(quantityInput.max); // 최대 수량 (HTML max 속성에서 가져옴)

    if (parseInt(quantityInput.value) < maxQuantity) {
        quantityInput.value = parseInt(quantityInput.value) + 1;
        updateTotalPrice();  // 수량이 변경될 때 총 가격을 업데이트
    }
}
function updateTotalPrice() {
    // storeDetail이 정의되어 있는지 확인
    if (typeof storeDetail === 'undefined') {
        console.error('storeDetail is not defined.');
        return;
    }

    let quantityInput = document.querySelector('.quantity-input');
    let quantity = parseInt(quantityInput.value);
    let totalPrice = storeDetail.price * quantity;

    // 총 가격을 천 단위로 포맷
    let formattedTotalPrice = totalPrice.toLocaleString();  // 숫자를 천 단위로 포맷

    // 총 가격을 화면에 업데이트하는 로직 추가 (예: 특정 요소에 총 가격 표시)
    document.getElementById('total-price').textContent =  formattedTotalPrice + ' 원';

    // Sticky Footer의 가격도 업데이트
    let stickyPriceElement = document.querySelector('.sticky-footer .price');
    if (stickyPriceElement) {
        stickyPriceElement.textContent =  formattedTotalPrice + ' 원';
    } else {
        console.error('Sticky footer price element not found.');
    }
}
document.addEventListener('DOMContentLoaded', function () {

    // 로컬스토리지에서 토큰 가져오기
    const token = localStorage.getItem('token');
    console.log("로컬스토리지 토큰 " + token);
    if (!token) {
        console.error('로그인 토큰이 없습니다.');
        return;
    }

    // like-icon 요소 선택
    const likeIcon = document.querySelector('.like-icon');
    if (!likeIcon) {
        console.error("like-icon 요소를 찾을 수 없습니다.");
        return;
    }

    // 상품 ID 가져오기
    const productId = likeIcon.getAttribute('data-product-id');
    if (!productId) {
        console.error("상품 ID를 찾을 수 없습니다.");
        return;
    }

    // 페이지 로드 시 좋아요 상태 확인
    fetchLikeStatusAndUpdateUI(productId, token);

    // like-icon 클릭 시 이벤트 발생
    likeIcon.addEventListener('click', function (event) {
        const likeHeart = event.target; // 클릭된 대상 자체를 가져옴
        if (!likeHeart.classList.contains('like-heart')) {
            console.error("like-heart 아이콘을 찾을 수 없습니다.");
            return;
        }

        // 좋아요 카운트 요소와 기타 필요한 정보 찾기
        const likeCountElement = likeHeart.closest('.like-icon').nextElementSibling; // 하트 아이콘 옆의 카운트 요소
        const currentCount = parseInt(likeCountElement.textContent);
        const isLiked = likeHeart.classList.contains('fa-solid'); // 채워진 하트 상태인지 확인

        console.log("상품 ID:", productId, "좋아요 상태:", isLiked, "현재 좋아요 수:", currentCount);

        // AJAX 요청으로 서버에 좋아요 상태 전달
        fetch('/like/toggle', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}` // Authorization 헤더에 토큰 추가
            },
            body: JSON.stringify({
                pro_idx: productId  // productId를 전송
            })
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                console.log("좋아요 상태 변경 성공");
                updateLikeUI(isLiked, likeHeart, likeCountElement, currentCount);
            } else {
                console.error("좋아요 상태 변경 실패");
            }
        })
        .catch(error => console.error('Error:', error));
    });


    // 좋아요 UI 업데이트 함수
    function updateLikeUI(isLiked, likeHeart, likeCountElement, currentCount) {
        if (isLiked) {
            likeHeart.classList.remove('fa-solid');  // 채워진 하트 제거
            likeHeart.classList.add('fa-regular');   // 빈 하트로 변경
            likeCountElement.textContent = currentCount - 1;  // 카운트 감소
        } else {
            likeHeart.classList.remove('fa-regular');  // 빈 하트 제거
            likeHeart.classList.add('fa-solid');       // 채워진 하트로 변경
            likeCountElement.textContent = currentCount + 1;  // 카운트 증가
        }
    }


    // 새로고침 시 좋아요 상태 불러오는 함수
    function fetchLikeStatusAndUpdateUI(productId, token) {
        fetch(`/like/statusAndCount?pro_idx=${productId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const likeHeart = document.querySelector('.like-heart');
                const likeCountElement = document.querySelector('.like-count'); // 좋아요 수가 표시되는 요소

                if (data.liked) {
                    likeHeart.classList.add('fa-solid');
                    likeHeart.classList.remove('fa-regular');
                } else {
                    likeHeart.classList.add('fa-regular');
                    likeHeart.classList.remove('fa-solid');
                }

                // 서버에서 받은 좋아요 수로 UI 업데이트
                likeCountElement.textContent = data.likeCount;
            } else {
                console.error('좋아요 상태를 가져오는 데 실패했습니다.');
            }
        })
        .catch(error => console.error('좋아요 상태 요청 중 오류 발생:', error));
    }
});


let reviewsPerPage = 5;  // 페이지당 리뷰 수
let currentPage = 1;  // 현재 페이지 번호

// 페이지 네이션 함수
function changePage(pageNumber) {
    currentPage = pageNumber;  // 현재 페이지를 업데이트
    applyPagination();  // 페이지 네이션 적용
}

// 페이지 네이션 적용 함수
function applyPagination() {
    let reviews = Array.from(document.getElementsByClassName("review-item"));  // 모든 리뷰 아이템을 가져옴
    const reviewList = document.getElementById("review-list");

    reviewList.innerHTML = "";  // 기존 리뷰 삭제

    // 현재 페이지에 맞는 리뷰 시작/끝 인덱스 계산
    let startIndex = (currentPage - 1) * reviewsPerPage;
    let endIndex = startIndex + reviewsPerPage;

    let paginatedReviews = reviews.slice(startIndex, endIndex);  // 현재 페이지의 리뷰만 가져옴

    // 필터링된 리뷰를 화면에 추가
    paginatedReviews.forEach(review => reviewList.appendChild(review));

    // 페이지 버튼 활성화 처리
    createPaginationButtons(reviews.length);  // 동적으로 페이지 버튼 생성
}

// 페이지 버튼 동적 생성 함수
function createPaginationButtons(totalReviews) {
    const paginationContainer = document.querySelector(".pagination");
    paginationContainer.innerHTML = "";  // 기존 버튼 삭제

    let totalPages = Math.ceil(totalReviews / reviewsPerPage);  // 총 페이지 수 계산
    let pageRange = 1;  // 현재 페이지를 기준으로 양쪽으로 보여줄 페이지 수 (현재 페이지 + 좌우 1개씩)

    // 이전 버튼 추가
    const prevButton = document.createElement("button");
    prevButton.textContent = "이전";
    prevButton.onclick = () => changePage(currentPage - 1);
    prevButton.disabled = currentPage === 1;  // 첫 페이지일 때 비활성화
    paginationContainer.appendChild(prevButton);

    // 시작 페이지와 끝 페이지 계산
    let startPage = Math.max(1, currentPage - pageRange);  // 최소 1페이지부터 시작
    let endPage = Math.min(totalPages, currentPage + pageRange);  // 최대 마지막 페이지까지

    // 페이지 번호 버튼 생성
    for (let i = startPage; i <= endPage; i++) {
        const button = document.createElement("button");
        button.textContent = i;
        button.onclick = () => changePage(i);  // 페이지 번호 클릭 시 이동
        if (i === currentPage) {
            button.classList.add("active");  // 현재 페이지는 활성화
        }
        paginationContainer.appendChild(button);
    }

    // 다음 버튼 추가
    const nextButton = document.createElement("button");
    nextButton.textContent = "다음";
    nextButton.onclick = () => changePage(currentPage + 1);
    nextButton.disabled = currentPage === totalPages;  // 마지막 페이지일 때 비활성화
    paginationContainer.appendChild(nextButton);
}

// 페이지 로드 시 기본적으로 첫 페이지를 적용
window.onload = function() {
    applyPagination();  // 첫 페이지 적용
}
