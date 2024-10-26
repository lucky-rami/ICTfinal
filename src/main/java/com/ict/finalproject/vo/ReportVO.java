package com.ict.finalproject.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportVO {
    private int idx;
    private int comment_idx;
    private int review_idx;
    private int community_idx;
    private int report_type;
    private String reason;
    private int useridx;
}
