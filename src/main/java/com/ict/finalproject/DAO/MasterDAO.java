package com.ict.finalproject.DAO;

import com.ict.finalproject.DTO.*;
import com.ict.finalproject.vo.MasterVO;
import com.ict.finalproject.vo.OrderListVO;
import com.ict.finalproject.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface MasterDAO {
    // 공지사항 추가 DAO
    int  insertNotice(MasterVO notice);
    // 자주묻는 질문 추가
    int insertFAQ(MasterVO faq);
    int createStore(MasterVO storeAdd);
    // 자주 묻는 질문 리스트 전체 출력
    public List<MasterVO> getFAQList();
    // 굿즈 상품테이블 리스트 전체 출력
    public List<MasterVO> getStoreList();
    // 굿즈 상품테이블 총 상품수 구하기
    int getTotalStore();

    // 굿즈 상품테이블 카테고리별 카운트 구하기
    Map<String, Object> getCategoryCountByCode(@Param("categoryCode") int categoryCode);

    // 커뮤니티 작성된 모든 게시글 불러오기
    List<MasterVO> getBoardList();

    // adminid를 통해 adminidx 가져오기
    Integer getAdminIdxByAdminid(String adminid);

    // 애니 리스트 불러오기
    public List<MasterVO> getAniAllList();

    // 공지사항 삽입
    void addNotice(MasterVO notice);

    // 공지사항 글 불러오기
    List<MasterVO> getNoticeList();

    List<MasterVO> getReviewList();

    // 신고 추가
    void insertReport(Integer useridx, String reason, LocalDateTime stopDT, LocalDateTime endDT, Integer comment_idx, Integer review_idx, Integer comunity_idx, int report_type);
    // 유저가 정지 상태인지 확인
    boolean isUserBanned(@Param("userid") String userid);

    // 유저가 탈퇴 상태인지 확인
    boolean isUserDeleted(@Param("idx") int idx);
    Integer findUserIdxByUserid(String userid);

    // 탈퇴 회원 조회 하기
    List<MasterVO> getMemberDelList(int currentPage, int pageSize);
    // 회원이 신고 한 유저 목록 구하기
    List<MasterVO> getReportinguserList(MasterVO vo);
    void deleteFaq(int idx);
    int getTotalUserDelCount();
    void deleteEvent(int idx); // 이벤트 삭제 메서드
    public int getTodayReplyCount();
    public int getWeekReplyCount();
    int getTodayReviewCount(); // 오늘 작성된 리뷰 수 조회
    int getWeekReviewCount(); // 일주일간 작성된 리뷰 수 조회
    void deleteReview(int idx); // 리뷰 삭제 메서드
    int getTodayBoardCount();  // 오늘 작성된 게시글 수
    int getLastWeekBoardCount();
    void deleteBoard(int idx);
    // 신고 내역을 t_report 테이블에 삽입
    void updateReport(@Param("idx") int idx, @Param("handleState") int handleState, @Param("handleDT") LocalDateTime handleDT);

    // 모든 리뷰 불러오기
    List<MasterVO> getReplyList(MasterVO vo);

    // idx에 대한 리뷰 디테일 불러오기
    MasterVO getReviewDetail(int idx);

    // 애니 정보 선택
    MasterVO aniSelect(int idx);

    // 애니 idx의 기존 이미지 값 불러오기
    String getCurrentImgFile(int idx);

    // 애니 정보 수정
    int updateAnimation(MasterVO vo);

    // 문의 내용 목록 불러오기
    List<MasterVO> getQNAList();

    // 문의 들어온 내용 중 답변 처리 안된 갯수 파악하기
    int getUnansweredQnaCount();

    // t_ban에 들어와 있는 데이터 목록 불러오기
    List<MasterVO>getReportingUser();

    // 전체 신고 누적 횟수 카운트
    int getTotalReportCount();

    // 특정 아이디의 신고 횟수 카운트
    int getTotalUserReport(String userid);

    void insertReply(@Param("idx") int idx, @Param("reply") String reply, @Param("adminIdx") int adminIdx, @Param("regDT") LocalDateTime regDT);

    void updateHandleState(int idx);
    Integer findAdminIdxByUserid(String adminId);

    // 애니 데이터 베이스 추가
    void insertAnimation(MasterVO aniVO);

    // 애니 삭제 하기
    void deletePostByIdx(int idx);

    // 해당 굿즈의 idx 구하기
    MasterVO getStoreByIdx(int idx);

    // 굿즈 상품 수정
    int updateStore(MasterVO store);

    List<MasterVO> findSubCategoriesByCategory(@Param("category") int category);
    void insertProductImg(MasterVO masterVO);

    // 공지사항 수정
    MasterVO getNoticeById(@Param("idx") int idx);
    int updateNotice(MasterVO noticeEdit);

    MasterVO noticeSelect (int idx);

    // QnA와 답변 데이터를 가져오는 메서드
    MasterVO getQnaReplyById(int idx);
    MasterVO getAdminByAdminId(@Param("adminid") String adminid);


    int doesProductExist(int pro_idx);
    MasterVO getProductImgByIdx(int idx);
    int updateProductImg(MasterVO productImg);

    // 애니 페이징네이션
    int getTotalAnimeCount(); // 총 애니메이션 수를 가져옴
    List<MasterVO> getAniListWithPaging(Map<String, Object> params);// 페이징된 애니 리스트 가져오기

    // 굿즈 페이징 네이션
    List<MasterVO> getStoreListWithPaging(Map<String, Object> params);

    // Board All 페이징
    List<MasterVO> getBoardListWithPaging(Map<String, Object> params);
    int getTotalBoardCount();

    // Board ReviewAll
    List<MasterVO> getReviewListWithPaging(Map<String, Object> params);
    int getTotalReviewCount();

    // 이벤트 페이지
    int insertEvent(MasterVO event);
    List<MasterVO> getEventList();
    public MasterVO getEventDetail(int idx);

    int updateEvent(MasterVO event);
    MasterVO getEventByIdx(int idx);

    // 회원가입 통계 차트
    List<Map<String, Object>> getUserRegistrationStats();
    // 일별 차트 보여주기
    List<Map<String, Object>> getAniSalesData();  // 애니메이션 매출 데이터 조회
    List<Map<String, Object>> getDailySalesData();

    // 주문 테이블에서 총 회원 수 조회
    int getTotalUsers();

    // 주문 테이블에서 총 주문 수 조회
    int getTotalOrders();

    // 주문 테이블에서 총 매출액 조회
    double getTotalRevenue();

    // 굿즈 상품 삭제
    void deleteStoreByIdx(int idx);
    void deleteProductImagesByProductIdx(int idx);

    // 회원별 신고계정 목록 페이징
    List<MasterVO> getReportingUserWithPaging(int offset, int pageSize);
    int getTotalReportingUserCount();

    // 회원별 리뷰 목록
    List<MasterVO> getReplyListWithPaging(int offset, int pageSize);
    int getTotalReplyCount();

    public MasterVO getCommentByIdx(int idx);
    public List<MasterVO> getRepliesByCommentIdx(int idx);
    MasterVO findAdminByAdminid(String adminid);

    List<MasterVO> getUserListWithPaging(int offset, int pageSize);
    MasterVO getFAQById(int idx);
    void updateFAQ(MasterVO faq);

    void deleteReport(int idx);

    Map<String, Object> getCategoryCodeCountByani(int categorytCode);

    // 주문관리
    List<MasterVO> getSalesStatistics(Map<String, Object> params);
    List<MasterVO> getOrdersByDate(String date);
    List<MasterVO> getOrdersByMonth(String month);

    // 공지사항 페이징
    List<MasterVO> getNoticeListByPage(int startRecord, int pageSize);
    int getTotalNoticeCount();

    // 문의사항 페이징
    List<MasterVO> getQNAListByPage(int startRecord, int pageSize);
    int getTotalQnaCount();


    Integer findUserIdxByCommentIdx(Integer commentIdx);

    // 채원 시작
    // 주문 상품 데이터 불러오기
    List<CurrentOrderDataDTO> getUserOrderList(@Param("pageSize") int pageSize,
                                               @Param("offset") int offset,
                                               @Param("startDate") String startDate,
                                               @Param("endDate") String endDate,
                                               @Param("searchType") String searchType,
                                               @Param("searchKeyword") String searchKeyword);
    int getTotalOrderListCount(@Param("startDate") String startDate,
                               @Param("endDate") String endDate,
                               @Param("searchType") String searchType,
                               @Param("searchKeyword") String searchKeyword);
    OrderStateCountDTO getStateCount();
    // 주문 상세정보 데이터
    OrderListDTO getDetailInfo(int order_idx);
    List<OrderListVO> getDetailProducts(int order_idx);
    // 운송장 번호 저장
    int saveTrackingNumber(int order_idx, String trackingNum);
    // 배송시작으로 상태 변경
    void updatedeliOrderState(int order_idx, int state);
    // 주문 상태 변경
    int updateOrderState(int idx, int orderState);
    // 매출내역
    List<SalesListDTO> getSalesList(@Param("pageSize") int pageSize,
                                    @Param("offset") int offset,
                                    @Param("startDate") String startDate,
                                    @Param("endDate") String endDate);
    int getTotalSalesListCount(@Param("startDate") String startDate,
                               @Param("endDate") String endDate);
    List<CurrentOrderDataDTO> getSalesDetailList(@Param("pageSize") int pageSize,
                                                 @Param("offset") int offset,
                                                 @Param("orderDate") String orderDate);
    int getTotalSalesDetailListCount(@Param("orderDate") String orderDate);

    void updateAllEndDT(@Param("useridx") int useridx,
                        @Param("endDT") LocalDateTime endDT);
    // 전체 FAQ 개수 조회
    int getTotalFAQCount();

    // 특정 범위의 FAQ 목록 조회
    List<MasterVO> getFAQListByPage(int startRecord, int pageSize);
    int getTotalEventCount(); // 전체 이벤트 개수 조회
    List<MasterVO> getEventListByPage(int startRecord, int pageSize);

    List<MasterVO> getLatestActivities();
    List<MasterVO> getRecentOrders();
    Integer findAdminIdxByAdminid(String adminid);
    boolean checkAdminDeleted(int idx);
    MasterVO adminLogin(String adminid, String adminpwd);
    List<SalesListDTO> getMonthlySales();
    List<SalesListDTO> getDailySales();
    List<SalesListDTO> getAniSales();
    List<SalesListDTO> getCategorySales();
    Integer findUserIdByCommentIdx(int comment_idx);
    Integer findUserIdByReviewIdx(int review_idx);
    Integer findUserIdByCommunityIdx(int comunity_idx);
}
