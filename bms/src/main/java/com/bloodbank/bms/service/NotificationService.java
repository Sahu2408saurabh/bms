package com.bloodbank.bms.service;

import com.bloodbank.bms.entity.Donor;
import com.bloodbank.bms.entity.BloodInventory;
import com.bloodbank.bms.entity.BloodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private DonorService donorService;
    
    @Autowired
    private BloodInventoryService bloodInventoryService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private BloodRequestService bloodRequestService;

    // Send donation reminders to eligible donors
    @Scheduled(cron = "0 0 9 * * ?") // Run daily at 9 AM
    public void sendDonationReminders() {
        List<Donor> eligibleDonors = donorService.findAll().stream()
                .filter(donor -> donor.isAvailable() && 
                               donor.getNextEligibleDate() != null &&
                               donor.getNextEligibleDate().isBefore(LocalDate.now().plusDays(7)))
                .toList();
        
        for (Donor donor : eligibleDonors) {
            emailService.sendDonationReminderEmail(
                donor.getEmail(),
                donor.getFullName(),
                donor.getNextEligibleDate().toString()
            );
        }
        
        System.out.println("✅ Sent donation reminders to " + eligibleDonors.size() + " donors");
    }

    // Check low stock and send alerts
    @Scheduled(cron = "0 0 10 * * ?") // Run daily at 10 AM
    public void checkLowStock() {
        List<BloodInventory> lowStock = bloodInventoryService.findLowStock();
        
        for (BloodInventory inventory : lowStock) {
            emailService.sendLowStockAlert(
                "admin@bloodbank.com",
                inventory.getBloodGroup().getDisplayName(),
                inventory.getAvailableUnits()
            );
        }
        
        if (!lowStock.isEmpty()) {
            System.out.println("⚠️ Sent low stock alerts for " + lowStock.size() + " blood groups");
        }
    }

    // Send request status updates
    public void sendRequestStatusUpdate(BloodRequest request) {
        if (request.getContactEmail() != null && !request.getContactEmail().isEmpty()) {
            emailService.sendRequestStatusEmail(
                request.getContactEmail(),
                request.getPatientName(),
                request.getStatus(),
                request.getBloodGroup().getDisplayName()
            );
        }
    }

    // Send emergency alerts to available donors
    public void sendEmergencyAlert(String bloodGroup, String location) {
        List<Donor> availableDonors = donorService.findByBloodGroup(
            com.bloodbank.bms.entity.BloodGroup.valueOf(bloodGroup)
        ).stream()
         .filter(Donor::isAvailable)
         .toList();
        
        for (Donor donor : availableDonors) {
            emailService.sendEmergencyAlertEmail(
                donor.getEmail(),
                donor.getFullName(),
                bloodGroup,
                location
            );
        }
        
        System.out.println("🚨 Sent emergency alerts to " + availableDonors.size() + " donors");
    }

    // Send SMS notification (placeholder for SMS integration)
    public void sendSmsNotification(String phoneNumber, String message) {
        // Implement SMS service integration here
        // This could integrate with services like Twilio, Fast2SMS, etc.
        System.out.println("📱 SMS to " + phoneNumber + ": " + message);
    }
}