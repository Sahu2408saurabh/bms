package com.bloodbank.bms.repository;

import com.bloodbank.bms.entity.BloodGroup;
import com.bloodbank.bms.entity.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    List<BloodRequest> findByBloodGroup(BloodGroup bloodGroup);
    List<BloodRequest> findByStatus(String status);
    List<BloodRequest> findByUrgency(String urgency);
    List<BloodRequest> findByCity(String city);
    long countByStatus(String status);
}