package com.ict.finalproject.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreVO {
    private int idx;
    private String title;
    private int price;
    private String thumImg;
    private String ani_title;
    private String relDT;
    private String brand;
    private int fee;
    private String regDT;
    private int stock;
    private String pro_detail;
    private int category;
    private int second_category;
    private int liked;
    private int likeCount;
    private String detailImg;
    //상세페이지 숨겨진 이미지ㅣ 불러오기
    private List<String> detailImages;
    private String category_name;// 여러 이미지 저장을 위한 리스트
}