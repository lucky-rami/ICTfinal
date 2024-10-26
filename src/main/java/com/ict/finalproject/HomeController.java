package com.ict.finalproject;

import com.ict.finalproject.Service.HomeService;
import com.ict.finalproject.Service.OrderService;
import com.ict.finalproject.vo.AniListVO;
import com.ict.finalproject.vo.StoreVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
public class HomeController {
    @Autowired
    HomeService service;

    ModelAndView mav = null;
    @GetMapping("/")
    public ModelAndView Home() {
        // 홈
        // 1. 좋아요 많은 애니 포스터 28개
        List<String> ani_poster1 = service.getAniPoster(0);
        List<String> ani_poster2 = service.getAniPoster(14);

        // 2. 굿즈 5개 -> 최신순 (굿즈idx, 썸네일이미지, 굿즈명)
        List<StoreVO> goods_list= service.getGoodsList();

        mav = new ModelAndView();

        mav.addObject("ani_poster1", ani_poster1);
        mav.addObject("ani_poster2", ani_poster2);
        mav.addObject("goods_list", goods_list);
        mav.setViewName("home");

        return mav;
    }
    @GetMapping("/api/protected-endpoint")
    public ResponseEntity<String> protectedEndpoint(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization 헤더 값: " + authorizationHeader);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization 헤더가 없습니다.");
        }

        return ResponseEntity.ok("Authorization 헤더가 정상적으로 전달되었습니다.");
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam("search_key") String searchKey) {
        //애니 리스트
        List<AniListVO> aniSearch = service.aniSearchAll(searchKey);
        //굿즈 리스트
        List<StoreVO> goodsSearch = service.goodsSearchAll(searchKey);

        int aniSearchCount = aniSearch.size();
        int goodsSearchCount = goodsSearch.size();
        int totalSearchCount = aniSearchCount + goodsSearchCount;
        log.info("totalSearchCount : {}",totalSearchCount);

        mav = new ModelAndView();
        mav.addObject("aniSearch",aniSearch);
        mav.addObject("goodsSearch",goodsSearch);
        mav.addObject("aniSearchCount", aniSearchCount); // 애니 리스트의 갯수
        mav.addObject("goodsSearchCount", goodsSearchCount);
        mav.addObject("totalSearchCount", totalSearchCount);// 굿즈 리스트의 갯수
        mav.addObject("searchKey", searchKey);
        mav.setViewName("search_all");

        return mav;
    }
}
