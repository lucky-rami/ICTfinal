package com.ict.finalproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ict.finalproject.DTO.LoginRequestDTO;
import com.ict.finalproject.DTO.ReviewBeforeDTO;
import com.ict.finalproject.DTO.ReviewCompletedDTO;
import com.ict.finalproject.JWT.JWTUtil;
import com.ict.finalproject.Service.MemberService;
import com.ict.finalproject.vo.MemberVO;
import com.ict.finalproject.vo.ReviewVO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Inject
    MemberService service;
    ModelAndView mav = null;
    @Autowired
    JWTUtil jwtUtil;

    //로그인 페이지 view
    @GetMapping("/login")
    public ModelAndView loginPage(){
        mav = new ModelAndView();
        mav.setViewName("join/login");
        return mav;
    }

    @GetMapping("/checkAuthentication")
    public ResponseEntity<String> checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증되지 않은 사용자입니다.");
        } else {
            return ResponseEntity.ok("인증된 사용자 ID: " + authentication.getName());
        }
    }

    @GetMapping("/userinfo")
    public String getUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            System.out.println("인증되지 않은 사용자.");
            return "인증되지 않은 사용자입니다.";
        }

        if (!authentication.isAuthenticated()) {
            System.out.println("사용자가 인증되지 않았습니다.");
            return "사용자가 인증되지 않았습니다.";
        }

        String userid = authentication.getName();
        System.out.println("인증된 사용자 ID: " + userid);

        // 인증된 사용자 ID가 "anonymousUser"인지 확인
        if ("anonymousUser".equals(userid)) {
            System.out.println("등록된 사용자 없음");
            return "등록된 사용자 없음";
        } else {
            System.out.println("사용자 ID: " + userid);
            return userid;
        }
    }


    @PostMapping("/loginOk")
    public ModelAndView loginOk(@ModelAttribute LoginRequestDTO loginRequest, HttpServletRequest request, HttpServletResponse response) {
        // ModelAndView 객체 초기화
        ModelAndView mav = new ModelAndView();

        // 클라이언트에서 전달받은 로그인 정보
        String userid = loginRequest.getUserid();
        String userpwd = loginRequest.getUserpwd();

        // 회원 정보 검증: 데이터베이스에서 사용자 정보 조회
        MemberVO member = service.memberLogin(userid, userpwd);
        if (member == null) {
            // 사용자 정보가 없으면 로그인 실패로 간주하고 로그인 페이지로 리다이렉트
            mav.setViewName("redirect:/user/login");
            return mav;
        }

        // 비밀번호 검증
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(userpwd, member.getUserpwd())) {
            // 비밀번호가 일치하지 않을 경우 로그인 페이지로 리다이렉트
            mav.setViewName("redirect:/user/login");
            return mav;
        }

        // JWT 토큰 생성 (7일 동안 유효)
        String token = jwtUtil.createJwt(userid, 604800000L);  // role 대신 userid만 사용하여 토큰 생성

        // JWT 토큰을 HTTP 응답 헤더에 추가
        response.setHeader("Authorization", "Bearer " + token);

        // 로그 출력
        log.info("로그인 성공: " + userid);
        log.info("응답 헤더에 설정된 토큰 값: " + response.getHeader("Authorization"));

        // JWT 토큰을 ModelAndView 객체에 추가
        mav.addObject("token", token);
        mav.addObject("userid", userid);

        // 메인 페이지로 리다이렉트
        mav.setViewName("redirect:/");
        return mav;
    }


    //회원가입 페이지 view
    @GetMapping("/join")
    public ModelAndView joinPage(){
        mav = new ModelAndView();
        mav.setViewName("join/join");
        return mav;
    }

    @PostMapping("/joinformOk")
    public ModelAndView joinOk(HttpServletRequest request, @RequestParam String userid, @RequestParam String userpwd, @RequestParam String username, @RequestParam String email) {
        // ModelAndView 객체 초기화
        ModelAndView mav = new ModelAndView();

        try {
            // 비밀번호 암호화를 위한 PasswordEncoder 생성
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // 회원 정보 객체 생성 및 설정
            MemberVO vo = new MemberVO();
            vo.setUserid(userid);
            vo.setUserpwd(passwordEncoder.encode(userpwd));  // 비밀번호 암호화
            vo.setUsername(username);
            vo.setEmail(email);

            // 회원 정보 생성 (데이터베이스에 사용자 추가)
            int resultMember = service.memberCreate(vo);

            if (resultMember == 1) {
                // 회원가입 성공 시 JWT 토큰 생성 (1시간 동안 유효)
                String token = jwtUtil.createJwt(userid, 3600000L);  // role 제거, userid만 사용

                // 세션에 JWT 토큰 저장
                request.getSession().setAttribute("token", token);

                // 로그인 페이지로 리다이렉트
                mav.setViewName("redirect:/user/login");
            } else {
                // 회원가입 실패 시 회원가입 페이지로 리다이렉트
                mav.setViewName("redirect:/user/join");
                mav.addObject("errorMessage", "회원가입에 실패하였습니다. 다시 시도해 주세요.");
            }
        } catch (Exception e) {
            // 오류 발생 시 회원가입 페이지로 리다이렉트 및 오류 메시지 추가
            log.error("회원가입 중 오류 발생: " + e.getMessage());
            mav.setViewName("redirect:/user/join");
            mav.addObject("errorMessage", "회원가입 중 오류가 발생하였습니다.");
        }

        return mav;
    }


    //마이페이지 view
    @GetMapping("/mypage")
    public ModelAndView mypage(){
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_main");

        return mav;
    }

    //마이페이지-좋아요 view
    @GetMapping("/mypage_heart")
    public ModelAndView mypageHeart(){
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_heart");

        return mav;
    }

    //마이페이지-주문리스트 view
    @GetMapping("/mypage_order")
    public ModelAndView mypageOrder(){
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_order");

        return mav;
    }

    //마이페이지-주문상세 view
    @GetMapping("/mypage_order_detail")
    public ModelAndView mypageOrderDetail(){
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_order_detail");

        return mav;
    }

    //마이페이지-적립금리스트 view
    @GetMapping("/mypage_point")
    public ModelAndView mypagePoint(){
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_point");

        return mav;
    }

    //마이페이지-리뷰리스트 view
    @GetMapping("/mypage_review")
    public ModelAndView mypageReview(){
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_review");

        return mav;
    }

    //마이페이지-리뷰리스트 Data(작성전, 작성완료)
    @PostMapping("/reviewList")
    public ResponseEntity<Map<String, Object>> getReviewList(@RequestHeader("Authorization") String Headertoken){
        System.out.println(Headertoken);
        Map<String, Object> response = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();

        // Authorization 헤더 확인
        if (Headertoken == null || !Headertoken.startsWith("Bearer ")) {
            response.put("error", "Authorization 헤더가 없거나 잘못되었습니다.");
            headers.setLocation(URI.create("/user/login"));  // 리다이렉션 경로 설정
            return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).body(response);
        }  // 303 또는 302 응답


        // 토큰 값에서 'Bearer ' 문자열 제거
        String token = Headertoken.substring(7);

        if (token.isEmpty()) {
            response.put("error", "JWT 토큰이 비어 있습니다.");
            headers.setLocation(URI.create("/user/login"));
            return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).body(response);
        }

        String userid;
        try {
            userid = jwtUtil.getUserIdFromToken(token);  // 토큰에서 사용자 ID 추출
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "JWT 토큰 파싱 중 오류가 발생했습니다: " + e.getMessage());
            headers.setLocation(URI.create("/user/login"));
            return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).body(response);
        }

        if (userid == null || userid.isEmpty()) {
            response.put("error", "유효하지 않은 JWT 토큰입니다.");
            headers.setLocation(URI.create("/user/login"));
            return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).body(response);
        }

        // userid로 useridx 구하기
        Integer useridx = service.getUseridx(userid);
        if (useridx == null) {
            response.put("error", "사용자 ID에 해당하는 인덱스를 찾을 수 없습니다.");
            headers.setLocation(URI.create("/user/login"));
            return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).body(response);
        }

        // 리뷰 작성 해야되는 데이터 SELECT
        List<ReviewBeforeDTO> reviewBefore = service.getReviewBefore(useridx);
        response.put("reviewBefore",reviewBefore);
        // 갯수
        int reviewBeforeAmount = service.getReviewBeforeAmount(useridx);
        response.put("reviewBeforeAmount",reviewBeforeAmount);
        log.info("*********************reviewBeforeAmount : {}",reviewBeforeAmount);

        // 작성된 리뷰 데이터 SELECT
        List<ReviewCompletedDTO> reviewCompleted = service.getReviewCompleted(useridx);
        response.put("reviewCompleted",reviewCompleted);
        log.info(reviewCompleted.toString());
        // 갯수
        int reviewCompletedAmount = service.getReviewCompletedAmount(useridx);
        response.put("reviewCompletedAmount",reviewCompletedAmount);
        log.info("*********************reviewCompletedAmount : {}",reviewCompletedAmount);

        // 성공적으로 조회된 데이터를 반환
        return ResponseEntity.ok(response);
    }

    // 리뷰 Create
    @PostMapping("/reviewWriteOK")
    public ResponseEntity<String> reviewWriteOK(@RequestParam("grade") int grade,
                                               @RequestParam("orderList_idx") int orderList_Idx,
                                               @RequestParam("content") String content,
                                               @RequestParam(value = "file", required = false) List<MultipartFile> files,
                                               @RequestHeader("Authorization") String Headertoken,
                                               HttpSession session){
        Map<String, Object> response = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();

        // Authorization 헤더 확인
        if (Headertoken == null || !Headertoken.startsWith("Bearer ")) {
            response.put("error", "Authorization 헤더가 없거나 잘못되었습니다.");
            headers.setLocation(URI.create("/user/login"));  // 리다이렉션 경로 설정
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);  // 303 또는 302 응답
        }
        // 토큰 값에서 'Bearer ' 문자열 제거
        String token = Headertoken.substring(7);

        if (token.isEmpty()) {
            response.put("error", "JWT 토큰이 비어 있습니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }

        String userid;
        try {
            userid = jwtUtil.getUserIdFromToken(token);  // 토큰에서 사용자 ID 추출
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "JWT 토큰 파싱 중 오류가 발생했습니다: " + e.getMessage());
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }

        if (userid == null || userid.isEmpty()) {
            response.put("error", "유효하지 않은 JWT 토큰입니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }

        // userid로 useridx 구하기
        Integer useridx = service.getUseridx(userid);
        if (useridx == null) {
            response.put("error", "사용자 ID에 해당하는 인덱스를 찾을 수 없습니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }

        // 리뷰 이미지파일 업로드 위치 정하기
        String path = session.getServletContext().getRealPath("/reviewFileUpload");
        log.info("파일 저장 경로: {}", path);

        String imgfile1 = null;
        String imgfile2 = null;
        String uniqueFilename="";

        try {
            // 파일이 있는 경우에만 처리
            if (files != null && !files.isEmpty()) {
                // 파일 업로드 처리
                for (int i = 0; i < files.size(); i++) {
                    MultipartFile file = files.get(i);
                    if (!file.isEmpty()) {
                        uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                        File f = new File(path, uniqueFilename);
                        file.transferTo(f); // 파일 저장

                        if (i == 0) {
                            imgfile1 = uniqueFilename;
                        } else if (i == 1) {
                            imgfile2 = uniqueFilename;
                        }
                    }
                }
            }

            // DB에 저장할 리뷰 데이터 설정
            ReviewVO review = new ReviewVO();
            review.setGrade(grade);
            review.setOrderList_idx(orderList_Idx);
            review.setContent(content);
            review.setUseridx(useridx);
            review.setImgfile1(imgfile1);
            review.setImgfile2(imgfile2);

            int result = service.saveReview(review); // 리뷰 저장 서비스 호출

            if (result > 0) {
                return ResponseEntity.ok("리뷰가 성공적으로 등록되었습니다.");
            } else {
                fileDel(path, imgfile1);
                fileDel(path, imgfile2);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 등록 실패");
            }

        } catch (Exception e) {
            fileDel(path, imgfile1);
            fileDel(path, imgfile2);
            log.error("리뷰 등록 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 등록 중 오류 발생");
        }
    }

    // 파일 삭제
    public void fileDel(String path, String filename){
        File f = new File(path, filename);
        if(f.exists()){
            //파일이 있는 경우 -> 삭제
            f.delete();
        }
    }

    // 리뷰 수정
    @PostMapping("/reviewEditOK")
    public ResponseEntity<String> reviewEditOK(@RequestParam("grade") int grade,
                                               @RequestParam("orderList_idx") int orderList_Idx,
                                               @RequestParam("content") String content,
                                               @RequestParam(value = "file", required = false) List<MultipartFile> files,
                                               @RequestParam(value = "deletedFiles", required = false) String deletedFilesJson,
                                               @RequestHeader("Authorization") String Headertoken,
                                               HttpSession session){
        Map<String, Object> response = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();

        // Authorization 헤더 확인
        if (Headertoken == null || !Headertoken.startsWith("Bearer ")) {
            response.put("error", "Authorization 헤더가 없거나 잘못되었습니다.");
            headers.setLocation(URI.create("/user/login"));  // 리다이렉션 경로 설정
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);  // 303 또는 302 응답
        }
        // 토큰 값에서 'Bearer ' 문자열 제거
        String token = Headertoken.substring(7);

        if (token.isEmpty()) {
            response.put("error", "JWT 토큰이 비어 있습니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }

        String userid;
        try {
            userid = jwtUtil.getUserIdFromToken(token);  // 토큰에서 사용자 ID 추출
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "JWT 토큰 파싱 중 오류가 발생했습니다: " + e.getMessage());
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }

        if (userid == null || userid.isEmpty()) {
            response.put("error", "유효하지 않은 JWT 토큰입니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }

        // userid로 useridx 구하기
        Integer useridx = service.getUseridx(userid);
        if (useridx == null) {
            response.put("error", "사용자 ID에 해당하는 인덱스를 찾을 수 없습니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }

        // 업데이트 전 데이터 저장
        ReviewVO reviewEditbefore = service.getReviewEditbefore(orderList_Idx);
        reviewEditbefore.setGrade(grade);
        reviewEditbefore.setContent(content);

        // 리뷰 이미지파일 업로드 위치 정하기
        String path = session.getServletContext().getRealPath("/reviewFileUpload");
        log.info("파일 저장 경로: {}", path);

        // JSON 형식의 삭제된 파일 목록을 배열로 변환
        List<String> deletedFiles = new ArrayList<>();
        if (deletedFilesJson != null && !deletedFilesJson.isEmpty()) {//삭제된 파일이 존재하면
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                deletedFiles = objectMapper.readValue(deletedFilesJson, new TypeReference<List<String>>(){});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        String imgfile1 = null;
        String imgfile2 = null;
        String uniqueFilename="";

        try {
            // 삭제된 파일 처리
            for (String deletedFile : deletedFiles) {
                fileDel(path, deletedFile);
            }

            // 파일이 있는 경우에만 처리
            if (files != null && !files.isEmpty()) {
                // 파일 업로드 처리
                for (int i = 0; i < files.size(); i++) {
                    MultipartFile file = files.get(i);
                    if (!file.isEmpty()) {
                        uniqueFilename = UUID.randomUUID().toString() + "_" +  file.getOriginalFilename();
                        File f = new File(path, uniqueFilename);
                        file.transferTo(f); // 파일 저장

                        if (i == 0) {
                            imgfile1 = uniqueFilename;
                        } else if (i == 1) {
                            imgfile2 = uniqueFilename;
                        }
                    }
                }
            }

            // 기존 파일이 있으면 유지
            // 받아온 파일이 존재하지 않는데 기존이미지파일이 존재하고 삭제된 파일도 아니다 -> 기존 파일이 존재한다!
            if (imgfile1 == null && reviewEditbefore.getImgfile1() != null && !deletedFiles.contains(reviewEditbefore.getImgfile1())) {
                imgfile1 = reviewEditbefore.getImgfile1();
            }
            if (imgfile2 == null && reviewEditbefore.getImgfile2() != null && !deletedFiles.contains(reviewEditbefore.getImgfile2())) {
                imgfile2 = reviewEditbefore.getImgfile2();
            }

            reviewEditbefore.setImgfile1(imgfile1);
            reviewEditbefore.setImgfile2(imgfile2);

            int result = service.updateReview(reviewEditbefore); // 리뷰 저장 서비스 호출

            if (result > 0) {
                return ResponseEntity.ok("리뷰가 성공적으로 수정되었습니다.");
            } else {
                fileDel(path, imgfile1);
                fileDel(path, imgfile2);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 수정 실패");
            }

        } catch (Exception e) {
            fileDel(path, imgfile1);
            fileDel(path, imgfile2);
            log.error("리뷰 등록 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 수정 중 오류 발생");
        }
    }

    //리뷰삭제
    @PostMapping("/reviewDelOK")
    public ResponseEntity<String> reviewDelOK(@RequestParam("orderList_idx") int orderList_Idx,
                                               @RequestHeader("Authorization") String Headertoken,
                                               HttpSession session){
        Map<String, Object> response = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();

        // Authorization 헤더 확인
        if (Headertoken == null || !Headertoken.startsWith("Bearer ")) {
            response.put("error", "Authorization 헤더가 없거나 잘못되었습니다.");
            headers.setLocation(URI.create("/user/login"));  // 리다이렉션 경로 설정
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);  // 303 또는 302 응답
        }
        // 토큰 값에서 'Bearer ' 문자열 제거
        String token = Headertoken.substring(7);

        if (token.isEmpty()) {
            response.put("error", "JWT 토큰이 비어 있습니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }

        String userid;
        try {
            userid = jwtUtil.getUserIdFromToken(token);  // 토큰에서 사용자 ID 추출
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "JWT 토큰 파싱 중 오류가 발생했습니다: " + e.getMessage());
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }

        if (userid == null || userid.isEmpty()) {
            response.put("error", "유효하지 않은 JWT 토큰입니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }

        // userid로 useridx 구하기
        Integer useridx = service.getUseridx(userid);
        if (useridx == null) {
            response.put("error", "사용자 ID에 해당하는 인덱스를 찾을 수 없습니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        }

        // 삭제 전 데이터 저장
        ReviewVO reviewDelbefore = service.getReviewEditbefore(orderList_Idx);

        // 리뷰 이미지파일 업로드 위치 정하기
        String path = session.getServletContext().getRealPath("/reviewFileUpload");
        log.info("파일 저장 경로: {}", path);

        try {
            // 리뷰 삭제
            service.reviewDelete(reviewDelbefore.getOrderList_idx());
            fileDel(path,reviewDelbefore.getImgfile1());
            fileDel(path,reviewDelbefore.getImgfile2());
            return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("리뷰 삭제 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 삭제 중 오류 발생");
        }
    }


    //마이페이지-문의리스트 view
    @GetMapping("/mypage_qna")
    public ModelAndView mypageQna(){
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_qna");

        return mav;
    }

    //마이페이지-회원정보수정 view
    @GetMapping("/mypage_userEdit")
    public ModelAndView mypageEdit(){
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_userEdit");

        return mav;
    }

    //마이페이지-회원탈퇴 view
    @GetMapping("/mypage_userDel")
    public ModelAndView mypageDel(){
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_userDel");

        return mav;
    }
}
