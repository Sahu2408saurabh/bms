package com.bloodbank.bms.service;

import com.bloodbank.bms.entity.RegistrationStatus;
import com.bloodbank.bms.entity.Staff;
import com.bloodbank.bms.entity.StaffRegistration;
import com.bloodbank.bms.repository.StaffRegistrationRepository;
import com.bloodbank.bms.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StaffRegistrationService {

    @Autowired
    private StaffRegistrationRepository staffRegistrationRepository;
    
    @Autowired
    private StaffRepository staffRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;

    public List<StaffRegistration> findAll() {
        return staffRegistrationRepository.findAll();
    }

    public Optional<StaffRegistration> findById(Long id) {
        return staffRegistrationRepository.findById(id);
    }

    public StaffRegistration save(StaffRegistration staffRegistration) {
        // Encode password before saving
        if (staffRegistration.getPassword() != null && 
            !staffRegistration.getPassword().startsWith("$2a$")) {
            staffRegistration.setPassword(passwordEncoder.encode(staffRegistration.getPassword()));
        }
        return staffRegistrationRepository.save(staffRegistration);
    }

    public List<StaffRegistration> findPendingRegistrations() {
        return staffRegistrationRepository.findByStatusOrderByRegistrationDateDesc(RegistrationStatus.PENDING);
    }

    public long countPendingRegistrations() {
        return staffRegistrationRepository.countByStatus(RegistrationStatus.PENDING);
    }

    public StaffRegistration approveRegistration(Long registrationId, String approvedBy, String adminNotes) {
        Optional<StaffRegistration> registrationOpt = findById(registrationId);
        if (registrationOpt.isPresent()) {
            StaffRegistration registration = registrationOpt.get();
            registration.setStatus(RegistrationStatus.APPROVED);
            registration.setApprovedBy(approvedBy);
            registration.setApprovedDate(LocalDateTime.now());
            registration.setAdminNotes(adminNotes);
            
            // Create active staff account
            createStaffFromRegistration(registration);
            
            return staffRegistrationRepository.save(registration);
        }
        return null;
    }

    public StaffRegistration rejectRegistration(Long registrationId, String approvedBy, String adminNotes) {
        Optional<StaffRegistration> registrationOpt = findById(registrationId);
        if (registrationOpt.isPresent()) {
            StaffRegistration registration = registrationOpt.get();
            registration.setStatus(RegistrationStatus.REJECTED);
            registration.setApprovedBy(approvedBy);
            registration.setApprovedDate(LocalDateTime.now());
            registration.setAdminNotes(adminNotes);
            
            return staffRegistrationRepository.save(registration);
        }
        return null;
    }

    private void createStaffFromRegistration(StaffRegistration registration) {
        Staff staff = new Staff();
        staff.setFullName(registration.getFullName());
        staff.setEmail(registration.getEmail());
        staff.setPassword(registration.getPassword());
        staff.setPhoneNumber(registration.getPhoneNumber());
        staff.setDepartment(registration.getDepartment());
        staff.setDesignation(registration.getDesignation());
        staff.setQualifications(registration.getQualifications());
        staff.setAddress(registration.getAddress());
        staff.setCity(registration.getCity());
        staff.setState(registration.getState());
        staff.setPincode(registration.getPincode());
        staff.setActive(true);
        
        staffRepository.save(staff);
        
        // Send approval email
        emailService.sendStaffApprovalEmail(staff.getEmail(), staff.getFullName());
    }
}