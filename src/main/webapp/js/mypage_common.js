function formatNumber(number) {
    return number.toLocaleString();
}

// 로그아웃 기능 구현
function logout() {
    // 로컬 스토리지에서 토큰 제거
    localStorage.removeItem("token");  // 로컬 스토리지에서 토큰 제거
    alert("로그아웃 성공");
    location.href = '/'; // 메인 페이지로 이동
}

const token = localStorage.getItem("token");

function mypageCommInfo(){
    $.ajax({
        url: '/user/mypageCommInfo',
        type: 'POST',
        contentType: 'application/json',
        headers: {
            "Authorization": `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
        },
        success:function(response){
            console.log(response.currentID);
            console.log(response.reservePoint);
            console.log(response.reviewCompletedAmount);

            const currentID = response.currentID;
            const reservePoint = response.reservePoint;
            const reviewCompletedAmount = response.reviewCompletedAmount;
            // 내 아이디 정보
            $('.currentID').text(currentID+" 님");
            //내 적립금 현황
            $('.reservePoint').text(formatNumber(reservePoint));
            // 내 리뷰 수 업데이트
            $('.afterCountSpan').text(reviewCompletedAmount);
        },
         error: function(error) {
            console.log('오류 발생: ' + error);  // 에러 메시지 자체 출력
        }
    })
}


$(function(){
    mypageCommInfo();
});

window.addEventListener('scroll', function() {
    const mypageWrap = document.querySelector('.mypage_wrap');
    const mypageLeft = document.querySelector('.mypage_left');
    const footer = document.querySelector('footer');

    const wrapTop = mypageWrap.getBoundingClientRect().top + window.scrollY;
    const footerTop = footer.getBoundingClientRect().top + window.scrollY;
    const scrollTop = window.scrollY;
    const mypageLeftHeight = mypageLeft.offsetHeight;
    const offset = 210; // 원하는 고정 위치
    const buffer = 150; // 푸터보다 위에서 멈추기 위한 추가 오프셋

    // 1. 푸터보다 50px 위에서 멈추도록 수정
    if (scrollTop >= wrapTop - offset && scrollTop + mypageLeftHeight < footerTop - offset - buffer) {
        mypageLeft.style.position = 'fixed';
        mypageLeft.style.top = `${offset}px`;
    }
    // 2. 푸터에서 50px 위에서 멈추게 설정
    else if (scrollTop + mypageLeftHeight >= footerTop - offset - buffer) {
        mypageLeft.style.position = 'absolute';
        mypageLeft.style.top = `${footerTop - wrapTop - mypageLeftHeight - buffer}px`;
    }
    // 3. 스크롤이 상단으로 올라갈 때 원래 위치로
    else {
        mypageLeft.style.position = 'relative';
        mypageLeft.style.top = '0';
    }
});





