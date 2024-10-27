package com.ict.finalproject.DAO;

import com.ict.finalproject.DTO.BasketDTO;
import com.ict.finalproject.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StoreDAO {
    //최근 상품 목록 가져오기
    public List<StoreVO> getRecentProducts();
    //전체 상품 목록 가져오기
    public List<StoreVO> getStoreList();
    //총 상품 목록 가져오기
    public int getTotalProductCount();
    //페이지네이션 처리를 위한 상품목록 가져오기
    public List<StoreVO> getPagedProducts(@Param("pageSize") int pageSize, @Param("offset") int offset, @Param("category") Integer category,@Param("second_category") Integer second_category); //페이지네이션
    public int getPagedProductsCnt(@Param("category") Integer category,@Param("second_category") Integer second_category);
    // 필터 타입에 따른 상품 리스트 가져오기 (필터 타입: 최신순, 인기순 등)
    public List<StoreVO> getStoreListByFilter(@Param("filterType")String filterType);


    // 검색어에 따른 상품 검색
    public List<StoreVO> searchStoreList(@Param("query") String query);
    // 첫 번째 카테고리 목록 가져오기
    public List<ProductFilterVO> getFirstCategoryList();  // 이 부분 추가
    // 상품 상세 정보 가져오기
    public StoreVO getStoreDetail(int storeId);

    // 해당 상세상품의 숨은이미지 리스트 가져오기
    public List<String> getImagesByProductId(int productId);

    // 카테고리 코드에 따른 하위 카테고리 목록 가져오기
    public List<String> getSubcategoriesByFirstCategory(@Param("code") int code);
    // 추가된 부분: 카테고리별 상품 목록 가져오기
    public List<StoreVO> getProductsByCategory(@Param("pageSize") int pageSize, @Param("offset") int offset, @Param("category") int category);

    public String getCategoryType(int categoryCode);
    //별점 가져오기
    Double getAverageRating(int productId);

    //리뷰 총 개수 가져오기
    int getReviewCount(int productId);

    //리뷰 리스트 가져오기
    List<ReviewVO> getReviewsByProductId(int productId);  // 특정 상품의 리뷰 목록 가져오기


    //채원
    //장바구니에 상품 있는지 체크
    int checkProductInBasket(BasketVO basketvo);
    //장바구니 테이블에 상품데이터 저장
    int basketInput(BasketVO basketvo);
    //장바구니 리스트
    List<BasketDTO> basketList(int useridx);
    //장바구니 상품 삭제(x버튼)
    int basketDelete(int idx,int useridx);
    //장바구니 상품 삭제(선택,전체상품삭제)
    void basketChoiceAndAllDelOk(int idx, int useridx);
    //장바구니 상품갯수 +
    int basketPlusAmount(int idx,int useridx, int newTotal);
    //장바구니 상품갯수 -
    int basketMinusAmount(int idx,int useridx, int newTotal);
    //신고당한 데이터
    ReviewVO getReportedData(int review_idx);
    // 신고접수 -> 이미 신고한 글인지 확인
    int checkReportExists(ReportVO reportVO);
    // 리뷰 신고 접수
    int reportInput(ReportVO reportVO);

}
