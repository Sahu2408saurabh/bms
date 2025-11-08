package com.bloodbank.bms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "blood_inventory")
public class BloodInventory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", nullable = false)
    private BloodGroup bloodGroup;
    
    @Column(name = "quantity_ml")
    private int quantityML;
    
    @Column(name = "available_units")
    private int availableUnits;
    
    @Column(name = "last_updated")
    private LocalDate lastUpdated;
    
    @Column(name = "expiry_date")
    private LocalDate expiryDate;
    
    private String status; // AVAILABLE, CRITICAL, OUT_OF_STOCK
    
    // Constructors
    public BloodInventory() {
        this.lastUpdated = LocalDate.now();
    }
    
    public BloodInventory(BloodGroup bloodGroup, int quantityML, int availableUnits) {
        this();
        this.bloodGroup = bloodGroup;
        this.quantityML = quantityML;
        this.availableUnits = availableUnits;
        this.status = availableUnits > 10 ? "AVAILABLE" : availableUnits > 3 ? "CRITICAL" : "OUT_OF_STOCK";
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public BloodGroup getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(BloodGroup bloodGroup) { this.bloodGroup = bloodGroup; }
    
    public int getQuantityML() { return quantityML; }
    public void setQuantityML(int quantityML) { this.quantityML = quantityML; }
    
    public int getAvailableUnits() { return availableUnits; }
    public void setAvailableUnits(int availableUnits) { 
        this.availableUnits = availableUnits;
        this.status = availableUnits > 10 ? "AVAILABLE" : availableUnits > 3 ? "CRITICAL" : "OUT_OF_STOCK";
        this.lastUpdated = LocalDate.now();
    }
    
    public LocalDate getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDate lastUpdated) { this.lastUpdated = lastUpdated; }
    
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}