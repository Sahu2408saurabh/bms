package com.bloodbank.bms.controller;

import com.bloodbank.bms.entity.StaffRegistration;
import com.bloodbank.bms.service.BloodInventoryService;
import com.bloodbank.bms.service.BloodRequestService;
import com.bloodbank.bms.service.DonorService;
import com.bloodbank.bms.service.StaffRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DonorService donorService;
    
    @Autowired
    private BloodRequestService bloodRequestService;
    
    @Autowired
    private BloodInventoryService bloodInventoryService;
    
    @Autowired
    private StaffRegistrationService staffRegistrationService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Statistics for dashboard
        long totalDonors = donorService.count();
        long availableDonors = donorService.countAvailableDonors();
        long pendingRequests = bloodRequestService.countPendingRequests();
        long totalBloodUnits = bloodInventoryService.findAll().stream()
                .mapToInt(inventory -> inventory.getAvailableUnits())
                .sum();
        long pendingStaffApprovals = staffRegistrationService.countPendingRegistrations();
        
        model.addAttribute("totalDonors", totalDonors);
        model.addAttribute("availableDonors", availableDonors);
        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("totalBloodUnits", totalBloodUnits);
        model.addAttribute("pendingStaffApprovals", pendingStaffApprovals);
        model.addAttribute("title", "Admin Dashboard - Blood Bank");
        return "admin-dashboard";
    }

    @GetMapping("/donors")
    public String manageDonors(Model model) {
        model.addAttribute("donors", donorService.findAll());
        model.addAttribute("title", "Manage Donors - Admin");
        return "admin-donors";
    }

    @GetMapping("/blood-inventory")
    public String manageBloodInventory(Model model) {
        model.addAttribute("inventory", bloodInventoryService.findAll());
        model.addAttribute("title", "Manage Blood Inventory - Admin");
        return "admin-blood-inventory";
    }

    @GetMapping("/blood-requests")
    public String manageBloodRequests(Model model) {
        model.addAttribute("requests", bloodRequestService.findAll());
        model.addAttribute("pendingRequests", bloodRequestService.findPendingRequests());
        model.addAttribute("title", "Manage Blood Requests - Admin");
        return "admin-blood-requests";
    }

    // ✅ NEW: Staff Registration Approvals
    @GetMapping("/staff-approvals")
    public String staffApprovals(Model model) {
        model.addAttribute("pendingRegistrations", staffRegistrationService.findPendingRegistrations());
        model.addAttribute("title", "Staff Registration Approvals - Admin");
        return "admin-staff-approvals";
    }

    // ✅ NEW: Approve Staff Registration
    @PostMapping("/staff-approve/{id}")
    public String approveStaff(@PathVariable Long id,
                              @RequestParam String adminNotes,
                              RedirectAttributes redirectAttributes) {
        try {
            // In real app, get current admin username from security context
            String approvedBy = "System Admin";
            
            StaffRegistration approvedRegistration = staffRegistrationService.approveRegistration(id, approvedBy, adminNotes);
            
            if (approvedRegistration != null) {
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Staff registration approved successfully! Active staff account created for " + approvedRegistration.getFullName() + ".");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Staff registration not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to approve staff registration: " + e.getMessage());
        }
        return "redirect:/admin/staff-approvals";
    }

    // ✅ NEW: Reject Staff Registration
    @PostMapping("/staff-reject/{id}")
    public String rejectStaff(@PathVariable Long id,
                             @RequestParam String adminNotes,
                             RedirectAttributes redirectAttributes) {
        try {
            String approvedBy = "System Admin";
            
            StaffRegistration rejectedRegistration = staffRegistrationService.rejectRegistration(id, approvedBy, adminNotes);
            
            if (rejectedRegistration != null) {
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Staff registration rejected successfully.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", 
                    "Staff registration not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to reject staff registration: " + e.getMessage());
        }
        return "redirect:/admin/staff-approvals";
    }

    @PostMapping("/blood-request/update-status")
    public String updateRequestStatus(@RequestParam Long requestId, 
                                    @RequestParam String status,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Implementation for updating blood request status
            // This would typically involve finding the request and updating its status
            redirectAttributes.addFlashAttribute("successMessage", 
                "Request status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to update request status: " + e.getMessage());
        }
        return "redirect:/admin/blood-requests";
    }

    @PostMapping("/blood-inventory/update")
    public String updateBloodInventory(@RequestParam Long inventoryId,
                                     @RequestParam int availableUnits,
                                     RedirectAttributes redirectAttributes) {
        try {
            // Implementation for updating blood inventory
            redirectAttributes.addFlashAttribute("successMessage", 
                "Blood inventory updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to update inventory: " + e.getMessage());
        }
        return "redirect:/admin/blood-inventory";
    }

    // ✅ NEW: Delete Donor
    @PostMapping("/donor/delete/{id}")
    public String deleteDonor(@PathVariable Long id,
                            RedirectAttributes redirectAttributes) {
        try {
            donorService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Donor deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Failed to delete donor: " + e.getMessage());
        }
        return "redirect:/admin/donors";
    }

	/*
	 * // ✅ NEW: System Reports
	 * 
	 * @GetMapping("/reports") public String systemReports(Model model) {
	 * model.addAttribute("title", "System Reports - Admin"); // Add report data
	 * here return "admin-reports"; }
	 */

    // ✅ NEW: System Settings
    @GetMapping("/settings")
    public String systemSettings(Model model) {
        model.addAttribute("title", "System Settings - Admin");
        return "admin-settings";
    }
}