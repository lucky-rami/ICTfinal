package com.ict.finalproject.controller;

import com.ict.finalproject.DTO.BasketDTO;
import com.ict.finalproject.JWT.JWTUtil;
import com.ict.finalproject.Service.MemberService;
import com.ict.finalproject.Service.NoticeService;
import com.ict.finalproject.Service.StoreService;
import com.ict.finalproject.vo.*;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Console;
import java.net.URI;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class storeMainController {

    @Autowired
    StoreService storeService;
    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    MemberService memberService;

    @Autowired
    ServletContext context;

    @Autowired
    NoticeService noticeService;


    // 메인 페이지 이동
    @GetMapping("/storeMain")
    public ModelAndView storeMain() {
        List<StoreVO> recentProducts = storeService.getRecentProducts();  // 3개월 내의 데이터 호출

        // 데이터가 제대로 가져와졌는지 확인
        if (recentProducts == null || recentProducts.isEmpty()) {
            System.out.println("storeMain에서 데이터가 없습니다.");
        } else {
            System.out.println("storeMain에서 데이터가 있습니다. 총 " + recentProducts.size() + "개의 상품이 있습니다.");
        }

        // Origin 이미지 경로에서 파일을 읽어옴
        String originImageDirPath = context.getRealPath("/img/store/origin");
        List<Map<String, String>> originImageInfoList = getImageInfoList(originImageDirPath);  // 이미지 리스트 생성

        // Event 이미지 경로에서 파일을 읽어옴
        String eventImageDirPath = context.getRealPath("/img/store/event");
        List<Map<String, String>> eventImageInfoList = getImageInfoList(eventImageDirPath);  // 이벤트 이미지 리스트 생성

        ModelAndView mav = new ModelAndView("store/storeMain");  // storeMain.jsp로 이동
        mav.addObject("recentProducts", recentProducts);  // recentProducts 데이터를 JSP로 전달
        mav.addObject("originImageInfoList", originImageInfoList);  // origin 이미지 경로와 이름 정보를 JSP로 전달
        mav.addObject("eventImageInfoList", eventImageInfoList);  // event 이미지 경로와 이름 정보를 JSP로 전달
        return mav;
    }

    // 이미지를 처리하는 로직을 중복해서 쓰지 않도록 메서드로 분리
    private List<Map<String, String>> getImageInfoList(String imageDirPath) {
        File folder = new File(imageDirPath);
        File[] listOfFiles = folder.listFiles();

        List<Map<String, String>> imageInfoList = new ArrayList<>();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    String imageNameWithExt = file.getName();  // 확장자가 포함된 파일 이름
                    String imageName = imageNameWithExt;

                    // 확장자를 제거한 이름만 추출
                    if (imageNameWithExt.contains(".")) {
                        imageName = imageNameWithExt.substring(0, imageNameWithExt.lastIndexOf("."));  // 확장자 제거
                    }

                    // 이미지 경로와 이미지 이름을 함께 저장
                    Map<String, String> imageInfo = new HashMap<>();
                    imageInfo.put("imageNameWithExt", imageNameWithExt);  // 확장자 포함된 경로
                    imageInfo.put("imageName", imageName);  // 확장자 제거된 이름
                    imageInfoList.add(imageInfo);
                }
            }
        }
        return imageInfoList;
    }

    @GetMapping("/storeList")
    public ModelAndView getStoreListAndView(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "12") int pageSize,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) Integer second_category,
            @RequestParam(required = false) String filterType) {



        // offset 계산
        int offset = (pageNum - 1) * pageSize;


        // 페이징 처리된 상품 목록을 가져옴 (카테고리와 필터 타입을 처리)
        List<StoreVO> pagedProducts;

        // 1. 카테고리 필터링이 있는 경우
        if (category != null) {
            pagedProducts = storeService.getProductsByCategory(pageSize, offset, category);
        }
        // 2. 필터 타입(최신순, 인기순, 가격순 등) 필터링이 있는 경우
        else if (filterType != null) {
            pagedProducts = storeService.getStoreListByFilter(filterType);
        }

        // 3. 필터링 조건이 없는 경우 전체 상품을 가져옴
        else {
            pagedProducts = storeService.getPagedProducts(pageSize, offset, null,null);
        }

        // 총 상품 개수를 가져옴
        int totalProducts = 0;
        if(category != null){
            totalProducts = storeService.getPagedProductsCnt(category, second_category);
        }else{
            totalProducts = pagedProducts.isEmpty() ? 0 : storeService.getTotalProductCount();
        }
        // 총 페이지 수 계산
        int totalPages = totalProducts > 0 ? (int) Math.ceil((double) totalProducts / pageSize) : 0;


        // 카테고리 목록을 가져옴
        List<ProductFilterVO> firstCategoryList = storeService.getFirstCategoryList();



        // ModelAndView 설정
        ModelAndView mav = new ModelAndView("store/storeList");
        mav.addObject("pagedProducts", pagedProducts); // 페이지별 상품 목록 전달
        mav.addObject("firstCategoryList", firstCategoryList); // 카테고리 필터 전달
        mav.addObject("currentPage", pageNum); // 현재 페이지 전달
        mav.addObject("totalPages", totalPages); // 총 페이지 수 전달
        mav.addObject("selectedCategory", category); // 선택된 카테고리 전달 (null일 수 있음)
        mav.addObject("selectedFilterType", filterType); // 선택된 필터 타입 전달 (null일 수 있음)

        return mav;
    }

    //API구현
