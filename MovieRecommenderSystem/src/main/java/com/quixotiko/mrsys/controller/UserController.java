package com.quixotiko.mrsys.controller;

import com.quixotiko.mrsys.domain.Rating;
import com.quixotiko.mrsys.domain.User;
import com.quixotiko.mrsys.service.RatingService;
import com.quixotiko.mrsys.service.UserService;
import com.quixotiko.mrsys.utils.Result;
import jakarta.annotation.Resource;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.*;

import java.util.ArrayList;
import java.util.List;


@Controller
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private UserService userService;
    @Resource
    private RatingService ratingService;

    @PostMapping("/user/login")
    public String loginController(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes, Model model){

        User user = userService.loginService(username, password);
        if(user!=null){
            List<Rating> ratings = ratingService.getRatingsService();
            List<Integer> movieids = new ArrayList<>();
            for (Rating r : ratings) {
                movieids.add(r.getId().getMovieid());
            }
            logger.info("Received movieids: {}"+ movieids.toString());
            model.addAttribute("user", user);
            model.addAttribute("movieIds", movieids);
            return "/recommendpage";
        }else{
            return Result.error("123","账号或密码错误！").toString();
        }
    }

    @PostMapping("/user/register")
    public String registController(@RequestParam String username, @RequestParam String password, Model model){
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        User user = userService.registerService(newUser);
        if(user!=null){
//            return Result.success(user,"注册成功！");
            return "recommendpage";
        }else{
            return Result.error("456","用户名已存在！").toString();
        }
    }
    @GetMapping("/recommendpage")
    public String recommendPage(Model model) {
        // 你的处理逻辑
        return "recommendpage";
    }

}
