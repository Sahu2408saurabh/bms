package com.bloodbank.bms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("title", "Blood Bank Management System");
        return "index";
    }
    
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "Login - Blood Bank");
        return "login";
    }
    
    @GetMapping("/blood/availability")
    public String bloodAvailability(Model model) {
        model.addAttribute("title", "Blood Availability - Blood Bank");
        return "blood-availability";
    }
    
    @GetMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("title", "About Us - Blood Bank");
        return "about";
    }
    
    @GetMapping("/contact")
    public String contactPage(Model model) {
        model.addAttribute("title", "Contact Us - Blood Bank");
        return "contact";
    }
    
    // ADMIN DASHBOARD REMOVE KAR DIYA - Yeh AdminController mein hai
}