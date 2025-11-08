package com.bloodbank.bms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "donors")
@PrimaryKeyJoinColumn(name = "user_id")
public class Donor extends User {
    
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", nullable = false)
    private BloodGroup bloodGroup;
    
    private String gender;
    
    @Column(name = "last_donation_date")
    private LocalDate lastDonationDate;
    
    @Column(name = "next_eligible_date")
    private LocalDate nextEligibleDate;
    
    private String healthInfo;
    
    @Column(name = "is_available")
    private boolean isAvailable = true;
    
    // Constructors
    public Donor() {
        super();
        setRole(Role.DONOR);
    }
    
    public Donor(String fullName, String email, String password, String phoneNumber, BloodGroup bloodGroup) {
        super(fullName, email, password, phoneNumber, Role.DONOR);
        this.bloodGroup = bloodGroup;
    }
    
    // Getters and Setters
    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public LocalDate getLastDonationDate() { return lastDonationDate; }
    public void setLastDonationDate(LocalDate lastDonationDate) { 
        this.lastDonationDate = lastDonationDate;
        // Automatically set next eligible date (90 days after last donation)
        if (lastDonationDate != null) {
            this.nextEligibleDate = lastDonationDate.plusDays(90);
        }
    }
    
    public LocalDate getNextEligibleDate() { return nextEligibleDate; }
    public void setNextEligibleDate(LocalDate nextEligibleDate) { this.nextEligibleDate = nextEligibleDate; }
    
    public String getHealthInfo() { return healthInfo; }
    public void setHealthInfo(String healthInfo) { this.healthInfo = healthInfo; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}