package com.bloodbank.bms.controller;

import com.bloodbank.bms.entity.StaffRegistration;
import com.bloodbank.bms.service.StaffRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/staff")
public class StaffRegistrationController {

    @Autowired
    private StaffRegistrationService staffRegistrationService;

    // Staff Registration Form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("staffRegistration", new StaffRegistration());
        model.addAttribute("title", "Staff Registration - Blood Bank");
        return "staff-registration";
    }

    // Submit Staff Registration
    @PostMapping("/register")
    public String registerStaff(@ModelAttribute StaffRegistration staffRegistration,
                               RedirectAttributes redirectAttributes) {
        try {
            staffRegistrationService.save(staffRegistration);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "🎉 Registration submitted successfully!\n\n" +
                "📋 Your application is under review by our admin team.\n\n" +
                "📧 You will receive an email notification once approved.\n\n" +
                "⏳ Expected review time: 24-48 hours\n\n" +
                "📞 Contact admin@bloodbank.com for queries");
            return "redirect:/staff/register?success";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "❌ Registration failed: " + e.getMessage() + "\n\n" +
                "Please check your information and try again.");
            return "redirect:/staff/register?error";
        }
    }
}