//    @GetMapping("/pagedProducts")
//    @ResponseBody  // JSON으로 데이터를 반환하기 위해 추가
//    public List<StoreVO> getPagedProducts(
//            @RequestParam int pageNum,
//            @RequestParam int pageSize,
//            @RequestParam(required = false) Integer category,
//            @RequestParam(required = false) Integer second_category) {
//
//        int offset = (pageNum - 1) * pageSize;
//
//        // 두 번째 카테고리를 포함한 상품 목록을 가져옵니다.
//        List<StoreVO> pagedProducts = storeService.getPagedProducts(pageSize, offset, category, second_category);
//
//
//
//        return pagedProducts; // JSON으로 반환
//    }
    @GetMapping("/pagedProducts")
    @ResponseBody  // JSON으로 데이터를 반환하기 위해 추가
    public Map<String, Object> getPagedProducts(
            @RequestParam int pageNum,
            @RequestParam int pageSize,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) Integer second_category) {

        int offset = (pageNum - 1) * pageSize;

        // 두 번째 카테고리를 포함한 상품 목록과 상품 개수를 가져옵니다.
        List<StoreVO> pagedProducts = storeService.getPagedProducts(pageSize, offset, category, second_category);
        int pagedProductscnt = storeService.getPagedProductsCnt(category, second_category);

        // 두 데이터를 Map에 담아 반환합니다.
        Map<String, Object> response = new HashMap<>();
        response.put("pagedProducts", pagedProducts);
        response.put("pagedProductscnt", pagedProductscnt);

        return response;
    }



    // 검색된 상품 목록 가져오기
    @GetMapping("/searchProducts")
    @ResponseBody
    public List<StoreVO> searchProducts(@RequestParam("query") String query) {
        return storeService.searchStoreList(query);
    }


    // 필터링된 상품 목록 가져오기 (AJAX 요청 처리)
    @PostMapping("/filterStoreList")
    @ResponseBody
    public List<ProductFilterVO> filterStoreList(@RequestBody ProductFilterVO filterCriteria) {
        // 필터 로그 출력
        System.out.println("Received type: " + filterCriteria.getType());
        System.out.println("Received stock: " + filterCriteria.getStock());

        // 필터링된 상품 리스트 가져오기
        List<ProductFilterVO> filteredStoreList = storeService.getFirstCategoryList();

        return filteredStoreList;
    }

        // 상품 상세 정보 가져오기
        @GetMapping("/storeDetail/{storeId}")
        public ModelAndView getStoreDetail(@PathVariable int storeId) {
            ModelAndView mav = new ModelAndView("store/storeDetail");

            // 1. 기본 상품 정보 가져오기
            StoreVO storeDetail = storeService.getStoreDetail(storeId);

            // 2. 숨겨진 이미지 리스트 가져오기
            List<String> hiddenImages = storeService.getImagesByProductId(storeId);

            // 3. 기본 상품 정보와 숨겨진 이미지 리스트 설정
            storeDetail.setDetailImages(hiddenImages);

            // 4. 카테고리 코드로 해당 타입을 조회
            String categoryType = storeService.getCategoryType(storeDetail.getCategory());

            // 5. 해당 상품에 대한 리뷰 평점 평균 가져오기
            Double averageRating = storeService.getAverageRating(storeId);
            if (averageRating == null) {
                averageRating = 0.0;  // 리뷰가 없으면 기본값 0.0
            }

            // 6. 해당 상품에 대한 리뷰 수 가져오기
            int reviewCount = storeService.getReviewCount(storeId);

            // 7. 해당 상품에 대한 리뷰 목록 가져오기
            List<ReviewVO> reviews = storeService.getReviewsByProductId(storeId);
            System.out.println("reviews: "+reviews);
            // revies에 있는 img.url http://192.168.1.180:8000/이미지 이름이 들어가야함

            mav.addObject("storeDetail", storeDetail);
            mav.addObject("categoryType", categoryType);
            mav.addObject("averageRating", averageRating);  // 리뷰 평점 추가
            mav.addObject("reviewCount", reviewCount);  // 리뷰 수 추가
            mav.addObject("reviews", reviews);  // 리뷰 목록 추가

            return mav;
        }


        @GetMapping("/subcategories")
        @ResponseBody
        public List<String> subcategoriesByFirstCategory(@RequestParam("code") int categoryCode) {
            // 서버에서 categoryCode 로그 출력
            System.out.println("Received category code: " + categoryCode);

            // StoreService를 통해 하위 카테고리 목록을 가져옴
            List<String> subcategories = storeService.getSubcategoriesByFirstCategory(categoryCode);

            // 하위 카테고리 목록을 로그로 출력하여 확인
            System.out.println("Subcategories: " + subcategories);

            // JSON 형식으로 클라이언트에 반환
            return subcategories;
        }


    @RestController
    @RequestMapping("/api/notice")
    public class NoticeController {

        @GetMapping("/titlesAndDates")
        public List<Map<String, String>> getNoticeTitlesAndDates() {
            // 전체 공지사항 리스트에서 title과 regDT만 추출
            List<NoticeVO> noticeList = noticeService.getAllNotices();
            List<Map<String, String>> titlesAndDates = noticeList.stream()
                    .map(notice -> {
                        Map<String, String> map = new HashMap<>();
                        map.put("title", notice.getTitle());
                        map.put("regDT", notice.getRegDT());
                        return map;
                    })
                    .collect(Collectors.toList());

            return titlesAndDates;  // JSON 형식으로 title과 regDT 반환
        }
    }




