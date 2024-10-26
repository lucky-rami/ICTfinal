package com.ict.finalproject.Service;

import com.ict.finalproject.vo.AniListVO;
import com.ict.finalproject.vo.StoreVO;

import java.util.List;

public interface HomeService {
    List<StoreVO> getGoodsList();
    List<AniListVO> aniSearchAll(String searchKey);
    List<StoreVO> goodsSearchAll(String searchKey);
    List<String> getAniPoster(int num);
}
