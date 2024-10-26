package com.ict.finalproject.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewVO {
    private int idx;
    private int useridx;
    private int orderList_idx;
    private String content;
    private int grade;
    private String imgfile1;
    private String imgfile2;
    private String regDT;
    private int modifyState;
    private String modifyDT;
    private int delState;
    private String delDT;
    private String token;
    private String userid;
}
