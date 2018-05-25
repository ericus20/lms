package com.developersboard.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Eric on 5/24/2018.
 *
 * @author Eric Opoku
 */
@Controller
public class IndexController {


    @GetMapping(path = "/")
    public String root() {
        return "redirect:/lms";
    }

    @GetMapping(path = "/lms")
    public String index() {
        return "index";
    }

    @GetMapping(path = "/login")
    public String login() {
        return "user/login";
    }



}