//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    // 채원 시작
    // 헤더에서 토큰을 추출하고, 토큰의 유효성을 검증한 후 사용자 ID와 useridx를 반환 함수(코드가 너무 중복돼서 따로 뺌)
    private ResponseEntity<Map<String, Object>> extractUserIdFromToken(String Headertoken) {
        Map<String, Object> response = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();

        // Authorization 헤더 확인
        if (Headertoken == null || !Headertoken.startsWith("Bearer ")) {
            log.info("1");
            response.put("error", "Authorization 헤더가 없거나 잘못되었습니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(response, headers, HttpStatus.SEE_OTHER);
        }

        // 토큰 값에서 'Bearer ' 문자열 제거
        String token = Headertoken.substring(7);
        if (token.isEmpty()) {
            log.info("2");
            response.put("error", "JWT 토큰이 비어 있습니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(response, headers, HttpStatus.SEE_OTHER);
        }

        String userid;
        try {
            log.info("token :{}",token);
            userid = jwtUtil.getUserIdFromToken(token);  // 토큰에서 사용자 ID 추출
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "JWT 토큰 파싱 중 오류가 발생했습니다: " + e.getMessage());
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(response, headers, HttpStatus.SEE_OTHER);
        }

        if (userid == null || userid.isEmpty()) {
            log.info("4");
            response.put("error", "유효하지 않은 JWT 토큰입니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(response, headers, HttpStatus.SEE_OTHER);
        }

        // userid로 useridx 구하기
        Integer useridx = memberService.getUseridx(userid);
        if (useridx == null) {
            log.info("5");
            response.put("error", "사용자 ID에 해당하는 인덱스를 찾을 수 없습니다.");
            headers.setLocation(URI.create("/user/login"));
            return new ResponseEntity<>(response, headers, HttpStatus.SEE_OTHER);
        }

        // 정상 처리된 경우 사용자 ID와 useridx를 반환
        response.put("userid", userid);
        response.put("useridx", useridx);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/basketOK")
    public ResponseEntity<String> basketOK(@RequestBody BasketVO basketvo,
                                           @RequestHeader("Authorization") String Headertoken){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        basketvo.setUseridx(useridx);
        // 장바구니에 이미 있는지 검증
        int productExists = storeService.checkProductInBasket(basketvo);
        if (productExists > 0) {
            // 이미 장바구니에 있는 경우
            return ResponseEntity.status(HttpStatus.CONFLICT).body("장바구니에 이미 존재하는 상품입니다.");
        }

        // 장바구니에 데이터 저장
        int result = storeService.basketInput(basketvo);
        log.info("**********basket : {}",basketvo.toString());

        if(result>0){
            return ResponseEntity.ok("장바구니에 상품담기 완료");
        }else{
            return new ResponseEntity<>(tokenResponse.getHeaders(), HttpStatus.SEE_OTHER);
        }

    }

    //장바구니 페이지로 이동
    @GetMapping("/shoppingBag")
    public ModelAndView shoppingBag(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("store/shopping_bag");
        return mav;
    }

    //장바구니 데이터 select
    @PostMapping("/basketList")
    public ResponseEntity<Map<String, Object>> basketList(@RequestHeader("Authorization") String Headertoken){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // 장바구니 리스트
        List<BasketDTO> basketList = storeService.basketList(useridx);
        Map<String, Object> response = new HashMap<>();
        response.put("basketList", basketList);  // 장바구니 리스트를 맵에 저장

        return ResponseEntity.ok(response);
    }

    //장바구니 상품 삭제(x버튼)
    @PostMapping("/basketDelOk")
    public ResponseEntity<String> basketDelOk(@RequestParam int idx,
                                              @RequestHeader("Authorization") String Headertoken){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // 장바구니에서 상품 삭제 update
        int result = storeService.basketDelete(idx,useridx);

        if(result>0){
            return ResponseEntity.ok("장바구니 선택 상품 삭제 완료");
        }else{
            return new ResponseEntity<>(tokenResponse.getHeaders(), HttpStatus.SEE_OTHER);
        }
    }

    // 장바구니 상품 선택 삭제
    @PostMapping("/basketChoiceDelOk")
    public ResponseEntity<String> basketChoiceDelOk(@RequestBody Map<String, List<Integer>> request,
                                                    @RequestHeader("Authorization") String Headertoken){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        //받아온 값
        List<Integer> idxList = request.get("idxList");

        if (idxList == null || idxList.isEmpty()) {
            return ResponseEntity.badRequest().body("삭제할 상품을 선택해주세요.");
        }

        // 장바구니에서 상품 삭제 update
        for (int idx : idxList) {
            storeService.basketChoiceAndAllDelOk(idx, useridx);
        }

        return ResponseEntity.ok("장바구니 선택 상품 삭제 완료");
    }

    //장바구니 전체삭제
    @PostMapping("/basketAllDelOk")
    public ResponseEntity<String> basketAllDelOk(@RequestBody Map<String, List<Integer>> request,
                                                 @RequestHeader("Authorization") String Headertoken){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        //받아온 값
        List<Integer> idxList = request.get("idxList");

        if (idxList == null || idxList.isEmpty()) {
            return ResponseEntity.badRequest().body("삭제할 상품을 선택해주세요.");
        }

        // 장바구니에서 상품 삭제 update
        for (int idx : idxList) {
            storeService.basketChoiceAndAllDelOk(idx, useridx);
        }

        return ResponseEntity.ok("장바구니 전체 상품 삭제 완료");
    }

    // 장바구니 plus amount
    @PostMapping("/basket_plusCount")
    public ResponseEntity<String> basket_plusCount(@RequestParam int idx, @RequestParam int newTotal,
                                                   @RequestHeader("Authorization") String Headertoken){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // 장바구니에서 상품 삭제 update
        int result = storeService.basketPlusAmount(idx,useridx,newTotal);

        if(result>0){
            return ResponseEntity.ok("장바구니 상품 갯수 증가 완료");
        }else{
            return new ResponseEntity<>(tokenResponse.getHeaders(), HttpStatus.SEE_OTHER);
        }
    }

    // 장바구니 minus amount
    @PostMapping("/basket_minusCount")
    public ResponseEntity<String> basket_minusCount(@RequestParam int idx, @RequestParam int newTotal,
                                                    @RequestHeader("Authorization") String Headertoken){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // 장바구니에서 상품 삭제 update
        int result = storeService.basketMinusAmount(idx,useridx,newTotal);

        if(result>0){
            return ResponseEntity.ok("장바구니 상품 갯수 감소 완료");
        }else{
            return new ResponseEntity<>(tokenResponse.getHeaders(), HttpStatus.SEE_OTHER);
        }
    }
}

