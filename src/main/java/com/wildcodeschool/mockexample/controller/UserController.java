package com.wildcodeschool.mockexample.controller;

import com.wildcodeschool.mockexample.entity.User;
import com.wildcodeschool.mockexample.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    private UserManager userManager;

    @GetMapping("/create")
    @ResponseBody
    public User create() throws InstantiationException {
        String email = "bastien";
        String password = "tacostacos";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        return userManager.signUp(user);
    }
}
