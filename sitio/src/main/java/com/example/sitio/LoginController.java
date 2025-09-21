package com.example.sitio;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "password2";

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, 
                              @RequestParam String password, 
                              Model model) {
        
        if (VALID_USERNAME.equals(username) && VALID_PASSWORD.equals(password)) {
            model.addAttribute("message", "¡Login exitoso! Bienvenido " + username);
            return "success";
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }
}