package com.ict.finalproject.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor //매게변수 없는 생성자
@AllArgsConstructor //매게변수 전체 다 있는거
public class AniListVO {

    private String post_img;   // 이미지 URL
    private String title;     // 제목
    private String anitype;   // 장르
    private String director;   // 감독
    private String agetype;    // 나이 관람
    private int idx;
    private String regDT; // 날짜

    private int anilike; // 좋아요 수
    /*@@@@@@@@@@@@별점@@@@@@@@@@@@@@@@*/
    private Integer ani_idx;  // 애니메이션 인덱스
    private Double grade;     // 별점
    private Integer useridx;   // 사용자 ID
    /*@@@@@@@@@줄거라@@@@@@@@@@*/
    private String outline;
    private String token;
    private String userid;

    /*@@@@@이벤트@@@@@@*/
    private String thumfile;
    private String content;
    private String event_date;
    private int adminidx;
    private String fullImageUrl;

}
