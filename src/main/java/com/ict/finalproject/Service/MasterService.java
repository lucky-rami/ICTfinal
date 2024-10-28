package com.ict.finalproject.Service;

import com.ict.finalproject.DTO.*;
import com.ict.finalproject.vo.MasterVO;
import com.ict.finalproject.vo.OrderListVO;
import com.ict.finalproject.vo.StoreVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public interface MasterService {
    MasterVO createNotice(MasterVO notice);
    MasterVO createFAQ(MasterVO faq);
    int createStore(MasterVO storeAdd);
    public List<MasterVO> getFAQList();
    public List<MasterVO> getStoreList();
    int getTotalStore();
    Map<String, Object> getCategoryCountByCode(@Param("categoryCode") int categoryCode);
    List<MasterVO> getBoardList();
    Integer getAdminIdxByAdminid(String adminid);
    public List<MasterVO> getAniAllList();
    List<MasterVO> getNoticeList();
    List<MasterVO> getReviewList();

    boolean checkUserBanStatus(String userid);
    boolean checkUserDelected(int idx);
    Integer findUserIdxByUserid(String userid);
    List<MasterVO> getMemberDelList(MasterVO vo);
    List<MasterVO> getReportinguserList(int offset, int pageSize,MasterVO vo);
    void updateReportAndBan(int idx, Integer useridx, String reason, LocalDateTime stopDT,
                            LocalDateTime handleDT, LocalDateTime endDT, int handleState, int comment_idx);
    List<MasterVO> getReplyList(MasterVO vo);
    MasterVO getReviewDetail(int idx);
    boolean updateAnimation(MasterVO vo);
    MasterVO aniSelect(int idx);
    String getCurrentImgFile(int idx);
    List<MasterVO> getQNAList();
    int getUnansweredQnaCount();
    List<MasterVO>getReportingUser();
    int getTotalReportCount();
    int getTotalUserReport(String userid);
    void updateQnaAndReply(int idx, String reply, int adminIdx);
    Integer findAdminIdxByUserid(String adminId);
    void addAnimation(MasterVO aniVO);
    void deletePostByIdx(int idx);
    MasterVO getStoreByIdx(int idx);
    boolean updateStore(MasterVO store);
    List<MasterVO> getSubCategoriesByCategory(int category);
    void insertProductImg(MasterVO masterVO);
    MasterVO getNoticeById(int idx);
    boolean updateNotice(MasterVO noticeEdit);
    MasterVO noticeSelect (int idx);
    MasterVO getQnaById(int idx);
    boolean validateAdmin(String adminid, String adminpwd);
    boolean doesProductExist(int pro_idx);
    MasterVO getProductImgByIdx(int idx);
    boolean updateProductImg(MasterVO productImg);
    int getTotalAnimeCount(); // 총 애니메이션 수를 가져옴
    List<MasterVO> getAniListWithPaging(int currentPage, int pageSize); // 페이징된 애니 리스트 가져오기
    List<MasterVO> getStoreListWithPaging(int currentPage, int pageSize);
    List<MasterVO> getBoardListWithPaging(int currentPage, int pageSize);
    int getTotalBoardCount();
    List<MasterVO> getReviewListWithPaging(int offset, int pageSize);
    int getTotalReviewCount();
    void addEvent(MasterVO event);
    List<MasterVO> getEventList();
    public MasterVO getEventDetail(int idx);
    boolean updateEvent(MasterVO event);
    MasterVO getEventByIdx(int idx);
    List<Map<String, Object>> getUserRegistrationStats();
    void deleteStoreByIdx(int idx);
    void deleteProductImagesByProductIdx(int idx);
    List<MasterVO> getReportingUserWithPaging(int offset, int pageSize);
    int getTotalReportingUserCount();
    List<MasterVO> getReplyListWithPaging(int offset, int pageSize);
    int getTotalReplyCount();
    public MasterVO getCommentByIdx(int idx);
    public List<MasterVO> getRepliesByCommentIdx(int idx);
    boolean authenticateAdmin(String adminid, String adminpwd);
    List<MasterVO> getUserListWithPaging(int offset, int pageSize);
    MasterVO getFAQById(int idx);
    void updateFAQ(MasterVO faq);
    void deleteReport(int idx);
    Map<String, Object>getCategoryCodeCountByani(int categorytCode);
    List<MasterVO> getSalesStatistics(Map<String, Object> params);
    List<MasterVO> getOrdersByDate(String date);
    List<MasterVO> getOrdersByMonth(String month);
    List<MasterVO> getNoticeListByPage(int startRecord, int pageSize);
    int getTotalNoticeCount();
    List<MasterVO> getQNAListByPage(int startRecord, int pageSize);
    int getTotalQnaCount();
    Integer findUserIdxByCommentIdx(Integer commentIdx);

    // 채원 시작
    // 주문 상품 데이터 불러오기
    List<CurrentOrderDataDTO> getUserOrderList(int page, int pageSize,String startDate, String endDate, String searchType, String searchKeyword);
    int getTotalOrderListCount(String startDate, String endDate, String searchType, String searchKeyword);
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
    // 매출 내역
    List<SalesListDTO> getSalesList(int page, int pageSize, String startDate, String endDate);
    int getTotalSalesListCount(String startDate, String endDate);
    List<CurrentOrderDataDTO> getSalesDetailList(int page, int pageSize, String orderDate);
    int getTotalSalesDetailListCount(String orderDate);
    //차트
    //매출액
    List<SalesListDTO> getMonthlySales();
    List<SalesListDTO> getDailySales();
    List<SalesListDTO> getAniSales();
    List<SalesListDTO> getCategorySales();
    void updateAllEndDT(int useridx, LocalDateTime endDT);
    void insertReport(Integer useridx, String reason, LocalDateTime stopDT, LocalDateTime endDT, Integer comment_idx, Integer review_idx, Integer comunity_idx, int report_type);
    void updateReport(int idx, int handleState, LocalDateTime handleDT);
    // 전체 FAQ 개수 조회
    int getTotalFAQCount();
    List<Map<String, Object>> getAniSalesData();  // 애니메이션 매출 데이터 조회
    List<Map<String, Object>> getDailySalesData();

    // 특정 범위의 FAQ 목록 조회
    List<MasterVO> getFAQListByPage(int startRecord, int pageSize);
    int getTotalEventCount(); // 전체 이벤트 개수 조회
    List<MasterVO> getEventListByPage(int startRecord, int pageSize);
    List<MasterVO> getLatestActivities();
    List<MasterVO> getRecentOrders();
    Integer findAdminIdxByAdminid(String adminid);
    boolean checkAdminDeleted(int idx);
    MasterVO adminLogin(String adminid, String adminpwd);
    Integer findUserIdByCommentIdx(int comment_idx);
    Integer findUserIdByReviewIdx(int review_idx);
    Integer findUserIdByCommunityIdx(int comunity_idx);
}
