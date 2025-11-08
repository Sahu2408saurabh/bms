package com.bloodbank.bms.repository;

import com.bloodbank.bms.entity.DonationRecord;
import com.bloodbank.bms.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DonationRecordRepository extends JpaRepository<DonationRecord, Long> {
    List<DonationRecord> findByDonor(Donor donor);
    List<DonationRecord> findByDonationDateBetween(LocalDate startDate, LocalDate endDate);
    List<DonationRecord> findByStatus(String status);
    long countByDonationDate(LocalDate donationDate);
}