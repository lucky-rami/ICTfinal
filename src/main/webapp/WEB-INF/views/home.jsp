<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="/WEB-INF/inc/main_header.jspf"%>
<title>AniWave</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.7.1/gsap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.7.1/ScrollTrigger.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css" />
<link href="/css/home.css" rel="stylesheet" type="text/css"></link>
<script>
        // URL에서 쿼리 파라미터를 추출하여 객체로 변환하는 함수
        function getQueryParams() {
            const params = new URLSearchParams(window.location.search);
            const queryParams = {};
            for (const [key, value] of params.entries()) {
                queryParams[key] = value;
            }
            return queryParams;
        }

        // 페이지 로드 시 실행될 함수
        document.addEventListener("DOMContentLoaded", function () {
            // URL에서 JWT 토큰과 사용자 정보를 가져옴
            const params = getQueryParams();
            const token = params.token;    // JWT 토큰 추출
            const userid = params.userid;  // 사용자 이름 추출

            if (token) {
                // JWT 토큰을 로컬 스토리지에 저장
                localStorage.setItem("token", token);
                console.log("JWT 토큰이 로컬 스토리지에 저장되었습니다.");
                }
        });

         document.addEventListener("DOMContentLoaded", function () {
                // 현재 URL에서 쿼리 파라미터 제거
                const url = new URL(window.location.href);
                url.search = "";  // 쿼리 파라미터 부분을 비워서 제거
                window.history.replaceState({}, document.title, url.toString());  // URL을 쿼리 파라미터 없이 대체
            });
    </script>
<div class="pin-spacer">
  <div id="mainVisual">
    <div class="main_banner">
      <div class="main_video">
        <video
          id="myVideo"
          preload="auto"
          autoplay
          loop
          muted
          src="/img/main_img/ani1_4kup.mp4"
        ></video>
      </div>
      <div class="main_txt">
        <p class="txt01"><span>#</span>Ready for the Animation</p>
        <p class="txt02">ENJOY THE ANIMATION</p>
        <p class="txt03">
          다양한 애니메이션과 굿즈로 당신에게 즐거움을 선사합니다.
        </p>
      </div>
    </div>
    <div class="main_sub">
      <span>애니메이션과 함께하는 새로운 오늘,</span>
      다양한 작품과 굿즈로 당신만의 특별한 세상을 만들어보세요!
    </div>
  </div>
</div>

