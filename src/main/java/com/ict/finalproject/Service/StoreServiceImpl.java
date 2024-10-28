package com.ict.finalproject.Service;

import com.ict.finalproject.DAO.StoreDAO;
import com.ict.finalproject.DTO.BasketDTO;
import com.ict.finalproject.vo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    StoreDAO dao;


    @Override
    public List<StoreVO> getRecentProducts() {
        return dao.getRecentProducts();
    }

    @Override
    public List<StoreVO> getStoreList() {
        return dao.getStoreList();
    }

    @Override
    public List<StoreVO> getProductsByTitle(String ani_title){ return dao.getProductsByTitle(ani_title);    }

    @Override
    public List<StoreVO> getProductsByImageTitle(String ani_title){
        return dao.getProductsByImageTitle(ani_title);
    }

    @Override
    public List<StoreVO> getPagedProducts(int pageSize, int offset, Integer category,  Integer second_category) {
        return dao.getPagedProducts(pageSize, offset, category, second_category);
    }
    @Override
    public int getPagedProductsCnt(Integer category,  Integer second_category) {
        return dao.getPagedProductsCnt(category, second_category);
    }
    @Override
    public int getTotalProductCount() {
        return dao.getTotalProductCount();
    }

    @Override
    public List<StoreVO> getStoreListByFilter(String filterType) {
        // 필터 타입에 따른 상품 목록을 가져오는 로직 구현
        return dao.getStoreListByFilter(filterType);
    }

    @Override
    public List<StoreVO> searchStoreList(String query, Integer category, Integer second_category, int offset, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);
        params.put("category", category);
        params.put("second_category", second_category);
        params.put("offset", offset);
        params.put("pageSize", pageSize);
        return dao.searchStoreList(params);
    }

    @Override
    public int getSearchResultsCount(String query, Integer category, Integer second_category) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);
        params.put("category", category);
        params.put("second_category", second_category);
        return dao.getSearchResultsCount(params);
    }

    @Override
    public StoreVO getStoreDetail(int storeId) {
        return dao.getStoreDetail(storeId);
    }

    @Override
    public List<String> getImagesByProductId(int productId) {
        return dao.getImagesByProductId(productId);  // 숨겨진 이미지만 가져오기
    }


    @Override
    public List<ProductFilterVO> getFirstCategoryList() {
        return dao.getFirstCategoryList();
    }

    @Override
    public List<String> getSubcategoriesByFirstCategory(int code) {
        return dao.getSubcategoriesByFirstCategory(code);
    }

    @Override
    public List<StoreVO> getProductsByCategory(int pageSize, int offset, int category) {
        return dao.getProductsByCategory(pageSize, offset, category);
    }


    @Override
    public String getCategoryType(int categoryCode) {
        // categoryCode를 통해 카테고리 타입 조회
        return dao.getCategoryType(categoryCode);
    }


    @Override
    public Double getAverageRating(int productId) {
        return dao.getAverageRating(productId);  // DAO에서 평균 평점을 가져옴
    }


    @Override
    public int getReviewCount(int productId) {
        return dao.getReviewCount(productId);
    }

    @Override
    public List<ReviewVO> getReviewsByProductId(int productId) {
        return dao.getReviewsByProductId(productId);
    }












    //채원
    @Override
    public int checkProductInBasket(BasketVO basketvo) {
        return dao.checkProductInBasket(basketvo);
    }

    @Override
    public int basketInput(BasketVO basketvo) {
        return dao.basketInput(basketvo);
    }

    @Override
    public List<BasketDTO> basketList(int useridx) {
        return dao.basketList(useridx);
    }

    @Override
    public int basketDelete(int idx, int useridx) {
        return dao.basketDelete(idx, useridx);
    }

    @Override
    public void basketChoiceAndAllDelOk(int idx, int useridx) {
        dao.basketChoiceAndAllDelOk(idx,useridx);
    }

    @Override
    public int basketPlusAmount(int idx, int useridx, int newTotal) {
        return dao.basketPlusAmount(idx, useridx,newTotal);
    }

    @Override
    public int basketMinusAmount(int idx, int useridx, int newTotal) {
        return dao.basketMinusAmount(idx, useridx,newTotal);
    }

    @Override
    public ReviewVO getReportedData(int review_idx) {
        return dao.getReportedData(review_idx);
    }

    @Override
    public int checkReportExists(ReportVO reportVO) {
        return dao.checkReportExists(reportVO);
    }

    @Override
    public int reportInput(ReportVO reportVO) {
        return dao.reportInput(reportVO);
    }

}


