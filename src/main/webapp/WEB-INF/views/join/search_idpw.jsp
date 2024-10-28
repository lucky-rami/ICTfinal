<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>아이디/비밀번호 찾기</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css" />
  <link rel="stylesheet" href="/css/search_idpw.css" type="text/css" />
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script src="/js/search_idpw.js"></script>
  <script>
    $(document).ready(function () {
      // 탭 전환 함수
      //window.reviewTab = function (tab) {
        //$('.serachIdPw_bottom').removeClass('on').addClass('off'); // 모든 탭을 숨김
        //$('#' + tab).removeClass('off').addClass('on'); // 선택된 탭을 표시
      //};


      // 아이디 찾기 AJAX 요청
      $('#findIdForm').submit(function (event) {
        event.preventDefault();
        $.ajax({
          url: '/user/findId',
          type: 'POST',
          data: {
            username: $('#username_id').val(),
            email: $('#email_id').val()
          },
          success: function (response) {
            showModal('아이디 찾기', response , 'login');
          },
          error: function () {
            alert('아이디를 찾을 수 없습니다. 다시 시도해주세요.');
          }
        });
      });

      // 비밀번호 찾기 AJAX 요청
      $('#findPasswordForm').submit(function (event) {
        event.preventDefault();
        $.ajax({
          url: '/user/findPwd',
          type: 'POST',
          data: {
            userid: $('#userid_pw').val(),
            username: $('#username_pw').val(),
            email: $('#email_pw').val()
          },
          success: function (response) {
            showModal('비밀번호 찾기', response, 'changePassword');
          },
          error: function () {
            alert('비밀번호를 찾을 수 없습니다. 다시 시도해주세요.');
          }
        });
      });

      // 모달 표시 함수
      window.showModal = function (title, message, buttonType) {
        $('.success_modal_container_flex div:first-child span').text(title);
        $('.success_modal_container_flex div:nth-child(2)').html(message);

        let buttonHtml = '';
        if (buttonType === 'login') {
          buttonHtml = '<button onclick="location.href=\'/user/login\'">로그인 하기</button>';
        } else if (buttonType === 'changePassword') {
          buttonHtml = '<button onclick="showChangePasswordForm()">비밀번호 변경</button>';
        }

        $('.success_modal_container_flex div:nth-child(3)').html(buttonHtml);
        $('.success_modal_body').fadeIn();
      };

      // 비밀번호 변경 폼 표시 함수
      window.showChangePasswordForm = function () {
        $('.success_modal_body').fadeOut();
        $('#findPasswordForm').hide();
        $('#changePasswordForm').show();
      };

      // 비밀번호 변경 요청
      $('#changePasswordForm').submit(function (event) {
        event.preventDefault();
        var newPassword = $('#newPassword').val();
        var confirmPassword = $('#confirmPassword').val();
        var userid = $('#userid_pw').val();

        if (newPassword !== confirmPassword) {
          alert('새 비밀번호가 일치하지 않습니다.');
          return;
        }

        $.ajax({
          url: '/user/changePassword',
          type: 'POST',
          data: {
            userid: userid,
            newPassword: newPassword
          },
          success: function () {
            alert('비밀번호가 성공적으로 변경되었습니다.');
            location.href = '/user/login';
          },
          error: function () {
            alert('비밀번호 변경에 실패했습니다.');
          }
        });
      });

      // 모달 닫기
      $('.fa-x').click(function () {
        $('.success_modal_body').fadeOut();
      });
    });
  </script>
</head>

<body>
  <div class="serachIdPw_div">
    <div class="serachIdPw_back_div">
     <div class="serachIdPw_back_img" style="background: url('/img/join_img/join_back1.png')"></div>
           <div class="serachIdPw_back_img" style="background: url('/img/join_img/join_back2.png')"></div>
           <div class="serachIdPw_back_img" style="background: url('/img/join_img/join_back3.png')"></div>
           <div class="serachIdPw_back_img" style="background: url('/img/join_img/join_back4.png')"></div>
           <div class="serachIdPw_back_img" style="background: url('/img/join_img/join_back1.png')"></div>
    </div>
    <div class="serachIdPw_back_div_color"></div>
    <div class="serachIdPw_div_pa">
      <div class="serachIdPw_div_ch">
        <div class="login_tab">
          <ul>
            <li onclick="reviewTab('tab1')">아이디 찾기</li>
            <li class="search_off" onclick="reviewTab('tab2')">비밀번호 찾기</li>
          </ul>
        </div>
        <div class="serachIdPw_bottom on" id="tab1">
          <h1>아이디를 잊으셨나요?<span>가입시 입력하신 정보로 아이디를 찾으실 수 있습니다.</span></h1>
          <form id="findIdForm">
            <div class="serachIdPw_input">
              <input type="text" id="username_id" name="username" placeholder="이름을 입력하세요." required />
              <input type="text" id="email_id" name="email" placeholder="이메일을 입력하세요." required />
            </div>
            <div class="serachIdPw_qna">위의 정보로 아이디 찾기가 불가능 하신가요?<br /><a href="/notice2">고객센터로 문의하기</a></div>
            <div class="serachIdPw_btn">
              <button type="submit">아이디 찾기</button>
            </div>
          </form>
        </div>
        <div class="serachIdPw_bottom off" id="tab2">
          <h1>비밀번호를 잊으셨나요?<span>가입시 입력하신 정보로 비밀번호를 찾으실 수 있습니다.</span></h1>
          <form id="findPasswordForm">
            <div class="serachIdPw_input">
              <input type="text" id="userid_pw" name="userid" placeholder="아이디를 입력하세요." required />
              <input type="text" id="username_pw" name="username" placeholder="이름을 입력하세요." required />
              <input type="text" id="email_pw" name="email" placeholder="이메일을 입력하세요." required />
            </div>
            <div class="serachIdPw_qna">위의 정보로 비밀번호 찾기가 불가능 하신가요?<br /><a href="/notice2">고객센터로 문의하기</a></div>
            <div class="serachIdPw_btn">
              <button type="submit">비밀번호 찾기</button>
            </div>
          </form>

          <!-- 비밀번호 변경 폼 -->
          <form id="changePasswordForm" style="display: none;">
            <div class="serachIdPw_input">
              <input type="password" id="newPassword" name="newPassword" placeholder="새 비밀번호를 입력하세요." required />
              <input type="password" id="confirmPassword" name="confirmPassword" placeholder="새 비밀번호 확인" required />
            </div>
            <div class="serachIdPw_btn">
              <button type="submit">비밀번호 변경</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- 성공 모달 -->
  <div class="success_modal_body" style="display: none;">
    <div class="success_modal_container">
      <div class="success_modal_container_flex">
        <div>
          <span>비밀번호 찾기</span>
          <i class="fa-solid fa-x"></i>
        </div>
        <div>회원님의 비밀번호는 암호화된 상태입니다.</div>
        <div></div>
      </div>
    </div>
  </div>
</body>
</html>