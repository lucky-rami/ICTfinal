package com.ict.finalproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class storeMainController {
     @GetMapping("/storeMain")
 public String storeMain() {
     return "store/storeMain";
 }

 
    @GetMapping("/storeList")
 public String storeList(){
    return "store/storeList";
 } 
}