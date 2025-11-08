package com.bloodbank.bms.service;

import com.bloodbank.bms.entity.DonationRecord;
import com.bloodbank.bms.entity.Donor;
import com.bloodbank.bms.repository.DonationRecordRepository;
import com.bloodbank.bms.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DonationRecordService {

    @Autowired
    private DonationRecordRepository donationRecordRepository;
    
    @Autowired
    private DonorRepository donorRepository;
    
    @Autowired
    private EmailService emailService;

    public List<DonationRecord> findAll() {
        return donationRecordRepository.findAll();
    }

    public Optional<DonationRecord> findById(Long id) {
        return donationRecordRepository.findById(id);
    }

    public DonationRecord save(DonationRecord donationRecord) {
        return donationRecordRepository.save(donationRecord);
    }

    public void deleteById(Long id) {
        donationRecordRepository.deleteById(id);
    }

    public List<DonationRecord> findByDonor(Donor donor) {
        return donationRecordRepository.findByDonor(donor);
    }

    public List<DonationRecord> findRecentDonations() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        return donationRecordRepository.findByDonationDateBetween(thirtyDaysAgo, LocalDate.now());
    }

    public DonationRecord recordNewDonation(Donor donor, Integer bloodVolumeML) {
        DonationRecord donation = new DonationRecord(donor, LocalDate.now(), bloodVolumeML);
        DonationRecord savedDonation = save(donation);
        
        // Update donor's last donation date
        donor.setLastDonationDate(LocalDate.now());
        donorRepository.save(donor);
        
        // Send thank you email
        emailService.sendDonationThankYouEmail(
            donor.getEmail(),
            donor.getFullName(),
            bloodVolumeML,
            savedDonation.getNextEligibleDate().toString()
        );
        
        return savedDonation;
    }

    public long getTodayDonationsCount() {
        return donationRecordRepository.countByDonationDate(LocalDate.now());
    }
}