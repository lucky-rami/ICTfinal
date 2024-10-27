package com.ict.finalproject.Service;

import com.ict.finalproject.DAO.MemberDAO;
import com.ict.finalproject.DTO.*;
import com.ict.finalproject.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {
    private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);
    @Autowired
    MemberDAO dao;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public int memberCreate(MemberVO vo) {
        return dao.memberCreate(vo);
    }

    @Override
    public MemberVO memberLogin(String userid, String userpwd) {
        return dao.memberLogin(userid, userpwd);
    }

    @Override
    public Integer getUseridx(String userid) {
        return dao.getUseridx(userid);
    }

    @Override
    public List<MemberVO> getMemberList(MemberVO vo) {
        return dao.getMemberList(vo);
    }

    @Override
    public int getTotalUser() {
        return dao.getTotalUser();
    }

    @Override
    public int getNewUsers() {
        return dao.getNewUsers();
    }

    @Override
    public int getNewSignups() {
        return dao.getNewSignups();
    }

    //리뷰작성전 리스트 SELECT
    @Override
    public List<ReviewBeforeDTO> getReviewBefore(int useridx) {
        return dao.getReviewBefore(useridx);
    }

    @Override
    public int saveReview(ReviewVO review) {
        return dao.saveReview(review);
    }

    @Override
    public List<ReviewCompletedDTO> getReviewCompleted(int useridx) {
        return dao.getReviewCompleted(useridx);
    }

    @Override
    public ReviewVO getReviewEditbefore(int orderList_Idx) {
        return dao.getReviewEditbefore(orderList_Idx);
    }

    @Override
    public int updateReview(ReviewVO reviewEditbefore) {
        return dao.updateReview(reviewEditbefore);
    }

    @Override
    public void reviewDelete(int orderList_idx) {
        dao.reviewDelete(orderList_idx);
    }

    @Override
    public int getReviewBeforeAmount(int useridx) {
        return dao.getReviewBeforeAmount(useridx);
    }

    @Override
    public int getReviewCompletedAmount(int useridx) {
        return dao.getReviewCompletedAmount(useridx);
    }

    @Override
    public MemberVO getUserinfo(int useridx) {
        return dao.getUserinfo(useridx);
    }

    @Override
    public int updateUser(MemberVO member) {
        return dao.updateUser(member);
    }

    @Override
    public int userDelOk(int useridx) {
        return dao.userDelOk(useridx);
    }

    @Override
    public void userDelInsert(UserDelReasonDTO userDelReasonDTO) {
        dao.userDelInsert(userDelReasonDTO);
    }

    @Override
    public List<CurrentOrderDataDTO> getCurrentOrderData(int useridx) {
        return dao.getCurrentOrderData(useridx);
    }

    @Override
    public List<StoreVO> getCurrentLikeGoodsData(int useridx) {
        return dao.getCurrentLikeGoodsData(useridx);
    }

    @Override
    public List<AniListVO> getCurrentLikeAniData(int useridx) {
        return dao.getCurrentLikeAniData(useridx);
    }

    @Override
    public List<StoreVO> getLikeGoods(int page, int pageSize, int useridx) {
        int offset = (page-1)*pageSize;
        return dao.getLikeGoods(pageSize,offset,useridx);
    }

    @Override
    public int getTotalLikeGoodsCount(int useridx) {
        return dao.getTotalLikeGoodsCount(useridx);
    }

    @Override
    public List<AniListVO> getLikeAni(int page, int pageSize, int useridx) {
        int offset = (page-1)*pageSize;
        return dao.getLikeAni(pageSize,offset,useridx);
    }

    @Override
    public int getTotalLikeAniCount(int useridx) {
        return dao.getTotalLikeAniCount(useridx);
    }

    @Override
    public int deleteGoodsLike(int useridx, int pro_idx) {
        return dao.deleteGoodsLike(useridx,pro_idx);
    }

    @Override
    public int deleteAniLike(int useridx, int ani_idx) {
        return dao.deleteAniLike(useridx,ani_idx);
    }

    @Override
    public PageResponse<OrderListDTO> getOrderListWithPaging(int userIdx, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        System.out.println("Offset: " + offset + ", Limit: " + pageSize);

        // 1. 주문 리스트 가져오기 (페이징된)
        List<OrderListDTO> orders = dao.getPagedOrderList(userIdx, offset, pageSize);
        log.info("################orders :{}",orders.toString());

        // 2. 각 주문에 대해 상품 목록을 조회하고 추가
        for (OrderListDTO order : orders) {
            List<OrderProDTO> products = dao.getOrderProducts(order.getOrder_idx());
            log.info("################products :{}",products.toString());
            order.setProducts(products);
        }

        // 전체 데이터 개수 조회
        long totalElements = dao.getTotalOrderCount(userIdx);

        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        // 3. PageResponse에 결과를 담아서 반환
        PageResponse<OrderListDTO> response = new PageResponse<>();
        response.setContent(orders);
        response.setPage(page);
        response.setSize(pageSize);
        response.setTotalElements(totalElements);
        response.setTotalPages(totalPages);

        return response;
    }

    //주문 정보 상세 데이터
    @Override
    public OrderListDTO getOrderDetailData(int order_idx, int useridx) {
        // 1. 주문내역 상세 정보 가져오기
        OrderListDTO orderDetail = dao.getOrderDetailData(order_idx, useridx);
        // 2. 주문한 상품 정보 가져오기
        List<OrderProDTO> products = dao.getOrderProducts(order_idx);
        orderDetail.setProducts(products);

        return orderDetail;
    }

    @Transactional
    @Override
    public void pointUpdate(int useridx, int type, int point) {
        // T_point에 포인트 내역 추가
        dao.pointInsert(useridx, type, point);
        // T_user point 업데이트
        dao.userPointUpdate(useridx, point);
    }

    @Override
    public List<PointVO> getPointList(int page, int pageSize, int useridx) {
        int offset = (page-1)*pageSize;
        return dao.getPointList(pageSize, offset, useridx);
    }

    @Override
    public int getTotalPointCount(int useridx) {
        return dao.getTotalPointCount(useridx);
    }

    @Override
    public String findId(String username, String email) {
        return dao.findId(username, email);
    }

    @Override
    public String findPwd(String userid, String username ,String email) {
        return dao.findPwd(userid,username ,email);
    }

    @Override
    public boolean changePassword(String userid, String userpwd) {
        String encryptedPassword = passwordEncoder.encode(userpwd);
        int result = dao.changePassword(userid, encryptedPassword);
        return result > 0;
    }

    @Override
    public boolean checkIdDuplicate(String userid) {
        int count = dao.checkUserIdExists(userid);
        System.out.println("아이디: " + userid + ", 존재 여부: " + (count > 0)); // 디버깅 로그
        return count > 0;
    }

    @Override
    public MemberVO findUserByEmail(String email) {
        return dao.findUserByEmail(email);
    }

    @Override
    public List<MemberVO> getLatestMembers() {
        return dao.getLatestMembers();
    }
}
