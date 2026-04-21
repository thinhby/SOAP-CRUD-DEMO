package soap.crud.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import soap.crud.demo.services.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {

        try {
            userService.register(username, password);
            return "redirect:/home";
        } catch (Exception e) {
            return "redirect:/register?error";
        }
    }
}
