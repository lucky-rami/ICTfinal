package com.ict.finalproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ict.finalproject.DTO.*;
import com.ict.finalproject.JWT.JWTUtil;
import com.ict.finalproject.Service.MasterService;
import com.ict.finalproject.Service.MemberService;
import com.ict.finalproject.vo.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {


    @Inject
    MemberService service;
    @Autowired
    MasterService masterService;
    ModelAndView mav = null;
    @Autowired
    JWTUtil jwtUtil;
    @Autowired
    public UserController( MemberService service, JWTUtil jwtUtil, MasterService masterService) {
        this.service = service;
        this.jwtUtil = jwtUtil;
        this.masterService = masterService;
    }

    //로그인 페이지 view
    @GetMapping("/login")
    public ModelAndView loginPage() {
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

    /*@GetMapping("/userinfo")
    public String getUserInfo() {
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
    }*/

    // 구글 소설 로그인


    @PostMapping("/loginOk")
    public ModelAndView loginOk(@ModelAttribute LoginRequestDTO loginRequest) {
        ModelAndView mav = new ModelAndView();

        String userid = loginRequest.getUserid();
        String userpwd = loginRequest.getUserpwd();

        // useridx를 얻기 위해 userid로 먼저 조회
        Integer idx = masterService.findUserIdxByUserid(userid);

        if (idx != null) {
            // 탈퇴된 회원인지 확인
            boolean isDeleted = masterService.checkUserDelected(idx);
            if (isDeleted) {
                mav.addObject("errorMessage", "탈퇴된 회원 아이디입니다. 다시 확인해주세요.");
                mav.setViewName("join/login");
                return mav;  // 탈퇴된 회원이므로 로그인 실패 처리
            }
        } else {
            mav.addObject("errorMessage", "잘못된 사용자명 또는 비밀번호입니다.");
            mav.setViewName("join/login");
            return mav;  // 회원이 없는 경우
        }

        // 회원 정보 검증
        MemberVO member = service.memberLogin(userid, userpwd);
        if (member == null) {
            mav.addObject("errorMessage", "잘못된 사용자명 또는 비밀번호입니다.");
            mav.setViewName("join/login");
            return mav;
        }

        // 비밀번호 검증
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(userpwd, member.getUserpwd())) {
            mav.addObject("errorMessage", "잘못된 비밀번호입니다.");
            mav.setViewName("join/login");
            return mav;
        }

        // 사용자가 신고되어 정지된 상태인지 확인
        boolean isBanned = masterService.checkUserBanStatus(userid);
        if (isBanned) {
            mav.addObject("isBanned", true);
            mav.setViewName("join/login");
            return mav;
        }

        // 로그인 성공 시: JWT 토큰 생성
        String token = jwtUtil.createJwt(userid,604800000L);  // 7일 동안 유효
        mav.addObject("token", token);
        mav.setViewName("redirect:/");  // 메인 페이지로 리다이렉트
        return mav;
    }






    //회원가입 페이지 view
    @GetMapping("/join")
    public ModelAndView joinPage() {
        mav = new ModelAndView();
        mav.setViewName("join/join");
        return mav;
    }

    @PostMapping("/joinformOk")
    public ResponseEntity<Map<String, Object>> joinOk(@RequestBody MemberVO vo) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 비밀번호 암호화 및 회원 정보 생성
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            vo.setUserpwd(passwordEncoder.encode(vo.getUserpwd()));

            int resultMember = service.memberCreate(vo);

            if (resultMember == 1) {
                String token = jwtUtil.createJwt(vo.getUserid(), 3600000L);
                response.put("success", true);
                response.put("token", token);
                response.put("redirectUrl", "/user/login"); // 리다이렉트 URL 추가
            } else {
                response.put("success", false);
                response.put("errorMessage", "회원가입에 실패하였습니다. 다시 시도해 주세요.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("errorMessage", "회원가입 중 오류가 발생하였습니다.");
        }
        return ResponseEntity.ok(response);
    }


    // 채원 시작
    // 헤더에서 토큰을 추출하고, 토큰의 유효성을 검증한 후 사용자 ID와 useridx를 반환 함수(코드가 너무 중복돼서 따로 뺌)
    private ResponseEntity<Map<String, Object>> extractUserIdFromToken(String Headertoken) {
        Map<String, Object> response = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();

        // Authorization 헤더 확인
        if (Headertoken == null || !Headertoken.startsWith("Bearer ")) {
            response.put("error", "Authorization 헤더가 없거나 잘못되었습니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(response, headers, HttpStatus.SEE_OTHER);
        }

        // 토큰 값에서 'Bearer ' 문자열 제거
        String token = Headertoken.substring(7);
        if (token.isEmpty()) {
            response.put("error", "JWT 토큰이 비어 있습니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(response, headers, HttpStatus.SEE_OTHER);
        }

        String userid;
        try {
            userid = jwtUtil.getUserIdFromToken(token);  // 토큰에서 사용자 ID 추출
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "JWT 토큰 파싱 중 오류가 발생했습니다: " + e.getMessage());
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(response, headers, HttpStatus.SEE_OTHER);
        }

        if (userid == null || userid.isEmpty()) {
            response.put("error", "유효하지 않은 JWT 토큰입니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(response, headers, HttpStatus.SEE_OTHER);
        }

        // userid로 useridx 구하기
        Integer useridx = service.getUseridx(userid);
        if (useridx == null) {
            response.put("error", "사용자 ID에 해당하는 인덱스를 찾을 수 없습니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(response, headers, HttpStatus.SEE_OTHER);
        }

        // 정상 처리된 경우 사용자 ID와 useridx를 반환
        response.put("userid", userid);
        response.put("useridx", useridx);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/mypageCommInfo")
    public ResponseEntity<Map<String, Object>> mypageCommInfo(@RequestHeader("Authorization") String Headertoken) {
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // 회원정보 select
        MemberVO userinfo = service.getUserinfo(useridx);

        Map<String, Object> response = new HashMap<>();
        response.put("currentID", userinfo.getUserid());
        response.put("reservePoint", userinfo.getPoint());
        // 리뷰 갯수 select
        int reviewCompletedAmount = service.getReviewCompletedAmount(useridx);
        response.put("reviewCompletedAmount", reviewCompletedAmount);

        // 성공적으로 조회된 데이터를 반환
        return ResponseEntity.ok(response);
    }


    //마이페이지 view
    @GetMapping("/mypage")
    public ModelAndView mypage() {
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_main");

        return mav;
    }

    //마이페이지 메인 데이터 뿌려주기
    @PostMapping("/mypageMainList")
    public ResponseEntity<Map<String, Object>> mypageMainList(@RequestHeader("Authorization") String Headertoken) {
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // 최근 주문 : 주문일, 상품정보, 주문이름, 주문번호, 결제금액
        List<CurrentOrderDataDTO> currentOrderData = service.getCurrentOrderData(useridx);
//        log.info("********currentOrderData : {}",currentOrderData);

        // 애니 좋아요 내역
        List<AniListVO> currentLikeAniData = service.getCurrentLikeAniData(useridx);
        // 굿즈 좋아요 내역
        List<StoreVO> currentLikeGoodsData = service.getCurrentLikeGoodsData(useridx);

        Map<String, Object> response = new HashMap<>();
        response.put("currentOrderData", currentOrderData);
        response.put("currentLikeGoodsData", currentLikeGoodsData);
        response.put("currentLikeAniData", currentLikeAniData);

        // 성공적으로 조회된 데이터를 반환
        return ResponseEntity.ok(response);
    }

    //마이페이지-좋아요 view
    @GetMapping("/mypage_heart")
    public ModelAndView mypageHeart() {
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_heart");

        return mav;
    }

    //마이페이지 좋아요 데이터
    @PostMapping("/myHeartGoodsList")
    public ResponseEntity<Map<String, Object>> getHeartGoodsList(
            @RequestBody Map<String, Integer> params, @RequestHeader("Authorization") String Headertoken) {
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        int page = params.getOrDefault("page", 1);
        int pageSize = params.getOrDefault("pageSize", 16);

        List<StoreVO> likeGoodsList = service.getLikeGoods(page, pageSize,useridx);
        int totalProductCount = service.getTotalLikeGoodsCount(useridx);
        int totalPages = (int) Math.ceil((double) totalProductCount / pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("likeGoodsList", likeGoodsList);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);
        response.put("goodsTotalCount",totalProductCount);

        return ResponseEntity.ok(response);
    }

    //마이페이지 좋아요 데이터
    @PostMapping("/myHeartAniList")
    public ResponseEntity<Map<String, Object>> getHeartAniList(
            @RequestBody Map<String, Integer> params, @RequestHeader("Authorization") String Headertoken) {
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        int page = params.getOrDefault("page", 1);
        int pageSize = params.getOrDefault("pageSize", 20);

        List<AniListVO> likeAniList = service.getLikeAni(page, pageSize,useridx);
        int totalProductCount = service.getTotalLikeAniCount(useridx);
        int totalPages = (int) Math.ceil((double) totalProductCount / pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("likeAniList", likeAniList);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);
        response.put("aniTotalCount",totalProductCount);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/likeDelOk")
    public ResponseEntity<String> likeDelOk(@RequestBody Map<String, Integer> params,
                                              @RequestHeader("Authorization") String Headertoken){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // pro_idx와 ani_idx 가져오기
        Integer pro_idx = params.get("pro_idx");
        Integer ani_idx = params.get("ani_idx");

        try {
            // pro_idx가 0이 아닐 때만 좋아요 삭제
            if (pro_idx != null&&pro_idx != 0) {
                int result = service.deleteGoodsLike(useridx, pro_idx);
                if (result > 0) {
                    return ResponseEntity.ok("굿즈 좋아요 취소 완료");
                }else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("굿즈 좋아요 취소 실패");
                }
            }
            // ani_idx가 0이 아닐 때만 좋아요 삭제
            if (ani_idx != null&&ani_idx != 0) {
                int result = service.deleteAniLike(useridx, ani_idx);
                if (result > 0) {
                    return ResponseEntity.ok("애니 좋아요 취소 완료");
                }else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("애니 좋아요 취소 실패");
                }
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효한 좋아요 대상이 없습니다.2");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("좋아요 취소 중 오류 발생: " + e.getMessage());
        }
    }


    //마이페이지-주문리스트 view
    @GetMapping("/mypage_order")
    public ModelAndView mypageOrder() {
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_order");

        return mav;
    }

    // 주문리스트 데이터 뿌려주기
    @PostMapping("/getOrderListAll")
    public ResponseEntity<PageResponse<OrderListDTO>> getOrderListAll(
            @RequestHeader("Authorization") String headerToken,
            @RequestBody Map<String, Integer> params) {

        // JWT 토큰에서 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(headerToken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // 페이지 번호와 크기 가져오기 (기본값: page=1, size=10)
        int page = params.getOrDefault("page", 1);
        int pageSize = params.getOrDefault("pageSize", 5);

        // 서비스 호출
        PageResponse<OrderListDTO> orderList = service.getOrderListWithPaging(useridx, page, pageSize);

        // 클라이언트에 반환
        return ResponseEntity.ok(orderList);
    }

    //마이페이지-주문상세 view
    @GetMapping("/mypage_order_detail/{order_idx}")
    public ModelAndView mypageOrderDetail(@PathVariable("order_idx") int order_idx) {
        mav = new ModelAndView();
        mav.addObject("order_idx", order_idx);
        mav.setViewName("mypage/mypage_order_detail");
        return mav;
    }

    //마이페이지 주문 상세 데이터 뿌려주기
    @PostMapping("/getOrderDetailOk")
    public ResponseEntity<Map<String, Object>> getOrderDetailOk(@RequestParam("order_idx") int order_idx,
                                                                @RequestHeader("Authorization") String headerToken){
        // JWT 토큰에서 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(headerToken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        //주문 상세 내역 데이터
        OrderListDTO orderDatail = service.getOrderDetailData(order_idx,useridx);
        // 주문자 데이터
        MemberVO orderer = service.getUserinfo(useridx);
        Map<String, Object> response = new HashMap<>();
        response.put("orderDatail", orderDatail);
        response.put("orderer", orderer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //마이페이지-적립금리스트 view
    @GetMapping("/mypage_point")
    public ModelAndView mypagePoint() {
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_point");

        return mav;
    }

    // 적립금 내역 데이터
    @PostMapping("/getPointList")
    public ResponseEntity<Map<String, Object>> getPointList(@RequestBody Map<String, Integer> params,
                                                            @RequestHeader("Authorization") String Headertoken) {
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        int page = params.getOrDefault("page", 1);
        int pageSize = params.getOrDefault("pageSize", 10);

        List<PointVO> pointList = service.getPointList(page, pageSize, useridx);
        int totalProductCount = service.getTotalPointCount(useridx);
        int totalPages = (int) Math.ceil((double) totalProductCount / pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("pointList", pointList);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);

        return ResponseEntity.ok(response);
    }

    //마이페이지-리뷰리스트 view
    @GetMapping("/mypage_review")
    public ModelAndView mypageReview() {
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_review");

        return mav;
    }

    //마이페이지-리뷰리스트 Data(작성전, 작성완료)
    @PostMapping("/reviewList")
    public ResponseEntity<Map<String, Object>> getReviewList(@RequestHeader("Authorization") String Headertoken) {
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // 리뷰 작성 해야되는 데이터 SELECT
        List<ReviewBeforeDTO> reviewBefore = service.getReviewBefore(useridx);
        Map<String, Object> response = new HashMap<>();
        response.put("reviewBefore", reviewBefore);
        // 갯수
        int reviewBeforeAmount = service.getReviewBeforeAmount(useridx);
        response.put("reviewBeforeAmount", reviewBeforeAmount);

        // 작성된 리뷰 데이터 SELECT
        List<ReviewCompletedDTO> reviewCompleted = service.getReviewCompleted(useridx);
        response.put("reviewCompleted", reviewCompleted);

        // 갯수
        int reviewCompletedAmount = service.getReviewCompletedAmount(useridx);
        response.put("reviewCompletedAmount", reviewCompletedAmount);

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
                                                HttpSession session) {
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        String imgfile1 = null;
        String imgfile2 = null;
        String uniqueFilename = "";

        try {
            // 파일이 있는 경우에만 처리
            if (files != null && !files.isEmpty()) {
                // 파일 업로드 처리
                for (int i = 0; i < files.size(); i++) {
                    MultipartFile file = files.get(i);
                    if (!file.isEmpty()) {
                        uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                        // 로컬 서버에 파일 저장하지 않고, 이미지 서버로 전송
                        uploadFileToImageServer(file, uniqueFilename);

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
                service.pointUpdate(useridx, 2, 150);
                return ResponseEntity.ok("리뷰가 성공적으로 등록되었습니다.");
            } else {
                fileDel(imgfile1);
                fileDel(imgfile2);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 등록 실패");
            }

        } catch (Exception e) {
            fileDel(imgfile1);
            fileDel(imgfile2);
            log.error("리뷰 등록 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 등록 중 오류 발생");
        }
    }

    // 이미지 서버로 파일을 전송하는 메소드
    private void uploadFileToImageServer(MultipartFile file, String uniqueFilename) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String imageServerUrl = "http://192.168.1.92:8000/upload"; // 이미지 서버의 파일 업로드 엔드포인트

        // 파일을 MultiValueMap으로 준비
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), uniqueFilename));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 이미지 서버로 파일 전송
        restTemplate.postForEntity(imageServerUrl, requestEntity, String.class);
    }

    // MultipartFile을 InputStream 리소스로 변환하는 클래스
    class MultipartInputStreamFileResource extends InputStreamResource {
        private final String filename;

        public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() throws IOException {
            return -1; // length를 모르는 경우 -1 반환
        }
    }

    // 파일 삭제
    public void fileDel(String filename) {
        RestTemplate restTemplate = new RestTemplate();
        String deleteUrl = "http://192.168.1.92:8000/delete/" + filename;

        try {
            restTemplate.delete(deleteUrl);
            System.out.println("File deleted successfully: " + filename);
        } catch (RestClientException e) {
            System.err.println("Failed to delete file: " + filename + ". Error: " + e.getMessage());
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
                                               HttpSession session) {
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // 업데이트 전 데이터 저장
        ReviewVO reviewEditbefore = service.getReviewEditbefore(orderList_Idx);
        reviewEditbefore.setGrade(grade);
        reviewEditbefore.setContent(content);

        // JSON 형식의 삭제된 파일 목록을 배열로 변환
        List<String> deletedFiles = new ArrayList<>();
        if (deletedFilesJson != null && !deletedFilesJson.isEmpty()) {//삭제된 파일이 존재하면
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                deletedFiles = objectMapper.readValue(deletedFilesJson, new TypeReference<List<String>>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        String imgfile1 = null;
        String imgfile2 = null;
        String uniqueFilename = "";

        try {
            // 삭제된 파일 처리
            for (String deletedFile : deletedFiles) {
                fileDel(deletedFile);
            }

            // 파일이 있는 경우에만 처리
            if (files != null && !files.isEmpty()) {
                // 파일 업로드 처리
                for (int i = 0; i < files.size(); i++) {
                    MultipartFile file = files.get(i);
                    if (!file.isEmpty()) {
                        uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                        // 로컬 서버에 파일 저장하지 않고, 이미지 서버로 전송
                        uploadFileToImageServer(file, uniqueFilename);

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
                fileDel(imgfile1);
                fileDel(imgfile2);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 수정 실패");
            }

        } catch (Exception e) {
            fileDel(imgfile1);
            fileDel(imgfile2);
            log.error("리뷰 수정 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 수정 중 오류 발생");
        }
    }

    //리뷰삭제
    @PostMapping("/reviewDelOK")
    public ResponseEntity<String> reviewDelOK(@RequestParam("orderList_idx") int orderList_Idx,
                                              @RequestHeader("Authorization") String Headertoken,
                                              HttpSession session) {
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // 삭제 전 데이터 저장
        ReviewVO reviewDelbefore = service.getReviewEditbefore(orderList_Idx);

        // 리뷰 이미지파일 업로드 위치 정하기
        String path = session.getServletContext().getRealPath("/reviewFileUpload");
        log.info("파일 저장 경로: {}", path);

        try {
            // 리뷰 삭제
            service.reviewDelete(reviewDelbefore.getOrderList_idx());
            fileDel(reviewDelbefore.getImgfile1());
            fileDel(reviewDelbefore.getImgfile2());
            return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            log.error("리뷰 삭제 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 삭제 중 오류 발생");
        }
    }


    //마이페이지-문의리스트 view
    @GetMapping("/mypage_qna")
    public ModelAndView mypageQna() {
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_qna");

        return mav;
    }

    //마이페이지-회원정보수정 view
    @GetMapping("/mypage_userEdit")
    public ModelAndView mypageEdit() {
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_userEdit");

        return mav;
    }

    //마이페이지-수정 전 유저 정보
    @PostMapping("/userInfo")
    public ResponseEntity<Map<String, Object>> getuserInfo(@RequestHeader("Authorization") String Headertoken) {
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // 회원정보 select
        MemberVO userinfo = service.getUserinfo(useridx);
        Map<String, Object> response = new HashMap<>();
        response.put("userinfo", userinfo);

        // 성공적으로 조회된 데이터를 반환
        return ResponseEntity.ok(response);
    }

    //회원수정
    @PostMapping("/userEditOK")
    public ResponseEntity<String> userEditOK(@RequestBody MemberVO member,
                                             @RequestHeader("Authorization") String Headertoken) {
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        try {
            member.setIdx(useridx);
            int result = service.updateUser(member);
            if(result>0){
                return ResponseEntity.ok("회원 정보가 성공적으로 수정되었습니다.");
            }
            else{
                log.error("회원 수정 중 오류 발생");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 수정 중 오류 발생");
            }
        } catch (Exception e) {
            log.error("회원 수정 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원 수정 중 오류 발생");
        }
    }

    //마이페이지-회원탈퇴 이유 view
    @GetMapping("/mypage_userDelReason")
    public ModelAndView mypageDelReason(){
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_userDelReason");

        return mav;
    }

    @PostMapping("/userDelReasonOK")
    public void userDelReasonOk(@RequestBody UserDelReasonDTO userDelReasonDTO, HttpSession session){
        log.info("******userDelReasonDTO : {}",userDelReasonDTO.toString());

        // 세션에 값 저장 -> checkPwd에서 사용하기 위함
        session.setAttribute("userDelReason", userDelReasonDTO);
    }

    //마이페이지-회원탈퇴 약관동의 view
    @GetMapping("/mypage_userDelAgree")
    public ModelAndView mypageDelAgree(){
        mav = new ModelAndView();
        mav.setViewName("mypage/mypage_userDelAgree");

        return mav;
    }

    //회원탈퇴 전 비밀번호 확인
    @PostMapping("/checkPwd")
    public ResponseEntity<String> checkPwd(@RequestParam String userpwd,
                         @RequestHeader("Authorization") String Headertoken){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        String userid = (String) responseBody.get("userid");

        // 회원 정보 검증: 데이터베이스에서 사용자 정보 조회
        MemberVO member = service.memberLogin(userid, userpwd);
        if (member == null) {
            // 사용자 정보가 없으면 로그인 실패로 간주하고 로그인 페이지로 리다이렉트
            tokenResponse.getHeaders().setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(tokenResponse.getHeaders(), HttpStatus.SEE_OTHER);
        }

        // 비밀번호 검증
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(userpwd, member.getUserpwd())) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), HttpStatus.SEE_OTHER);
        }

        return ResponseEntity.ok("비밀번호가 일치합니다.");
    }

    //회원 탈퇴
    @PostMapping("/userDelOk")
    public ResponseEntity<String> checkPwd(@RequestHeader("Authorization") String Headertoken, HttpSession session){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // 회원 탈퇴
        // 세션에서 userDelReasonDTO 값 가져오기
        UserDelReasonDTO userDelReasonDTO = (UserDelReasonDTO) session.getAttribute("userDelReason");
        if (userDelReasonDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("탈퇴 이유가 없습니다.");
        }

        log.info("탈퇴 이유: {}", userDelReasonDTO.toString());
        userDelReasonDTO.setUseridx(useridx);
        int result = service.userDelOk(useridx);
        service.userDelInsert(userDelReasonDTO);

        if(result>0){
            return ResponseEntity.ok("회원 탈퇴 완료");
        }else{
            return new ResponseEntity<>(tokenResponse.getHeaders(), HttpStatus.SEE_OTHER);
        }
    }

    // 아이디/비번찾기 페이지
    @GetMapping("/idSearch")
    public ModelAndView idSearch(){
        mav = new ModelAndView();
        mav.setViewName("join/search_idpw");

        return mav;
    }
    @GetMapping("/pwdSearch")
    public ModelAndView pwdSearch(){
        mav = new ModelAndView();
        mav.setViewName("join/search_idpw");

        return mav;
    }

    // 아이디 찾기
    @PostMapping("/findId")
    public ResponseEntity<String> findId(@RequestParam String username, @RequestParam String email){
        String userid = service.findId(username, email);
        if(userid!= null){
            return ResponseEntity.ok("회원님의 아이디는  " + userid + " 입니다");
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("찾으시는 아이디가 없습니다.");
        }
    }

    // 비밀번호찾기
    @PostMapping("findPwd")
    public ResponseEntity<String> findPwd(@RequestParam String userid, @RequestParam String username , @RequestParam String email){
        String userpwd = service.findPwd(userid, username ,email);
        if(userpwd!= null){
            return ResponseEntity.ok("회원님의 비밀번호는 암호화된 상태입니다.");
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("찾으시는 아이디의 비밀번호가 없습니다.");
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestParam String userid, @RequestParam String newPassword){
        boolean isUpdated = service.changePassword(userid, newPassword);
        if(isUpdated){
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경에 실패했습니다.");
        }
    }

    @GetMapping("/checkIdDuplicate")
    public ResponseEntity<Map<String, Boolean>> checkIdDuplicate(@RequestParam String userid) {
        boolean exists = service.checkIdDuplicate(userid);
        System.out.println("아이디 존재 여부: " + exists); // 디버깅용 로그 추가
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}
