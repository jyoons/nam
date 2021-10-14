package com.namandnam.nni.fo.main.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/")
    public String first(HttpServletRequest request, Model model) throws Exception {

    	log.info(">> main test");
    	//return "redirect:/board/list";
        return "redirect:/main";
    }
    
    
    @GetMapping("/main")
    public String main(HttpServletRequest request, Model model) throws Exception {

    	
//    	log.info(">> pebble test");S
    	
    	return "fo/main/main";
    }
}
