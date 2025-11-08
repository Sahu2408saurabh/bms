package com.bloodbank.bms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private TemplateEngine templateEngine;

    // Simple email send
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("noreply@bloodbank.org");
            
            mailSender.send(message);
            System.out.println("✅ Email sent to: " + to);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email to " + to + ": " + e.getMessage());
        }
    }

    // HTML email send
    public void sendHtmlEmail(String to, String subject, String templateName, Context context) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            String htmlContent = templateEngine.process(templateName, context);
            
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            helper.setFrom("noreply@bloodbank.org");
            
            mailSender.send(mimeMessage);
            System.out.println("✅ HTML Email sent to: " + to);
        } catch (MessagingException e) {
            System.err.println("❌ Failed to send HTML email to " + to + ": " + e.getMessage());
        }
    }

    // Donor registration confirmation
    public void sendDonorWelcomeEmail(String to, String donorName) {
        try {
            Context context = new Context();
            context.setVariable("donorName", donorName);
            context.setVariable("message", "Thank you for registering as a blood donor. Your contribution can save lives!");
            
            sendHtmlEmail(to, "Welcome to Blood Bank - Thank You for Registering", "email/donor-welcome", context);
        } catch (Exception e) {
            System.err.println("❌ Failed to send welcome email: " + e.getMessage());
        }
    }

    // Blood request confirmation
    public void sendRequestConfirmationEmail(String to, String patientName, String bloodGroup) {
        try {
            Context context = new Context();
            context.setVariable("patientName", patientName);
            context.setVariable("bloodGroup", bloodGroup);
            context.setVariable("message", "We have received your blood request. Our team will contact you shortly.");
            
            sendHtmlEmail(to, "Blood Request Received - Blood Bank", "email/request-confirmation", context);
        } catch (Exception e) {
            System.err.println("❌ Failed to send request confirmation email: " + e.getMessage());
        }
    }

    // Low stock alert to admin
    public void sendLowStockAlert(String to, String bloodGroup, int currentStock) {
        String subject = "🚨 Low Stock Alert - " + bloodGroup + " Blood";
        String text = "Alert: " + bloodGroup + " blood stock is running low. Current stock: " + currentStock + " units.";
        
        sendSimpleEmail(to, subject, text);
    }

    // Donation thank you email
    public void sendDonationThankYouEmail(String to, String donorName, int bloodVolumeML, String nextEligibleDate) {
        String subject = "Thank You for Your Blood Donation";
        String text = "Dear " + donorName + ",\n\n" +
                     "Thank you for your generous blood donation of " + bloodVolumeML + "ml. " +
                     "Your contribution can save up to 3 lives!\n\n" +
                     "Your next eligible donation date: " + nextEligibleDate + "\n\n" +
                     "Stay healthy and hydrated!\n\n" +
                     "Blood Bank Management System";
        
        sendSimpleEmail(to, subject, text);
    }
 // Add these methods to the existing EmailService class

 // Donation reminder email
 public void sendDonationReminderEmail(String to, String donorName, String nextEligibleDate) {
     String subject = "🩸 Donation Reminder - You're Eligible to Donate Blood!";
     String text = "Dear " + donorName + ",\n\n" +
                  "We hope this message finds you well!\n\n" +
                  "You are now eligible to donate blood again. Your next eligible donation date was: " + nextEligibleDate + "\n\n" +
                  "💪 Your blood can save up to 3 lives!\n\n" +
                  "📍 Visit our blood bank anytime between 9 AM - 5 PM\n\n" +
                  "📞 For appointment: +91-9876543210\n\n" +
                  "Thank you for being a lifesaver!\n\n" +
                  "Blood Bank Management System";
     
     sendSimpleEmail(to, subject, text);
 }

 // Request status update email
 public void sendRequestStatusEmail(String to, String patientName, String status, String bloodGroup) {
     String subject = "🩸 Blood Request Status Update - " + status;
     String text = "Dear Requester,\n\n" +
                  "Update for patient: " + patientName + "\n" +
                  "Blood Group: " + bloodGroup + "\n" +
                  "Status: " + status + "\n\n";
     
     if ("APPROVED".equals(status)) {
         text += "✅ Your blood request has been approved!\n" +
                 "Our team will contact you shortly for further details.\n\n";
     } else if ("REJECTED".equals(status)) {
         text += "❌ Unfortunately, your request could not be fulfilled at this time.\n" +
                 "Please contact us for alternative options.\n\n";
     } else {
         text += "⏳ Your request is still under processing.\n\n";
     }
     
     text += "📞 Contact: +91-9876543210\n\n" +
             "Blood Bank Management System";
     
     sendSimpleEmail(to, subject, text);
 }

 // Emergency alert to donors
 public void sendEmergencyAlertEmail(String to, String donorName, String bloodGroup, String location) {
     String subject = "🚨 URGENT: Emergency Blood Requirement - " + bloodGroup;
     String text = "Dear " + donorName + ",\n\n" +
                  "EMERGENCY BLOOD REQUEST!\n\n" +
                  "Blood Group Needed: " + bloodGroup + "\n" +
                  "Location: " + location + "\n\n" +
                  "🆘 Immediate donation required to save a life!\n\n" +
                  "📍 Please visit nearest blood bank immediately\n" +
                  "📞 Emergency Contact: +91-9876543210\n\n" +
                  "Your immediate response can save a life!\n\n" +
                  "Blood Bank Emergency Response Team";
     
     sendSimpleEmail(to, subject, text);
 }

 // Staff approval notification
 public void sendStaffApprovalEmail(String to, String staffName) {
     String subject = "🎉 Staff Registration Approved - Blood Bank";
     String text = "Dear " + staffName + ",\n\n" +
                  "Congratulations! Your staff registration has been approved.\n\n" +
                  "You can now login to the Blood Bank Management System using your registered credentials.\n\n" +
                  "🔗 Login URL: http://localhost:8080/bms/login\n" +
                  "📧 Username: " + to + "\n\n" +
                  "Welcome to our team!\n\n" +
                  "For any assistance, contact admin@bloodbank.com\n\n" +
                  "Blood Bank Management System";
     
     sendSimpleEmail(to, subject, text);
 }

 // Appointment confirmation
 public void sendAppointmentConfirmation(String to, String donorName, String appointmentDate, String purpose) {
     String subject = "📅 Appointment Confirmed - Blood Bank";
     String text = "Dear " + donorName + ",\n\n" +
                  "Your appointment has been confirmed!\n\n" +
                  "📅 Date & Time: " + appointmentDate + "\n" +
                  "🎯 Purpose: " + purpose + "\n\n" +
                  "📍 Location: Main Blood Bank Center\n" +
                  "📞 Contact: +91-9876543210\n\n" +
                  "Please arrive 15 minutes before your scheduled time.\n\n" +
                  "Thank you!\n\n" +
                  "Blood Bank Management System";
     
     sendSimpleEmail(to, subject, text);
 }
    
    
}