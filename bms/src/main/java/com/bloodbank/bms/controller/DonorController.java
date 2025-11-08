package com.bloodbank.bms.controller;

import com.bloodbank.bms.entity.DonationRecord;
import com.bloodbank.bms.entity.Donor;
import com.bloodbank.bms.service.DonationRecordService;
import com.bloodbank.bms.service.DonorService;
import com.bloodbank.bms.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/donor")
public class DonorController {

    @Autowired
    private DonorService donorService;
    
    @Autowired
    private DonationRecordService donationRecordService;
    
    @Autowired
    private EmailService emailService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("donor", new Donor());
        model.addAttribute("title", "Donor Registration - Blood Bank");
        return "donor-registration";
    }

    @PostMapping("/register")
    public String registerDonor(@ModelAttribute Donor donor, 
                               RedirectAttributes redirectAttributes) {
        try {
            donor.setAvailable(true);
            Donor savedDonor = donorService.save(donor);
            
            // Send welcome email to ACTUAL donor email
            if (savedDonor.getEmail() != null && !savedDonor.getEmail().isEmpty()) {
                emailService.sendDonorWelcomeEmail(savedDonor.getEmail(), savedDonor.getFullName());
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Registration successful! Welcome email sent to " + savedDonor.getEmail() + ". Thank you for registering as a blood donor.");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Registration successful! Thank you for registering as a blood donor.");
            }
            
            return "redirect:/donor/register?success";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Registration failed: " + e.getMessage());
            return "redirect:/donor/register?error";
        }
    }

    @GetMapping("/list")
    public String listDonors(Model model) {
        List<Donor> donors = donorService.findAll();
        model.addAttribute("donors", donors);
        model.addAttribute("title", "Donor List - Blood Bank");
        return "donor-list";
    }

    @GetMapping("/available")
    public String availableDonors(Model model) {
        List<Donor> donors = donorService.findAvailableDonors();
        model.addAttribute("donors", donors);
        model.addAttribute("title", "Available Donors - Blood Bank");
        return "donor-list";
    }

    // Donor Profile Page
    @GetMapping("/profile/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        Optional<Donor> donor = donorService.findById(id);
        if (donor.isPresent()) {
            List<DonationRecord> donationHistory = donationRecordService.findByDonor(donor.get());
            
            model.addAttribute("donor", donor.get());
            model.addAttribute("donationHistory", donationHistory);
            model.addAttribute("title", "Donor Profile - Blood Bank");
            return "donor-profile";
        } else {
            model.addAttribute("errorMessage", "Donor not found");
            return "redirect:/donor/list";
        }
    }

    // Edit Donor Profile Form
    @GetMapping("/edit/{id}")
    public String editProfileForm(@PathVariable Long id, Model model) {
        Optional<Donor> donor = donorService.findById(id);
        if (donor.isPresent()) {
            model.addAttribute("donor", donor.get());
            model.addAttribute("title", "Edit Donor Profile - Blood Bank");
            return "donor-edit";
        } else {
            model.addAttribute("errorMessage", "Donor not found");
            return "redirect:/donor/list";
        }
    }

    // Update Donor Profile
    @PostMapping("/update/{id}")
    public String updateProfile(@PathVariable Long id, 
                               @ModelAttribute Donor donorDetails,
                               RedirectAttributes redirectAttributes) {
        try {
            Optional<Donor> existingDonor = donorService.findById(id);
            if (existingDonor.isPresent()) {
                Donor donor = existingDonor.get();
                
                // Update fields
                donor.setFullName(donorDetails.getFullName());
                donor.setPhoneNumber(donorDetails.getPhoneNumber());
                donor.setDateOfBirth(donorDetails.getDateOfBirth());
                donor.setGender(donorDetails.getGender());
                donor.setAddress(donorDetails.getAddress());
                donor.setCity(donorDetails.getCity());
                donor.setState(donorDetails.getState());
                donor.setPincode(donorDetails.getPincode());
                donor.setHealthInfo(donorDetails.getHealthInfo());
                donor.setAvailable(donorDetails.isAvailable());
                
                donorService.save(donor);
                
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Profile updated successfully!");
                return "redirect:/donor/profile/" + id;
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to update profile: " + e.getMessage());
        }
        return "redirect:/donor/edit/" + id;
    }

    // Record new donation
    @PostMapping("/donate/{id}")
    public String recordDonation(@PathVariable Long id,
                                @RequestParam Integer bloodVolumeML,
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<Donor> donor = donorService.findById(id);
            if (donor.isPresent()) {
                donationRecordService.recordNewDonation(donor.get(), bloodVolumeML);
                
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Donation recorded successfully! Thank you email sent.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to record donation: " + e.getMessage());
        }
        return "redirect:/donor/profile/" + id;
    }
}