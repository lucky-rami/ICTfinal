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
