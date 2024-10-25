package com.ict.finalproject.DAO;

import com.ict.finalproject.DTO.CancelResDTO;
import com.ict.finalproject.DTO.PaymentReqDTO;
import com.ict.finalproject.vo.OrderListVO;
import com.ict.finalproject.vo.OrderVO;
import com.ict.finalproject.vo.PaymentVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface OrderDAO {
    void insertOrder(OrderVO order);
    void insertOrderList(OrderListVO orderList);

    OrderVO selectOrderById(int order_idx);
    List<OrderListVO> selectOrderListByOrderIdx(int order_idx);

    void insertPaymentRequest(PaymentReqDTO paymentRequest);
    void updateOrderRequest(PaymentReqDTO paymentRequest);

    // orderId로 결제 정보 조회
    PaymentVO getPaymentByOrderId(String orderId);

    // 결제 성공 업데이트
    void updatePaymentSuccess(PaymentVO payment);
    void orderPayState(int order_idx);
    void orderListState(int order_idx);
    List<OrderListVO> getOrderListByOrderIdx(int order_idx);
    void decreaseProductStock(int order_proIdx, int order_amount);

    // 성공시 success페이지에 뿌려줄 데이터들
    OrderVO orderSuccessData(int order_idx);
    List<OrderListVO> orderListSuccessData(int order_idx);
    PaymentVO paymentSuccessData(int order_idx);
    int getUseridx(int order_idx);

    // 결제 실패
    void updatePaymentFailure(PaymentVO payment);

    // 결제 취소
    // paymentkey값 가져오기
    String getPaymentKey(int order_idx);
    // step 1 데이터 뿌려주기
    List<OrderListVO> getCancelProduct(int order_idx);
    // 사용한 적립금 가져오기
    int getUsePoint(int order_idx);
    // 결제 취소
    void PaymentCancelSuccess(String paymentKey);
    long getPaymentId(String paymentKey);
    void savePaymentCancels(Map<String, Object> paramMap);
    void updateOrderStateCancel(int order_idx);
    void updateOrderListState(int pro_idx, int order_idx, int orderState, int cancelCount);
    void increaseProductStock(int pro_idx, int cancelCount);
    List<OrderListVO> getOrderProducts(int order_idx);
    Integer getBalanceAmount(long payment_id);
    Integer getBalancePoint(long payment_id);
    int getAmount(String paymentkey);
    //구매확정
    void updateOrderStateConfirm(int order_idx,int pro_idx);
    OrderListVO getOrderProductPriceAmount(int order_idx,int pro_idx);

}
