package com.ict.finalproject.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ict.finalproject.DAO.OrderDAO;
import com.ict.finalproject.DTO.*;
import com.ict.finalproject.vo.OrderListVO;
import com.ict.finalproject.vo.OrderVO;
import com.ict.finalproject.vo.PaymentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService{
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    OrderDAO dao;

    @Override
    public int createOrder(int useridx, int total_price) {
        OrderVO order = new OrderVO(useridx, total_price);
        dao.insertOrder(order);
        return order.getIdx();  // 자동 증가된 order_idx 반환
    }

    @Override
    public void addProductToOrderList(int order_idx, int pro_idx, int amount) {
        OrderListVO orderList = new OrderListVO(order_idx, pro_idx,amount);
        dao.insertOrderList(orderList);
    }

    @Override
    public OrderVO getOrderById(int order_idx) {
        return dao.selectOrderById(order_idx);
    }

    @Override
    public List<OrderListVO> getOrderListByOrderIdx(int order_idx) {
        return dao.selectOrderListByOrderIdx(order_idx);
    }

    @Override
    public void savePaymentRequest(PaymentReqDTO paymentRequest) {
        // 결제 요청 정보를 DB에 저장
        dao.insertPaymentRequest(paymentRequest);
    }

    @Override
    public void saveOrderRequest(PaymentReqDTO paymentRequest) {
        dao.updateOrderRequest(paymentRequest);
    }

    @Value("${toss.secretKey}")  // application.properties의 secretKey 값
    private String secretKey;

    @Override
    public int approvePayment(PaymentApprovalDTO approvalDTO) {
        // 1. DB에서 저장된 결제 요청 정보 조회 (orderId로 조회)
        PaymentVO payment = dao.getPaymentByOrderId(approvalDTO.getOrderId());
        System.out.println("************* payment : "+payment);

        if (payment == null) {
            throw new RuntimeException("해당 주문 ID의 결제 정보가 존재하지 않습니다.");
        }

        // 2. DB에 저장된 금액과 Toss에서 받은 금액이 일치하는지 확인
        if (payment.getAmount() != approvalDTO.getAmount()) {
            throw new RuntimeException("결제 금액이 일치하지 않습니다.");
        }

        // 3. Toss Payments에 결제 승인 요청
        // 변수선언
        String paymentKey = approvalDTO.getPaymentKey();
        String orderId = approvalDTO.getOrderId();
        int amount = approvalDTO.getAmount();

        // Toss Payments API로 승인 요청
        String apiUrl = "https://api.tosspayments.com/v1/payments/confirm";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // secretKey + ":" 을 인코딩
        String auth = secretKey + ":";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);

        // 승인 요청 데이터
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("paymentKey", paymentKey);
        requestData.put("orderId",orderId);
        requestData.put("amount", amount);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PaymentResDTO> response = restTemplate.postForEntity(apiUrl, requestEntity, PaymentResDTO.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // Toss API에서 받은 결제 정보
            PaymentResDTO responseData = response.getBody();
            log.info("***************************response.getBody() : {}",response.getBody());
            log.info("%%%%%%%%%%%%%%%%%%%%%%%%responseData : {}",responseData);
            payment.setPaytype(responseData.getMethod());
            payment.setOrdername(responseData.getOrderName());
            payment.setPaymentKey(approvalDTO.getPaymentKey());
            // 4. 결제 승인 성공 처리
            dao.updatePaymentSuccess(payment);

            // 승인과 동시에 T_order, T_orderList State 변경 + 재고관리 + 장바구니에 있으면 delState
            // 1. T_order의 payState
            dao.orderPayState(payment.getOrder_idx());
            // 2. T_orderList의 orderState
            dao.orderListState(payment.getOrder_idx());
            // 3. 주문한 상품 갯수만큼 stock 빼기
            List<OrderListVO> orderProducts = dao.getOrderListByOrderIdx(payment.getOrder_idx());
            for (OrderListVO orderProduct : orderProducts) {
                int order_proIdx = orderProduct.getPro_idx();
                int order_amount = orderProduct.getAmount();

                // 상품의 재고를 차감하는 메서드 호출
                dao.decreaseProductStock(order_proIdx, order_amount);
            }
            //장바구니에서 결제한 경우 장바구니에서 삭제 -> 아직
        } else {
            throw new RuntimeException("결제 승인 실패: " + response.getBody());
        }

        return payment.getOrder_idx();
    }

    @Override
    public OrderVO orderSuccessData(int order_idx) {
        return dao.orderSuccessData(order_idx);
    }

    @Override
    public List<OrderListVO> orderListSuccessData(int order_idx) {
        return dao.orderListSuccessData(order_idx);
    }

    @Override
    public PaymentVO paymentSuccessData(int order_idx) {
        return dao.paymentSuccessData(order_idx);
    }

    @Override
    public int getUseridx(int order_idx) {
        return dao.getUseridx(order_idx);
    }

    @Override
    public void handleFailure(String orderId, String errorCode, String errorMessage) {
        PaymentVO payment = dao.getPaymentByOrderId(orderId);
        payment.setFailReason(errorMessage);  // 실패 이유 저장
        payment.setSuccessYN(0);  // 실패로 처리
        dao.updatePaymentFailure(payment);
    }

    @Override
    public String getPaymentKey(int order_idx) {
        return dao.getPaymentKey(order_idx);
    }

    @Override
    public List<OrderListVO> getCancelProduct(int order_idx) {
        return dao.getCancelProduct(order_idx);
    }

    @Override
    public int getUsePoint(int order_idx) {
        return dao.getUsePoint(order_idx);
    }

    @Override
    public ResponseEntity<String> cancelPayment(PayCancelDTO sessionPayCancelDTO) {
        try {
            // 1.  Toss Payments에 결제 취소 요청
            // 변수선언
            String paymentKey = sessionPayCancelDTO.getPaymentkey();
            String cancelReason = sessionPayCancelDTO.getCancelReason();
            int cancelAmount = sessionPayCancelDTO.getCancelAmount();

            log.info("cancelReason : {}", sessionPayCancelDTO.getCancelReason());
            log.info("cancelAmount : {}", sessionPayCancelDTO.getCancelAmount());

            // Toss Payments API로 승인 요청
            String apiUrl = "https://api.tosspayments.com/v1/payments/"+paymentKey+"/cancel";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));

            // secretKey + ":" 을 인코딩
            String auth = secretKey + ":";
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            headers.set("Authorization", "Basic " + encodedAuth);

            // 승인 요청 데이터
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("cancelAmount",cancelAmount);
            requestData.put("cancelReason", cancelReason);
            log.info("Toss API 요청 데이터: {}", requestData);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);
            RestTemplate restTemplate = new RestTemplate();
            // Toss Payments API로 승인 요청
            ResponseEntity<CancelResDTO> response = restTemplate.postForEntity(apiUrl, requestEntity, CancelResDTO.class);

            log.info("Toss API 응답: {}", response.getBody());


            if (response.getStatusCode() == HttpStatus.OK) {
                // Toss API에서 받은 결제 정보
                CancelResDTO responseData = response.getBody();
                Map<String, Object> paramMap = new HashMap<>();
                // 최신 취소 내역 (리스트의 마지막 요소)
                CancelResDTO.CancelInfo latestCancel = responseData.getCancels().get(responseData.getCancels().size() - 1);

                paramMap.put("cancelInfo", latestCancel);
                // 결제 취소 성공 정보 저장(T_payment)
                dao.PaymentCancelSuccess(responseData.getPaymentKey());
                // T_payment_cancel에 취소 정보 저장
                //1. payment_id 알아내기
                long payment_id = dao.getPaymentId(responseData.getPaymentKey());
                // 2. 결제정보 저장
                paramMap.put("payment_id", payment_id);
                // 반환되는 적립금
                paramMap.put("refundUsePoint",sessionPayCancelDTO.getRefundUsePoint());
                dao.savePaymentCancels(paramMap);

                // 승인과 동시에 T_order, T_orderList State 변경 + 재고관리
                int order_idx = sessionPayCancelDTO.getOrder_idx();
                Map<Integer, Integer> cancelProducts = sessionPayCancelDTO.getCancelProducts();
                // 1. T_order의 cancelDT, state
                dao.updateOrderStateCancel(order_idx);
                // 2. T_orderList의 orderState
                List<OrderListVO> orderProducts = dao.getOrderListByOrderIdx(order_idx);
                for (OrderListVO orderProduct : orderProducts) {
                    int pro_idx = orderProduct.getPro_idx();
                    int amount = orderProduct.getAmount();
                    int cancelCount = cancelProducts.getOrDefault(pro_idx, 0);

                    // DB에서 이미 취소된 수량 가져오기
                    int currentCancelCount = orderProduct.getCancelCount();
                    int totalCanceledCount = currentCancelCount + cancelCount; // 새롭게 취소된 수량과 기존 취소 수량 합산


                    // cancelCount -> amount = cancelCount면 전체 취소라서 orderState=7로 업데이트
                    if (totalCanceledCount == amount) {
                        log.info("111111111111111111cancelCount : {}",cancelCount);
                        dao.updateOrderListState(pro_idx, order_idx, 7, cancelCount); // 전체 취소 상태
                    } else if (totalCanceledCount > 0) {
                        log.info("2222222222222222222cancelCount : {}",cancelCount);
                        dao.updateOrderListState(pro_idx, order_idx, 8, cancelCount); // 부분 취소 상태
                    }
                    // 취소된 상품만큼 재고(stock) 더하기
                    if (cancelCount > 0) {
                        dao.increaseProductStock(pro_idx, cancelCount);
                    }
                }
                // 성공적으로 결제 취소 완료, 메시지와 상태 코드 반환
                return ResponseEntity.ok("결제 취소가 성공적으로 처리되었습니다.");
            } else {
                // Toss API에서 결제 취소 실패 응답이 왔을 때
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("결제 취소 실패: " + response.getBody());
            }
        }catch (Exception e) {
            // 에러가 발생한 경우
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("결제 취소 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Override
    public List<OrderListVO> getOrderProducts(int order_idx) {
        return dao.getOrderProducts(order_idx);
    }

    @Override
    public long getPaymentId(String paymentkey) {
        return dao.getPaymentId(paymentkey);
    }

    @Override
    public Integer getBalanceAmount(long payment_id) {
        return dao.getBalanceAmount(payment_id);
    }

    @Override
    public Integer getBalancePoint(long payment_id) {
        return dao.getBalancePoint(payment_id);
    }

    @Override
    public int getAmount(String paymentkey) {
        return dao.getAmount(paymentkey);
    }

    @Override
    public int confirmOrder(int order_idx, int pro_idx) {
        // orderState=6(구매확정)으로 변경
        dao.updateOrderStateConfirm(order_idx,pro_idx);

        // 주문한 상품 가격과 갯수 출력(p.price, ol.amount)
        OrderListVO orderProductInfo = dao.getOrderProductPriceAmount(order_idx,pro_idx);

        int rewardPoints = (orderProductInfo.getPrice()*(orderProductInfo.getAmount()-orderProductInfo.getCancelCount()))/100;
        log.info("예상 적립금 : {}",rewardPoints);

        return rewardPoints;
    }

    //환불
//    @Override
//    public String refundPayment(int cancelAmount, String paymentkey) {
//        try {
//            // 1.  Toss Payments에 결제 취소 요청
//            // 변수선언
//            String cancelReason = "상품 환불";
//
//            // Toss Payments API로 승인 요청
//            String apiUrl = "https://api.tosspayments.com/v1/payments/"+paymentkey+"/cancel";
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
//
//            // secretKey + ":" 을 인코딩
//            String auth = secretKey + ":";
//            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
//            headers.set("Authorization", "Basic " + encodedAuth);
//
//            // 승인 요청 데이터
//            Map<String, Object> requestData = new HashMap<>();
//            requestData.put("cancelAmount",cancelAmount);
//            requestData.put("cancelReason", cancelReason);
//            log.info("Toss API 요청 데이터: {}", requestData);
//
//            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestData, headers);
//            RestTemplate restTemplate = new RestTemplate();
//            // Toss Payments API로 승인 요청
//            ResponseEntity<CancelResDTO> response = restTemplate.postForEntity(apiUrl, requestEntity, CancelResDTO.class);
//
//            log.info("Toss API 응답: {}", response.getBody());
//
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                // Toss API에서 받은 결제 정보
////                CancelResDTO responseData = response.getBody();
////                Map<String, Object> paramMap = new HashMap<>();
////                // 환불내역(최신 취소 내역)
////                CancelResDTO.CancelInfo latestCancel = responseData.getCancels().get(responseData.getCancels().size() - 1);
////
////                paramMap.put("cancelInfo", latestCancel);
////                // 결제 환불 성공 정보 저장(T_payment) -> cancelDT, cancelState
////                dao.PaymentCancelSuccess(responseData.getPaymentKey());
////                // T_payment_cancel에 환불 정보 저장
////                //1. payment_id 알아내기
////                long payment_id = dao.getPaymentId(responseData.getPaymentKey());
////                // 2. 결제정보 저장
////                paramMap.put("payment_id", payment_id);
////                // 반환되는 적립금
////                paramMap.put("refundUsePoint",sessionPayCancelDTO.getRefundUsePoint());
////                dao.savePaymentCancels(paramMap);
////
////                // 승인과 동시에 T_order, T_orderList State 변경 + 재고관리
////                int order_idx = sessionPayCancelDTO.getOrder_idx();
////                Map<Integer, Integer> cancelProducts = sessionPayCancelDTO.getCancelProducts();
////                // 1. T_order의 cancelDT, state
////                dao.updateOrderStateCancel(order_idx);
//                // 2. T_orderList의 orderState
////                List<OrderListVO> orderProducts = dao.getOrderListByOrderIdx(order_idx);
////                for (OrderListVO orderProduct : orderProducts) {
////                    int pro_idx = orderProduct.getPro_idx();
////                    int amount = orderProduct.getAmount();
////                    int cancelCount = cancelProducts.getOrDefault(pro_idx, 0);
////
////                    // DB에서 이미 취소된 수량 가져오기
////                    int currentCancelCount = orderProduct.getCancelCount();
////                    int totalCanceledCount = currentCancelCount + cancelCount; // 새롭게 취소된 수량과 기존 취소 수량 합산
////
////
////                    // cancelCount -> amount = cancelCount면 전체 취소라서 orderState=7로 업데이트
////                    if (totalCanceledCount == amount) {
////                        dao.updateOrderListState(pro_idx, order_idx, 7, cancelCount); // 전체 취소 상태
////                    } else if (totalCanceledCount > 0) {
////                        dao.updateOrderListState(pro_idx, order_idx, 8, cancelCount); // 부분 취소 상태
////                    }
////                    // 취소된 상품만큼 재고(stock) 더하기
////                    if (cancelCount > 0) {
////                        dao.increaseProductStock(pro_idx, cancelCount);
////                    }
////                }
//                // 성공적으로 결제 취소 완료, 메시지와 상태 코드 반환
//                return ResponseEntity.ok("결제 취소가 성공적으로 처리되었습니다.");
//            } else {
//                // Toss API에서 결제 취소 실패 응답이 왔을 때
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body("결제 취소 실패: " + response.getBody());
//            }
//        }catch (Exception e) {
//            // 에러가 발생한 경우
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("결제 취소 중 오류가 발생했습니다: " + e.getMessage());
//        }
//    }

}
