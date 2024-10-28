<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.6.0/css/all.min.css"
    />
    <title>Document</title>
    <style>
      @import url("https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard-dynamic-subset.css");
      @import url("https://fonts.googleapis.com/css2?family=Outfit:wght@100..900&display=swap");
      body {
        background-color: #242323;
        margin: 0;
        padding: 0;
        font-family: var(--e-font), var(--k-font);
      }
      :root {
        --k-font: "Pretendard";
        --e-font: "Outfit";
      }

      button {
        cursor: pointer;
      }
      .invalidPage {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        height: 100vh;
        color: #a7a4a5;
      }
      .invalidPage div:nth-of-type(1) {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 0.5em;
        font-size: 2em;
      }
      .invalidPage div:nth-of-type(2) {
        margin-top: 20px;
        display: flex;
        gap: 0.5em;
      }

      .invalidPage div:nth-of-type(2) button {
        padding: 1em;
        width: 10em;
        border-radius: 5px;
        font-weight: bold;
        font-family: var(--e-font), var(--k-font);
      }
    </style>
  </head>
  <body>
    <div class="invalidPage">
      <div>
        <i class="fa-solid fa-circle-exclamation"></i>
        <p>유효하지 않은 페이지입니다.</p>
      </div>
      <p>관리자에게 문의하세요.(010-8991-9506)</p>
      <div>
        <button onclick="location.href='/'">홈으로</button>
        <button onclick="location.href='/notice2'">고객센터</button>
      </div>
    </div>
  </body>
</html>
