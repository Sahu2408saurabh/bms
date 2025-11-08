package com.bloodbank.bms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "donation_records")
public class DonationRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private Donor donor;
    
    @Column(name = "donation_date", nullable = false)
    private LocalDate donationDate;
    
    @Column(name = "blood_volume_ml")
    private Integer bloodVolumeML;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group")
    private BloodGroup bloodGroup;
    
    private String status; // COMPLETED, CANCELLED, SCHEDULED
    
    @Column(name = "next_eligible_date")
    private LocalDate nextEligibleDate;
    
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public DonationRecord() {
        this.createdAt = LocalDateTime.now();
        this.status = "COMPLETED";
    }
    
    public DonationRecord(Donor donor, LocalDate donationDate, Integer bloodVolumeML) {
        this();
        this.donor = donor;
        this.donationDate = donationDate;
        this.bloodVolumeML = bloodVolumeML;
        this.bloodGroup = donor.getBloodGroup();
        // Set next eligible date (90 days after donation)
        this.nextEligibleDate = donationDate.plusDays(90);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Donor getDonor() { return donor; }
    public void setDonor(Donor donor) { this.donor = donor; }
    
    public LocalDate getDonationDate() { return donationDate; }
    public void setDonationDate(LocalDate donationDate) { 
        this.donationDate = donationDate;
        if (donationDate != null) {
            this.nextEligibleDate = donationDate.plusDays(90);
        }
    }
    
    public Integer getBloodVolumeML() { return bloodVolumeML; }
    public void setBloodVolumeML(Integer bloodVolumeML) { this.bloodVolumeML = bloodVolumeML; }
    
    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDate getNextEligibleDate() { return nextEligibleDate; }
    public void setNextEligibleDate(LocalDate nextEligibleDate) { this.nextEligibleDate = nextEligibleDate; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}