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

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

}
