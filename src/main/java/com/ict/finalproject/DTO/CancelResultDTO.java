package com.ict.finalproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelResultDTO {
    //결제 취소시 사용될 데이터
    private String cancelResult;//취소 처리상태
    private int balanceAmount;//남은 취소 가능 금액
    private int balancePoint;//남은 적립금 반환 금액
}
