package com.bloodbank.bms.service;

import com.bloodbank.bms.entity.BloodGroup;
import com.bloodbank.bms.entity.Donor;
import com.bloodbank.bms.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DonorService {

    @Autowired
    private DonorRepository donorRepository;

    public List<Donor> findAll() {
        return donorRepository.findAll();
    }

    public Optional<Donor> findById(Long id) {
        return donorRepository.findById(id);
    }

    public Donor save(Donor donor) {
        return donorRepository.save(donor);
    }

    public void deleteById(Long id) {
        donorRepository.deleteById(id);
    }

    public List<Donor> findByBloodGroup(BloodGroup bloodGroup) {
        return donorRepository.findByBloodGroup(bloodGroup);
    }

    public List<Donor> findByCity(String city) {
        return donorRepository.findByCity(city);
    }

    public List<Donor> findAvailableDonors() {
        return donorRepository.findByIsAvailableTrue();
    }

    public long countAvailableDonors() {
        return donorRepository.countByIsAvailableTrue();
    }

    public long count() {
        return donorRepository.count();
    }
}