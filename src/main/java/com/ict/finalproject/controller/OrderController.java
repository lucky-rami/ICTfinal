package com.ict.finalproject.controller;

import com.ict.finalproject.DTO.*;
import com.ict.finalproject.JWT.JWTUtil;
import com.ict.finalproject.Service.MemberService;
import com.ict.finalproject.Service.OrderService;
import com.ict.finalproject.vo.OrderListVO;
import com.ict.finalproject.vo.OrderVO;
import com.ict.finalproject.vo.PaymentVO;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    MemberService memberService;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    OrderService service;

    ModelAndView mav = null;

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
        Integer useridx = memberService.getUseridx(userid);
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

    // 주문하기 버튼 클릭 후 T_order, T_orderlist에 데이터 삽입해주는 부분
    @PostMapping("/submit")
    public ResponseEntity<Object> submitOrder(@RequestBody OrderRequest orderRequest, @RequestHeader("Authorization") String Headertoken) {

        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        Map<String, Object> response = new HashMap<>();

        // T_order 테이블에 주문 데이터 삽입
        int order_idx = service.createOrder(useridx, orderRequest.getTotal_price());

        // T_orderlist에 선택된 상품 목록 및 수량 삽입
        List<Integer> products = orderRequest.getProducts();
        List<Integer> productsCounts = orderRequest.getProductsCounts();

        for (int i = 0; i < products.size(); i++) {
            int pro_idx = products.get(i);
            int amount = productsCounts.get(i);
            service.addProductToOrderList(order_idx, pro_idx, amount);
        }

        // 주문 완료 후 order_idx를 응답에 포함
        response.put("order_idx", order_idx);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/orderpage/{order_idx}")
    public ModelAndView showOrderPage(@PathVariable("order_idx") int order_idx) {
        mav = new ModelAndView();

        // 주문 정보와 상품 목록을 조회
        OrderVO order = service.getOrderById(order_idx);
        List<OrderListVO> orderList = service.getOrderListByOrderIdx(order_idx);

        log.info(order.toString());
        log.info(orderList.toString());

        // 주문 정보와 상품 목록을 ModelAndView에 추가
        mav.addObject("order", order);
        mav.addObject("orderList", orderList);

        mav.setViewName("store/orderpage");
        return mav;
    }

    //로그인 여부
    @ResponseBody
    @GetMapping("/getuserid")
    public String getuserid(@RequestParam("Authorization") String token){
        log.info("getuserid 메서드 호출됨");
        String userid = jwtUtil.getUserIdFromToken(token); //토큰에서 사용자 아이디 추출
        log.info("userid : " + userid);
        return userid;
    }
    
    // 결제요청정보 DB에 저장
    @PostMapping("/request")
    public ResponseEntity<?> requestPayment(@RequestBody PaymentReqDTO paymentRequest,@RequestHeader("Authorization") String Headertoken) {
        // JWT 토큰 검증
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");
        paymentRequest.setUseridx(useridx);

        service.savePaymentRequest(paymentRequest); // 결제 요청 정보 DB에 저장
        service.saveOrderRequest(paymentRequest);
        return ResponseEntity.ok("Payment requested");
    }

    @GetMapping("/success")
    public ModelAndView paymentSuccess(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam int amount, HttpSession session) {
        // 세션에 paymentKey가 없는 경우 또는 이전에 사용된 경우 유효하지 않은 접근으로 처리
        if (session.getAttribute("paymentKey") != null && !session.getAttribute("paymentKey").equals(paymentKey)) {
            mav.setViewName("store/invalidPage"); // 유효하지 않은 접근에 대한 페이지
            mav.addObject("errorMessage", "유효하지 않은 페이지입니다.");
            return mav;
        }

        try {
            PaymentApprovalDTO approvalDTO = new PaymentApprovalDTO();
            approvalDTO.setPaymentKey(paymentKey);
            approvalDTO.setOrderId(orderId);
            approvalDTO.setAmount(amount);

            // Toss 결제 승인 처리
            int order_idx = service.approvePayment(approvalDTO);

            // 결제 승인에 성공하면 세션에 paymentKey 저장
            session.setAttribute("paymentKey", paymentKey);


            // 적립금 빼기
            int useridx = service.getUseridx(order_idx);
            int use_point = service.getUsePoint(order_idx);
            memberService.pointUpdate(useridx, 3, -use_point);
            log.info("사용자 ID {}의 적립금 {}원이 차감 되었습니다.", useridx, use_point);

            // 첫 번째 접근 후 세션에서 paymentKey 제거
            session.removeAttribute("paymentKey");

            // 화면에 뿌릴 데이터
            // 1. T_order : regDT(주문접수일시), 배송정보(수령인, 전화번호, 주소, 요청사항), 총 금액, 적립금 사용액
            OrderVO orderData = service.orderSuccessData(order_idx);
            log.info("***********orderData : {}",orderData.toString());
            // 2. T_orderList : 애니명, 굿즈명, 굿즈가격, amount
            List<OrderListVO> orderListData = service.orderListSuccessData(order_idx);
            // 3. T_payment : paytype, 결제완료일시, (영수증 출력시 필요한 데이터? -> 생각)
            PaymentVO paymentData = service.paymentSuccessData(order_idx);

            mav = new ModelAndView();
            mav.addObject("orderData",orderData);
            mav.addObject("orderListData",orderListData);
            mav.addObject("paymentData",paymentData);

            mav.setViewName("store/orderOk");
        } catch (Exception e) {
            // 결제 승인 실패 시 실패 페이지로 이동
            mav.setViewName("store/invalidPage");
            mav.addObject("errorMessage", "유효하지 않은 페이지입니다.");
        }

        return mav;
    }

    @GetMapping("/fail")
    public String paymentFail(@RequestParam String orderId, @RequestParam String code, @RequestParam String message) {
        // 실패 처리 로직
        service.handleFailure(orderId, code, message);
        return "결제가 실패했습니다. 다시 시도해주세요.";
    }

    // 결제 취소 구현
    // 결제 취소 페이지로 이동할거임
    @PostMapping("/cancel_basicInfo")
    public ResponseEntity<String> cancelBasicInfo(@RequestBody PayCancelDTO payCancelDTO,
                                               @RequestHeader("Authorization") String Headertoken,
                                               HttpSession session){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // 프론트에서 가져온 order_idx를 PayCancelDTO에 담았음
        int order_idx = payCancelDTO.getOrder_idx();
        // paymentkey값 알아와서 저장하기
        String paymentkey = service.getPaymentKey(order_idx);
        log.info("paymentkey:{}",paymentkey);
        payCancelDTO.setPaymentkey(paymentkey);
        log.info("11111111111111111-payCancelDTO:{}",payCancelDTO.toString());
        session.setAttribute("PayCancelDTO", payCancelDTO);
        return ResponseEntity.ok("PayCancelDTO에 기본정보 저장 완료");
    }

    //결제 취소 step 1 view
    @GetMapping("/mypage_cancel1")
    public ModelAndView mypageCancel1(@RequestParam("order_idx") int order_idx, HttpSession session) {
        // 세션에서 PayCancelDTO 가져오기
        PayCancelDTO payCancelDTO = (PayCancelDTO) session.getAttribute("PayCancelDTO");

        // 세션에 PayCancelDTO가 없으면 새로 생성하거나 취소 상품 정보를 초기화
        if (payCancelDTO == null || payCancelDTO.getOrder_idx() != order_idx) {
            payCancelDTO = new PayCancelDTO();
            payCancelDTO.setOrder_idx(order_idx);
            String paymentkey = service.getPaymentKey(order_idx);
            payCancelDTO.setPaymentkey(paymentkey);
            session.setAttribute("PayCancelDTO", payCancelDTO);
        }

        // 취소 상품 정보 초기화
        payCancelDTO.setCancelProducts(new HashMap<>());
        session.setAttribute("PayCancelDTO", payCancelDTO);

        // 화면에 데이터 뿌려주기
        List<OrderListVO> cancelProductList = service.getCancelProduct(order_idx);
        ModelAndView mav = new ModelAndView();
        mav.addObject("cancelProductList", cancelProductList);
        mav.setViewName("mypage/mypage_cancel1");

        return mav;
    }

    //취소 수량 저장
    @PostMapping("/cancel_countInfo")
    public ResponseEntity<String> cancelCountInfo(@RequestBody PayCancelDTO payCancelDTO,
                                                  @RequestHeader("Authorization") String Headertoken,
                                                  HttpSession session){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // 세션에서 기존의 PayCancelDTO 가져오기
        PayCancelDTO sessionPayCancelDTO = (PayCancelDTO) session.getAttribute("PayCancelDTO");

        if (sessionPayCancelDTO == null) {
            log.error("세션에 저장된 PayCancelDTO가 없습니다.");
            return new ResponseEntity<>("세션에 저장된 데이터가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // cancelCount만 업데이트
        sessionPayCancelDTO.setCancelProducts(payCancelDTO.getCancelProducts());
        log.info("cancelProducts: {}", sessionPayCancelDTO.getCancelProducts());

        // 세션에 다시 저장
        session.setAttribute("PayCancelDTO", sessionPayCancelDTO);

        return ResponseEntity.ok("PayCancelDTO에 취소수량 저장 완료");
    }

    //결제 취소 step 2 view
    @GetMapping("/mypage_cancel2")
    public ModelAndView mypageCancel2(HttpSession session) {
        // 세션에서 PayCancelDTO 가져오기
        PayCancelDTO payCancelDTO = (PayCancelDTO) session.getAttribute("PayCancelDTO");

        mav = new ModelAndView();
        // null 체크
        if (payCancelDTO == null) {
            return new ModelAndView("mypage/mypage_order");
        }

        log.info("22222222222-payCancelDTO:{}",payCancelDTO.toString());

        // PayCancelDTO에 저장된 취소할 상품 정보 (pro_idx와 취소 수량)
        Map<Integer, Integer> cancelProducts = payCancelDTO.getCancelProducts(); // pro_idx와 취소 수량
        // 로그로 cancelProducts 내용 출력
        log.info("cancelProducts map: {}", cancelProducts);

        List<OrderListVO> allCancelProducts = service.getCancelProduct(payCancelDTO.getOrder_idx());
        // 로그로 allCancelProducts 내용 출력
        log.info("allCancelProducts: {}", allCancelProducts);

        // 필터링 진행
        List<OrderListVO> filteredCancelProducts = allCancelProducts.stream()
                .filter(product -> {
                    // 필터링 조건 로그로 출력
                    log.info("product.getPro_idx(): {}, cancelProducts.containsKey(product.getPro_idx()): {}",
                            product.getPro_idx(), cancelProducts.containsKey(product.getPro_idx()));
                    return cancelProducts.containsKey(product.getPro_idx());
                })
                .collect(Collectors.toList());

        mav.addObject("cancelProduct", filteredCancelProducts);
        mav.addObject("cancelProducts", cancelProducts); // 여러 상품의 취소 수량 전달
        mav.setViewName("mypage/mypage_cancel2");

        return mav;
    }

    //취소 이유 저장
    @PostMapping("/cancel_reasonInfo")
    public ResponseEntity<String> cancelReasonInfo(@RequestBody PayCancelDTO payCancelDTO,
                                                  @RequestHeader("Authorization") String Headertoken,
                                                  HttpSession session){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // 세션에서 기존의 PayCancelDTO 가져오기
        PayCancelDTO sessionPayCancelDTO = (PayCancelDTO) session.getAttribute("PayCancelDTO");

        if (sessionPayCancelDTO == null) {
            log.error("세션에 저장된 PayCancelDTO가 없습니다.");
            return new ResponseEntity<>("세션에 저장된 데이터가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // getCancelReason 업데이트
        sessionPayCancelDTO.setCancelReason(payCancelDTO.getCancelReason());

        // 세션에 다시 저장
        session.setAttribute("PayCancelDTO", sessionPayCancelDTO);

        return ResponseEntity.ok("PayCancelDTO에 취소수량 저장 완료");
    }

    //결제 취소 step 3 view
    @GetMapping("/mypage_cancel3")
    public ModelAndView mypageCancel3(HttpSession session) {
        // 세션에서 PayCancelDTO 가져오기
        PayCancelDTO payCancelDTO = (PayCancelDTO) session.getAttribute("PayCancelDTO");

        mav = new ModelAndView();
        // null 체크
        if (payCancelDTO == null) {
            return new ModelAndView("mypage/mypage_order");
        }

        log.info("3333333333-payCancelDTO:{}",payCancelDTO.toString());

        Map<Integer, Integer> cancelProducts = payCancelDTO.getCancelProducts();
        List<OrderListVO> allCancelProducts = service.getCancelProduct(payCancelDTO.getOrder_idx());
        // 로그로 allCancelProducts 내용 출력
        log.info("allCancelProducts: {}", allCancelProducts);

        // 1. 필터링 진행: 선택한 취소 상품만 가져오기
        List<OrderListVO> filteredCancelProducts = allCancelProducts.stream()
                .filter(product -> cancelProducts.containsKey(product.getPro_idx()))
                .collect(Collectors.toList());

        // 2. 전체 취소 여부 확인 로직 추가
        List<Integer> allProductIds = allCancelProducts.stream()
                .map(OrderListVO::getPro_idx)
                .collect(Collectors.toList());

        List<Integer> selectedProductIds = new ArrayList<>(cancelProducts.keySet());

        // 모든 상품이 선택되었는지 확인
        boolean allProductsSelected = allProductIds.containsAll(selectedProductIds) && allProductIds.size() == selectedProductIds.size();

        // 모든 상품이 선택되었고, 취소 수량이 상품 수량과 같은지 확인
        boolean isFullCancellation = allProductsSelected && allCancelProducts.stream()
                .allMatch(product -> cancelProducts.get(product.getPro_idx()) != null &&
                        cancelProducts.get(product.getPro_idx()).equals(product.getAmount() - product.getCancelCount()));

        log.info("전체 취소 여부: {}", isFullCancellation);

        // 3. 총 주문 금액 계산 (배송비 제외)
        // 얘는 처음에 주문한 금액이 다 들어가있어야됨
        List<OrderListVO> allOrderProducts = service.getOrderProducts(payCancelDTO.getOrder_idx());
        int totalProductPrice = allOrderProducts.stream()
                .mapToInt(product -> product.getPrice() * product.getAmount()) // 상품 가격 * 상품 수량
                .sum();
        log.info("총 상품 금액 (배송비 제외): {}", totalProductPrice);

        int refundDeliveryFee = 0; // 환불할 배송비

        if (isFullCancellation) {
            // 전체 취소일 때, 15만원 미만이면 배송비 환불
            if (totalProductPrice < 150000) {
                refundDeliveryFee = 2500;
            }
        }

        // 4. 사용자가 사용한 적립금
        int use_point = service.getUsePoint(payCancelDTO.getOrder_idx());

        // 5. 취소 가능 금액, 취소 가능 적립금
        long payment_id = service.getPaymentId(payCancelDTO.getPaymentkey());
        Integer balanceAmount = service.getBalanceAmount(payment_id);
        int amount = service.getAmount(payCancelDTO.getPaymentkey());
        int balanceAmountSet = (balanceAmount == null) ? 0 : balanceAmount;
        balanceAmountSet = amount-balanceAmountSet;
        Integer balancePoint = service.getBalancePoint(payment_id);
        int balancePointSet = (balancePoint == null) ? 0 : balancePoint;

        log.info("balanceAmount : {}",balanceAmountSet);
        log.info("balancePoint : {}",balancePointSet);

        mav.addObject("cancelProduct", filteredCancelProducts);
        mav.addObject("use_point",use_point);
        mav.addObject("cancelProducts", cancelProducts);
        mav.addObject("refundDeliveryFee", refundDeliveryFee);
        mav.addObject("totalProductPrice",totalProductPrice);
        mav.addObject("balanceAmount",balanceAmountSet);
        mav.addObject("balancePoint",balancePointSet);
        mav.setViewName("mypage/mypage_cancel3");

        return mav;
    }

    //결제 취소
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelOk(@RequestBody PayCancelDTO payCancelDTO,
                                                   @RequestHeader("Authorization") String Headertoken,
                                                   HttpSession session){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        // 세션에서 기존의 PayCancelDTO 가져오기
        PayCancelDTO sessionPayCancelDTO = (PayCancelDTO) session.getAttribute("PayCancelDTO");

        if (sessionPayCancelDTO == null) {
            log.error("세션에 저장된 PayCancelDTO가 없습니다.");
            return new ResponseEntity<>("세션에 저장된 데이터가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 취소 금액, 환불적립금 DTO에 저장
        sessionPayCancelDTO.setCancelAmount(payCancelDTO.getCancelAmount());
        sessionPayCancelDTO.setRefundUsePoint(payCancelDTO.getRefundUsePoint());

        log.info("cancelAmount : {}",sessionPayCancelDTO.getCancelAmount());
        log.info("cancelReason : {}", sessionPayCancelDTO.getCancelReason());

        // 결제 취소 service
        ResponseEntity<String> cancelResponse = service.cancelPayment(sessionPayCancelDTO);

        // 결제 취소가 성공했을 때 적립금 반환 처리
        if (cancelResponse.getStatusCode() == HttpStatus.OK) {
            int refundUsePoint = sessionPayCancelDTO.getRefundUsePoint();

            if (refundUsePoint > 0) {
                // 적립금 반환 로직 (유저의 적립금을 업데이트)
                memberService.pointUpdate(useridx, 4, refundUsePoint);
                log.info("사용자 ID {}의 적립금 {}원이 반환되었습니다.", useridx, refundUsePoint);
            }
            // 성공 응답 반환
            return ResponseEntity.ok("결제 취소가 성공적으로 처리되었습니다.");
        }else {
            // 실패한 경우, 클라이언트에 실패 메시지 전달
            log.error("결제 취소 중 오류 발생: {}", cancelResponse.getBody());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 취소 중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }
    }

    // 주문상태 구매확정으로 변경
    @PostMapping("/orderConfirmOk")
    public ResponseEntity<String> orderConfirm(@RequestBody Map<String, Object> requestData, @RequestHeader("Authorization") String Headertoken) {
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        // useridx 가져오기
        Map<String, Object> responseBody = tokenResponse.getBody();
        Integer useridx = (Integer) responseBody.get("useridx");

        int order_idx = Integer.parseInt(requestData.get("order_idx").toString());
        int pro_idx = Integer.parseInt(requestData.get("pro_idx").toString());

        try {
            // 구매 확정 처리
            int rewardPoints = service.confirmOrder(order_idx,pro_idx);
            // 구매확정 적립금 적립
            memberService.pointUpdate(useridx, 1, rewardPoints);
            log.info("사용자 ID {}의 적립금 {}원이 적립 되었습니다.", useridx, rewardPoints);

            return ResponseEntity.ok("구매 확정 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("구매 확정 중 에러 발생: " + e.getMessage());
        }
    }

    // 환불 모달
    @PostMapping("/refundModal")
    public ResponseEntity<Map<String, Object>> refundModal(@RequestParam("idx") int idx) {
        //환불 상품 정보 idx, order_idx, count(환불가능갯수)
        RefundDTO refundModal = service.getrefundModalInfo(idx);
        Map<String, Object> response = new HashMap<>();
        response.put("refundModal", refundModal);
        return ResponseEntity.ok(response);
    }

    //환불
    @PostMapping("/refund")
    public ResponseEntity<String> refundOk(@RequestBody RefundDTO refundInfo,
                                           @RequestHeader("Authorization") String Headertoken){
        // JWT 토큰 검증 및 useridx 추출
        ResponseEntity<Map<String, Object>> tokenResponse = extractUserIdFromToken(Headertoken);
        if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(tokenResponse.getHeaders(), tokenResponse.getStatusCode());
        }

        //paymentkey 값 가져오기
        String paymentkey = service.getPaymentKey(refundInfo.getOrder_idx());
        refundInfo.setPaymentkey(paymentkey);
        // 환불 service
        ResponseEntity<String> cancelResponse = service.refundPayment(refundInfo);

        // 환불이 성공했을 때 적립금 반환 처리
        if (cancelResponse.getStatusCode() == HttpStatus.OK) {
            int refundUsePoint = refundInfo.getRefundPoint();
            int refund_useridx = service.getRefundUserIdx(refundInfo.getOrder_idx());

            if (refundUsePoint > 0) {
                // 적립금 반환 로직 (유저의 적립금을 업데이트)
                memberService.pointUpdate(refund_useridx, 5, refundUsePoint);
                log.info("사용자 ID {}의 적립금 {}원이 반환되었습니다.",refund_useridx , refundUsePoint);
            }
            // 성공 응답 반환
            return ResponseEntity.ok("환불이 성공적으로 처리되었습니다.");
        }else {
            // 실패한 경우, 클라이언트에 실패 메시지 전달
            log.error("환불 중 오류 발생: {}", cancelResponse.getBody());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("환불 중 오류가 발생했습니다.");
        }
    }

}
