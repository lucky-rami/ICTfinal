package com.ict.finalproject.Service;

import com.ict.finalproject.DAO.MasterDAO;
import com.ict.finalproject.vo.MasterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MasterServiceImpl implements MasterService {
    @Autowired
    MasterDAO dao;

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
}
