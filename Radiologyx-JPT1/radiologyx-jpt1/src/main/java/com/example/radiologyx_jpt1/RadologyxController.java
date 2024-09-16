package com.example.radiologyx_jpt1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RadologyxController {
    @GetMapping()
    public String demo (){
        return "index";
    }

   @GetMapping("/login")
    public String login (){
        return "login";
   }

   @Autowired
   private UserService userService;

   @GetMapping("/register")
    public String register(Model model){
       model.addAttribute("user", new UserDTO());
       return "register";
   }

   @PostMapping("/register")
    public String reigsterUser(@ModelAttribute("user") UserDTO userDTO, Model model) {
       try {
           userService.registerNewUser(userDTO);
           model.addAttribute("successMessage", "Registrierung war erfolgreich");
           return "login";
       }
       catch (Exception e){
           model.addAttribute("errorMessage", e.getMessage());
           return "register";
       }
   }



}
