package org.daou.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by user on 2016-12-07.
 */
@Controller
@RequestMapping("/")
public class WelcomeController {

    /**
     * 초기 메인페이지를 리턴하는 Controller
     *
     * @return the string
     */
    @RequestMapping(method = RequestMethod.GET)
    public String welcome(){
        return "main";
    }
}
