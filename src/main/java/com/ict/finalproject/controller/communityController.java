package com.ict.finalproject.controller;

import com.ict.finalproject.DTO.ReportDTO;
import com.ict.finalproject.JWT.JWTUtil;
import com.ict.finalproject.Service.CommentService;
import com.ict.finalproject.Service.CommuService;
import com.ict.finalproject.Service.MemberService;
import com.ict.finalproject.vo.CommentVO;
import com.ict.finalproject.vo.CommuVO;
import com.ict.finalproject.vo.MemberVO;
import com.ict.finalproject.vo.PagingVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.Comment;
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

    @Autowired
    private CommentService commentService;

//    ModelAndView mav = null;

    // 커뮤니티 리스트 페이지
    @GetMapping("/cmList")
    public String cmList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "commtype", defaultValue = "all") String commtype,
            @RequestParam(value = "orderBy", defaultValue = "DEFAULT") String orderBy,
            @RequestParam(value = "searchCategory", defaultValue = "TITLE_AND_CONTENT") String searchCategory,
            @RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword,
            Model model) {

        System.out.println("페이지 요청 - page: " + page + ", size: " + size + ", commtype: " + commtype +
                ", orderBy: " + orderBy + ", searchCategory: " + searchCategory + ", searchKeyword: " + searchKeyword);



        // PagingVO 객체를 생성하여 필요한 값 설정
        PagingVO pagingVO = new PagingVO(page, 0, size);
        pagingVO.setForCommunity(commtype, orderBy, searchCategory, searchKeyword);

        // 전체 항목 수를 조회하면서 PagingVO를 사용
        int totalElements = commuService.getTotalCount(pagingVO);
        pagingVO.setTotalElements(totalElements);

        // totalElements를 이용해 다시 PagingVO 초기화
        pagingVO = new PagingVO(page, totalElements, size);
        pagingVO.setForCommunity(commtype, orderBy, searchCategory, searchKeyword);

        // 페이징 조건에 맞는 커뮤니티 목록 조회
        List<CommuVO> list = commuService.getCommuList(pagingVO);

        // 모델에 데이터를 추가하여 JSP로 전달
        model.addAttribute("list", list);
        model.addAttribute("pagingVO", pagingVO);
        model.addAttribute("commtype", commtype);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("searchCategory", searchCategory);
        model.addAttribute("searchKeyword", searchKeyword);

        return "community/cmList";
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

        // 댓글 목록 조회
        //List<CommentVO> comments = commentService.getComment(idx);
        // 댓글 목록 조회 (comm_idx 사용)
        List<CommentVO> comments = commentService.getComment(Detail.getComm_idx()); // comm_idx를 사용하여 댓글 목록 가져오기

        // 모델에 게시글 세부 정보 및 이전/다음 게시글 정보 추가
        model.addAttribute("vo", Detail);         // 현재 게시글 세부 정보
        model.addAttribute("comments", comments);   // 댓글 목록
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
    public MemberVO getuser(@RequestParam("Authorization")String token){
        MemberVO vo = new MemberVO();

        String userid = jwtUtil.getUserIdFromToken(token); //토큰에서 사용자 아이디 추출
        int useridx = mservice.getUseridx(userid);//유저에서 사용자 아이디 추출
        vo.setUserid(userid);
        vo.setIdx(useridx);


        System.out.println("useridx" + useridx);
        System.out.println("userid : " + userid);
        return vo;
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

    
    //게시글 또는 댓글 신고
    @PostMapping("/submitReport")
    public ResponseEntity<String> submitReport(@RequestBody ReportDTO reportDTO) {
        System.out.println("Received Report Data: " + reportDTO);
        try {
            int reportExists = 0;

            //댓글 확인
            reportExists = commuService.checkCommentReportExists(reportDTO);
            System.out.println("댓글 : " +reportExists);
            // 0인데 getComment_idx가 null이 아니면 신규 댓글이라 판단
            if(reportExists == 0 && reportDTO.getComment_idx() != null){
                commuService.insertReport(reportDTO);
                return ResponseEntity.ok("신고가 접수되었습니다.");
            }else if (reportExists > 0){    // 1이면 신고된댓글
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 신고된 댓글입니다.");
            }
            // 위에서 댓글 다 보냈으니 글 신고 된건지 확인
            reportExists = commuService.checkPostReportExists(reportDTO);
            if(reportExists > 0){   // 1이면 신고된 게시물
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 신고된 게시물입니다.");
            }else{  // 아니면 신규 게시물신고
                commuService.insertReport(reportDTO);
                return ResponseEntity.ok("신고가 접수되었습니다.");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("신고 처리 중 오류가 발생했습니다.");
        }
    }


}
