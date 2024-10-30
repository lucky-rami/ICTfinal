package com.ict.finalproject.Service;

import com.ict.finalproject.DAO.MasterDAO;
import com.ict.finalproject.DTO.*;
import com.ict.finalproject.vo.MasterVO;
import com.ict.finalproject.vo.OrderListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.DataInput;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MasterServiceImpl implements MasterService {
    @Autowired
    MasterDAO dao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    // 관리자페이지 공지사항 추가
    public MasterVO createNotice(MasterVO notice) {
        /**
         * 관리자 페이지에서 공지사항을 추가하는 메서드입니다.
         *
         * @param vo 공지사항의 정보를 담고 있는 MasterVO 객체 (제목, 내용, 작성자 정보 포함)
         * @return 데이터베이스에 공지사항을 성공적으로 추가한 경우 1, 실패한 경우 0 반환
         */
        int result = dao.insertNotice(notice);
        return result > 0 ? notice : null;
    }

    @Override
    public MasterVO createFAQ(MasterVO faq) {
        int result = dao.insertFAQ(faq);
        return result > 0 ? faq : null;
    }

    @Override
    @Transactional
    public int createStore(MasterVO storeAdd) {
        dao.createStore(storeAdd);
        return storeAdd.getIdx();
    }

    @Override
    public List<MasterVO> getFAQList() {
        return dao.getFAQList();
    }

    @Override
    public List<MasterVO> getStoreList() {
        return dao.getStoreList();
    }

    @Override
    public int getTotalStore() {
        return dao.getTotalStore();
    }

    @Override
    public Map<String, Object> getCategoryCountByCode(int categoryCode) {
        return dao.getCategoryCountByCode(categoryCode);
    }

    @Override
    public List<MasterVO> getBoardList() {
        return dao.getBoardList();
    }

    @Override
    public Integer getAdminIdxByAdminid(String adminid) {
        return dao.getAdminIdxByAdminid(adminid);
    }

    @Override
    public List<MasterVO> getAniAllList() {
        return dao.getAniAllList();
    }

    @Override
    public List<MasterVO> getNoticeList() {
        return dao.getNoticeList();
    }

    @Override
    public List<MasterVO> getReviewList() {
        return dao.getReviewList();
    }


    @Override
    public boolean checkUserBanStatus(String userid) {
        return dao.isUserBanned(userid);
    }

    @Override
    public boolean checkUserDelected(int idx) {
        return dao.isUserDeleted(idx);
    }

    @Override
    public Integer findUserIdxByUserid(String userid) {
        return dao.findUserIdxByUserid(userid);
    }

    @Override
    public List<MasterVO> getMemberDelList(int currentPage, int pageSize) {
        return dao.getMemberDelList(currentPage,pageSize);
    }

    @Override
    public List<MasterVO> getReportinguserList(MasterVO vo) {
        return dao.getReportinguserList(vo);
    }

    @Override
    public void deleteFaq(int idx) {
        dao.deleteFaq(idx);
    }

    @Override
    public void deleteBoard(int idx) {
        dao.deleteBoard(idx);
    }

    @Override
    public void deleteReply(int idx) {
        dao.deleteComment(idx);
        dao.deleteReport(idx);
        dao.deleteReply(idx);
    }

    @Override
    public void deleteComment(int idx) {
        dao.deleteComment(idx);
    }

    @Override
    public List<MasterVO> getReportinguserListWithPaging(int offset, int pageSize) {
        return dao.getReportinguserListWithPaging(offset, pageSize);
    }

    @Override
    public int getActiveReportCount() {
        return dao.getActiveReportCount();
    }

    @Override
    public int getInactiveReportCount() {
        return dao.getInactiveReportCount();
    }

    @Override
    public int getCompletedReportCount() {
        return dao.getCompletedReportCount();
    }

    @Override
    public int getNonCompletedReportCount() {
        return dao.getNonCompletedReportCount();
    }

    @Override
    public void deleteReport1(int idx) {
        dao.deleteReport1(idx);
    }

    @Override
    public int getTodayBoardCount() {
        return dao.getTodayBoardCount();
    }

    @Override
    public int getLastWeekBoardCount() {
        return dao.getLastWeekBoardCount();
    }

    @Override
    public int getTotalUserDelCount() {
        return dao.getTotalUserDelCount();
    }

    @Override
    public void deleteEvent(int idx) {
        dao.deleteEvent(idx);
    }

    @Override
    public int getTodayReplyCount() {
        return dao.getTodayReplyCount();
    }

    @Override
    public int getWeekReplyCount() {
        return dao.getWeekReplyCount();
    }

    @Override
    public int getTodayReviewCount() {
        return dao.getTodayReviewCount();
    }

    @Override
    public int getWeekReviewCount() {
        return dao.getWeekReviewCount();
    }

    @Override
    public void deleteReview(int idx) {
        dao.deleteReview(idx);
    }

    @Override
    public void updateReportAndBan(int idx,Integer useridx, String reason, LocalDateTime stopDT, LocalDateTime handleDT, LocalDateTime endDT, int handleState, int comment_idx) {
        if (handleState == 2) {
            return;
        }

        // 특정 idx에 대한 handleState와 handleDT 업데이트
        dao.updateReport(idx, handleState, handleDT);

        // 같은 useridx에 대한 모든 endDT 업데이트
        dao.updateAllEndDT(useridx, endDT);

    }

    @Override
    public List<MasterVO> getReplyList(MasterVO vo) {
        return dao.getReplyList(vo);
    }

    @Override
    public MasterVO getReviewDetail(int idx) {
        return dao.getReviewDetail(idx);
    }

    @Override
    public boolean updateAnimation(MasterVO vo) {
        int result = dao.updateAnimation(vo);
        return result > 0;
    }

    @Override
    public MasterVO aniSelect(int idx) {
        return dao.aniSelect(idx);
    }

    @Override
    public String getCurrentImgFile(int idx) {
        return dao.getCurrentImgFile(idx);
    }

    @Override
    public List<MasterVO> getQNAList() {
        return dao.getQNAList();
    }

    @Override
    public int getUnansweredQnaCount() {
        return dao.getUnansweredQnaCount();
    }

    @Override
    public List<MasterVO> getReportingUser() {
        return dao.getReportingUser();
    }

    @Override
    public int getTotalReportCount() {
        return dao.getTotalReportCount();
    }

    @Override
    public int getTotalUserReport(String userid) {
        return dao.getTotalUserReport(userid);
    }

    @Override
    public void updateQnaAndReply(int idx, String reply, int adminIdx) {
        dao.updateHandleState(idx);
        dao.insertReply(idx, reply, adminIdx, LocalDateTime.now());
    }

    @Override
    public Integer findAdminIdxByUserid(String adminId) {
        return dao.findAdminIdxByUserid(adminId);
    }

    @Override
    public void addAnimation(MasterVO aniVO) {
        dao.insertAnimation(aniVO);
    }

    @Override
    public void deletePostByIdx(int idx) {
        dao.deletePostByIdx(idx);
    }

    @Override
    public MasterVO getStoreByIdx(int idx) {
        return dao.getStoreByIdx(idx);
    }

    @Override
    public boolean updateStore(MasterVO store) {
        int result = dao.updateStore(store);
        return result > 0;
    }

    @Override
    public List<MasterVO> getSubCategoriesByCategory(int category) {
        return dao.findSubCategoriesByCategory(category);
    }

    @Override
    public void insertProductImg(MasterVO masterVO) {
        dao.insertProductImg(masterVO);
    }


    @Override
    public MasterVO getNoticeById(int idx) {
        return dao.getNoticeById(idx);
    }

    @Override
    public boolean updateNotice(MasterVO noticeEdit) {
        return dao.updateNotice(noticeEdit) > 0;
    }

    @Override
    public MasterVO noticeSelect(int idx) {
        return dao.noticeSelect(idx);
    }

    @Override
    public MasterVO getQnaById(int idx) {
        return dao.getQnaReplyById(idx);
    }

    @Override
    public boolean validateAdmin(String adminid, String adminpwd) {
        MasterVO admin = dao.getAdminByAdminId(adminid);

        if (admin != null) {
            log.info("Database password (hashed): " + admin.getAdminpwd()); // 해시된 비밀번호 로그
            log.info("User input password (plain): " + adminpwd); // 사용자가 입력한 비밀번호 로그

            try {
                // 비밀번호 비교
                boolean matches = passwordEncoder.matches(adminpwd, admin.getAdminpwd());
                log.info("Password match result: " + matches);
                return matches; // 비밀번호가 일치하는지 반환
            } catch (Exception e) {
                log.error("Error occurred during password comparison", e);
                throw new RuntimeException("Password comparison failed");
            }
        } else {
            log.info("Admin not found with id: " + adminid);
            return false;
        }
    }

    @Override
    public boolean doesProductExist(int pro_idx) {
        int count = dao.doesProductExist(pro_idx);
        return count > 0;
    }

    @Override
    public MasterVO getProductImgByIdx(int idx) {
        return dao.getProductImgByIdx(idx);
    }

    @Override
    public boolean updateProductImg(MasterVO productImg) {
        int result = dao.updateProductImg(productImg);
        return result > 0;
    }

    @Override
    public int getTotalAnimeCount() {
        return dao.getTotalAnimeCount();
    }

    @Override
    public List<MasterVO> getAniListWithPaging(int offset, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", limit);
        return dao.getAniListWithPaging(params);
    }

    @Override
    public List<MasterVO> getStoreListWithPaging(int offset, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", limit);
        return dao.getStoreListWithPaging(params);
    }

    @Override
    public List<MasterVO> getBoardListWithPaging(int offset, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", limit);
        return dao.getBoardListWithPaging(params);
    }

    @Override
    public int getTotalBoardCount() {
        return dao.getTotalBoardCount();
    }

    @Override
    public List<MasterVO> getReviewListWithPaging(int offset, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", limit);
        return dao.getReviewListWithPaging(params);
    }

    @Override
    public int getTotalReviewCount() {
        return dao.getTotalReviewCount();
    }

    @Override
    public void addEvent(MasterVO event) {
        int result = dao.insertEvent(event);
        if (result > 0) {
            log.info("이벤트 등록 성공. Event idx: {}", result);
        } else {
            log.error("이벤트 등록 실패");
        }
    }

    @Override
    public List<MasterVO> getEventList() {
        return dao.getEventList();
    }

    @Override
    public MasterVO getEventDetail(int idx) {
        return dao.getEventDetail(idx);
    }

    @Override
    public boolean updateEvent(MasterVO event) {
        return dao.updateEvent(event) > 0;
    }

    @Override
    public MasterVO getEventByIdx(int idx) {
        return dao.getEventByIdx(idx);
    }

    @Override
    public List<Map<String, Object>> getUserRegistrationStats() {
        return dao.getUserRegistrationStats();
    }

    @Override
    public void deleteStoreByIdx(int idx) {
        dao.deleteStoreByIdx(idx);
    }

    @Override
    public void deleteProductImagesByProductIdx(int idx) {
        dao.deleteProductImagesByProductIdx(idx);
    }

    @Override
    public List<MasterVO> getReportingUserWithPaging(int offset, int pageSize) {
        return dao.getReportingUserWithPaging(offset, pageSize);
    }

    @Override
    public int getTotalReportingUserCount() {
        return dao.getTotalReportingUserCount();
    }

    @Override
    public List<MasterVO> getReplyListWithPaging(int offset, int pageSize) {
        return dao.getReplyListWithPaging(offset, pageSize);
    }

    @Override
    public int getTotalReplyCount() {
        return dao.getTotalReplyCount();
    }

    @Override
    public MasterVO getCommentByIdx(int idx) {
        return dao.getCommentByIdx(idx);
    }

    @Override
    public List<MasterVO> getRepliesByCommentIdx(int idx) {
        return dao.getRepliesByCommentIdx(idx);
    }

    @Override
    public boolean authenticateAdmin(String adminid, String adminpwd) {
        MasterVO admin = dao.findAdminByAdminid(adminid);
        if (admin != null && admin.getAdminpwd().equals(adminpwd)) {
            return true; // 인증 성공
        }
        return false; // 인증 실패
    }

    @Override
    public List<MasterVO> getUserListWithPaging(int offset, int pageSize) {
        return dao.getUserListWithPaging(offset, pageSize);
    }

    @Override
    public MasterVO getFAQById(int idx) {
        return dao.getFAQById(idx);
    }

    @Override
    public void updateFAQ(MasterVO faq) {
        dao.updateFAQ(faq);
    }

    @Override
    public void deleteReport(int idx) {
        try {
            dao.deleteReport1(idx);
            dao.deleteReport(idx);
        } catch (Exception e) {
            System.err.println("삭제 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getCategoryCodeCountByani(int categorytCode) {
        return dao.getCategoryCodeCountByani(categorytCode);
    }

    @Override
    public List<MasterVO> getSalesStatistics(Map<String, Object> params) {
        return dao.getSalesStatistics(params);
    }

    @Override
    public List<MasterVO> getOrdersByDate(String date) {
        return dao.getOrdersByDate(date);
    }

    @Override
    public List<MasterVO> getOrdersByMonth(String month) {
        return dao.getOrdersByMonth(month);
    }

    @Override
    public List<MasterVO> getNoticeListByPage(int startRecord, int pageSize) {
        return dao.getNoticeListByPage(startRecord, pageSize);
    }

    @Override
    public int getTotalNoticeCount() {
        return dao.getTotalNoticeCount();
    }

    @Override
    public List<MasterVO> getQNAListByPage(int startRecord, int pageSize) {
        return dao.getQNAListByPage(startRecord, pageSize);
    }

    @Override
    public int getTotalQnaCount() {
        return dao.getTotalQnaCount();
    }

    @Override
    public Integer findUserIdxByCommentIdx(Integer commentIdx) {
        return dao.findUserIdxByCommentIdx(commentIdx);
    }

    // 채원 시작
    // 주문 상품 데이터 불러오기
    @Override
    public List<CurrentOrderDataDTO> getUserOrderList(int page, int pageSize, String startDate, String endDate, String searchType, String searchKeyword) {
        int offset = (page-1)*pageSize;
        return dao.getUserOrderList(pageSize, offset,startDate,endDate, searchType, searchKeyword);
    }

    @Override
    public int getTotalOrderListCount(String startDate, String endDate, String searchType, String searchKeyword) {
        return dao.getTotalOrderListCount(startDate,endDate, searchType, searchKeyword);
    }

    @Override
    public OrderStateCountDTO getStateCount() {
        return dao.getStateCount();
    }

    @Override
    public OrderListDTO getDetailInfo(int order_idx) {
        return dao.getDetailInfo(order_idx);
    }

    @Override
    public List<OrderListVO> getDetailProducts(int order_idx) {
        return dao.getDetailProducts(order_idx);
    }

    @Override
    public int saveTrackingNumber(int order_idx, String trackingNum) {
        return dao.saveTrackingNumber(order_idx,trackingNum);
    }

    @Override
    public void updatedeliOrderState(int order_idx, int state) {
        dao.updatedeliOrderState(order_idx, state);
    }

    @Override
    public int updateOrderState(int idx, int orderState) {
        return dao.updateOrderState(idx, orderState);
    }

    @Override
    public List<SalesListDTO> getSalesList(int page, int pageSize, String startDate, String endDate) {
        int offset = (page-1)*pageSize;
        return dao.getSalesList(pageSize, offset,startDate,endDate);
    }

    @Override
    public int getTotalSalesListCount(String startDate, String endDate) {
        return dao.getTotalSalesListCount(startDate,endDate);
    }

    @Override
    public List<CurrentOrderDataDTO> getSalesDetailList(int page, int pageSize, String orderDate) {
        int offset = (page-1)*pageSize;
        return dao.getSalesDetailList(pageSize, offset,orderDate);
    }

    @Override
    public int getTotalSalesDetailListCount(String orderDate) {
        return dao.getTotalSalesDetailListCount(orderDate);
    }

    @Override
    public void updateAllEndDT(int useridx, LocalDateTime endDT) {
        dao.updateAllEndDT(useridx, endDT);
    }

    @Override
    public void insertReport(Integer useridx, String reason, LocalDateTime stopDT, LocalDateTime endDT, Integer comment_idx, Integer review_idx, Integer comunity_idx, int report_type) {
        System.out.println("Inserting report with values: " +
                "useridx=" + useridx +
                ", reason=" + reason +
                ", stopDT=" + stopDT +
                ", endDT=" + endDT +
                ", comment_idx=" + comment_idx +
                ", review_idx=" + review_idx +
                ", comunity_idx=" + comunity_idx);
        dao.insertReport(useridx, reason, stopDT, endDT, comment_idx, review_idx, comunity_idx, report_type);
    }


    @Override
    public void updateReport(int idx, int handleState, LocalDateTime handleDT) {
        dao.updateReport(idx, handleState, handleDT);
    }

    @Override
    public int getTotalFAQCount() {
        return dao.getTotalFAQCount();
    }

    @Override
    public List<Map<String, Object>> getAniSalesData() {
        return dao.getAniSalesData();
    }

    @Override
    public List<Map<String, Object>> getDailySalesData() {
        return dao.getDailySalesData();
    }

    @Override
    public int getTotalUsers() {
        return dao.getTotalUsers();
    }

    @Override
    public int getTotalOrders() {
        return dao.getTotalOrders();
    }

    @Override
    public double getTotalRevenue() {
        return dao.getTotalRevenue();
    }


    @Override
    public List<MasterVO> getFAQListByPage(int startRecord, int pageSize) {
        return dao.getFAQListByPage(startRecord, pageSize);
    }

    @Override
    public int getTotalEventCount() {
        return dao.getTotalEventCount();
    }

    @Override
    public List<MasterVO> getEventListByPage(int startRecord, int pageSize) {
        return dao.getEventListByPage(startRecord, pageSize);
    }

    @Override
    public List<MasterVO> getLatestActivities() {
        return dao.getLatestActivities();
    }

    @Override
    public List<MasterVO> getRecentOrders() {
        return dao.getRecentOrders();
    }

    @Override
    public Integer findAdminIdxByAdminid(String adminid) {
        return dao.getAdminIdxByAdminid(adminid);
    }

    @Override
    public boolean checkAdminDeleted(int idx) {
        return dao.checkAdminDeleted(idx);
    }

    @Override
    public MasterVO adminLogin(String adminid, String adminpwd) {
        return dao.adminLogin(adminid, adminpwd);
    }

    @Override
    public Integer findUserIdByCommentIdx(int comment_idx) {
        return dao.findUserIdByCommentIdx(comment_idx);
    }

    @Override
    public Integer findUserIdByReviewIdx(int review_idx) {
        return dao.findUserIdByReviewIdx(review_idx);
    }

    @Override
    public Integer findUserIdByCommunityIdx(int comunity_idx) {
        return dao.findUserIdByCommunityIdx(comunity_idx);
    }

    @Override
    public List<SalesListDTO> getMonthlySales() {
        return dao.getMonthlySales();
    }

    @Override
    public List<SalesListDTO> getDailySales() {
        return dao.getDailySales();
    }

    @Override
    public List<SalesListDTO> getAniSales() {
        return dao.getAniSales();
    }

    @Override
    public List<SalesListDTO> getCategorySales() {
        return dao.getCategorySales();
    }
}
