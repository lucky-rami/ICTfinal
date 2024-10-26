gsap.registerPlugin(ScrollTrigger);
  gsap.to("#mainVisual .main_video", {
    scrollTrigger: {
      pin: true,
      scrub: 0.2,
      trigger: "#mainVisual",
      toggleClass: "on",
      start: "top top",
      end: "+=1500",
    },
    width: "1500px",
    height: "730px",
    "margin-top": "130px",
  });
  $(function () {
    setTimeout(function () {
      $("#mainVisual .txt01").addClass("active");
    }, 400);
    setTimeout(function () {
      $("#mainVisual .txt03").addClass("active");
    }, 400);
    setTimeout(function () {
      $("#mainVisual .txt02").addClass("active");
    }, 800);

    const scrollList1 = $('#scrollList1');
    const scrollList2 = $('#scrollList2');

    // 리스트 복제
    scrollList1.append(scrollList1.html());
    scrollList2.append(scrollList2.html());

    function resetAnimation(element) {
        element.style.animation = 'none'; // 애니메이션을 일시적으로 해제
        element.offsetHeight; // 브라우저가 다시 계산하도록 트리거
        element.style.animation = ''; // 애니메이션 다시 적용
    }

    // 애니메이션 재설정 함수 호출
    resetAnimation(document.querySelector('.content1_img ul.active'));
    resetAnimation(document.querySelector('.content1_img ul:nth-of-type(2).active'));
});

feather.replace();
  var imgSwiper = new Swiper(".gall_slide .gall_list", {
    speed: 700,
    slidesPerView: "1.5",
    spaceBetween: 10,
    autoHeight: true,
    loop: true,
    centeredSlides: true,
    autoplay: {
      delay: 3000,
      disableOnInteraction: false,
    },
    navigation: {
      nextEl: ".swiper-button-next",
      prevEl: ".swiper-button-prev",
    },
    pagination: {
      el: ".swiper-pagination",
      type: "progressbar",
    },
    breakpoints: {
      1025: {
        slidesPerView: "4",
        spaceBetween: 40,
        centeredSlides: false,
      },
      769: {
        slidesPerView: "3",
        spaceBetween: 20,
        centeredSlides: true,
      },
      481: {
        slidesPerView: "2",
        spaceBetween: 15,
        centeredSlides: false,
      },
    },
});

AOS.init();
