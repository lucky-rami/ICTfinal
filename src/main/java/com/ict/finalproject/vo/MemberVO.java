package com.ict.finalproject.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {
    private int idx;
    private String userid;
    private String userpwd;
    private String username;
    private String tel;
    private String email;
    private String zipcode;
    private String addr;
    private String addrdetail;
    private String birth;
    private int point;
    private LocalDateTime regDT;
    private int adult_state;
    private int login_type;

    private int page;      // 현재 페이지 번호
    private int pageSize;

    private String join_status;

    private boolean isTodayUser;
    private boolean isRecentUser;
}
