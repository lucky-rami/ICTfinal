package com.ict.finalproject.controller;

import com.ict.finalproject.Service.AniListService;
import com.ict.finalproject.vo.AniListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class aniListController {

    @Autowired
    private AniListService aniListService;

    // 애니메이션 목록 조회
    @GetMapping("/aniList")
    public String getAniList(Model model) {
        List<AniListVO> aniList = aniListService.getAniList();
        model.addAttribute("aniList", aniList);
        return "ani/aniList"; // JSP 파일 이름 (예: aniList.jsp)
    }

    // 애니메이션 선택 조회
    @GetMapping("/aniListSelect")
    public String getAniListSelect(Model model) {
        List<AniListVO> aniListSelect = aniListService.getAniListSelect();
        model.addAttribute("aniListSelect", aniListSelect);
        return "aniListSelect"; // JSP 파일 이름
    }

    // 특정 애니메이션 상세 조회
    @GetMapping("/aniDetail")
    public String getAniDetailView(String title, Model model) {
        //AniListVO aniDetail = aniListService.getAniDetailView(title);
        aniListService.incrementAniHit(title); // 조회수 증가
        //model.addAttribute("aniDetail", aniDetail);
        return "aniDetail"; // JSP 파일 이름
    }

    @GetMapping("/aniList1")
    public String getAniListWithPagination(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10; // 한 페이지에 보여줄 데이터 수
        int offset = (page - 1) * pageSize; // 오프셋 계산

        List<AniListVO> aniList = aniListService.getAniListWithPagination(offset, pageSize);
        model.addAttribute("aniList", aniList);
        model.addAttribute("currentPage", page);
        return "aniList"; // JSP 파일 이름
    }
    @GetMapping("/your-api-endpoint")
    @ResponseBody // JSON 형식으로 반환
    public List<AniListVO> getAnimeList(@RequestParam int page) {
        int pageSize = 2; // 페이지당 데이터 수
        int offset = (page - 1) * pageSize; // 오프셋 계산
        return aniListService.getAniListWithPagination(offset, pageSize); // 페이지네이션으로 애니메이션 목록 반환
    }
}
