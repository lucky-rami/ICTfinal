<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf" %>


<!DOCTYPE html>
 <html lang="ko">
<head>
<meta charset="UTF-8">
<title>JSP Template</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
 <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.0/jquery.min.js"></script>
    <link href="${pageContext.request.contextPath}/css/aniList.css" rel="stylesheet" type="text/css">
    <script src="${pageContext.request.contextPath}/js/aniList.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</link>
<script>
var alllist=`${aniAllList}`;

</script>


<body>
    <div class="aniList">
        <nav class="aniList navbar-dark bg-dark">
                <div class="aniList category-title">카테고리</div>
                <hr />
                <div class="manage_menu">
                    <ul>
                        <li class="menu-item">장르</li>
                        <li class="submenu"><label><input type="checkbox" class="filter" value="미스터리"> 미스터리</label></li>
                        <li class="submenu"><label><input type="checkbox" class="filter" value="판타지"> 판타지</label></li>
                        <li class="submenu"><label><input type="checkbox" class="filter" value="드라마"> 드라마</label></li>
                        <li class="submenu"><label><input type="checkbox" class="filter" value="스포츠"> 스포츠</label></li>
                        <li class="submenu"><label><input type="checkbox" class="filter" value="코미디"> 코미디</label></li>
                        <li class="submenu"><label><input type="checkbox" class="filter" value="SF"> SF</label></li>
                    </ul>
                    <hr />
                        <ul>
                            <li class="menu-item">연령</li>
                            <li class="submenu"><label><input type="checkbox" data-age="전체" class="filter"> 전체관람가 </label></li> <!-- 전체 관람가 -->
                            <li class="submenu"><label><input type="checkbox" data-age="12세" class="filter"> 12세 이상</label></li> <!-- 12세 이상 -->
                            <li class="submenu"><label><input type="checkbox" data-age="15세" class="filter"> 15세 이상</label></li> <!-- 15세 이상 -->
                            <li class="submenu"><label><input type="checkbox" data-age="19세" class="filter"> 19세 이상</label></li> <!-- 19세 이상 -->
                        </ul>
                </div>
        </nav>
    </div>


<!--===============================================================================-->
    <div class="ani_view">
        <div class="ani_View">
            <div class="vod_list_select">
                <select name="select_input vod_select" class="select_input vod_select" style="background-color: white;" onchange="onSortChange(this.value)">
                    <option value="new" style="font-weight: 600; font-size: 14px;">최신순</option>
                    <option value="title" style="font-weight: 600; font-size: 14px;">별점순 ↑</option>
                    <option value="popular" style="font-weight: 600; font-size: 14px;">인기순</option>
                </select>
            </div>


                <div class="search-container">
                <input type="hidden" id="ani_idx" value="${ani.idx}">
                    <input type="text" id="searchInput" placeholder="검색어를 입력하세요..."  onkeyup="searchTable()">
                </div>
            </div>

    <div class="ani_viewList">
        <c:forEach var="ani" items="${aniAllList}">
            <div class="div_li" data-anitype="${ani.anitype}" data-agetype="${ani.agetype}">
                <div class="list_img_bg" data-title="${ani.title}" data-director="${ani.director}" data-idx="${ani.idx}">
                    <img src="http://192.168.1.180:8000/${ani.post_img}" alt="${ani.title}">
                    <div class="overlay">상세 보기</div>
                </div>
                <p>${ani.title}</p>
                <p class="genre">
                    <span>${ani.anitype}</span> <!-- 장르 문자열 -->
                    <span>${ani.agetype} 관람</span> <!-- 나이 관람 문자열 -->
                </p>
            </div>
        </c:forEach>
    </div>
        </div>
    </div>

    <!--==========================MODAL=======================================-->
<div class="animodal_body">
      <div class="animodal_background"></div>
          <div class="animodal_container">
            <div class="animodal_item">
              <div class="animodal_item_top">
                <div class="animodal_item_imgDiv">
                  <div class="animodal_item_imgBack">
                    <img src="" />
                  </div>
                  <img src="" />
                </div>
                <div class="animodal_item_infoDiv">
                  <nav>
                    <button id="likeButton" onclick="toggleLike()">
                        <i id="likeIcon" class="fa-regular fa-heart"></i>
                    </button>
                    <button><i class="fa-solid fa-x"></i></button>
                  </nav>
                  <div>
                    <div class="animodal_infoHead">
                      <div class="ani_ageGrade">
                        <div>
                          <label>
                            <i class="fa-solid fa-star"></i><span class="modal-grade"></span>
                          </label>
                        </div>
                      </div>
                      <h1></h1>
                      <div class="ani_typeinfo">
                        <span class="genre">${selectedAni.anitype}</span> <!-- 장르 추가 -->
                        <span class="age">${selectedAni.agetype}</span> <!-- 나이 관람 추가 -->
                        <span class="director"></span> <!-- 감독 추가 -->
                      </div>
                    </div>
                    <div class="animodal_infoBody">


                      <div id="animodal_usergrade" class="animodal_usergrade"  data-idx="idx">
                        <span>내 평가 : </span>
                            <div id="stars">
                                <i class="fa-regular fa-star" onclick="setRating(1, 'animodal_usergrade')"></i>
                                <i class="fa-regular fa-star" onclick="setRating(2, 'animodal_usergrade')"></i>
                                <i class="fa-regular fa-star" onclick="setRating(3, 'animodal_usergrade')"></i>
                                <i class="fa-regular fa-star" onclick="setRating(4, 'animodal_usergrade')"></i>
                                <i class="fa-regular fa-star" onclick="setRating(5, 'animodal_usergrade')"></i>
                            </div>
                        <button class="grade_inputBtn" onclick="submitRating()">등록</button>
                      </div>
                      <div class="ani_outline">
                        <div>
                            <span class="summary"></span> <!-- 줄거리를 표시할 부분 -->
                            <span class="ouline_more">...더 보기</span> <!-- 더 보기 버튼 -->
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>


            <div class="animodal_item_bottom">
                <div class="animodal_item_bottom_nav">
                <div>
                <div>
                    <div><span>비슷한 작품</span></div>
                    <div></div>
                </div>
              </div>
                </div>
                    <div class="bottom_ani_content">
                        <ul class="similar_ani_list">
                            <c:forEach var="rand" items="${randomSimilarAnis}">
                                <li class="similar_ani" data-idx="${rand.idx}">
                                    <div class="similar_ani_img">
                                        <img src="http://192.168.1.180:8000/${rand.post_img}"  alt="${rand.title}" >
                                    </div>
                                        <p class="similar_ani_title">${rand.title}</p>
                                        <p>title: ${rand.title}</p>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>

            </div>


            </div>
          </div>
        </div>

    2. 애니 줄거리 모달
    <div class="aniInfo_modal_body">
          <div class="aniInfo_modal_background"></div>
          <div class="aniInfo_modal_container">
            <div class="aniInfo_modal_container_flex">
              <div>
                <span>작품 정보</span>
                <i class="fa-solid fa-x"></i>
              </div>
              <div class="item-detail-info-modal">
                <div>
                  <section>
                    <span>줄거리</span>
                    <span class="ani-outline"></span>
                  </section>
                  <section>
                    <span>제작 정보</span>
                    <ul>
                      <li>
                        <span>감독
                          <span class="director-name"></span>
                        </span>
                      </li>
                      <li>
                        <span
                          >등록
                          <span class="registration-date">${regDT}</span>
                        </span>
                      </li>
                    </ul>
                  </section>
                </div>
              </div>
            </div>
      </div>
    </div>

</body>
