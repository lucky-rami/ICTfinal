package com.ict.finalproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelResDTO {
    private String mId;
    private String lastTransactionKey;
    private String paymentKey;
    private String orderId;
    private String orderName;
    private String status;
    private String requestedAt;
    private String approvedAt;
    private boolean useEscrow;
    private boolean cultureExpense;
    private int balanceAmount;//취소 남은 금액

    private CardInfo card;
    private List<CancelInfo> cancels;

    // 내부 클래스로 유지
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelInfo {
        private String transactionKey;
        private String cancelReason;
        private int cancelAmount;
        private String canceledAt;
        private String cancelStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardInfo {
        private String issuerCode;
        private String acquirerCode;
        private String number;
        private int amount;
        private boolean useCardPoint;
    }
}
