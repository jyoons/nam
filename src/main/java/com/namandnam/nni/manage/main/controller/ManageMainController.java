package com.namandnam.nni.manage.main.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping("/manage")
@RequiredArgsConstructor
public class ManageMainController {

    @GetMapping(value = {"", "/"})
    public String first(HttpServletRequest request, Model model) throws Exception {
        //return "redirect:/board/list";
        return "manage/login";
    }
    
    @GetMapping("/main")
    public String main(HttpServletRequest request, Model model) throws Exception {

        log.info(">> /manage/main");

    	return "manage/main/main";

    }
}
