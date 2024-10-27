package com.ict.finalproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundDTO {
    private int order_idx;
    private int orderList_idx;
    private int refundCount;
    private int refundAmount;
    private int refundPoint;
    private int pro_price;
    private String paymentkey;
    private int refund_useridx;
}
