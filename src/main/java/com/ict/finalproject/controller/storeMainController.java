package com.ict.finalproject.controller;

import com.ict.finalproject.Service.StoreService;
import com.ict.finalproject.vo.ProductFilterVO;
import com.ict.finalproject.vo.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;

import java.util.List;


@Controller

public class storeMainController {

    @Autowired
    StoreService storeService;
    
    
    @GetMapping("/storeMain")
 public String storeMain() {
     return "store/storeMain";
 }


    @GetMapping("/storeList")
    public ModelAndView getStoreListAndView(){
        System.out.println(11);
        List<StoreVO> storeList = storeService.getStoreList();
        System.out.println(storeList);
        ModelAndView mav = new ModelAndView();
        mav.addObject("storeList", storeList);
        mav.setViewName("store/storeList");
        return mav;
    }

    @GetMapping("/storeListAndView")
    public ModelAndView getStoreList() {
        // 데이터베이스에서 StoreVO 리스트를 가져오기
        List<StoreVO> storeList = storeService.getStoreList();

        // ModelAndView 객체 생성
        ModelAndView mav = new ModelAndView();

        // 모델에 데이터를 추가
        mav.addObject("storeList", storeList);

        // 반환할 JSP 경로 설정
        mav.setViewName("store/storeList");  // JSP 파일 경로

        return mav;
    }

    // 검색된 상품 목록 가져오기
    @GetMapping("/searchStoreList")
    public ModelAndView searchStoreList(@RequestParam("keyword") String keyword) {
        List<StoreVO> searchResults = storeService.searchStoreList(keyword);
        ModelAndView mav = new ModelAndView();
        mav.addObject("storeList", searchResults);
        mav.setViewName("store/storeList");
        return mav;
    }

    @PostMapping("/filterStoreList")
@ResponseBody
public List<ProductFilterVO> filterStoreList(@RequestBody ProductFilterVO filterCriteria) {
    
    // 로그 출력으로 전달된 데이터 확인
    System.out.println("Received ani_title: " + filterCriteria.getAni_title());
    System.out.println("Received category: " + filterCriteria.getCategory());
    System.out.println("Received stock: " + filterCriteria.getStock());
    
    // 필터링된 상품 리스트 가져오기
    List<ProductFilterVO> filteredStoreList = storeService.getStoreListByFilterCriteria(filterCriteria);

    return filteredStoreList;
}


//     @GetMapping("/storeDetail")
//    public String storDetail() {
//        return "store/storeDetail";
//    }

@GetMapping("/storeDetail/{idx}")
public ModelAndView getStoreDetail(@PathVariable("idx") int idx) {
    System.out.println("Received idx: " + idx); // idx 값 출력
    StoreVO storeDetail = storeService.getStoreDetail(idx); // idx로 상품 조회
    System.out.println("Store Detail: " + storeDetail); // StoreVO 내용 출력
    ModelAndView mav = new ModelAndView();
    mav.addObject("storeDetail", storeDetail);
    mav.setViewName("store/storeDetail");
    return mav;
}


    @GetMapping("/shoppingBag")
    public ModelAndView joinPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("store/shopping_bag");
        return mav;
    }

   
}