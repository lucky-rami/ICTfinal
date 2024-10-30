package com.ict.finalproject.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MasterVO {

    // 공통으로 들어갈는 것들
    private Integer idx;
    private Integer adminidx;
    private String adminid;
    private String title;
    private String regDT;
    private String content;
    private String useridx;
    private String author;
    private String userid;
    private String token;
    private String modifyDT;
    private String delDT;
    private String inDate;
    private String delReasonDetail;
    private String adminpwd;

    // 상품 테이블에서 사용 하는 것들
    private int price;
    private String thumImg;
    private String ani_title;
    private String relDT;
    private String brand;
    private String pro_detail;
    private int stock;
    private int fee;
    private String category;
    private String type;
    private int code;
    private int second_category;
    private int pro_idx;

    // 커뮤니티 테이블에서 사용 하는 것들
    private String commtype;
    private int hit;

    // 관리자 페이지에서 애니 테이블 관리
    private MultipartFile post_img;
    private String anitype;
    private String director;
    private String agetype;
    private String outline;
    private String anitype_type;
    private String age_type;
    private String post_img_filename;
    private int modifyState;

    private String detailImg;

    // 관리자페이지에서 공지사항 사용하는것들
    private String admin_name;

    // 관리자페이지에서 자주묻는 질문 사용하는 것들
    private String question;
    private String answer;
    private String faqtype;

    // 관리자페이지에서 신고 기능 사용하는 것들
    private String reason;
    private String stopDT;
    private String endDT;

    // 관리자 페이지에서 모든 리뷰
    private int comm_idx;
    private int parentidx;

    // 관리자페이지에서 사용하는 신고 기능 사용하는것들
    private int comment_idx;
    private int review_idx;
    private int report_type;
    private String reportDT;
    private String handleState;
    private String handleDT;
    private int comunity_idx;
    private int totalUserReport;
    private String reportedUser; // 신고된 유저 ID (문자열)
    private Integer commentIdx;
    private int report_count;
    private Integer reviewIdx; // 게시글 ID
    private Integer communityIdx; // 커뮤니티 ID
    private String commentAuthor;
    private String reviewAuthor;
    private String communityAuthor;




    // 관리자페이지에서 사용하는 리뷰 기능 사용하는 것들
    private int grade;
    private int orderList_idx;
    private String imgfile1;
    private String imgfile2;
    
    // 관리자 페이지에서 문의 기능 사용하는 것들
    private String qnatype;
    private int qna_idx;
    private String reply;
    private String attachmentUrl;

    // 페이징 네이션에 사용하는 기능들
    private int currentPage;     // 현재 페이지 번호
    private int pageSize;        // 한 페이지당 항목 수
    private int offset;          // 쿼리에 사용할 오프셋 값
    private int totalRecords;    // 전체 항목 수
    private int totalPages;      // 전체 페이지 수
    private int startPage;       // 페이지 네비게이션 시작 페이지
    private int endPage;         // 페이지 네비게이션 끝 페이지
    private int pageGroupSize;
    private int page;

    // 관리자 페이지 이벤트 페이지 사용하는 기능들
    private String event_date;
    private String thumfile;
    private String thumfileName;

    // 관리자에서 회원목록 기능 사용하는것

    private String userpwd;
    private String username;
    private String tel;
    private String email;
    private String zipcode;
    private String addr;
    private String addrdetail;
    private String birth;
    private int point;
    private int adult_state;
    private int login_type;

    // 관리자페이지 주문관리 기능 사용하는것들
    private int salesAmount; // 매출금액
    private int orderCount; // 주문 건수
    private int deliveryFee; // 배송비
    private int commission; // 수수료
    private int cancelCount; // 취소 건수
    private int cancelAmount; // 취소 금액
    private String date;
    private int paymentAmount;
    private String orderId;
    private String productName;
    private String orderDate;
    private String customerName;
    private String orderState;


    private int totalSales;
    private int totalPayment;
    private String aniTitles;

}
