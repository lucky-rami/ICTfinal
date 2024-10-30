package com.ict.finalproject.controller;


import com.ict.finalproject.DAO.TAdminDAO;
import com.ict.finalproject.DTO.*;
import com.ict.finalproject.JWT.JWTUtil;
import com.ict.finalproject.Service.MasterService;
import com.ict.finalproject.Service.MemberService;
import com.ict.finalproject.Service.TAdminService;
import com.ict.finalproject.vo.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/master")
public class masterController {

    @Autowired
    TAdminService tAdminService;
    @Autowired
    MemberService service;
    @Autowired
    MasterService masterService;
    @Autowired
    JWTUtil jwtUtil;
    @Autowired
    TAdminDAO dao;
    ModelAndView mav = null;
        /*TAdminService tAdminService;


    @Autowired
        public masterController(TAdminService tAdminService, JWTUtil jwtUtil) {
        this.tAdminService = tAdminService;
        this.jwtUtil = jwtUtil;
    }*/

    // t_admin에  admin아이디 있는지 체크 하는 API
    @GetMapping("/checkAdmin")
    public ResponseEntity<Boolean> checkAdmin(@RequestHeader("Authorization") String authHeader) {
        // Authorization 헤더 확인
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Authorization 헤더가 없거나 형식이 잘못되었습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        String token = authHeader.substring(7);  // 'Bearer ' 제거

        // JWT 토큰 유효성 검사
        if (!jwtUtil.validateToken(token)) {
            System.out.println("JWT 토큰이 유효하지 않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        try {
            // 토큰에서 사용자 ID 추출
            String userid = jwtUtil.getUserIdFromToken(token);
            System.out.println("JWT 토큰에서 추출한 사용자 ID: " + userid);

            // t_admin 테이블에서 해당 adminid가 존재하는지 확인
            boolean isAdmin = tAdminService.existsByAdminId(userid);
            System.out.println("t_admin 테이블에 " + userid + " 존재 여부: " + isAdmin);

            // 관리자 여부를 반환
            return ResponseEntity.ok(isAdmin);

        } catch (Exception e) {
            // 오류 발생 시 로그 출력 및 UNAUTHORIZED 응답
            System.out.println("JWT 처리 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
    }


    @ModelAttribute("unansweredCount")
    public int unansweredQnaCount() {
        return masterService.getUnansweredQnaCount();  // 미답변 문의 수 조회
    }

    @PostMapping("/logoutOk")
    public ResponseEntity<String> masterLogout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return ResponseEntity.ok("로그아웃 성공");
    }



    // Dashboard 매핑
    @GetMapping("/masterMain")
    public ModelAndView masterMain() {
        ModelAndView mav = new ModelAndView("master/masterMain");

        // 요약 데이터
        int totalUsers = masterService.getTotalUsers();  // 총 사용자 수 조회
        int totalOrders = masterService.getTotalOrders();  // 총 주문 수 조회
        int totalRevenue = (int) Math.round(masterService.getTotalRevenue());

        mav.addObject("totalUsers", totalUsers);  // 총 사용자 수를 Model에 추가
        mav.addObject("totalOrders", totalOrders);  // 총 주문 수를 Model에 추가
        mav.addObject("totalRevenue", totalRevenue);  // 총 매출액을 Model에 추가 (Double 타입 유지)

        // 기타 기존 데이터
        int unanswerCount = masterService.getUnansweredQnaCount();  // 답변되지 않은 문의 개수 조회
        mav.addObject("unanswerCount", unanswerCount);  // 답변되지 않은 문의 개수를 Model에 추가

        List<MemberVO> latestMembers = service.getLatestMembers();  // 최신 가입 회원 정보 조회
        mav.addObject("latestMembers", latestMembers);  // 최신 가입 회원 정보를 Model에 추가

        List<MasterVO> latestActivities = masterService.getLatestActivities();  // 최신 활동 정보 조회
        mav.addObject("latestActivities", latestActivities);  // 최신 활동 정보를 Model에 추가

        List<MasterVO> latestOrders = masterService.getRecentOrders();  // 최신 주문 정보 조회
        mav.addObject("latestOrders", latestOrders);  // 최신 주문 정보를 Model에 추가

        return mav;  // 최종 ModelAndView 반환
    }


    //Dashboard - 회원관리 - 회원 목록 리스트
    @GetMapping("/userMasterList")
    public ModelAndView masterUserList(
            @RequestParam(defaultValue = "1") String currentPage,
            @RequestParam(defaultValue = "10") int pageSize) {

        // 현재 페이지를 확인하여 부동 소수점일 경우 정수로 변환
        int currentPageInt;
        try {
            currentPageInt = Integer.parseInt(currentPage);
        } catch (NumberFormatException e) {
            // 변환 실패 시 기본값 1로 설정
            currentPageInt = 1;
        }

        int offset = Math.max(0, (currentPageInt - 1) * pageSize);
        List<MasterVO> memberList = masterService.getUserListWithPaging(offset, pageSize);


        // 오늘 가입자 수 구하기
        int newUsers = service.getNewUsers();

            // 최근 7일간 가입자 수 구하기
            int newSignups = service.getNewSignups();

            // 유저 리스트 가져오기

            int totalUser = service.getTotalUser(); // 총 유저 수
            int totalPages = (int) Math.ceil((double) totalUser / pageSize); // 총 페이지 수

            ModelAndView mav = new ModelAndView();
        mav.addObject("memberList", memberList);
        mav.addObject("currentPage", currentPageInt);
        mav.addObject("pageSize", pageSize);
        mav.addObject("totalPages", totalPages);
        mav.addObject("totalUser", totalUser);
        mav.addObject("newUsers", newUsers);
        mav.addObject("newSignups", newSignups);

        return mav;
    }


    @GetMapping("/userDelMasterList")
    public ModelAndView masterUserDelList(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        System.out.println("탈퇴 회원 목록 페이지 요청");

        // 총 탈퇴 회원 수 구하기
        int totalUsers = masterService.getTotalUserDelCount(); // 총 탈퇴 회원 수를 가져오는 서비스 메서드 호출
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize); // 총 페이지 수 계산

        // 페이지네이션의 시작 및 끝 페이지 계산
        int startPage = ((currentPage - 1) / 5) * 5 + 1; // 페이지 그룹의 시작 페이지
        int endPage = Math.min(startPage + 4, totalPages); // 페이지 그룹의 끝 페이지

        // 탈퇴 회원 목록 가져오기
        List<MasterVO> memberDelList = masterService.getMemberDelList(currentPage, pageSize);

        ModelAndView mav = new ModelAndView();
        mav.addObject("memberDelList", memberDelList);
        mav.addObject("totalUsers", totalUsers);
        mav.addObject("currentPage", currentPage);
        mav.addObject("pageSize", pageSize);
        mav.addObject("startPage", startPage);
        mav.addObject("endPage", endPage);
        mav.addObject("totalPages", totalPages);
        mav.setViewName("master/userDelMasterList");
        return mav;
    }


    @GetMapping("/checkAdminLogin")
    public ResponseEntity<Boolean> checkAdminLogin(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        System.out.println("Received token: " + token); // 토큰 출력


        if (token != null && jwtUtil.validateToken(token.replace("Bearer ", ""))) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    // Dashboard - 애니관리 -  애니목록 리스트
    @GetMapping("/aniMasterList")
    public ModelAndView masterAniList(@RequestParam(value = "currentPage", defaultValue = "1") double currentPage,
                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        int currentPageInt = (int) Math.floor(currentPage); // 정수로 변환
        int offset = Math.max(0, (currentPageInt - 1) * pageSize);
        List<MasterVO> aniList = masterService.getAniListWithPaging(offset, pageSize);
        // 전체 애니메이션 수 구하기
        int totalAniCount = masterService.getTotalAnimeCount();
        int totalPages = (int) Math.ceil((double) totalAniCount / pageSize);

        Map<String, Object> categoryCode1Count = masterService.getCategoryCodeCountByani(1);
        Map<String, Object> categoryCode2Count = masterService.getCategoryCodeCountByani(2);
        Map<String, Object> categoryCode3Count = masterService.getCategoryCodeCountByani(3);
        Map<String, Object> categoryCode4Count = masterService.getCategoryCodeCountByani(4);
        Map<String, Object> categoryCode5Count = masterService.getCategoryCodeCountByani(5);
        Map<String, Object> categoryCode6Count = masterService.getCategoryCodeCountByani(6);


        ModelAndView mav = new ModelAndView();
        mav.addObject("aniList", aniList);
        mav.addObject("currentPage", currentPageInt);
        mav.addObject("pageSize", pageSize);
        mav.addObject("totalPages", totalPages);
        mav.addObject("categoryCode1Count", categoryCode1Count.get("animation_count"));
        mav.addObject("categoryCode2Count", categoryCode2Count.get("animation_count"));
        mav.addObject("categoryCode3Count", categoryCode3Count.get("animation_count"));
        mav.addObject("categoryCode4Count", categoryCode4Count.get("animation_count"));
        mav.addObject("categoryCode5Count", categoryCode5Count.get("animation_count"));
        mav.addObject("categoryCode6Count", categoryCode6Count.get("animation_count"));
        mav.addObject("totalAniCount", totalAniCount);
        mav.setViewName("master/aniMasterList");
        return mav;
    }



    // Dashboard - 회원관리 - 신고계정목록 리스트
    @GetMapping("/reportinguserListMaster")
    public ModelAndView masterReportList(
            @RequestParam(value = "currentPage", defaultValue = "1") String currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        // currentPage를 정수형으로 변환
        int currentPageInt;
        try {
            currentPageInt = Integer.parseInt(currentPage);
        } catch (NumberFormatException e) {
            currentPageInt = 1;
        }

        // 페이징 계산
        int offset = Math.max(0, (currentPageInt - 1) * pageSize);
        List<MasterVO> reportinguserList = masterService.getReportinguserListWithPaging(offset, pageSize);

        // 총 신고 수, 접수중, 처리대기, 처리완료 수 조회
        int totalReports = masterService.getTotalReportCount();
        int activeReports = masterService.getActiveReportCount();
        int inactiveReports = masterService.getInactiveReportCount();
        int completedReports = masterService.getCompletedReportCount();
        int noncompletedReports = masterService.getNonCompletedReportCount();

        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalReports / pageSize);

        ModelAndView mav = new ModelAndView();
        mav.addObject("reportinguserList", reportinguserList);
        mav.addObject("totalReports", totalReports);
        mav.addObject("activeReports", activeReports);
        mav.addObject("inactiveReports", inactiveReports);
        mav.addObject("completedReports", completedReports);
        mav.addObject("noncompletedReports", noncompletedReports);
        mav.addObject("currentPage", currentPageInt);
        mav.addObject("pageSize", pageSize);
        mav.addObject("totalPages", totalPages);
        mav.setViewName("master/reportinguserListMaster");
        return mav;
    }



    // Dashboard - 애니관리 - 애니 목록 - 애니 추가
    @GetMapping("/aniAddMaster")
    public ModelAndView aniAddMaster() {
        mav = new ModelAndView();
        mav.setViewName("master/aniAddMaster");
        return mav;
    }

    @PostMapping("/aniAddMasterOk")
    public ResponseEntity<String> aniAddMasterOk(
            @RequestParam("title") String title,
            @RequestParam("director") String director,
            @RequestParam("outline") String outline,
            @RequestParam(value = "post_img", required = false) MultipartFile post_img,
            @RequestParam("agetype") String agetype,
            @RequestParam("anitype") String anitype,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Authorization 헤더 확인
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없습니다.");
        }

        // JWT 토큰에서 관리자 ID 추출
        String token = authorizationHeader.substring(7);  // "Bearer " 부분을 제거
        String adminid;
        try {
            adminid = jwtUtil.getUserIdFromToken(token); // JWT에서 관리자 ID 추출
            if (adminid == null || adminid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 JWT 토큰입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 토큰 파싱 중 오류가 발생했습니다.");
        }

        // adminid로 adminidx 변환
        Integer adminidx = masterService.getAdminIdxByAdminid(adminid);
        if (adminidx == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 정보를 찾을 수 없습니다.");
        }

        String post_img_filename = null;
        try {
            // 파일이 존재하면 외부 서버로 업로드
            if (post_img != null && !post_img.isEmpty()) {
                post_img_filename = uploadFileToExternalServer(post_img);
            }

            // MasterVO 객체에 데이터 설정
            MasterVO aniVO = new MasterVO();
            aniVO.setTitle(title);
            aniVO.setDirector(director);
            aniVO.setOutline(outline);
            aniVO.setPost_img_filename(post_img_filename); // 저장된 파일명 설정
            aniVO.setAgetype(agetype);
            aniVO.setAnitype(anitype);
            aniVO.setAdminidx(adminidx); // adminidx 값 설정

            // 서비스 호출하여 애니메이션 추가
            masterService.addAnimation(aniVO);

            // 성공 시 응답
            return ResponseEntity.ok("애니메이션이 성공적으로 추가되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("애니메이션 추가 중 오류가 발생했습니다.");
        }
    }





    private String uploadFileToExternalServer(MultipartFile file) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String imageServerUrl = "http://192.168.1.180:8000/upload"; // 이미지 서버의 파일 업로드 엔드포인트

        // 파일을 MultiValueMap으로 준비
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), uniqueFilename));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 이미지 서버로 파일 전송
        ResponseEntity<String> response = restTemplate.postForEntity(imageServerUrl, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // 서버에서 성공적으로 응답한 경우, 저장할 파일명만 반환
            return uniqueFilename; // URL이 아닌 파일명만 반환
        } else {
            throw new IOException("파일 업로드 실패: " + response.getStatusCode());
        }
    }


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



    // Dashboard - 애니관리 - 애니 목록 - 애니 수정
    @GetMapping("/aniEditMaster/{idx}")
    public ModelAndView aniEditMaster(@PathVariable("idx") int idx) {
        mav = new ModelAndView();
        mav.addObject("ani", masterService.aniSelect(idx));
        mav.setViewName("master/aniEditMaster");
        return mav;
    }

    @PostMapping("/aniEditMasterOk")
    public ModelAndView aniEditMasterOk(MasterVO vo,
                                        @RequestParam("idx") int idx,
                                        @RequestParam("post_img") MultipartFile post_img) throws IOException {
        mav = new ModelAndView();

        // 기존 post_img 값 가져오기 (DB에서 조회)
        String currentImg = masterService.getCurrentImgFile(idx);  // 기존 이미지 파일명 가져오기

        // 파일 처리 로직
        if (!post_img.isEmpty()) {
            // 파일이 저장될 로컬 경로 설정
            String uploadPath = new File("src/main/webapp/img/ani_img/").getAbsolutePath();
            String fileName = post_img.getOriginalFilename();
            File destination = new File(uploadPath + File.separator + fileName);

            // 디렉터리 생성 (존재하지 않으면)
            if (!destination.exists()) {
                destination.mkdirs();
            }

            post_img.transferTo(destination);  // 파일 저장
            vo.setPost_img_filename(fileName);  // 파일 이름 설정
        } else {
            vo.setPost_img_filename(currentImg);  // 업로드된 파일이 없으면 기존 파일명 유지
        }

        // 데이터베이스 업데이트
        masterService.updateAnimation(vo);

        mav.setViewName("redirect:/master/aniMasterList");
        return mav;
    }

    @PostMapping("/aniDeleteMaster/{idx}")
    public ResponseEntity<Map<String, Object>> aniDeleteMaster(@PathVariable("idx") int idx) {
        Map<String, Object> response = new HashMap<>();

        try {
            masterService.deletePostByIdx(idx);
            response.put("success", true);
            response.put("message", "삭제되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "삭제 중 오류가 발생했습니다.");
        }

        return ResponseEntity.ok(response); // JSON 응답 반환
    }

    // Dashboard - 굿즈관리 - 굿즈목록 리스트
    @GetMapping("/storeMasterList")
    public ModelAndView storeMasterList(@RequestParam(value = "currentPage", defaultValue = "1") String currentPageStr,
                                        @RequestParam(value = "pageSize", defaultValue = "10") String pageSizeStr) {
        System.out.println("관리자페이지 굿즈 상품 테이블 불러오기");

        // currentPage와 pageSize를 정수형으로 변환
        int currentPage;
        int pageSize;
        try {
            // 소수점이 포함된 경우 정수로 변환
            currentPage = (int) Double.parseDouble(currentPageStr);
            pageSize = (int) Double.parseDouble(pageSizeStr);
        } catch (NumberFormatException e) {
            // 변환에 실패하면 기본값으로 설정
            currentPage = 1;
            pageSize = 10;
        }

        // 페이징 로직 추가
        int offset = Math.max(0, (currentPage - 1) * pageSize);
        List<MasterVO> storeList = masterService.getStoreListWithPaging(offset, pageSize);

        // 총 상품 수 구하기
        int totalStore = masterService.getTotalStore();
        int totalPages = (int) Math.ceil((double) totalStore / pageSize);

        // 카테고리별 상품 수 구하기
        Map<String, Object> categoryCode1Count = masterService.getCategoryCountByCode(1); // 의류
        Map<String, Object> categoryCode2Count = masterService.getCategoryCountByCode(2); // 완구/취미
        Map<String, Object> categoryCode3Count = masterService.getCategoryCountByCode(3); // 문구/오피스
        Map<String, Object> categoryCode4Count = masterService.getCategoryCountByCode(4); // 생활용품

        ModelAndView mav = new ModelAndView();
        mav.addObject("storeList", storeList);
        mav.addObject("totalStore", totalStore);
        mav.addObject("categoryCode1Count", categoryCode1Count.get("product_category"));
        mav.addObject("categoryCode2Count", categoryCode2Count.get("product_category"));
        mav.addObject("categoryCode3Count", categoryCode3Count.get("product_category"));
        mav.addObject("categoryCode4Count", categoryCode4Count.get("product_category"));
        mav.addObject("currentPage", currentPage);
        mav.addObject("pageSize", pageSize);
        mav.addObject("totalPages", totalPages);
        mav.setViewName("master/storeMasterList");
        return mav;
    }


    // Dashboard - 신고관리 - 신고목록 리스트
    @GetMapping("/reportinguserMasterList")
    public ModelAndView reportinguserListMaster(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        int offset = (currentPage - 1) * pageSize;

        // 페이징을 적용한 신고된 유저 목록 조회
        List<MasterVO> reportingUser = masterService.getReportingUserWithPaging(offset, pageSize);

        // 디버깅을 위한 로그
        System.out.println("Retrieved reportingUser: " + reportingUser);

        // 각 유저별로 개별 신고 횟수를 계산 및 추가
        reportingUser.forEach(user -> {
            int totalUserReport = masterService.getTotalUserReport(user.getUserid());
            user.setTotalUserReport(totalUserReport);
        });

        // 전체 신고 누적 횟수 및 총 사용자 수 계산
        int totalReportUser = masterService.getTotalReportCount();
        int totalUsers = masterService.getTotalReportingUserCount();
        int totalPages = (int) Math.ceil((double) totalUsers / pageSize);

        // ModelAndView 객체 생성 및 데이터 추가
        ModelAndView mav = new ModelAndView();
        mav.addObject("reportingUser", reportingUser);
        mav.addObject("totalReportUser", totalReportUser);
        mav.addObject("currentPage", currentPage);
        mav.addObject("totalPages", totalPages);
        mav.addObject("pageSize", pageSize);
        mav.setViewName("master/reportinguserMasterList");

        return mav;
    }





    //  Dashboard - 게시판, 댓글, 리뷰 - 게시판 전체 목록
    @GetMapping("/boardMasterAll")
    public ModelAndView boardMasterAll(
            @RequestParam(value = "currentPage", defaultValue = "1") String currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        System.out.println("관리자페이지에서 커뮤니티 테이블 전체 글 목록 불러오기");

        // currentPage를 정수형으로 변환
        int currentPageInt;
        try {
            currentPageInt = Integer.parseInt(currentPage);
        } catch (NumberFormatException e) {
            // 변환 실패 시 기본값 1로 설정
            currentPageInt = 1;
        }

        // 총 게시글 수, 오늘 작성된 게시글 수, 7일간 작성된 게시글 수 가져오기
        int totalBoard = masterService.getTotalBoardCount();
        int todayBoard = masterService.getTodayBoardCount();
        int lastWeekBoard = masterService.getLastWeekBoardCount();

        // 페이징 로직 추가
        int offset = Math.max(0, (currentPageInt - 1) * pageSize);
        List<MasterVO> boardList = masterService.getBoardListWithPaging(offset, pageSize);

        int totalPages = (int) Math.ceil((double) totalBoard / pageSize);

        ModelAndView mav = new ModelAndView();
        mav.addObject("boardList", boardList);
        mav.addObject("currentPage", currentPageInt);
        mav.addObject("pageSize", pageSize);
        mav.addObject("totalPages", totalPages);
        mav.addObject("totalBoard", totalBoard);  // 총 게시글 수
        mav.addObject("todayBoard", todayBoard);  // 오늘 작성된 게시글 수
        mav.addObject("lastWeekBoard", lastWeekBoard);  // 7일간 작성된 게시글 수
        mav.setViewName("master/boardMasterAll");
        return mav;
    }



    // 해당 idx 값의 게시물 삭제하기
    @PostMapping("/boardMasterAllDelete/{idx}")
    public String boardMasterAllDelete(@PathVariable("idx") int idx) {
        System.out.println("게시글 삭제 요청: " + idx);

        // 게시글 삭제
        masterService.deleteBoard(idx);

        return "redirect:/master/boardMasterAll";  // 삭제 후 게시글 목록으로 리다이렉트
    }

    @PostMapping("/noticeMasterListDelete/{idx}")
    public String noticeMasterList(@PathVariable("idx") int idx) {
        System.out.println("게시글 삭제 요청: " + idx);

        // 공지사항 삭제
        masterService.deleteNotice(idx);

        return "redirect:/master/noticeMasterList";  // 삭제 후 게시글 목록으로 리다이렉트
    }

    @PostMapping("/boardMasterReviewDelete/{idx}")
    public String boardMasterReviewDelete(@PathVariable("idx") int idx) {
        System.out.println("리뷰 삭제 요청: " + idx);

        // 리뷰 삭제
        masterService.deleteReview(idx);

        return "redirect:/master/boardMasterReviewAll";  // 삭제 후 리뷰 목록으로 리다이렉트
    }



    //  Dashboard - 게시판, 댓글, 리뷰 - 댓글 전체 목록
    @GetMapping("/boardMasterReviewAll")
    public ModelAndView boardMasterReviewAll(
            @RequestParam(value = "currentPage", defaultValue = "1") String currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        int currentPageInt;
        try {
            currentPageInt = Integer.parseInt(currentPage);
        } catch (NumberFormatException e) {
            currentPageInt = 1;
        }

        // 페이징 계산
        int offset = Math.max(0, (currentPageInt - 1) * pageSize);
        List<MasterVO> reviewList = masterService.getReviewListWithPaging(offset, pageSize);

        // 전체 리뷰 수 조회
        int totalReviews = masterService.getTotalReviewCount();
        int totalPages = (int) Math.ceil((double) totalReviews / pageSize);

        // 오늘 작성된 리뷰 수 조회
        int newUsers = masterService.getTodayReviewCount();

        // 일주일간 작성된 리뷰 수 조회
        int newSignups = masterService.getWeekReviewCount();

        ModelAndView mav = new ModelAndView();
        mav.addObject("reviewList", reviewList);
        mav.addObject("currentPage", currentPageInt);
        mav.addObject("pageSize", pageSize);
        mav.addObject("totalPages", totalPages);
        mav.addObject("totalReviews", totalReviews); // 총 리뷰 수
        mav.addObject("newUsers", newUsers); // 오늘 작성된 리뷰 수
        mav.addObject("newSignups", newSignups); // 일주일간 작성된 리뷰 수
        mav.setViewName("master/boardMasterReviewAll");
        return mav;
    }




    //  Dashboard - 게시판, 댓글, 리뷰 - 리뷰 전체 목록
    @GetMapping("/boardMasterCommentAll")
    public ModelAndView boardMasterCommentAll(
            @RequestParam(value = "currentPage", defaultValue = "1") String currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        // currentPage를 double로 파싱한 후, Math.floor를 사용해 정수로 변환
        int currentPageInt = (int) Math.floor(Double.parseDouble(currentPage));
        int offset = (currentPageInt - 1) * pageSize;

        // 페이징을 적용한 댓글 목록 조회
        List<MasterVO> commentList = masterService.getReplyListWithPaging(offset, pageSize);

        // 전체 댓글 개수 조회
        int totalReplies = masterService.getTotalReplyCount();
        int totalPages = (int) Math.ceil((double) totalReplies / pageSize);

        // 오늘 작성된 댓글 수 조회
        int newUsers = masterService.getTodayReplyCount();

        // 일주일간 작성된 댓글 수 조회
        int newSignups = masterService.getWeekReplyCount();

        ModelAndView mav = new ModelAndView();
        mav.addObject("commentList", commentList);
        mav.addObject("currentPage", currentPageInt);
        mav.addObject("totalPages", totalPages);
        mav.addObject("pageSize", pageSize);
        mav.addObject("totalReplies", totalReplies); // 총 댓글 수
        mav.addObject("newUsers", newUsers); // 오늘 작성된 댓글 수
        mav.addObject("newSignups", newSignups); // 일주일간 작성된 댓글 수
        mav.setViewName("master/boardMasterCommentAll");
        return mav;
    }


    @GetMapping("/getCommentDetails")
    @ResponseBody
    public Map<String, Object> getCommentDetails(@RequestParam("idx") int idx) {
        Map<String, Object> response = new HashMap<>();
        try {
            MasterVO comment = masterService.getCommentByIdx(idx);
            response.put("comment", comment);
            List<MasterVO> replies = masterService.getRepliesByCommentIdx(idx);
            response.put("replies", replies);
        } catch (Exception e) {
            response.put("error", "댓글 정보를 가져오는 중 오류가 발생했습니다.");
            // Log the error for debugging
            e.printStackTrace();
        }
        return response;
    }


    @GetMapping("/getReviewDetail")
    public ResponseEntity<MasterVO> getReviewDetail(@RequestParam("idx") int idx) {
        // idx에 해당하는 리뷰 정보를 가져오기
        MasterVO review = masterService.getReviewDetail(idx);
        if (review != null) {
            try {
                // 이미지 파일이 있을 경우에만 URL 인코딩을 처리
                if (review.getImgfile1() != null && !review.getImgfile1().isEmpty()) {
                    String encodedImgFile1 = URLEncoder.encode(review.getImgfile1(), "UTF-8").replace("+", "%20");
                    review.setImgfile1(encodedImgFile1);
                } else {
                    review.setImgfile1(null); // 이미지가 없는 경우 처리
                }

                if (review.getImgfile2() != null && !review.getImgfile2().isEmpty()) {
                    String encodedImgFile2 = URLEncoder.encode(review.getImgfile2(), "UTF-8").replace("+", "%20");
                    review.setImgfile2(encodedImgFile2);
                } else {
                    review.setImgfile2(null); // 이미지가 없는 경우 처리
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(review, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //  Dashboard - 기타관리 - 공지사항 목록
    @GetMapping("/noticeMasterList")
    public ModelAndView noticeMasterList(
            @RequestParam(value = "currentPage", defaultValue = "1") String currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        System.out.println("관리자페이지 공지사항 목록 불러오기");

        // currentPage를 정수형으로 변환
        int currentPageInt;
        try {
            currentPageInt = Integer.parseInt(currentPage); // 문자열을 정수로 변환
        } catch (NumberFormatException e) {
            currentPageInt = 1; // 변환 실패 시 기본값 설정
        }

        // 공지사항 총 개수 조회
        int totalRecords = masterService.getTotalNoticeCount();

        // 페이징 계산
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        int startRecord = Math.max(0, (currentPageInt - 1) * pageSize); // 시작 레코드 계산

        // 페이징된 공지사항 목록 조회
        List<MasterVO> noticeList = masterService.getNoticeListByPage(startRecord, pageSize);

        // ModelAndView 설정
        ModelAndView mav = new ModelAndView(); // 새로운 ModelAndView 객체 생성
        mav.addObject("noticeList", noticeList);
        mav.addObject("currentPage", currentPageInt); // 정수형 페이지로 설정
        mav.addObject("pageSize", pageSize);
        mav.addObject("totalPages", totalPages);
        mav.addObject("totalRecords", totalRecords); // 전체 레코드 수 추가
        mav.setViewName("master/noticeMasterList"); // 뷰 이름 설정
        return mav; // 최종 ModelAndView 반환
    }


    //  Dashboard - 기타관리 - 공지사항 - 추가
    @GetMapping("/noticeAddMaster")
    public ModelAndView noticeAddMaster() {
        mav = new ModelAndView();
        mav.setViewName("master/noticeAddMaster");
        return mav;
    }

    @PostMapping("/noticeAddMasterOk")
    public ResponseEntity<String> noticeAddMasterOk(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("token") String token) {

        String bodyTag = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "html", Charset.forName("UTF-8")));

        try {
            // JWT 토큰에서 adminid 추출
            String adminid = jwtUtil.getUserIdFromToken(token);

            // adminid를 통해 adminidx 조회
            Integer adminidx = masterService.getAdminIdxByAdminid(adminid);

            if (adminidx == null) {
                bodyTag += "<script>alert('관리자 정보를 찾을 수 없습니다.');history.back();</script>";
                return new ResponseEntity<>(bodyTag, headers, HttpStatus.UNAUTHORIZED);
            }

            // 공지사항 등록 로직 (예: 데이터베이스에 공지사항 저장)
            MasterVO notice = new MasterVO();
            notice.setTitle(title);
            notice.setContent(content);
            notice.setAdminidx(adminidx);  // adminidx 설정

            // 공지사항 데이터베이스에 삽입
            MasterVO resultNotice = masterService.createNotice(notice);

            if (resultNotice == null) {
                bodyTag += "<script>alert('공지사항 등록에 실패했습니다.');history.back();</script>";
                return new ResponseEntity<>(bodyTag, headers, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                bodyTag += "<script>alert('공지사항이 성공적으로 등록되었습니다.');location.href='/master/noticeMasterList';</script>";
                return new ResponseEntity<>(bodyTag, headers, HttpStatus.OK);
            }

        } catch (Exception e) {
            log.error("공지사항 등록 중 오류 발생", e);
            bodyTag += "<script>alert('공지사항 등록 중 오류가 발생했습니다. 다시 시도해 주세요.');history.back();</script>";
            return new ResponseEntity<>(bodyTag, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //  Dashboard - 기타관리 - 공지사항 - 수정
    @GetMapping("/noticeEditMaster/{idx}")
    public ModelAndView noticeEditMaster(@PathVariable("idx") int idx) {
        mav = new ModelAndView();
        mav.addObject("noticeEdit", masterService.noticeSelect(idx));
        mav.setViewName("master/noticeEditMaster");
        return mav;
    }

    @PostMapping("/noticeEditMasterOk")
    public ResponseEntity<String> noticeEditMasterOk(
            @RequestParam(value = "idx") int idx, // idx는 필수
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestHeader("Authorization") String authorizationHeader) {

        log.info("전달된 토큰: " + authorizationHeader); // 토큰 확인

        // Authorization 헤더 확인
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없습니다.");
        }

        // JWT 토큰에서 관리자 ID 추출
        String token = authorizationHeader.substring(7); // "Bearer " 부분을 제거
        String adminid;
        try {
            adminid = jwtUtil.getUserIdFromToken(token); // JWT에서 관리자 ID 추출
            if (adminid == null || adminid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 JWT 토큰입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 토큰 파싱 중 오류가 발생했습니다.");
        }

        // adminid로 adminidx 변환
        Integer adminidx = masterService.getAdminIdxByAdminid(adminid);
        if (adminidx == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 정보를 찾을 수 없습니다.");
        }

        // idx 값 검증
        if (idx <= 0) {
            return ResponseEntity.badRequest().body("공지사항 ID가 잘못되었습니다."); // 수정된 부분
        }

        // 공지사항 정보 불러오기
        MasterVO editNotice = masterService.getNoticeById(idx);
        if (editNotice == null) {
            return ResponseEntity.notFound().build(); // 수정된 부분
        }

        // 공지사항 정보 업데이트
        editNotice.setTitle(title);
        editNotice.setContent(content);
        editNotice.setAdminidx(adminidx);

        try {
            boolean updateResult = masterService.updateNotice(editNotice);
            if (!updateResult) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 수정 실패. 다시 시도해 주세요.");

            }

            return ResponseEntity.ok("공지사항이 성공적으로 수정되었습니다."); // 성공 메시지

        } catch (Exception e) {
            log.error("공지사항 수정 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("공지사항 수정 중 오류가 발생했습니다. 다시 시도해 주세요.");
        }
    }

    // Dashboard - 기타관리 - 문의사항 리스트
    @GetMapping("/QNAMasterList")
    public ModelAndView QNAMasterList(
            @RequestParam(value = "currentPage", defaultValue = "1") double currentPage, // Double로 변경
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        int currentPageInt = (int) currentPage; // double을 int로 변환

        // 전체 QNA 개수 조회
        int totalRecords = masterService.getTotalQnaCount();

        // 페이징 계산
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        int startRecord = (currentPageInt - 1) * pageSize;

        // 페이징된 QNA 목록 조회
        List<MasterVO> qnaList = masterService.getQNAListByPage(startRecord, pageSize);


            int unanswerCount = masterService.getUnansweredQnaCount();

            // ModelAndView 설정
            ModelAndView mav = new ModelAndView();
        mav.addObject("qnaList", qnaList);
        mav.addObject("unanswerCount", unanswerCount);
        mav.addObject("currentPage", currentPageInt); // int로 설정
        mav.addObject("pageSize", pageSize);
        mav.addObject("totalPages", totalPages);
        mav.setViewName("master/QNAMasterList");
        return mav;
        }


        // 답변 내용 확인하기
    @GetMapping("/getQnaReply/{idx}")
    public ResponseEntity<MasterVO> getQnaReply(@PathVariable("idx") int idx) {
        try {
            // idx에 해당하는 QnA 데이터를 서비스에서 가져옴
            MasterVO qna = masterService.getQnaById(idx);

            if (qna != null) {
                return new ResponseEntity<>(qna, HttpStatus.OK); // JSON 형태로 반환
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/QNAanswerOK")
    public String QNAanswerOK(@RequestParam("reply") String reply,
                              @RequestParam("idx") int idx,
                              HttpServletRequest request) {

        // 요청에서 Authorization 헤더를 확인
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("토큰이 없습니다.");
        }

        // JWT 토큰에서 사용자 정보 추출
        String token = authorizationHeader.substring(7);  // "Bearer " 부분을 제거
        Claims claims;
        try {
            claims = jwtUtil.getClaims(token);  // JWT 파싱하여 Claims 객체로 변환
        } catch (Exception e) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        // 관리자 ID 확인 (토큰에 담긴 정보)
        String adminId = claims.getSubject();  // 토큰에서 관리자 ID 추출
        Integer adminIdx = masterService.findAdminIdxByUserid(adminId);
        if (adminIdx == null) {
            throw new RuntimeException("유효하지 않은 관리자입니다.");
        }

        // QNA 답변 처리 로직
        masterService.updateQnaAndReply(idx, reply, adminIdx);

        return "redirect:/master/QNAMasterList";
    }


    // Dashboard - 기타관리 - 자주묻는질문
    @GetMapping("/FAQMasterList")
    public ModelAndView FAQMasterList(
            @RequestParam(value = "currentPage", defaultValue = "1") String currentPageStr,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        int currentPage;
        try {
            currentPage = (int) Math.floor(Double.parseDouble(currentPageStr));
        } catch (NumberFormatException e) {
            currentPage = 1; // Default to 1 if parsing fails
        }

        // 전체 FAQ 개수 조회
        int totalRecords = masterService.getTotalFAQCount();

        // 페이징 계산
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        int startRecord = (currentPage - 1) * pageSize;

        // 페이징된 FAQ 목록 조회
        List<MasterVO> faqList = masterService.getFAQListByPage(startRecord, pageSize);

        ModelAndView mav = new ModelAndView();
        mav.addObject("faqList", faqList);
        mav.addObject("currentPage", currentPage);
        mav.addObject("totalPages", totalPages);
        mav.addObject("pageSize", pageSize);
        mav.setViewName("master/FAQMasterList");
        return mav;
    }

    // Dashboard - 기타관리 - 자주묻는질문 - 작성
    @GetMapping("/FAQAddMaster")
    public ModelAndView FAQAddMaster() {
        mav = new ModelAndView();
        mav.setViewName("master/FAQAddMaster");
        return mav;
    }

    @PostMapping("/FAQAddMasterOk")
    public ResponseEntity<String> FAQAddMasterOK(
            @RequestParam("code") String code,
            @RequestParam("question") String question,
            @RequestParam("answer") String answer,
            @RequestParam("token") String token) {

        String bodyTag = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "html", Charset.forName("UTF-8")));

        try {
            // 토큰으로 adminid 추출
            String adminid = jwtUtil.getUserIdFromToken(token);

            // adminid를 adminidx로 변환
            Integer adminidx = masterService.getAdminIdxByAdminid(adminid);

            if (adminidx == null) {
                bodyTag += "<script>alert('관리자 정보를 찾을 수 없습니다.');history.back();</script>";
                return new ResponseEntity<>(bodyTag, headers, HttpStatus.UNAUTHORIZED);
            }

            // 자주묻는 질문 등록 로직 (데이터베이스 저장)
            MasterVO faq = new MasterVO();
            faq.setFaqtype(code);
            faq.setQuestion(question);
            faq.setAnswer(answer);
            faq.setAdminidx(adminidx);

            MasterVO resultFaq = masterService.createFAQ(faq);

            if (resultFaq == null) {
                bodyTag += "<script>alert('FAQ 등록 실패. 다시 시도해 주세요.');history.back();</script>";
                return new ResponseEntity<>(bodyTag, headers, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                bodyTag += "<script>alert('FAQ가 성공적으로 등록되었습니다.');location.href='/master/FAQMasterList';</script>";
                return new ResponseEntity<>(bodyTag, headers, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("FAQ 등록 중 오류 발생", e);
            bodyTag += "<script>alert('FAQ 등록 중 오류가 발생했습니다. 다시 시도해 주세요.');history.back();</script>";
            return new ResponseEntity<>(bodyTag, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Dashboard - 기타관리 - 자주묻는질문 - 수정
    @GetMapping("/FAQEditMaster/{idx}")
    public ModelAndView FAQEditMaster(@PathVariable("idx") int idx) {
        mav = new ModelAndView();
        mav.addObject("FAQ", masterService.getFAQById(idx));
        mav.setViewName("master/FAQEditMaster");
        return mav;
    }

    @PostMapping("/FAQEditMasterOk")
    public ResponseEntity<String> FAQEditMasterOk(
            @RequestParam(value = "idx", defaultValue = "0") int idx, // 기본값 0
            @RequestParam("code") String code,
            @RequestParam("question") String question,
            @RequestParam("answer") String answer,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Authorization 헤더 확인
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없습니다.");
        }

        // JWT 토큰에서 관리자 ID 추출
        String token = authorizationHeader.substring(7);  // "Bearer " 부분을 제거
        String adminid;
        try {
            adminid = jwtUtil.getUserIdFromToken(token); // JWT에서 관리자 ID 추출
            if (adminid == null || adminid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 JWT 토큰입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 토큰 파싱 중 오류가 발생했습니다.");
        }

        // adminid로 adminidx 변환
        Integer adminidx = masterService.getAdminIdxByAdminid(adminid);
        if (adminidx == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 정보를 찾을 수 없습니다.");
        }

        MasterVO faq = new MasterVO();
        faq.setIdx(idx);
        faq.setFaqtype(code);
        faq.setQuestion(question);
        faq.setAnswer(answer);
        faq.setAdminidx(adminidx);

        try {
            masterService.updateFAQ(faq);
            return ResponseEntity.ok("자주묻는 질문이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("자주 묻는 질문 수정중 오류가 발생했습니다.");
        }
    }

    // Dashboard - 기타관리 - 자주묻는질문 - 삭제
    @GetMapping("/FAQDelMaster")
    public ModelAndView FAQDelMaster() {
        mav = new ModelAndView();
        mav.setViewName("master/FAQDelMaster");
        return mav;
    }

    // Dashboard - 기타관리 - 이벤트
    @GetMapping("/EventMasterList")
    public ModelAndView EventMasterList(
            @RequestParam(value = "currentPage", defaultValue = "1") String currentPageStr,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        int currentPage;
        try {
            currentPage = (int) Math.floor(Double.parseDouble(currentPageStr));
        } catch (NumberFormatException e) {
            currentPage = 1; // Default to 1 if parsing fails
        }

        // 전체 이벤트 개수 조회
        int totalRecords = masterService.getTotalEventCount();

        // 페이징 계산
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        int startRecord = (currentPage - 1) * pageSize;

        // 페이징된 이벤트 목록 조회
        List<MasterVO> eventList = masterService.getEventListByPage(startRecord, pageSize);

        ModelAndView mav = new ModelAndView();
        mav.addObject("eventList", eventList);
        mav.addObject("currentPage", currentPage);
        mav.addObject("totalPages", totalPages);
        mav.addObject("pageSize", pageSize);
        mav.setViewName("master/EventMasterList");
        return mav;
    }

    // Dashboard - 기타관리 - 이벤트 - 작성
    @GetMapping("/EventAddMaster")
    public ModelAndView EventAddMaster() {
        mav = new ModelAndView();
        mav.setViewName("master/EventAddMaster");
        return mav;
    }

    // Dashboard - 기타관리 - 이벤트 - 수정
    @GetMapping("/EventEditMaster/{idx}")
    public ModelAndView EventEditMaster(@PathVariable("idx") int idx) {
        ModelAndView mav = new ModelAndView();

        // 해당 idx의 이벤트 정보를 조회
        MasterVO event = masterService.getEventByIdx(idx);
        if (event == null) {
            // 이벤트가 존재하지 않을 경우 에러 페이지로 이동
            mav.setViewName("error");
            mav.addObject("message", "해당 이벤트를 찾을 수 없습니다.");
            return mav;
        }

        // 이벤트 정보가 존재하면 뷰에 전달
        mav.setViewName("master/EventEditMaster");
        mav.addObject("event", event);

        return mav;
    }

    // Dashboard - 기타관리 - 이벤트 - 삭제
    @PostMapping("/eventMasterDelete/{idx}")
    public String eventMasterDelete(@PathVariable("idx") int idx) {
        System.out.println("이벤트 삭제 요청: " + idx);
        masterService.deleteEvent(idx); // 서비스 호출로 이벤트 삭제
        return "redirect:/master/EventMasterList"; // 삭제 후 이벤트 목록으로 리다이렉트
    }

    @PostMapping("/boardreplyMasterDelete/{idx}")
    public String boardreplyMasterDelete(@PathVariable("idx") int idx) {
        System.out.println("이벤트 삭제 요청: " + idx);
        masterService.deleteReply(idx); // 서비스 호출로 이벤트 삭제
        return "redirect:/master/boardMasterCommentAll"; // 삭제 후 이벤트 목록으로 리다이렉트
    }


    // Dashboard - 굿즈관리 - 상품 추가
    @GetMapping("/storeAddMaster")
    public ModelAndView storeAddMaster() {
        mav = new ModelAndView();
        mav.setViewName("master/storeAddMaster");
        return mav;
    }

    private String getAdminIdFromToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("토큰이 없습니다.");
        }

        String token = authorizationHeader.substring(7);  // "Bearer " 부분을 제거
        String adminid = jwtUtil.getUserIdFromToken(token);  // JWT에서 관리자 ID 추출

        if (adminid == null || adminid.isEmpty()) {
            throw new RuntimeException("유효하지 않은 JWT 토큰입니다.");
        }

        return adminid;
    }

    @PostMapping("/EventEditMasterOk")
    public ResponseEntity<String> EventEditMasterOk(
            @RequestParam(value = "idx", required = false, defaultValue = "0") int idx,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("event_date") String event_date,
            @RequestParam("thumfile") MultipartFile thumfile,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        // Authorization 헤더 확인
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없습니다.");
        }

        // JWT 토큰에서 관리자 ID 추출
        String token = authorizationHeader.substring(7);  // "Bearer " 부분을 제거
        String adminid;
        try {
            adminid = jwtUtil.getUserIdFromToken(token); // JWT에서 관리자 ID 추출
            if (adminid == null || adminid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 JWT 토큰입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 토큰 파싱 중 오류가 발생했습니다.");
        }

        // adminid로 adminidx 변환
        Integer adminidx = masterService.getAdminIdxByAdminid(adminid);
        if (adminidx == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 정보를 찾을 수 없습니다.");
        }

        // 파일 저장 처리
        String thumfileName = null;
        try {
            if (thumfile != null && !thumfile.isEmpty()) {
                // 파일 저장 메서드 호출
                thumfileName = uploadFileToExternalServer(thumfile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 중 오류가 발생했습니다.");
        }

        // 이벤트 수정 엔티티 생성 및 데이터 설정
        MasterVO event = new MasterVO();
        event.setIdx(idx);
        event.setTitle(title);
        event.setContent(content);
        event.setEvent_date(event_date);
        event.setThumfile(thumfileName); // 파일명 설정
        event.setAdminidx(adminidx);

        try {
            // 이벤트 수정 서비스 호출
            boolean isUpdated = masterService.updateEvent(event);
            if (isUpdated) {
                return ResponseEntity.ok("이벤트가 성공적으로 수정되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이벤트 수정 중 오류가 발생했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이벤트 수정 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/storeAddMasterOk")
    public String storeAddMasterOK(
            @RequestParam("code") String code,
            @RequestParam("title") String title,
            @RequestParam("price") int price,
            @RequestParam(value = "thumImg", required = false) MultipartFile thumImg,
            @RequestParam("ani_title") String ani_title,
            @RequestParam("relDT") String relDT,
            @RequestParam("brand") String brand,
            @RequestParam("pro_detail") String pro_detail,
            @RequestParam("fee") int fee,
            @RequestParam("stock") int stock,
            @RequestParam("second_category") int second_category,
            @RequestParam(value = "detailImg", required = false) MultipartFile detailImg,
            @RequestHeader("Authorization") String authorizationHeader) {

        System.out.println("Received Authorization Header: " + authorizationHeader);

        // Authorization 헤더 확인
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return "redirect:/user/login"; // 토큰이 없을 경우 로그인 페이지로 리디렉션
        }

        // JWT 토큰에서 관리자 ID 추출
        String token = authorizationHeader.substring(7);  // "Bearer " 부분을 제거
        String adminid;
        int pro_idx;
        try {
            adminid = jwtUtil.getUserIdFromToken(token); // JWT에서 관리자 ID 추출
            if (adminid == null || adminid.isEmpty()) {
                return "redirect:/user/login"; // 유효하지 않은 토큰일 경우 로그인 페이지로 리디렉션
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/user/login"; // JWT 파싱 중 오류 발생 시 로그인 페이지로 리디렉션
        }

        // adminid로 adminidx 변환
        Integer adminidx = masterService.getAdminIdxByAdminid(adminid);
        if (adminidx == null) {
            return "redirect:/user/login"; // 관리자 정보를 찾을 수 없을 경우 로그인 페이지로 리디렉션
        }

        // 파일 저장 로직
        String thumimg_filename = null;
        String detailImg_filename = null;

        try {
            if (thumImg != null && !thumImg.isEmpty()) {
                thumimg_filename = uploadFileToExternalServer(thumImg);
            }
            if (detailImg != null && !detailImg.isEmpty()) {
                detailImg_filename = uploadFileToExternalServer(detailImg);
            }

            // t_product 테이블에 데이터 삽입
            MasterVO storeAdd = new MasterVO();
            storeAdd.setCategory(code);
            storeAdd.setTitle(title);
            storeAdd.setPrice(price);
            storeAdd.setThumImg(thumimg_filename);
            storeAdd.setAni_title(ani_title);
            storeAdd.setRelDT(relDT);
            storeAdd.setBrand(brand);
            storeAdd.setPro_detail(pro_detail);
            storeAdd.setFee(fee);
            storeAdd.setStock(stock);
            storeAdd.setSecond_category(second_category);
            storeAdd.setAdminidx(adminidx);

            // t_product 테이블에 데이터 삽입 후 idx 반환
            pro_idx = masterService.createStore(storeAdd);
            System.out.println("생성된 pro_idx: " + pro_idx); // 생성된 idx 값을 확인

            // pro_idx가 유효한지 확인
            if (pro_idx <= 0) {
                throw new RuntimeException("유효하지 않은 pro_idx입니다.");
            }

            // t_productimg 테이블에 데이터 삽입
            MasterVO productImg = new MasterVO();
            productImg.setPro_idx(pro_idx); // 생성된 pro_idx를 설정
            productImg.setDetailImg(detailImg_filename);
            masterService.insertProductImg(productImg);

            // 성공적으로 등록되었을 때 리스트 페이지로 리디렉션
            return "redirect:/master/storeMasterList";

        } catch (Exception e) {
            e.printStackTrace();
            // 오류 발생 시 에러 페이지로 리디렉션
            return "redirect:/errorPage"; // 적절한 에러 페이지 설정
        }
    }






    // Dashboard - 굿즈관리 - 상품 수정
    @GetMapping("/storeEditMaster/{idx}")
    public String showStoreEditForm(@PathVariable("idx") int idx, Model model) {
        MasterVO Editstore = masterService.getStoreByIdx(idx); // idx를 이용해 store 데이터를 가져옴
        model.addAttribute("Editstore", Editstore); // JSP로 store 데이터를 전달
        return "master/storeEditMaster"; // storeEdit.jsp를 반환
    }

    @PostMapping("/storeEditMasterOK")
    public ResponseEntity<String> storeEditMasterOK(
            @RequestParam("idx") int idx,
            @RequestParam("code") String code,
            @RequestParam(value = "second_category", required = false, defaultValue = "0") int second_category,
            @RequestParam("title") String title,
            @RequestParam("price") Integer price,
            @RequestParam(value = "thumImg", required = false) MultipartFile thumImg,
            @RequestParam("ani_title") String ani_title,
            @RequestParam("relDT") String relDT,
            @RequestParam("brand") String brand,
            @RequestParam("pro_detail") String pro_detail,
            @RequestParam("fee") int fee,
            @RequestParam("stock") int stock,
            @RequestParam(value = "detailImg", required = false) MultipartFile detailImg,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Authorization 헤더 확인
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없습니다.");
        }

        // JWT 토큰에서 관리자 ID 추출
        String token = authorizationHeader.substring(7);
        String adminid;
        try {
            adminid = jwtUtil.getUserIdFromToken(token);
            if (adminid == null || adminid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 JWT 토큰입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 토큰 파싱 중 오류가 발생했습니다.");
        }

        // adminid로 adminidx 변환
        Integer adminidx = masterService.getAdminIdxByAdminid(adminid);
        if (adminidx == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 정보를 찾을 수 없습니다.");
        }

        // 기존 상품 정보 불러오기
        MasterVO store = masterService.getStoreByIdx(idx);
        if (store == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 상품을 찾을 수 없습니다.");
        }

        // 파일 저장 로직
        String thumimg_filename = null;
        String detailImg_filename = null;

        try {
            if (thumImg != null && !thumImg.isEmpty()) {
                thumimg_filename = uploadFileToExternalServer(thumImg);
            }
            if (detailImg != null && !detailImg.isEmpty()) {
                detailImg_filename = uploadFileToExternalServer(detailImg);
            }

            // 상품 정보 수정
            store.setCategory(code);
            store.setSecond_category(second_category);
            store.setTitle(title);
            store.setPrice(price);
            store.setAni_title(ani_title);
            store.setRelDT(relDT);
            store.setBrand(brand);
            store.setPro_detail(pro_detail);
            store.setFee(fee);
            store.setStock(stock);
            store.setAdminidx(adminidx);

            if (thumimg_filename != null) {
                store.setThumImg(thumimg_filename);
            }

            boolean updateResult = masterService.updateStore(store);
            if (!updateResult) {
                throw new RuntimeException("굿즈 상품 수정 실패");
            }

            // detailImg 업데이트 로직
            if (detailImg_filename != null) {
                MasterVO productImg = masterService.getProductImgByIdx(idx);
                if (productImg == null) {
                    // 기존 이미지 데이터가 없으면 새로 삽입
                    productImg = new MasterVO();
                    productImg.setPro_idx(idx);
                    productImg.setDetailImg(detailImg_filename);
                    masterService.insertProductImg(productImg);
                } else {
                    // 기존 이미지 데이터가 있으면 업데이트
                    productImg.setDetailImg(detailImg_filename);
                    masterService.updateProductImg(productImg);
                }
            }

            return ResponseEntity.ok().body("굿즈 상품이 성공적으로 수정되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("굿즈 상품 수정 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/storeDeleteMaster/{idx}")
    @ResponseBody
    public Map<String, Object> storeDeleteMaster(@PathVariable("idx") int idx) {
        Map<String, Object> response = new HashMap<>();
        try {
            masterService.deleteProductImagesByProductIdx(idx);
            masterService.deleteStoreByIdx(idx);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "스토어 삭제 중 오류가 발생했습니다.");
        }
        return response;
    }




    @GetMapping("/getSubCategories/{category}")
    @ResponseBody
    public List<MasterVO> getSubCategories(@PathVariable("category") int category) {
        return masterService.getSubCategoriesByCategory(category);
    }


    // Dashboard - 굿즈관리 - 상품 수정
    @GetMapping("/orderEditMaster")
    public ModelAndView orderEditMaster() {
        mav = new ModelAndView();
        mav.setViewName("master/orderEditMaster");
        return mav;
    }

    //관리자 로그인 페이지 view

    //필요 없어보여서 지움 -> 배송지 정보 변경 : 관리자X
    @GetMapping("/masterLogin")
    public ModelAndView masterLogin() {
        mav = new ModelAndView();
        mav.setViewName("join/admin_login");
        return mav;
    }

    // 관리자 로그인
    @PostMapping("/masterLoginOk")
    public ResponseEntity<Map<String, String>> masterLoginOk(@RequestBody LoginRequestDTO loginRequest) {
        Map<String, String> response = new HashMap<>();

        String adminid = loginRequest.getAdminid();
        String adminpwd = loginRequest.getAdminpwd();

        try {
            // 관리자 정보 검증
            MasterVO admin = masterService.adminLogin(adminid, adminpwd);
            if (admin == null) {
                response.put("errorMessage", "잘못된 관리자명 또는 비밀번호입니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // 비밀번호 검증
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(adminpwd, admin.getAdminpwd())) {
                response.put("errorMessage", "잘못된 비밀번호입니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            // 로그인 성공 시: JWT 토큰 생성
            String token = jwtUtil.createJwt(adminid, 604800000L);  // 7일 동안 유효
            response.put("token", token);
            return ResponseEntity.ok(response);  // JSON 형식으로 토큰 반환
        } catch (Exception e) {
            e.printStackTrace();
            response.put("errorMessage", "서버 에러: 로그인 처리 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }






    @PostMapping("/reportinguserOK")
    public String reportinguserOK(@RequestParam("userid") String userid,
                                  @RequestParam("reason") String reason,
                                  @RequestParam("handleDT") String handleDT,
                                  @RequestParam("endDT") String endDT,
                                  @RequestParam("handleState") int handleState,
                                  @RequestParam("idx") int idx,  // 신고 ID
                                  @RequestParam("comment_idx") Integer comment_idx,  // 댓글 ID (NULL일 수 있음)
                                  @RequestParam("review_idx") Integer review_idx,    // 리뷰 ID (NULL일 수 있음)
                                  @RequestParam("comunity_idx") Integer comunity_idx, // 커뮤니티 게시글 ID (NULL일 수 있음)
                                  @RequestParam("report_type") int report_type, // 신고 유형
                                  HttpServletRequest request) {

        System.out.println("Received idx: " + idx);
        System.out.println("Received userid: " + userid);
        System.out.println("Received comment_idx: " + comment_idx);
        System.out.println("Received review_idx: " + review_idx);
        System.out.println("Received comunity_idx: " + comunity_idx);
        System.out.println("Received report_type: " + report_type);

        LocalDateTime stopDT = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime parsedHandleDT = LocalDate.parse(handleDT, formatter).atStartOfDay();
        LocalDateTime parsedEndDT = LocalDate.parse(endDT, formatter).atStartOfDay();

        // 신고할 대상의 useridx를 찾습니다.
        Integer useridx = null; // 초기값 설정

        // 댓글 신고인 경우
        if (comment_idx != null) {
            useridx = masterService.findUserIdByCommentIdx(comment_idx);  // 댓글 작성자의 ID를 찾는 메서드
        }

        // 리뷰 신고인 경우
        if (review_idx != null) {
            useridx = masterService.findUserIdByReviewIdx(review_idx);  // 리뷰 작성자의 ID를 찾는 메서드
        }

        // 커뮤니티 신고인 경우
        if (comunity_idx != null) {
            useridx = masterService.findUserIdByCommunityIdx(comunity_idx);  // 커뮤니티 게시글 작성자의 ID를 찾는 메서드
        }

        // 신고할 대상이 유효한지 체크
        if (useridx == null) {
            throw new RuntimeException("신고할 대상의 사용자 ID를 찾을 수 없습니다.");
        }

        System.out.println("Inserting report for user: " + useridx);
        System.out.println("Comment user ID: " + masterService.findUserIdByCommentIdx(comment_idx));
        System.out.println("Review user ID: " + masterService.findUserIdByReviewIdx(review_idx));
        System.out.println("Community user ID: " + masterService.findUserIdByCommunityIdx(comunity_idx));

        // handleState가 2인 경우, handleDT 업데이트와 t_ban 테이블 등록을 건너뜁니다.
        if (handleState != 2) {
            // 신고 등록 메서드에서 모든 ID를 포함하도록 합니다.
            masterService.insertReport(useridx, reason, stopDT, parsedEndDT, comment_idx, review_idx, comunity_idx, report_type); // 신고 등록

            masterService.updateAllEndDT(useridx, parsedEndDT); // endDT 업데이트
        }

        // 신고 상태와 처리 날짜 업데이트
        masterService.updateReport(idx, handleState, parsedHandleDT);

        return "redirect:/master/reportinguserListMaster";
    }




    @PostMapping("/reportingDeleteMaster/{idx}")
    public String reportingDeleteMaster(@PathVariable("idx") int idx) {
        System.out.println("Received idx: " + idx); // 로그로 idx 값 출력
        masterService.deleteReport(idx);
        masterService.deleteReport1(idx);
        return "redirect:/master/reportinguserListMaster";
    }





    // 이벤트 페이지 글 쓰기
    @PostMapping("/EventAddMasterOk")
    public ResponseEntity<String> EventAddMasterOk(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("event_date") String event_date,
            @RequestParam("thumfile") MultipartFile thumfile,
            @RequestHeader("Authorization") String authorizationHeader) {

        System.out.println("Received title: " + title);

        // Authorization 헤더 확인
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없습니다.");
        }

        // JWT 토큰에서 관리자 ID 추출
        String token = authorizationHeader.substring(7);  // "Bearer " 부분을 제거
        String adminid;
        try {
            adminid = jwtUtil.getUserIdFromToken(token); // JWT에서 관리자 ID 추출
            if (adminid == null || adminid.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 JWT 토큰입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT 토큰 파싱 중 오류가 발생했습니다.");
        }

        // adminid로 adminidx 변환
        Integer adminidx = masterService.getAdminIdxByAdminid(adminid);
        if (adminidx == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 정보를 찾을 수 없습니다.");
        }

        // 파일 저장 로직 (예시)
        String thumfileName = null;
        try {
            if (thumfile != null && !thumfile.isEmpty()) {
                // 파일 저장 메서드 (파일명 반환)
                thumfileName = uploadFileToExternalServer(thumfile);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 중 오류가 발생했습니다.");
        }

        // 이벤트 엔티티 생성 및 데이터 설정
        MasterVO event = new MasterVO();
        event.setTitle(title);
        event.setContent(content);
        event.setEvent_date(event_date);
        event.setThumfile(thumfileName); // 파일명 설정
        event.setAdminidx(adminidx);

        try {
            // 이벤트 추가 서비스 호출
            masterService.addEvent(event);
            return ResponseEntity.ok("이벤트가 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이벤트 등록 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/getEventDetail")
    @ResponseBody
    public MasterVO getEventDetail(@RequestParam("idx") int idx) {
        // 이벤트 상세 정보를 가져오기 위한 서비스 호출
        MasterVO eventDetail = masterService.getEventDetail(idx);
        return eventDetail;
    }

    //채원 시작
    // Dashboard - 주문관리 - 주문내역 리스트
    @GetMapping("/orderMasterList")
    public ModelAndView orderMasterList() {
        //주문 상태 count
        OrderStateCountDTO orderStateCount = masterService.getStateCount();
        mav = new ModelAndView();
        mav.addObject("orderStateCount",orderStateCount);
        mav.setViewName("master/orderMasterList");
        return mav;
    }

    @PostMapping("/getOrderList")
    public ResponseEntity<Map<String, Object>> getOrderList(@RequestBody Map<String, Object> params) {
        int page = (int) params.getOrDefault("page", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 6);

        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");

        String searchType = (String) params.get("searchType");
        String searchKeyword = (String) params.get("searchKeyword");

        List<CurrentOrderDataDTO> userOrderList = masterService.getUserOrderList(page, pageSize, startDate, endDate, searchType, searchKeyword);
        int totalOrderCount = masterService.getTotalOrderListCount(startDate, endDate, searchType, searchKeyword);
        int totalPages = (int) Math.ceil((double) totalOrderCount / pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("userOrderList", userOrderList);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);

        return ResponseEntity.ok(response);
    }

    //주문 상세 페이지로 이동
    @GetMapping("/orderDetail")
    public ModelAndView orderDetail(@RequestParam("order_idx") int order_idx) {
        // 주문 상세 정보 데이터 가져오기
        OrderListDTO orderDetailInfo = masterService.getDetailInfo(order_idx);
        // 주문 상품 상세 정보 데이터 가져오기
        List<OrderListVO> orderDetailProducts = masterService.getDetailProducts(order_idx);
        // 상품준비중 상태가 하나라도 있는지 확인
        boolean hasPreparingProducts = orderDetailProducts.stream().anyMatch(product -> product.getOrderState() == 2);
        mav = new ModelAndView();
        mav.addObject("orderDetailInfo",orderDetailInfo);
        mav.addObject("orderDetailProducts",orderDetailProducts);
        mav.addObject("hasPreparingProducts", hasPreparingProducts);
        mav.setViewName("master/orderDetail");
        return mav;
    }

    //운송장 번호 저장
    @PostMapping("/saveTrackingNumber")
    public ResponseEntity<String> saveTrackingNumber(@RequestBody Map<String, Object> data,
                                                     @RequestHeader("Authorization") String Headertoken) {
        HttpHeaders headers = new HttpHeaders();

        // Authorization 헤더 확인
        if (Headertoken == null || !Headertoken.startsWith("Bearer ")) {
            String errorMessage = "Authorization 헤더가 없거나 잘못되었습니다.";
            headers.setLocation(URI.create("/master/masterLogin"));
            return new ResponseEntity<>(errorMessage, headers, HttpStatus.SEE_OTHER);
        }

        // 토큰 값에서 'Bearer ' 문자열 제거
        String token = Headertoken.substring(7);
        if (token.isEmpty()) {
            String errorMessage = "JWT 토큰이 비어 있습니다.";
            headers.setLocation(URI.create("/master/masterLogin"));
            return new ResponseEntity<>(errorMessage, headers, HttpStatus.SEE_OTHER);
        }

        String userid;
        try {
            userid = jwtUtil.getUserIdFromToken(token);  // 토큰에서 사용자 ID 추출
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "JWT 토큰 파싱 중 오류가 발생했습니다: " + e.getMessage();
            headers.setLocation(URI.create("/master/masterLogin"));
            return new ResponseEntity<>(errorMessage, headers, HttpStatus.SEE_OTHER);
        }

        if (userid == null || userid.isEmpty()) {
            String errorMessage = "유효하지 않은 JWT 토큰입니다.";
            headers.setLocation(URI.create("/master/masterLogin"));
            return new ResponseEntity<>(errorMessage, headers, HttpStatus.SEE_OTHER);
        }

        // userid로 useridx 구하기
        Integer useridx = service.getUseridx(userid);
        if (useridx == null) {
            String errorMessage = "관리자 ID에 해당하는 인덱스를 찾을 수 없습니다.";
            headers.setLocation(URI.create("/master/masterLogin"));
            return new ResponseEntity<>(errorMessage, headers, HttpStatus.SEE_OTHER);
        }

        Object orderIdxObj = data.get("order_idx");
        int order_idx = 0;
        if (orderIdxObj instanceof String) {
            order_idx = Integer.parseInt((String) orderIdxObj);
        } else if (orderIdxObj instanceof Integer) {
            order_idx = (Integer) orderIdxObj;
        }
        String trackingNum = (String) data.get("trackingNum");

        // 운송장 번호를 데이터베이스에 저장
        int result = masterService.saveTrackingNumber(order_idx, trackingNum);

        if(result>0){
            // orderState=3(배송시작)으로 변경
            masterService.updatedeliOrderState(order_idx,3);
            return ResponseEntity.ok("운송장 번호가 저장되었습니다.");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("운송장 번호 저장 실패");
        }
    }

    // 상태 업데이트
    @PostMapping("/updateOrderState")
    public ResponseEntity<String> updateOrderState(@RequestBody Map<String, Object> data,
                                                   @RequestHeader("Authorization") String Headertoken) {
        HttpHeaders headers = new HttpHeaders();

        // Authorization 헤더 확인
        if (Headertoken == null || !Headertoken.startsWith("Bearer ")) {
            String errorMessage = "Authorization 헤더가 없거나 잘못되었습니다.";
            headers.setLocation(URI.create("/master/masterLogin"));
            return new ResponseEntity<>(errorMessage, headers, HttpStatus.SEE_OTHER);
        }

        // 토큰 값에서 'Bearer ' 문자열 제거
        String token = Headertoken.substring(7);
        if (token.isEmpty()) {
            String errorMessage = "JWT 토큰이 비어 있습니다.";
            headers.setLocation(URI.create("/master/masterLogin"));
            return new ResponseEntity<>(errorMessage, headers, HttpStatus.SEE_OTHER);
        }

        String userid;
        try {
            userid = jwtUtil.getUserIdFromToken(token);  // 토큰에서 사용자 ID 추출
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "JWT 토큰 파싱 중 오류가 발생했습니다: " + e.getMessage();
            headers.setLocation(URI.create("/master/masterLogin"));
            return new ResponseEntity<>(errorMessage, headers, HttpStatus.SEE_OTHER);
        }

        if (userid == null || userid.isEmpty()) {
            String errorMessage = "유효하지 않은 JWT 토큰입니다.";
            headers.setLocation(URI.create("/master/masterLogin"));
            return new ResponseEntity<>(errorMessage, headers, HttpStatus.SEE_OTHER);
        }

        // userid로 useridx 구하기
        Integer useridx = service.getUseridx(userid);
        if (useridx == null) {
            String errorMessage = "관리자 ID에 해당하는 인덱스를 찾을 수 없습니다.";
            headers.setLocation(URI.create("/master/masterLogin"));
            return new ResponseEntity<>(errorMessage, headers, HttpStatus.SEE_OTHER);
        }

        int idx = Integer.parseInt(data.get("idx").toString());
        int orderState = Integer.parseInt(data.get("orderState").toString());

        // 상태 업데이트
        int result = masterService.updateOrderState(idx, orderState);

        if(result>0){
            return ResponseEntity.ok("주문 상태가 업데이트되었습니다.");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("주문 상태 업데이트중 에러 발생");
        }
    }

    @GetMapping("/orderSalesMaster")
    public ModelAndView orderSalesMaster() {
        mav = new ModelAndView();
        mav.setViewName("master/orderSalesMaster");
        return mav;
    }

    //매출 내역 리스트
    @PostMapping("/getSalesList")
    public ResponseEntity<Map<String, Object>> getSalesList(@RequestBody Map<String, Object> params) {
        int page = (int) params.getOrDefault("page", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 6);

        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");

        List<SalesListDTO> salesList = masterService.getSalesList(page, pageSize, startDate, endDate);
        int totalOrderCount = masterService.getTotalSalesListCount(startDate, endDate);
        int totalPages = (int) Math.ceil((double) totalOrderCount / pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("salesList", salesList);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);

        return ResponseEntity.ok(response);
    }

    // 해당 일자에 맞는 매출 내역 모달
    @PostMapping("/getSalesDetails")
    public ResponseEntity<Map<String, Object>> getSalesDetails(@RequestBody Map<String, Object> params) {
        int page = (int) params.getOrDefault("page", 1);
        int pageSize = (int) params.getOrDefault("pageSize", 6);
        String orderDate = (String) params.get("orderDate");

        List<CurrentOrderDataDTO> salesDetailList = masterService.getSalesDetailList(page, pageSize, orderDate);
        int totalSalesDetailCount = masterService.getTotalSalesDetailListCount(orderDate);
        int totalPages = (int) Math.ceil((double) totalSalesDetailCount / pageSize);

        Map<String, Object> response = new HashMap<>();
        response.put("salesDetailList", salesDetailList);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);

        return ResponseEntity.ok(response);
    }

    // 매출 월/일 차트데이터
    @PostMapping("/getDayChart")
    public ResponseEntity<Map<String, Object>> getDayChart() {
        // 월별 매출 데이터
        List<SalesListDTO> monthlySales = masterService.getMonthlySales();
        // 일별 매출 데이터 (현재 주간 데이터)
        List<SalesListDTO> dailySales = masterService.getDailySales();
        Map<String, Object> response = new HashMap<>();
        response.put("monthlySales", monthlySales);
        response.put("dailySales", dailySales);
        return ResponseEntity.ok(response);
    }

    // 매출 상품별 차트데이터
    @PostMapping("/getProductChart")
    public ResponseEntity<Map<String, Object>> getProductChart() {
        // 애니별 매출 데이터
        List<SalesListDTO> aniSales = masterService.getAniSales();
        // 카테고리별 매출 데이터
        List<SalesListDTO> categorySales = masterService.getCategorySales();
        Map<String, Object> response = new HashMap<>();
        response.put("aniSales", aniSales);
        response.put("categorySales", categorySales);
        return ResponseEntity.ok(response);
    }

    // 회원가입자 수 차트 보여주기
    @GetMapping("/registrationChart")
    public ResponseEntity<List<Map<String, Object>>> getRegistrationStats() {
        List<Map<String, Object>> stats = masterService.getUserRegistrationStats();
        return ResponseEntity.ok(stats);
    }

    // 관리자페이지 메인 일별 차트 보여주기
    @GetMapping("/getCombinedSalesData")
    @ResponseBody
    public Map<String, Object> getCombinedSalesData() {
        Map<String, Object> response = new HashMap<>();

        // 애니메이션 매출 데이터 조회
        List<Map<String, Object>> aniSalesData = masterService.getAniSalesData();
        System.out.println("Ani Sales Data: " + aniSalesData); // 디버깅용 로그

        // 일별 매출 데이터 조회
        List<Map<String, Object>> dailySalesData = masterService.getDailySalesData();
        System.out.println("Daily Sales Data: " + dailySalesData); // 디버깅용 로그

        response.put("aniSalesData", aniSalesData);
        response.put("dailySalesData", dailySalesData);
        return response;
    }

    // idx에 해당하는 자주묻는 질문 삭제
    @PostMapping("/faqMasterDelete/{idx}")
    public String faqMasterDelete(@PathVariable("idx") int idx) {
        System.out.println("FAQ 삭제 요청: " + idx);

        // FAQ 삭제
        masterService.deleteFaq(idx);

        return "redirect:/master/FAQMasterList";  // 삭제 후 FAQ 목록으로 리다이렉트
    }
}
