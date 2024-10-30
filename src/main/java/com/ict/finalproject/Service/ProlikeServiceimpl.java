package com.ict.finalproject.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.finalproject.DAO.ProlikeDAO;
import com.ict.finalproject.vo.ProLikeVO;

@Service
public class ProlikeServiceimpl implements ProlikeService{
    
    @Autowired
    private ProlikeDAO dao;

    public void addLike(ProLikeVO likeVO) {
       dao.insertLike(likeVO);  // 좋아요 추가
    }

   
    public void removeLike(ProLikeVO likeVO) {
       dao.deleteLike(likeVO);  // 좋아요 취소
    }

  
    public boolean isLiked(int pro_idx, int userIdx) {
       return dao.selectLikeStatus(pro_idx, userIdx) != null;  // 좋아요 상태 확인
    }
}
