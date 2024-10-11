<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="/WEB-INF/inc/page_header.jspf"%>

<link rel="stylesheet" href="/css/mypage.css" type="text/css" />
<link rel="stylesheet" href="/css/mypage_order_detail.css" type="text/css" />

<div class="mypage_wrap">
  <div class="mypage_main_wrap">
    <div class="mypage_left">
     <a href="/user/mypage" class="mypage_home">MY PAGE</a>
      <section class="mypage_menu">
          <h3>나의 쇼핑정보</h3>
          <ul>
            <li>
              <a href="/user/mypage_order" class="option_active">주문배송조회</a>
            </li>
            <li>
              <a href="/user/mypage_review"> 상품 리뷰 </a>
            </li>
          </ul>
          <h3>나의 계정설정</h3>
          <ul>
            <li>
              <a href="/user/mypage_userEdit">회원정보수정</a>
            </li>
            <li>
              <a href="">내가 쓴 글</a>
            </li>
            <li>
              <a href="/user/mypage_point">적립금</a>
            </li>
            <li>
              <a href="/user/mypage_heart">좋아요</a>
            </li>
            <li>
              <a href="/user/mypage_userDel">회원탈퇴</a>
            </li>
          </ul>
          <h3>고객센터</h3>
          <ul>
            <li>
              <a href="/user/mypage_qna">1:1 문의</a>
            </li>
            <li>
              <a href="">공지사항</a>
            </li>
            <li>
              <a href="">FAQ</a>
            </li>
          </ul>
        </section>

      <section>
        <div class="btn_bx">
          <button type="button" class="logout_btn">LOGOUT</button>
        </div>
      </section>
    </div>
    <div class="mypage_right">
      <ul class="mypage_right_top">
          <li class="user_inform">
            <a class="atag_css" href="/user/mypage_userEdit"
              ><strong>회원정보 수정</strong><span>sing2727 님</span></a
            >
          </li>
          <li class="user_retention_details">
            <a class="atag_css" href="/user/mypage_point"
              ><strong class="">적립금</strong><span class="">360</span></a
            >
          </li>
          <li class="user_retention_details">
            <a class="atag_css" href="/user/mypage_review"
              ><strong class="">작성한 리뷰</strong><span class="">1</span></a
            >
          </li>
        </ul>

      <div class="mypage_right_element">
        <!-- 여기에 페이지에 맞는 요소 넣으면 됨 -->
        <div>
          <div class="order_detail_all">
            <div class="order_detail_state">
              <h4 class="my_tit">주문상세내역</h4>
              <div class="my_data">
                <p><span>주문일자 </span>2020.05.20</p>
                <p><span>주문번호 </span>ORD20200520-1294067</p>
              </div>
              <ol class="order_state">
                <li class="order_state_li">
                  <div class="order_state_div">
                    <div class="order_state_ok"><span>결제완료</span></div>
                  </div>
                </li>
                <li class="order_state_li">
                  <div class="order_state_div">
                    <div class="order_state_ok"><span>상품준비중</span></div>
                  </div>
                </li>
                <li class="order_state_li">
                  <div class="order_state_div">
                    <div class="order_state_ok"><span>배송시작</span></div>
                  </div>
                </li>
                <li class="order_state_li">
                  <div class="order_state_div">
                    <div class="order_state_ok"><span>배송중</span></div>
                  </div>
                </li>
                <li class="order_state_li">
                  <div class="order_state_div">
                    <div class="order_state_ok"><span>배송완료</span></div>
                  </div>
                </li>
                <li class="order_state_li">
                  <div class="order_current_state">
                    <div class="order_state_ok"><span>구매확정</span></div>
                  </div>
                </li>
              </ol>
            </div>
            <div class="order_detail_inform">
              <h4 class="my_tit">주문상품정보</h4>
              <div>
                <div class="tb_header">
                  <div class="tb_header_proinform">상품정보</div>
                  <div class="tb_header_fee">배송비</div>
                  <div class="tb_header_state">진행상태</div>
                </div>
                <ul class="tb_body_ul">
                  <li>
                    <ul>
                      <li class="order_data_list_one">
                        <a href="">
                          <div class="order_data_inform">
                            <div class="order_data_img"><img src="https://img.29cm.co.kr/item/202407/11ef4816869b278989fb1114c15fa422.jpg?width=80" /></div>
                            <div>
                              <p class="order_pro_name">WOMAN GNRL 에센셜 베이직 가디건 [GREY] / WBC1L04504</p>
                              <ul class="order_pro_option">
                                <li>[COLOR:SIZE]GREY:S</li>
                                <li>40,000원 / 수량 1개</li>
                              </ul>
                            </div>
                          </div>
                        </a>
                        <div class="order_delivery_fee_data">무료배송</div>
                        <div class="order_current_state_div">
                          <div>
                            <div class="order_state_all">
                              <p class="order_state_data">구매확정</p>
                              <p class="order_state_okdate">2023.04.21</p>
                            </div>
                            <button class="order_package">CJ대한통운 <span>571223585731</span></button>
                          </div>
                        </div>
                      </li>
                    </ul>
                  </li>
                </ul>
              </div>
            </div>
            <div>
              <div>
                <h4 class="my_tit">구매자정보</h4>
              </div>
              <table class="buyer_inform">
                <colgroup>
                  <col width="140" />
                  <col />
                  <col width="140" />
                  <col />
                </colgroup>
                <tbody>
                  <tr>
                    <th scope="row" class="th_style">주문자</th>
                    <td class="td_style">김채원</td>
                    <th scope="row" class="th_style">이메일주소</th>
                    <td class="td_style">sing2727@naver.com</td>
                  </tr>
                  <tr>
                    <th scope="row" class="th_style">전화번호</th>
                    <td class="td_style">-</td>
                    <th scope="row" class="th_style">휴대폰번호</th>
                    <td class="td_style">010-8991-9506</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div>
              <h4 class="my_tit">결제정보</h4>
              <div>
                <ol class="pay_inform_ol">
                  <li class="pay_inform_li">
                    <ol>
                      <li class="li_top"><span>주문금액</span><strong>47,600원</strong></li>
                      <li class="li_bottom"><span>상품금액</span><span>47,600원</span></li>
                      <li class="li_bottom"><span>배송비</span><span>0원</span></li>
                    </ol>
                  </li>
                  <li class="pay_inform_li">
                    <ol>
                      <li class="li_top"><span>할인금액</span><strong>7,140원</strong></li>
                      <li class="li_bottom"><span>쿠폰할인</span><span>7,140원</span></li>
                    </ol>
                  </li>
                  <li class="pay_inform_li_toss">
                    <ol>
                      <li class="li_top" style="color: var(--primary)"><strong>결제금액</strong><strong>40,460원</strong></li>
                      <li class="li_bottom"><span>BC카드</span><span>40,460원</span></li>
                      <li class="li_bottom"><span style="color: #a0a0a0">결제일시 2020.05.20 18:39</span></li>
                    </ol>
                  </li>
                </ol>
              </div>
            </div>
            <div>
              <div>
                <h4 class="my_tit">배송지정보</h4>
              </div>
              <table class="delivery_inform">
                <colgroup>
                  <col width="140" />
                  <col />
                  <col width="140" />
                  <col />
                </colgroup>
                <tbody>
                  <tr>
                    <th scope="row" class="th_style">받는사람</th>
                    <td colspan="3" class="td_style">김채원</td>
                  </tr>
                  <tr>
                    <th scope="row" class="th_style">전화번호</th>
                    <td class="td_style">-</td>
                    <th scope="row" class="th_style">휴대폰번호</th>
                    <td class="td_style">010-8991-9506</td>
                  </tr>
                  <tr>
                    <th scope="row" class="th_style">주소</th>
                    <td colspan="3" class="td_style">서울특별시 은평구 역말로 123-13 한빛월드빌 502호</td>
                  </tr>
                  <tr>
                    <th scope="row" class="th_style">배송요청사항</th>
                    <td colspan="3" class="td_style">부재시 문앞에 놓아주세요.</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="receipt_btn_div">
              <button type="button">영수증 출력</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

