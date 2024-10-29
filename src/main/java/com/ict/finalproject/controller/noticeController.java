package com.ict.finalproject.controller;


import com.ict.finalproject.JWT.JWTUtil;
import com.ict.finalproject.Service.MemberService;
import com.ict.finalproject.Service.NoticeService;
import com.ict.finalproject.vo.NoticeVO;
import com.ict.finalproject.vo.PagingVO;
import com.ict.finalproject.vo.QnaVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

@Controller
@Slf4j
public class noticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    MemberService mservice;

    @Autowired
    JWTUtil jwtUtil;

    @GetMapping("/notice2")
    public String getNoticeList(@RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "") String keyword,
                                @RequestHeader(value = "Authorization", required = false) String token,
                                Model model) {

        // 로그인된 사용자의 아이디를 추가하는 부분
        if (token != null) {
            token = token.replace("Bearer ", ""); // "Bearer " 제거
            String userId = jwtUtil.getUserIdFromToken(token); // 토큰에서 사용자 아이디 추출
            model.addAttribute("userid", userId); // 아이디를 Model에 추가
        }

        // 공지사항 페이징 처리
        PagingVO pVO = new PagingVO(page, 0, size);
        pVO.setForNotice(keyword);

        // 전체 항목 수 가져오기
        int totalElements = noticeService.getTotalCount(pVO);
        pVO.setTotalElements(totalElements);

        // 페이징 정보 재설정
        pVO = new PagingVO(page, totalElements, size);
        pVO.setForNotice(keyword);

        //페이징 조건에 맞는 공지사항 목록 조회
        List<NoticeVO> list = noticeService.getNotices(pVO);

        // FAQ 목록 가져오기
        List<NoticeVO> faqList = noticeService.getFaqs();


        // 모델에 데이터 추가
        model.addAttribute("list", list);
        model.addAttribute("pVO", pVO);  // 공지사항 페이징 정보
        model.addAttribute("keyword", keyword);
        model.addAttribute("faqs", faqList);

        return "notice/notice2";  // 고객센터 페이지로 이동
    }


    // 1:1 문의 등록
    @PostMapping("/inquirySubmit")
    public ResponseEntity<String> inquirySubmit(@RequestParam("title") String title,
                                                @RequestParam("content") String content,
                                                @RequestParam("qnatype") int qnatype,
                                                @RequestParam(value = "file", required = false) List<MultipartFile> files,
                                                @RequestHeader("Authorization") String token) {
        System.out.println("여긴오니?");
        System.out.println("1" + title + "2" + content + "3" + qnatype + "4" + files + "5" + token);


        // 토큰에서 "Bearer " 제거
        token = token.replace("Bearer ", "");

        //토큰에서 사용자 아이디 추출
        String userid = jwtUtil.getUserIdFromToken(token);
        System.out.println("id : " +userid);

        // userid로 index구하기
        int useridx= mservice.getUseridx(userid);

        String imgfile1 = null;
        String uniqueFilename = "";

        try {
            // 파일이 있는 경우에만 처리
            if (files != null && !files.isEmpty()) {
                for (int i = 0; i < files.size(); i++) {
                    MultipartFile file = files.get(i);
                    if (!file.isEmpty()) {
                        uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

                        // 이미지 서버로 파일 전송
                        uploadFileToImageServer(file, uniqueFilename);

                        if (i == 0) {
                            imgfile1 = uniqueFilename;
                        }
                    }
                }
            }

            // QnaVO 객체 생성 및 설정
            QnaVO qna = new QnaVO();
            qna.setTitle(title);
            qna.setContent(content);
            qna.setQnatype(qnatype);
            qna.setUseridx(useridx);
            qna.setImgfile1(imgfile1);


            // QNA 데이터 저장
            int result = noticeService.saveQna(qna);

            if (result > 0) {
                return ResponseEntity.ok("1:1 문의가 성공적으로 등록되었습니다.");
            } else {
                fileDel(imgfile1);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("문의 등록 실패");
            }
        } catch (Exception e) {
            fileDel(imgfile1);
            log.error("1:1 문의 등록 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("1:1 문의 등록 중 오류 발생");
        }
    }

    // 이미지 서버로 파일을 전송하는 메소드
    private void uploadFileToImageServer(MultipartFile file, String uniqueFilename) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String imageServerUrl = "http://192.168.1.180:8000/upload";

        // 파일을 MultiValueMap으로 준비
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), uniqueFilename));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 이미지 서버로 파일 전송
        restTemplate.postForEntity(imageServerUrl, requestEntity, String.class);
    }

    // MultipartFile을 InputStream 리소스로 변환하는 클래스
    class MultipartInputStreamFileResource extends InputStreamResource {
        private final String filename;

        public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public long contentLength() throws IOException {
            return -1;
        }
    }

    // 파일 삭제
    private void fileDel(String filename) {
        RestTemplate restTemplate = new RestTemplate();
        String deleteUrl = "http://192.168.1.180:8000/delete/" + filename;

        try {
            restTemplate.delete(deleteUrl);
            System.out.println("File deleted successfully: " + filename);
        } catch (RestClientException e) {
            System.err.println("Failed to delete file: " + filename + ". Error: " + e.getMessage());
        }
    }

    @PostMapping("/notice2/tap2")
    @ResponseBody
    public Map<String, Object> tap2(@RequestParam("type")String type){
        List<NoticeVO>list = new ArrayList<>();

        System.out.println("완요?");
        System.out.println(type);
        Map<String, Object> map = new HashMap<>();

        if(type == ""){
            list = noticeService.getFaqs(); //전체
        }else{
            list = noticeService.getSearchFas(type);
        }
        System.out.println(list);
        map.put("list",list);
        return map;
    }

    //문의사항 페이지로 이동(채원)
    @GetMapping("/qnaWirte")
    public ModelAndView qnaWirte(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("notice/notice2");

        return mav;
    }

}
