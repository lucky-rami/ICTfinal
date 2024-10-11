package com.ict.finalproject.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListVO {
    private int idx;
    private int order_idx;
    private int pro_idx;
    private int amount;
    private int orderState;

    // 매개변수가 있는 생성자
    public OrderListVO(int order_idx, int pro_idx) {
        this.order_idx = order_idx;
        this.pro_idx= pro_idx;
    }
}
