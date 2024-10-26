package com.ict.finalproject.Service;

import com.ict.finalproject.DAO.HomeDAO;
import com.ict.finalproject.vo.AniListVO;
import com.ict.finalproject.vo.StoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService{
    @Autowired
    HomeDAO dao;

    @Override
    public List<StoreVO> getGoodsList() {
        return dao.getGoodsList();
    }

    @Override
    public List<AniListVO> aniSearchAll(String searchKey) {
        return dao.aniSearchAll(searchKey);
    }

    @Override
    public List<StoreVO> goodsSearchAll(String searchKey) {
        return dao.goodsSearchAll(searchKey);
    }

    @Override
    public List<String> getAniPoster(int num) {
        return dao.getAniPoster(num);
    }
}
