package com.ict.finalproject.controller;

import com.ict.finalproject.JWT.JWTUtil;
import com.ict.finalproject.Service.CommuService;
import com.ict.finalproject.Service.MemberService;
import com.ict.finalproject.vo.CommuVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.List;


//@RequestMapping("/community")
@Controller
public class communityController {
    // userid로 index구하기
    @Autowired
    MemberService mservice;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    private CommuService commuService;


//    ModelAndView mav = null;

    // 커뮤니티 리스트 페이지
    // Controller: communityController.java
    @GetMapping("/cmList")
    public String cmList(
            @RequestParam(value = "commtype", required = false, defaultValue = "all") String commtype,
            @RequestParam(value = "orderBy", required = false, defaultValue = "DEFAULT") String orderBy,
            @RequestParam(value = "searchCategory", required = false, defaultValue = "TITLE_AND_CONTENT") String searchCategory,
            @RequestParam(value = "searchKeyword", required = false, defaultValue = "") String searchKeyword,
            Model model) {

        // commtype이 null이거나 빈 문자열일 때 기본값 "all"로 설정
        if (commtype == null || commtype.trim().isEmpty()) {
            commtype = "all";
        }

        // 전체 리스트 조회 또는 특정 커뮤니티 타입으로 필터링된 리스트 조회
        List<CommuVO> list;
        if ("all".equals(commtype)) {
            list = commuService.FilteredList(null, orderBy, searchCategory, searchKeyword);
        } else {
            list = commuService.FilteredList(commtype, orderBy, searchCategory, searchKeyword);
        }

        // 검색어 입력 후 검색어를 초기화하여 JSP로 전달
        searchKeyword = "";  // 검색 후 검색어 초기화

        // JSP에 전달할 데이터 설정
        model.addAttribute("list", list);
        model.addAttribute("commtype", commtype);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("searchKeyword", searchKeyword);

        return "community/cmList";  // JSP 파일 이름 (cmList.jsp)
    }

    //상세페이지
    @GetMapping("/cmView/{idx}")
    public String cmView(@PathVariable("idx") int idx, Model model) {
        System.out.println("Received idx: " + idx); // idx 값 확인

        // idx 값을 통해 게시글 상세 정보 조회
        CommuVO Detail = commuService.Detail(idx);
        if (Detail == null) {
            return "redirect:/error"; // 게시글이 존재하지 않으면 에러 페이지로 이동
        }

        // 조회수 증가 로직 실행
        commuService.HitCount(idx);

        // 이전 게시글 및 다음 게시글 조회
        CommuVO previousPost = commuService.PreviousPost(idx);
        CommuVO nextPost = commuService.NextPost(idx);

        // 모델에 게시글 세부 정보 및 이전/다음 게시글 정보 추가
        model.addAttribute("vo", Detail);         // 현재 게시글 세부 정보
        model.addAttribute("go", previousPost);   // 이전 게시글
        model.addAttribute("tun", nextPost);      // 다음 게시글

        // cmView.jsp 페이지로 이동
        return "community/cmView";
    }

    // 수정 페이지로 이동
    @GetMapping("/cmEdit/{idx}")
    public String editPage(@PathVariable("idx") int idx, Model model) {
        // 기존 게시글 정보 조회
        CommuVO detail = commuService.Detail(idx);
        model.addAttribute("vo", detail);
        return "community/cmEdit";  // 수정 페이지로 이동 (cmEdit.jsp)
    }


    @PostMapping("/cmEditOk")
    public String updateBoard(CommuVO board) {
        boolean success = commuService.UpdateBoard(board);

        // 수정 성공 시 수정된 게시글의 상세 페이지로 리다이렉트
        if (success) {
            return "redirect:/cmView/" + board.getIdx(); // 기존 "redirect:/cmView?idx="에서 수정
        } else {
            // 실패 시 오류 메시지 출력 후, 수정 페이지로 다시 이동
            return "community/cmEdit";
        }
    }


    @GetMapping("/cmDelete/{idx}")
    public String delete(@PathVariable("idx") int idx, Model model) {
        int result = commuService.Delete(idx);
        if (result > 0) {
            return "redirect:/cmList";  // 삭제 성공 시 게시글 목록 페이지로 이동
        } else {
            model.addAttribute("errorMessage", "게시글 삭제에 실패했습니다.");
            return "community/cmView";  // 삭제 실패 시 현재 페이지로 다시 이동
        }
    }





    //로그인 여부
    @ResponseBody
    @GetMapping("/getuser")
    public String getuser(@RequestParam("Authorization")String token){
        System.out.println("hi2");
        String userid = jwtUtil.getUserIdFromToken(token); //토큰에서 사용자 아이디 추출
        System.out.println("userid : " + userid);
        return userid;
    }

    //글등록폼
    @GetMapping("/cmWrite")
    public String cmwrite(){

//        mav = new ModelAndView();
//        mav.setViewName("community/cmWrite");
        return "community/cmWrite";
    }

    //글 등록(DB)
    @PostMapping("/cmWriteOk")
    public ResponseEntity<String> writeOk(
            @RequestParam("code") String code, // communitytype 테이블의 code 필드와 매핑
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("token") String token
    ) {
        //토큰에서 사용자 아이디 추출
        String userid = jwtUtil.getUserIdFromToken(token);
        // userid로 index구하기
        int useridx= mservice.getUseridx(userid);

        // 게시글 VO 객체 생성 및 설정
        CommuVO board = new CommuVO();
        board.setUseridx(useridx); // 사용자 ID 설정
        board.setCommtype(code); // communitytype의 code 필드 설정
        board.setTitle(title); // 제목 설정
        board.setContent(content); // 내용 설정
        //참고로 이런 비즈니스 로직은 service단에서 하고 컨트롤러에서는 그냥 서비스에 전달해주는 게 권장되는 방식
        String bodyTag = "";
        try {
            CommuVO resultBoard = commuService.writeBoard(board); // 게시글 등록 서비스 호출

            if (resultBoard == null) { // 등록 실패 시
                bodyTag += "<script>alert('등록 실패');history.back();</script>";
            } else { // 등록 성공 시
                // 자바스크립트로 페이지 이동 처리
                bodyTag += "<script>alert('등록 성공'); location.href='/cmList';</script>";
            }
        } catch (Exception e) {
            e.printStackTrace();
            bodyTag += "<script>alert('등록 실패');history.back();</script>";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "html", Charset.forName("UTF-8")));

        return new ResponseEntity<>(bodyTag, headers, HttpStatus.OK);
    }





    //커뮤니티-공지사항 이동
    @GetMapping("/allnotice")
    public String allnotice(){
        return "notice/notice2";
    }
}
