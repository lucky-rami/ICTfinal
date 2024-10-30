package com.ict.finalproject.DAO;

import com.ict.finalproject.vo.AniListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface AniListDAO {

    List<AniListVO> getAniList();

    List<AniListVO> getAniListSelect(); // 전체 애니메이션 목록 조회

    AniListVO getAniDetailView(@Param("title") String title); // 특정 애니메이션 상세 조회

    void incrementAniHit(String title); // 조회수 증가

    List<AniListVO> findAniListWithPagination(@Param("offset") int offset, @Param("limit") int limit); // 페이지네이션을 위한 애니메이션 목록 조회

    List<AniListVO> getMoviesByGenre(@Param("anitype") int anitype); // 특정 장르의 애니메이션 목록 조회


    // 별점 추가 메서드
    void addGrade(@Param("ani_idx") int aniIdx, @Param("grade") Double grade, @Param("useridx") int userIdx);
    int checkGrade(@Param("ani_idx") int aniIdx, @Param("useridx") int userIdx);
    int updateGrade(@Param("ani_idx") int aniIdx, @Param("grade") Double grade, @Param("useridx") int userIdx);

    // 평균 별점 조회 메서드
    double getAverageGrade(int ani_idx);


    // 줄거리 조회 메서드
    String getOutline(@Param("title") String title); // 특정 애니메이션의 줄거리 조회

    // 비슷한 작품 랜덤 구현
    List<AniListVO> getRandomSimilarAnis(String genreType);

    Integer getUseridx(String userid);

    void insertLike(@Param("ani_idx") int ani_idx, @Param("useridx") int useridx); // 좋아요 추가
    void removeLike(@Param("ani_idx") int ani_idx, @Param("useridx") int useridx); // 좋아요 제거
    int checkIfLiked(@Param("ani_idx") int ani_idx, @Param("useridx") int useridx); // 좋아요 여부 확인



   // List<AniListVO> getAniListfilter(String filter);
/*
    List<AniListVO> getSortedAniList(String sortCriteria); // 정렬된 애니메이션 목록 조회*/

    List<AniListVO> getAniListSortedBynew();
    List<AniListVO> getAniListSortedBytitle();
    List<AniListVO> getAniListSortedBypopular();

    List<AniListVO> getSimilarAnimes(@Param("ani_idx") int ani_idx, @Param("anitype") int anitype);



 List<AniListVO> getSimilarAnis(int ani_idx); // 유사한 애니메이션 목록을 가져오는 메서드 정의

 List<AniListVO> fetchEvents();

 List<AniListVO> fetchEvent_view(int idx);

 boolean checkLikeStatus(@Param("ani_idx") int ani_idx, @Param("useridx") int useridx);

 Integer getUserRating(@Param("ani_idx") int ani_idx, @Param("useridx") int useridx);//별점

   List<AniListVO> getNewEvent(); // 최근 이벤트를 가져오는 메서드
}