package com.ict.finalproject.Service;

import com.ict.finalproject.DAO.CommuDAO;
import com.ict.finalproject.DTO.ReportDTO;
import com.ict.finalproject.vo.CommuVO;
import com.ict.finalproject.vo.PagingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommuServiceImpl implements CommuService {

    @Autowired
    private CommuDAO commuDAO;

    @Override
    public CommuVO writeBoard(CommuVO board) {
        // 게시글 등록
        int result = commuDAO.insertBoard(board);
        return result > 0 ? board : null;
    }

    @Override
    public List<CommuVO> getCommuList(PagingVO pagingVO) {
        return commuDAO.getCommuList(pagingVO);
    }

    @Override
    public int getTotalCount(PagingVO pagingVO) {
        return commuDAO.getTotalCount(pagingVO);
    }


    //@Override
    //public List<CommuVO> List(String commtype) {
        //return commuDAO.List(commtype);
    //}

    //@Override
    //public List<CommuVO> FilteredList(String commtype, String orderBy, String searchCategory, String searchKeyword) {
        //return commuDAO.FilteredList(commtype, orderBy, searchCategory, searchKeyword);
    //}

    @Override
    public CommuVO Detail(int idx) {
        return commuDAO.Detail(idx);
    }

    @Override
    public void HitCount(int idx) {
        commuDAO.HitCount(idx);
    }

    @Override
    public CommuVO PreviousPost(int idx) {
        return commuDAO.PreviousPost(idx);
    }

    @Override
    public CommuVO NextPost(int idx) {
        return commuDAO.NextPost(idx);
    }

    @Override
    public boolean UpdateBoard(CommuVO board) {
        int result = commuDAO.UpdateBoard(board);
        return result > 0; // 1 이상이면 수정 성공
    }

    @Override
    public int Delete(int idx) {
        return commuDAO.Delete(idx);
    }

    @Override
    public int insertReport(ReportDTO reportDTO) {
        return commuDAO.insertReport(reportDTO);
    }

    @Override
    public int checkPostReportExists(ReportDTO reportDTO) {
        return commuDAO.checkPostReportExists(reportDTO);
    }

    @Override
    public int checkCommentReportExists(ReportDTO reportDTO) {
        return commuDAO.checkCommentReportExists(reportDTO);
    }


}
