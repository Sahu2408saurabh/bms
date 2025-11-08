package com.bloodbank.bms.service;

import com.bloodbank.bms.entity.BloodGroup;
import com.bloodbank.bms.entity.BloodRequest;
import com.bloodbank.bms.repository.BloodRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BloodRequestService {

    @Autowired
    private BloodRequestRepository bloodRequestRepository;

    public List<BloodRequest> findAll() {
        return bloodRequestRepository.findAll();
    }

    public Optional<BloodRequest> findById(Long id) {
        return bloodRequestRepository.findById(id);
    }

    public BloodRequest save(BloodRequest bloodRequest) {
        return bloodRequestRepository.save(bloodRequest);
    }

    public void deleteById(Long id) {
        bloodRequestRepository.deleteById(id);
    }

    public List<BloodRequest> findByBloodGroup(BloodGroup bloodGroup) {
        return bloodRequestRepository.findByBloodGroup(bloodGroup);
    }

    public List<BloodRequest> findByStatus(String status) {
        return bloodRequestRepository.findByStatus(status);
    }

    public List<BloodRequest> findPendingRequests() {
        return bloodRequestRepository.findByStatus("PENDING");
    }

    public long countPendingRequests() {
        return bloodRequestRepository.countByStatus("PENDING");
    }

    public long count() {
        return bloodRequestRepository.count();
    }
}