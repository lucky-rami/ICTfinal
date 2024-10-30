package com.ict.finalproject.Service;

import com.ict.finalproject.DAO.AniListDAO;
import com.ict.finalproject.vo.AniListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AniListServicelmpl implements AniListService{

    @Inject
    AniListDAO dao;
    @Autowired
    private AniListDAO aniListDAO;

    @Override
    public List<AniListVO> getAniList() {
        return aniListDAO.getAniList();
    }

    @Override
    public List<AniListVO> getAniListSelect() {
        return aniListDAO.getAniListSelect();
    }

    @Override
    public AniListVO getAniDetailView(String title, int idx) {
        return aniListDAO.getAniDetailView(title);
    }

    @Override
    public void incrementAniHit(String title) {
        aniListDAO.incrementAniHit(title);
    }

    @Override
    public List<AniListVO> getAniListWithPagination(int offset, int limit) {
        return aniListDAO.findAniListWithPagination(offset, limit);
    }

    @Override
    public List<AniListVO> getAniListByGenre(int anitype) {
        return aniListDAO.getMoviesByGenre(anitype); // 장르에 따른 애니메이션 목록을 반환
    }

   /* @Override
    public List<AniListVO> getAniListfilter(String sortCriteria) {
        return aniListDAO.getAniListfilter(sortCriteria);
    }*/

    @Override
    public void addGrade(int aniIdx, Double grade, int userIdx) {

        aniListDAO.addGrade(aniIdx, grade, userIdx);
    }
    @Override
    public int checkGrade(int aniIdx, int userIdx){
        return aniListDAO.checkGrade(aniIdx, userIdx);
    }
    @Override
    public int updateGrade(int aniIdx, Double grade, int userIdx){
        return aniListDAO.updateGrade(aniIdx, grade, userIdx);
    }

    @Override
    public String getOutline(String title) {
        return aniListDAO.getOutline(title);
    }

    @Override
    public List<AniListVO> getRandomSimilarAnis(String genreType) {
        return dao.getRandomSimilarAnis(genreType);
    }

    @Override
    public Double  getAverageGrade(int ani_idx) {
        // 평균 별점을 계산하기 위한 DAO 호출
        return aniListDAO.getAverageGrade(ani_idx);
    }


    @Override
    public void aniLike(int ani_idx, int useridx) {
        aniListDAO.insertLike(ani_idx, useridx);
    }

    @Override
    public void removeLike(int ani_idx, int useridx) {

        aniListDAO.removeLike(ani_idx, useridx);
    }

    @Override
    public boolean checkIfLiked(int ani_idx, int useridx) {
        Map<String, Integer> params = new HashMap<>();
        params.put("ani_idx", ani_idx);
        params.put("useridx", useridx);
        return dao.checkIfLiked(ani_idx, useridx) > 0; // 좋아요가 있다면 1, 없으면 0 반환
    }



    @Override
    public Integer getUseridx(String userid) {
        return dao.getUseridx(userid);
    }

  /*  @Override
    public List<AniListVO> getAniListfilter(String filter){
        return dao.getAniListfilter(filter);
    }

*/

      @Override
    public List<AniListVO> getAniListSortedBynew() {
        return aniListDAO.getAniListSortedBynew();
    }

    @Override
    public List<AniListVO> getAniListSortedBytitle() {
        return aniListDAO.getAniListSortedBytitle();
    }

    @Override
    public List<AniListVO> getAniListSortedBypopular() {
        return aniListDAO.getAniListSortedBypopular();
    }

     @Override
    public List<AniListVO> getSimilarAnimes(int ani_idx, int anitype) {
        return aniListDAO.getSimilarAnimes(ani_idx, anitype);
    }


        @Override
    public List<AniListVO> getSimilarAnis(int ani_idx) {
        return aniListDAO.getSimilarAnis(ani_idx); // DAO에서 유사 애니메이션 목록을 가져오는 로직
    }

    @Override
    public List<AniListVO> fetchEvents() {
        return aniListDAO.fetchEvents();
    }

    @Override
    public List<AniListVO> fetchEvent_view(int idx) {
        System.out.println("서비스Impl");
        return aniListDAO.fetchEvent_view(idx);
    }


        @Override
    public boolean checkLikeStatus(int ani_idx, int useridx) {
        return aniListDAO.checkLikeStatus(ani_idx, useridx);

    }

    @Override
    public Integer getUserRating(int ani_idx, int useridx) {
        return aniListDAO.getUserRating(ani_idx, useridx);
    }

   @Override
    public List<AniListVO> getNewEvent() {
        return aniListDAO.getNewEvent(); // DAO에서 최근 이벤트를 가져오기
    }
}
