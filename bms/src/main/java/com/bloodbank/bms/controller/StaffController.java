package com.bloodbank.bms.controller;

import com.bloodbank.bms.entity.BloodInventory;
import com.bloodbank.bms.entity.BloodRequest;
import com.bloodbank.bms.entity.DonationRecord;
import com.bloodbank.bms.entity.Staff;
import com.bloodbank.bms.service.BloodInventoryService;
import com.bloodbank.bms.service.BloodRequestService;
import com.bloodbank.bms.service.DonationRecordService;
import com.bloodbank.bms.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private BloodRequestService bloodRequestService;
    
    @Autowired
    private DonationRecordService donationRecordService;
    
    @Autowired
    private BloodInventoryService bloodInventoryService;
    
    @Autowired
    private StaffService staffService;

    // Staff Dashboard
    @GetMapping("/dashboard")
    public String staffDashboard(Model model) {
        long pendingRequests = bloodRequestService.countPendingRequests();
        long todayDonations = donationRecordService.getTodayDonationsCount();
        long totalBloodUnits = bloodInventoryService.findAll().stream()
                .mapToInt(BloodInventory::getAvailableUnits)
                .sum();
        
        List<BloodRequest> pendingBloodRequests = bloodRequestService.findPendingRequests();
        List<DonationRecord> recentDonations = donationRecordService.findRecentDonations();
        
        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("todayDonations", todayDonations);
        model.addAttribute("totalBloodUnits", totalBloodUnits);
        model.addAttribute("pendingBloodRequests", pendingBloodRequests);
        model.addAttribute("recentDonations", recentDonations);
        model.addAttribute("title", "Staff Dashboard - Blood Bank");
        
        return "staff-dashboard";
    }

    // Manage Blood Requests
    @GetMapping("/requests")
    public String manageRequests(Model model) {
        List<BloodRequest> requests = bloodRequestService.findAll();
        model.addAttribute("requests", requests);
        model.addAttribute("title", "Manage Blood Requests - Staff");
        return "staff-requests";
    }

    // Update Request Status
    @PostMapping("/requests/update-status")
    public String updateRequestStatus(@RequestParam Long requestId,
                                    @RequestParam String status,
                                    @RequestParam(required = false) String staffNotes,
                                    RedirectAttributes redirectAttributes) {
        try {
            Optional<BloodRequest> request = bloodRequestService.findById(requestId);
            if (request.isPresent()) {
                BloodRequest bloodRequest = request.get();
                bloodRequest.setStatus(status);
                if (staffNotes != null && !staffNotes.isEmpty()) {
                    bloodRequest.setNotes((bloodRequest.getNotes() != null ? bloodRequest.getNotes() + "\n" : "") + 
                                        "Staff Note: " + staffNotes);
                }
                bloodRequestService.save(bloodRequest);
                
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Request status updated to " + status + " successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to update request status: " + e.getMessage());
        }
        return "redirect:/staff/requests";
    }

    // Manage Donations
    @GetMapping("/donations")
    public String manageDonations(Model model) {
        List<DonationRecord> donations = donationRecordService.findAll();
        model.addAttribute("donations", donations);
        model.addAttribute("title", "Manage Donations - Staff");
        return "staff-donations";
    }

    // Add New Donation Record
    @GetMapping("/donations/new")
    public String showNewDonationForm(Model model) {
        model.addAttribute("donationRecord", new DonationRecord());
        model.addAttribute("title", "Record New Donation - Staff");
        return "staff-new-donation";
    }

    @PostMapping("/donations/save")
    public String saveDonationRecord(@ModelAttribute DonationRecord donationRecord,
                                   RedirectAttributes redirectAttributes) {
        try {
            donationRecordService.save(donationRecord);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Donation record saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to save donation record: " + e.getMessage());
        }
        return "redirect:/staff/donations";
    }

    // Manage Blood Inventory
    @GetMapping("/inventory")
    public String manageInventory(Model model) {
        List<BloodInventory> inventory = bloodInventoryService.findAll();
        model.addAttribute("inventory", inventory);
        model.addAttribute("title", "Manage Blood Inventory - Staff");
        return "staff-inventory";
    }

    // Update Blood Inventory
    @PostMapping("/inventory/update")
    public String updateInventory(@RequestParam Long inventoryId,
                                @RequestParam int availableUnits,
                                RedirectAttributes redirectAttributes) {
        try {
            Optional<BloodInventory> inventory = bloodInventoryService.findById(inventoryId);
            if (inventory.isPresent()) {
                BloodInventory bloodInventory = inventory.get();
                bloodInventory.setAvailableUnits(availableUnits);
                bloodInventoryService.save(bloodInventory);
                
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Blood inventory updated successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to update inventory: " + e.getMessage());
        }
        return "redirect:/staff/inventory";
    }

    // Staff Profile
    @GetMapping("/profile")
    public String staffProfile(Model model) {
        // In real app, get current logged-in staff
        // For demo, use first staff
        List<Staff> staffList = staffService.findAll();
        if (!staffList.isEmpty()) {
            model.addAttribute("staff", staffList.get(0));
        }
        model.addAttribute("title", "Staff Profile - Blood Bank");
        return "staff-profile";
    }
}