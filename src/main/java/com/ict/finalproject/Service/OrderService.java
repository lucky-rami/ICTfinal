package com.ict.finalproject.Service;

import com.ict.finalproject.DTO.CancelResultDTO;
import com.ict.finalproject.DTO.PayCancelDTO;
import com.ict.finalproject.DTO.PaymentApprovalDTO;
import com.ict.finalproject.DTO.PaymentReqDTO;
import com.ict.finalproject.vo.OrderListVO;
import com.ict.finalproject.vo.OrderVO;
import com.ict.finalproject.vo.PaymentVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    // 주문정보 저장 service
    int createOrder(int useridx, int total_price);
    void addProductToOrderList(int order_idx, int pro_idx, int amount);

    // 주문정보 뿌려주는 service -> 주문서에서 사용
    OrderVO getOrderById(int order_idx);
    List<OrderListVO> getOrderListByOrderIdx(int order_idx);

    // 결제 요청정보 DB에 저장 service
    void savePaymentRequest(PaymentReqDTO paymentRequest);
    void saveOrderRequest(PaymentReqDTO paymentRequest);

    // 결제 승인요청
    int approvePayment(PaymentApprovalDTO approvalDTO);

    // 성공시 success페이지에 뿌려줄 데이터들
    OrderVO orderSuccessData(int order_idx);
    List<OrderListVO> orderListSuccessData(int order_idx);
    PaymentVO paymentSuccessData(int order_idx);
    int getUseridx(int order_idx);
    
    // 결제 실패
    void handleFailure(String orderId, String code, String message);

    // 결제 취소
    // paymentkey값 가져오기
    String getPaymentKey(int order_idx);
    // step 1 데이터 뿌려주기
    List<OrderListVO> getCancelProduct(int order_idx);
    // 사용한 적립금 가져오기
    int getUsePoint(int order_idx);
    // 결제 취소
    ResponseEntity<String> cancelPayment(PayCancelDTO sessionPayCancelDTO);
    List<OrderListVO> getOrderProducts(int order_idx);
    long getPaymentId(String paymentkey);
    Integer getBalanceAmount(long payment_id);
    Integer getBalancePoint(long payment_id);
    int getAmount(String paymentkey);

    // 구매확정
    int confirmOrder(int order_idx,int pro_idx);

    //환불
//    String refundPayment(int cancelAmount,String paymentkey);

}
