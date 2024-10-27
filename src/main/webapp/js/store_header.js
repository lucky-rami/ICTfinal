$(function () {
    // 로고(홈) 링크 클릭 시 로컬 스토리지 초기화
    $(".sh_logo a").on("click", function () {
        localStorage.removeItem('activeMenuIndex');
        localStorage.removeItem('isCartActive');
    });

    // 로컬 스토리지에서 활성화된 메뉴 인덱스와 장바구니 상태 가져오기
    let activeIndex = localStorage.getItem('activeMenuIndex');
    let isCartActive = localStorage.getItem('isCartActive');

    if (isCartActive == 'true') {
        // 장바구니가 활성화되어 있는 경우
        $(".mymenu .order a").addClass("active");
    } else if (activeIndex !== null) {
        // 로컬 스토리지에 저장된 인덱스가 있을 경우 해당 메뉴를 활성화
        $(".snav_inner nav ul li").eq(activeIndex).addClass("check");
        $(".snav_inner nav ul li a").eq(activeIndex).addClass("active");
    } else {
        // 로컬 스토리지에 저장된 값이 없으면 "홈" 메뉴를 기본값으로 활성화
        $(".snav_inner nav ul li").first().addClass("check");
        $(".snav_inner nav ul li a").first().addClass("active");
    }

    // snav_inner의 메뉴 클릭 시 이벤트 발생
    $(".snav_inner nav ul li").click(function () {
        // 모든 li에서 check 클래스를 제거
        $(".snav_inner nav ul li").removeClass("check");
        $(".snav_inner nav ul li a").removeClass("active");
        $(".mymenu .order a").removeClass("active"); // 장바구니의 active 해제

        // 클릭된 메뉴에 check와 active 클래스 추가
        $(this).addClass("check");
        $(this).find("a").addClass("active");

        // 클릭된 메뉴의 인덱스를 로컬 스토리지에 저장하고 장바구니 비활성화
        let index = $(this).index();
        localStorage.setItem('activeMenuIndex', index);
        localStorage.setItem('isCartActive', false);
    });

    // 장바구니 클릭 시 이벤트 발생
    $(".mymenu .order a").click(function () {
        // 다른 메뉴의 활성화 상태 제거
        $(".snav_inner nav ul li").removeClass("check");
        $(".snav_inner nav ul li a").removeClass("active");

        // 장바구니에만 active 클래스 추가
        $(this).addClass("active");

        // 로컬 스토리지에 장바구니 활성화 저장
        localStorage.setItem('isCartActive', true);
        localStorage.removeItem('activeMenuIndex'); // 다른 메뉴 인덱스 초기화
    });

    $(window).on("scroll", function () {
        var scrollTop = $(window).scrollTop(); // 현재 스크롤 위치
        var headerHeight = $("#header").outerHeight(); // 헤더의 높이

        if (scrollTop > 100) {
        // 스크롤이 100px 이상일 때
        $("#header").addClass("fixed");
        $("body").css("padding-top", headerHeight + "px"); // 헤더의 높이만큼 패딩 추가
        } else {
        $("#header").removeClass("fixed");
        $("body").css("padding-top", "0"); // 패딩 제거
        }
    });
});
