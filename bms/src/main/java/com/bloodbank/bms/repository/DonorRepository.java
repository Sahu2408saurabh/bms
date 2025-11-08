package com.bloodbank.bms.repository;

import com.bloodbank.bms.entity.BloodGroup;
import com.bloodbank.bms.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonorRepository extends JpaRepository<Donor, Long> {
    List<Donor> findByBloodGroup(BloodGroup bloodGroup);
    List<Donor> findByCity(String city);
    List<Donor> findByIsAvailableTrue();
    long countByIsAvailableTrue();
}