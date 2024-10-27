console.log("JWTLogin.js 파일 로드됨");
console.log("로그인 페이지에서 자바스크립트 로드됨");
async function userlogin(username, password) {
    try {
        const response = await fetch('/loginOk', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',  // 반드시 JSON 형식으로 설정
                'Accept': 'application/json'         // 서버로부터 JSON 응답 기대
            },
            body: JSON.stringify({ userid: username, userpwd: password })  // JSON 형식으로 데이터 전송
        });

        const data = await response.json();  // JSON 응답 파싱

        if (response.ok) {
            if (data.success) {
                const token = data.token;
                const userid = data.userid;

                // JWT 토큰을 로컬 스토리지에 저장
                localStorage.setItem("token", token);
                console.log("로그인 성공. JWT 토큰이 로컬 스토리지에 저장되었습니다.");

                // 로그인 성공 시 메인 페이지로 이동
                window.location.href = "/";  // 원하는 경로로 리다이렉트
            } else if (data.isBanned) {
                // 로그인이 정지된 상태일 때 경고 메시지 표시
                alert("로그인이 정지되었습니다.");
            } else if (data.isDeleted) {
                // 탈퇴된 회원일 경우 경고 메시지 표시
                alert("탈퇴된 회원 아이디입니다.");
            } else {
                // 로그인 실패 처리
                alert(data.message || "로그인 실패: 잘못된 사용자명 또는 비밀번호입니다.");
            }
        } else {
            console.error("서버 응답 오류:", response.status);
            alert("서버 오류 발생: " + response.status);
        }
    } catch (error) {
        console.error("로그인 중 오류 발생:", error);
        alert("로그인 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
}



// 인증된 요청을 위한 함수
async function makeAuthenticatedRequest(url, options = {}) {
    const token = localStorage.getItem("token");  // 로컬 스토리지에서 JWT 토큰 가져오기
    if (!token) {
        alert("로그인이 필요합니다.");  // 토큰이 없을 경우 로그인 필요 메시지
        throw new Error("JWT 토큰이 없습니다. 로그인이 필요합니다.");  // 에러 발생
    }

    // Authorization 헤더에 JWT 토큰 추가
    if (!options.headers) options.headers = {};
    options.headers['Authorization'] = `Bearer ${token}`;
    console.log("Authorization 헤더에 설정된 JWT 토큰: ", options.headers['Authorization']);  // 디버그용 로그

    try {
        const response = await fetch(url, options);  // 요청 보내기
        return response;  // 응답 반환
    } catch (error) {
        console.error("요청 중 오류 발생:", error);  // 요청 오류 출력
        alert("요청 처리 중 오류가 발생했습니다. 나중에 다시 시도해 주세요.");
        throw error;  // 에러 발생
    }
}

/* admin 로그인 */
async function adminLogin(adminid, adminpwd) {
    console.log("관리자 로그인 함수 실행됨"); // 함수 시작 시 로그

    try {
        const response = await fetch('/master/masterLoginOk', {  // 컨트롤러 경로와 일치하도록 수정
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({ adminid: adminid, adminpwd: adminpwd })
        });

        if (!response.ok) {
            console.error("서버 응답 오류:", response.status);
            alert("서버 오류 발생: " + response.status);
            return; // 오류 발생 시 함수 종료
        }

        const data = await response.json();  // 응답을 JSON으로 변환

        if (data.token) {
            const token = data.token;
            localStorage.setItem("token", token);
            console.log("로그인 성공. JWT 토큰이 로컬 스토리지에 저장되었습니다.");

            // 로그인 성공 후 페이지 이동 (토큰을 URL에 포함하지 않음)
            window.location.href = "/master/masterMain";  // 메인 페이지로 이동

            // URL에서 토큰을 제거하고 간단한 URL만 남김
            window.history.pushState({}, document.title, "/master/masterMain");
        } else {
            alert("로그인 실패. 다시 시도해 주세요.");
        }
    } catch (error) {
        console.error("로그인 중 오류 발생:", error);
        alert("로그인 중 오류가 발생했습니다. 다시 시도해 주세요.");
    }
}