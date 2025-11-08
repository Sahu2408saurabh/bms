package com.bloodbank.bms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff")
@PrimaryKeyJoinColumn(name = "user_id")
public class Staff extends User {
    
    @Column(name = "staff_id", unique = true)
    private String staffId;
    
    private String department;
    
    private String designation;
    
    @Column(name = "join_date")
    private LocalDateTime joinDate;
    
    private String qualifications;
    
    @Column(name = "is_active")
    private boolean active = true;
    
    // Constructors
    public Staff() {
        super();
        setRole(Role.STAFF);
        this.joinDate = LocalDateTime.now();
    }
    
    public Staff(String fullName, String email, String password, String phoneNumber, 
                 String department, String designation) {
        super(fullName, email, password, phoneNumber, Role.STAFF);
        this.department = department;
        this.designation = designation;
        this.joinDate = LocalDateTime.now();
        this.active = true;
        // Generate staff ID
        this.staffId = "STAFF" + System.currentTimeMillis();
    }
    
    // Getters and Setters
    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    
    public LocalDateTime getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDateTime joinDate) { this.joinDate = joinDate; }
    
    public String getQualifications() { return qualifications; }
    public void setQualifications(String qualifications) { this.qualifications = qualifications; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}