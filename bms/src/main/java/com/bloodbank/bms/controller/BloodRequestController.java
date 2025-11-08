package com.bloodbank.bms.controller;

import com.bloodbank.bms.entity.BloodRequest;
import com.bloodbank.bms.service.BloodRequestService;
import com.bloodbank.bms.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/recipient")
public class BloodRequestController {

    @Autowired
    private BloodRequestService bloodRequestService;
    
    @Autowired
    private EmailService emailService;

    @GetMapping("/request")
    public String showRequestForm(Model model) {
        model.addAttribute("bloodRequest", new BloodRequest());
        model.addAttribute("title", "Blood Request - Blood Bank");
        return "blood-request";
    }

    @PostMapping("/request")
    public String submitRequest(@ModelAttribute BloodRequest bloodRequest, 
                               RedirectAttributes redirectAttributes) {
        try {
            bloodRequest.setStatus("PENDING");
            BloodRequest savedRequest = bloodRequestService.save(bloodRequest);
            
            // Send confirmation email if email provided
            if (savedRequest.getContactEmail() != null && !savedRequest.getContactEmail().isEmpty()) {
                emailService.sendRequestConfirmationEmail(
                    savedRequest.getContactEmail(), 
                    savedRequest.getPatientName(), 
                    savedRequest.getBloodGroup().getDisplayName()
                );
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Blood request submitted successfully! Confirmation email sent to " + savedRequest.getContactEmail() + ". We will contact you soon.");
            } else {
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Blood request submitted successfully! We will contact you at " + savedRequest.getContactPhone() + " soon.");
            }
            
            return "redirect:/recipient/request?success";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Request submission failed: " + e.getMessage());
            return "redirect:/recipient/request?error";
        }
    }

    @GetMapping("/requests")
    public String viewRequests(Model model) {
        List<BloodRequest> requests = bloodRequestService.findAll();
        model.addAttribute("requests", requests);
        model.addAttribute("title", "Blood Requests - Blood Bank");
        return "blood-requests-list";
    }

    @GetMapping("/pending")
    public String viewPendingRequests(Model model) {
        List<BloodRequest> requests = bloodRequestService.findPendingRequests();
        model.addAttribute("requests", requests);
        model.addAttribute("title", "Pending Blood Requests - Blood Bank");
        return "blood-requests-list";
    }

    // ✅ NEW: Request Status Tracking
    @GetMapping("/track")
    public String trackRequestForm() {
        return "request-track";
    }

    @PostMapping("/track")
    public String trackRequest(@RequestParam String phoneNumber, 
                             Model model) {
        List<BloodRequest> requests = bloodRequestService.findAll();
        BloodRequest foundRequest = null;
        
        for (BloodRequest request : requests) {
            if (request.getContactPhone().equals(phoneNumber)) {
                foundRequest = request;
                break;
            }
        }
        
        if (foundRequest != null) {
            model.addAttribute("request", foundRequest);
            model.addAttribute("title", "Request Status - Blood Bank");
            return "request-status";
        } else {
            model.addAttribute("errorMessage", "No request found with phone number: " + phoneNumber);
            return "request-track";
        }
    }

    // ✅ NEW: View Request Details
    @GetMapping("/request/{id}")
    public String viewRequestDetails(@PathVariable Long id, Model model) {
        Optional<BloodRequest> request = bloodRequestService.findById(id);
        if (request.isPresent()) {
            model.addAttribute("request", request.get());
            model.addAttribute("title", "Request Details - Blood Bank");
            return "request-details";
        } else {
            model.addAttribute("errorMessage", "Request not found");
            return "redirect:/recipient/requests";
        }
    }
}