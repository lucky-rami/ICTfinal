package com.ict.finalproject.DAO;

import com.ict.finalproject.vo.AniListVO;
import com.ict.finalproject.vo.StoreVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HomeDAO {
    List<StoreVO> getGoodsList();
    List<AniListVO> aniSearchAll(String searchKey);
    List<StoreVO> goodsSearchAll(String searchKey);
    List<String> getAniPoster(int num);
}
