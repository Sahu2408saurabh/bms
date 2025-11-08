package com.bloodbank.bms.config;

import com.bloodbank.bms.entity.*;
import com.bloodbank.bms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DonorRepository donorRepository;
    
    @Autowired
    private BloodInventoryRepository bloodInventoryRepository;
    
    @Autowired
    private BloodRequestRepository bloodRequestRepository;
    
    @Autowired
    private DonationRecordRepository donationRecordRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Loading sample data...");
        
        // Create sample blood inventory
        if (bloodInventoryRepository.count() == 0) {
            bloodInventoryRepository.save(new BloodInventory(BloodGroup.A_POSITIVE, 5000, 25));
            bloodInventoryRepository.save(new BloodInventory(BloodGroup.A_NEGATIVE, 3000, 15));
            bloodInventoryRepository.save(new BloodInventory(BloodGroup.B_POSITIVE, 6000, 30));
            bloodInventoryRepository.save(new BloodInventory(BloodGroup.B_NEGATIVE, 2400, 12));
            bloodInventoryRepository.save(new BloodInventory(BloodGroup.O_POSITIVE, 8000, 40));
            bloodInventoryRepository.save(new BloodInventory(BloodGroup.O_NEGATIVE, 1600, 8));
            bloodInventoryRepository.save(new BloodInventory(BloodGroup.AB_POSITIVE, 3600, 18));
            bloodInventoryRepository.save(new BloodInventory(BloodGroup.AB_NEGATIVE, 1000, 5));
            
            System.out.println("✅ Sample blood inventory created!");
        }

        // Create sample admin user
        if (userRepository.findByEmail("admin@bloodbank.com").isEmpty()) {
            User admin = new User();
            admin.setFullName("System Administrator");
            admin.setEmail("admin@bloodbank.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhoneNumber("9876543210");
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("✅ Admin user created!");
        }

        // Create sample donors
        if (donorRepository.count() == 0) {
            Donor donor1 = new Donor();
            donor1.setFullName("Rahul Sharma");
            donor1.setEmail("rahul@example.com"); // Real email for testing
            donor1.setPassword(passwordEncoder.encode("donor123"));
            donor1.setPhoneNumber("9876543211");
            donor1.setBloodGroup(BloodGroup.O_POSITIVE);
            donor1.setGender("Male");
            donor1.setCity("Mumbai");
            donor1.setState("Maharashtra");
            donor1.setAvailable(true);
            donorRepository.save(donor1);
            
            Donor donor2 = new Donor();
            donor2.setFullName("Priya Singh");
            donor2.setEmail("priya@example.com"); // Real email for testing
            donor2.setPassword(passwordEncoder.encode("donor123"));
            donor2.setPhoneNumber("9876543212");
            donor2.setBloodGroup(BloodGroup.A_POSITIVE);
            donor2.setGender("Female");
            donor2.setCity("Delhi");
            donor2.setState("Delhi");
            donor2.setAvailable(true);
            donorRepository.save(donor2);
            
            Donor donor3 = new Donor();
            donor3.setFullName("Amit Kumar");
            donor3.setEmail("amit@example.com"); // Real email for testing
            donor3.setPassword(passwordEncoder.encode("donor123"));
            donor3.setPhoneNumber("9876543213");
            donor3.setBloodGroup(BloodGroup.B_POSITIVE);
            donor3.setGender("Male");
            donor3.setCity("Bangalore");
            donor3.setState("Karnataka");
            donor3.setAvailable(false);
            donorRepository.save(donor3);
            
            System.out.println("✅ Sample donors created!");
        }

		/*
		 * // Create sample donation records if (donationRecordRepository.count() == 0)
		 * { Donor donor1 = donorRepository.findByEmail("rahul@example.com").get();
		 * DonationRecord donation1 = new DonationRecord(donor1,
		 * LocalDate.now().minusDays(30), 450);
		 * donationRecordRepository.save(donation1);
		 * 
		 * DonationRecord donation2 = new DonationRecord(donor1,
		 * LocalDate.now().minusDays(120), 450);
		 * donationRecordRepository.save(donation2);
		 * 
		 * System.out.println("✅ Sample donation records created!"); }
		 */
        // Create sample blood requests
        if (bloodRequestRepository.count() == 0) {
            BloodRequest request1 = new BloodRequest();
            request1.setPatientName("Rohan Mehta");
            request1.setPatientAge(35);
            request1.setBloodGroup(BloodGroup.O_POSITIVE);
            request1.setUnits(2);
            request1.setHospital("City Hospital");
            request1.setCity("Mumbai");
            request1.setContactPerson("Mrs. Mehta");
            request1.setContactPhone("9876543220");
            request1.setContactEmail("rohan.family@example.com"); // Real email for testing
            request1.setUrgency("HIGH");
            request1.setStatus("PENDING");
            bloodRequestRepository.save(request1);
            
            BloodRequest request2 = new BloodRequest();
            request2.setPatientName("Sneha Patel");
            request2.setPatientAge(28);
            request2.setBloodGroup(BloodGroup.A_POSITIVE);
            request2.setUnits(1);
            request2.setHospital("General Hospital");
            request2.setCity("Delhi");
            request2.setContactPerson("Mr. Patel");
            request2.setContactPhone("9876543221");
            request2.setContactEmail("sneha.patel@example.com"); // Real email for testing
            request2.setUrgency("MEDIUM");
            request2.setStatus("PENDING");
            bloodRequestRepository.save(request2);
            
            System.out.println("✅ Sample blood requests created!");
        }
        
        System.out.println("🎉 Data loading completed!");
    }
}