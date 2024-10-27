package com.ict.finalproject.DAO;


import com.ict.finalproject.DTO.*;
import com.ict.finalproject.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface MemberDAO {
        // 회원가입
        public int memberCreate(MemberVO vo);

        // 로그인
        public MemberVO memberLogin(String userid, String userpwd);

        // idx로 해당 아이디 구하기
        Integer getUseridx(String userid);

        // 관리자페이지에서 모든 회원 전체 출력
        public List<MemberVO> getMemberList(MemberVO vo);

        // 총 유저수 구하기
        int getTotalUser();

        // 오늘 가입자 수 구하기
        int getNewUsers();

        // 최근 7일간 가입자 수 구하기
        int getNewSignups();

        //채원시작
        //리뷰작성전 리스트 SELECT
        List<ReviewBeforeDTO> getReviewBefore(int useridx);

        //리뷰 등록
        int saveReview(ReviewVO review);

        //리뷰 작성완료 리스트
        List<ReviewCompletedDTO> getReviewCompleted(int useridx);

        //리뷰 원글 저장
        ReviewVO getReviewEditbefore(int orderList_Idx);

        //리뷰 수정
        int updateReview(ReviewVO reviewEditbefore);

        //리뷰 삭제
        void reviewDelete(int orderList_idx);

        //작성 할 수 있는 리뷰갯수
        int getReviewBeforeAmount(int useridx);

        //작성완료한 리뷰갯수
        int getReviewCompletedAmount(int useridx);

        //회원 수정을 위해 정보 select
        MemberVO getUserinfo(int useridx);

        //회원 수정
        int updateUser(MemberVO member);

        //회원 탈퇴
        int userDelOk(int useridx);
        void userDelInsert(UserDelReasonDTO userDelReasonDTO);

        // 마이페이지 최근주문 최대 5개 데이터
        List<CurrentOrderDataDTO> getCurrentOrderData(int useridx);
        // 마이페이지 최근 굿즈 좋아요 5개 데이터
        List<StoreVO> getCurrentLikeGoodsData(int useridx);
        // 마이페이지 최근 애니 좋아요 5개 데이터
        List<AniListVO> getCurrentLikeAniData(int useridx);
        // 마이페이지 좋아요 굿즈
        List<StoreVO> getLikeGoods(int pageSize,int offset, int useridx);
        int getTotalLikeGoodsCount(int useridx);
        // 마이페이지 좋아요 애니
        List<AniListVO> getLikeAni(int page, int pageSize, int useridx);
        int getTotalLikeAniCount(int useridx);
        // 마이페이지 좋아요 취소
        int deleteGoodsLike(int useridx, int pro_idx);
        int deleteAniLike(int useridx, int ani_idx);
        // 마이페이지 주문내역
        List<OrderListDTO> getPagedOrderList(int userIdx, int offset, int pageSize);
        List<OrderProDTO> getOrderProducts(int order_idx);
        long getTotalOrderCount(int userIdx);
        OrderListDTO getOrderDetailData(int order_idx, int useridx);
        int getPaymentId(int order_idx);
        PayCancelDTO getCancelData(int payment_id);
        //적립금 업데이트
        void pointInsert(int useridx, int type, int point);
        void userPointUpdate(int useridx, int point);
        //적립금 내역
        List<PointVO> getPointList(int pageSize, int offset, int useridx);
        int getTotalPointCount(int useridx);
        // 문의내역
        List<QnaVO> getQnAList(int pageSize, int offset, int useridx);
        int getTotalQnACount(int useridx);
        QnaVO getQnADetail(int qna_idx);
        int qnaDelete(int qna_idx);
        // 내가 쓴 글
        List<CommuVO> getCmList(int pageSize, int offset, int useridx);
        int getTotalCmCount(int useridx);
        int cmDelete(int comm_idx);

        // 아이디 및 비밀번호 찾기 , 비밀번호 변경
        String findId(String username, String email);
        String findPwd(String userid,String username, String email);
        int changePassword(String userid, String userpwd);
        int checkUserIdExists(String userid);

        // 구글 소설 로그인
        MemberVO findUserByEmail(String email);

        // 최신 가입자 구하기
        List<MemberVO> getLatestMembers();
}
