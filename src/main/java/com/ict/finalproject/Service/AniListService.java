package com.ict.finalproject.Service;

import com.ict.finalproject.vo.AniListVO;

import java.util.List;

public interface AniListService {
    List<AniListVO> getAniList(); // 전체 애니메이션 목록 조회

    List<AniListVO> getAniListSelect(); // 애니메이션 선택 조회

    AniListVO getAniDetailView(String title, int idx); // 특정 애니메이션 상세 조회

    void incrementAniHit(String title); // 조회수 증가

    List<AniListVO> getAniListWithPagination(int offset, int limit); // 페이지네이션을 위한 애니메이션 목록 조회

    List<AniListVO> getAniListByGenre(int anitype); // 특정 장르의 애니메이션 목록 조회 추가

    // List<AniListVO> getSortedAniList(String sortCriteria); // 정렬된 애니메이션 목록 조회

    void addGrade(int aniIdx, Double grade, int userIdx); // 별점


    String getOutline(String title); // 특정 애니메이션의 줄거리 조회
    List<AniListVO> getRandomSimilarAnis(String genreType);

    Double getAverageGrade(int ani_idx); // 애니메이션 ID를 입력받아 평균 평점을 반환
    Integer getUseridx(String userid);



    void aniLike(int ani_idx, int useridx); // 좋아요 추가
    void removeLike(int ani_idx, int useridx); // 좋아요 제거
    boolean checkIfLiked(int ani_idx, int useridx); // 특정 애니메이션에 대해 좋아요를 눌렀는지 확인




    List<AniListVO> getAniListfilter(String filter);

    List<AniListVO> getAniListSortedBynew();
    List<AniListVO> getAniListSortedBytitle();
    List<AniListVO> getAniListSortedBypopular();


    List<AniListVO> getSimilarAnimes(int ani_idx, int anitype); // 비슷한 애니메이션 목록 조회

    List<AniListVO> getSimilarAnis(int ani_idx); // 이것도 비슷한 이미지끌고 오는거

    List<AniListVO> fetchEvents();

    List<AniListVO> fetchEvent_view(int aniId);

    boolean checkLikeStatus(int ani_idx, int useridx);


}