<div class="mainContents">
  <div class="mainContent1">
    <div
      class="inc01 aos-init aos-animate"
      data-aos="fade-up"
      data-aos-delay="100"
      data-aos-easing="ease-out-cubic"
      data-aos-duration="500"
    >
      <div class="tit">
        <p class="content_title">Recommend List</p>
        <p class="recommend_list">당신에게 추천하는 애니예요!</p>
        <div class="pl">
          <span style="margin-bottom: 5px"
            >모두가 애니메이션을 즐기는 세상</span
          >
          <br />
          sing2727님이 선호할만한 애니메이션을 만나보세요!
        </div>
      </div>
      <div class="recommend_ani">
        <ul class="grid_imgs">
          <li data-aos="zoom-in" data-aos-offset="300">
            <a href="">
              <div class="mask"></div>
              <div class="txt">
                <p>정적인 공간, 나만의 아늑한 안식처</p>
                <span
                  >View more<i class="on fa fa-long-arrow-right"></i
                ></span>
              </div>
              <img class="thum" src="/img/main_img/anipost1.png" alt="" />
            </a>
          </li>
          <li data-aos="zoom-in" data-aos-offset="300">
            <a href="">
              <div class="mask"></div>
              <div class="txt">
                <p>정적인 공간, 나만의 아늑한 안식처</p>
                <span
                  >View more<i class="on fa fa-long-arrow-right"></i
                ></span>
              </div>
              <img class="thum" src="/img/main_img/anipost2.png" alt="" />
            </a>
          </li>
          <li data-aos="zoom-in" data-aos-offset="300">
            <a href="">
              <div class="mask"></div>
              <div class="txt">
                <p>정적인 공간, 나만의 아늑한 안식처</p>
                <span
                  >View more<i class="on fa fa-long-arrow-right"></i
                ></span>
              </div>
              <img class="thum" src="/img/main_img/anipost3.png" alt="" />
            </a>
          </li>
          <li data-aos="zoom-in" data-aos-offset="300">
            <a href="">
              <div class="mask"></div>
              <div class="txt">
                <p>정적인 공간, 나만의 아늑한 안식처</p>
                <span
                  >View more<i class="on fa fa-long-arrow-right"></i
                ></span>
              </div>
              <img class="thum" src="/img/main_img/anipost4.png" alt="" />
            </a>
          </li>
          <li data-aos="zoom-in" data-aos-offset="300">
            <a href="">
              <div class="mask"></div>
              <div class="txt">
                <p>정적인 공간, 나만의 아늑한 안식처</p>
                <span
                  >View more<i class="on fa fa-long-arrow-right"></i
                ></span>
              </div>
              <img class="thum" src="/img/main_img/anipost5.png" alt="" />
            </a>
          </li>
          <li data-aos="zoom-in" data-aos-offset="300">
            <a href="">
              <div class="mask"></div>
              <div class="txt">
                <p>정적인 공간, 나만의 아늑한 안식처</p>
                <span
                  >View more<i class="on fa fa-long-arrow-right"></i
                ></span>
              </div>
              <img class="thum" src="/img/main_img/anipost6.png" alt="" />
            </a>
          </li>
          <li data-aos="zoom-in" data-aos-offset="300">
            <a href="">
              <div class="mask"></div>
              <div class="txt">
                <p>정적인 공간, 나만의 아늑한 안식처</p>
                <span
                  >View more<i class="on fa fa-long-arrow-right"></i
                ></span>
              </div>
              <img class="thum" src="/img/main_img/anipost7.jpg" alt="" />
            </a>
          </li>
          <li data-aos="zoom-in" data-aos-offset="300">
            <a href="">
              <div class="mask"></div>
              <div class="txt">
                <p>정적인 공간, 나만의 아늑한 안식처</p>
                <span
                  >View more<i class="on fa fa-long-arrow-right"></i
                ></span>
              </div>
              <img class="thum" src="/img/main_img/anipost8.jpg" alt="" />
            </a>
          </li>
        </ul>
      </div>
      <div class="more_btn_div">
        <a class="more_btn" href="/aniList">MORE VIEW</a>
      </div>
    </div>
  </div>
  <div class="mainContent2">
    <div class="inc02">
      <div class="inner">
        <div
          class="tit"
          data-aos="fade-up"
          data-aos-delay="100"
          class="aos-init aos-animate"
          data-aos-easing="ease-out-cubic"
          data-aos-duration="500"
        >
          <p class="content_title">New Goods</p>
          <div class="control">
            <div class="swiper-button-prev prev">
              <i data-feather="chevron-left"></i>
            </div>
            <p class="new_goods">새로운 굿즈를 만나보세요!</p>
            <div class="swiper-button-next next">
              <i data-feather="chevron-right"></i>
            </div>
          </div>
          당신을 위한 새로운 굿즈가 기다리고 있습니다.
        </div>
        <div
          class="gall_slide"
          data-aos="fade-up"
          data-aos-delay="100"
          class="aos-init aos-animate"
          data-aos-easing="ease-out-cubic"
          data-aos-duration="500"
        >
          <div class="swiper-container gall_list">
            <ul class="swiper-wrapper">
              <c:forEach var="goods" items="${goods_list}">
                <li class="swiper-slide">
                    <a href="/storeDetail/${goods.idx}">
                      <div class="img_wrap">
                        <div class="mask"></div>
                        <div class="txt">
                          <p>${goods.title}</p>
                          <span
                            >View more<i class="on fa fa-long-arrow-right"></i
                          ></span>
                        </div>
                        <div
                          class="thum_img"
                          style="
                            background: url('http://192.168.1.180:8000/${goods.thumImg}')
                              no-repeat center / cover;
                          "
                        ></div>
                      </div>
                    </a>
                </li>
              </c:forEach>

            </ul>
          </div>
          <div class="pager_wrap">
            <div class="swiper-pagination"></div>
            <a class="more_btn" href="/storeMain">MORE VIEW</a>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div
    class="mainContent3 aos-init aos-animate"
    data-aos="fade-up"
    data-aos-delay="100"
    data-aos-easing="ease-out-cubic"
    data-aos-duration="500"
  >
    <div class="inc03">
      <div class="inner">
        <div class="l_txt">
          <p class="content_title">Community Space</p>
          <div class="big_txt">
            <p class="b_txt" style="background-size: 100%">
              유저들과 소통하고,
            </p>
            <p class="b_txt" style="background-size: 100%">
              다양한 이벤트를 즐기며,
            </p>
            <p class="b_txt" style="background-size: 100%">
              궁금한 사항은 언제든지 문의하세요!
            </p>
          </div>
        </div>
        <div class="r_cont">
          <div class="cont">
            <a href="/cmList">
              <p class="view_btn">
                view<svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="feather feather-plus"
                >
                  <line x1="12" y1="5" x2="12" y2="19"></line>
                  <line x1="5" y1="12" x2="19" y2="12"></line>
                </svg>
              </p>
              <div>
                <p class="img"></p>
                <p class="txt">
                  <span>커뮤니티</span>
                  커뮤니티는 애니메이션을 사랑하는 유저들이 서로 소통하고
                  정보를 공유할 수 있는 공간입니다. 이곳에서는 애니메이션
                  관련 의견을 나누고, 굿즈에 대한 다양한 이야기를 나누며,
                  함께 즐거운 경험을 만들어 갈 수 있습니다!
                </p>
              </div>
            </a>
          </div>
          <div class="cont">
            <a href="">
              <div>
                <p class="img"></p>
                <p class="txt">
                  <span>이벤트</span>
                  유저들은 이곳에서 참여할 수 있는 이벤트 정보를 확인하고,
                  다양한 혜택과 보상을 받을 수 있는 기회를 얻을 수 있습니다.
                  최신 이벤트 소식을 놓치지 않고, 특별한 즐거움을 경험해
                  보세요!
                </p>
              </div>
              <p class="view_btn">
                view<svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="feather feather-plus"
                >
                  <line x1="12" y1="5" x2="12" y2="19"></line>
                  <line x1="5" y1="12" x2="19" y2="12"></line>
                </svg>
              </p>
            </a>
          </div>
          <div class="cont">
            <a href="">
              <p class="view_btn">
                view<svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="24"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  class="feather feather-plus"
                >
                  <line x1="12" y1="5" x2="12" y2="19"></line>
                  <line x1="5" y1="12" x2="19" y2="12"></line>
                </svg>
              </p>
              <div>
                <p class="img"></p>
                <p class="txt">
                  <span>1:1 문의</span>
                  유저들은 이곳을 통해 다양한 질문이나 문제를 쉽게 해결할 수
                  있으며, 관리자와 소통하여 더 나은 서비스를 경험할 수
                  있습니다. 빠르고 정확한 답변을 통해 불편함을 최소화할 수
                  있는 문의 페이지를 활용해보세요!
                </p>
              </div>
            </a>
          </div>
        </div>
      </div>
    </div>
    <div
      class="inc04 aos-init aos-animate"
      data-aos="fade-up"
      data-aos-delay="100"
      data-aos-easing="ease-out-cubic"
      data-aos-duration="500"
    >
      <div class="l_get">
        <div class="l_get_div">
          READY FOR<br />THE ANIMATION ?
          <p></p>
        </div>
      </div>
      <div class="r_get">
        <div class="r_get_div">Going out to have fun</div>
        <p>
          당신이 원하는 애니메이션을 빠르고 쉽게 즐겨보세요. <br />원하는
          애니메이션의 굿즈를 구매하고 공유해보세요.
        </p>
      </div>
    </div>
  </div>
  <div>dsad</div>
</div>

<script src="https://cdn.jsdelivr.net/npm/feather-icons/dist/feather.min.js"></script>
<script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
<script src="/js/home.js"></script>

<%@include file="/WEB-INF/inc/footer.jspf"%>