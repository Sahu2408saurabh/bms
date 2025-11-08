package com.bloodbank.bms.service;

import com.bloodbank.bms.entity.BloodGroup;
import com.bloodbank.bms.entity.BloodInventory;
import com.bloodbank.bms.repository.BloodInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BloodInventoryService {

    @Autowired
    private BloodInventoryRepository bloodInventoryRepository;

    public List<BloodInventory> findAll() {
        return bloodInventoryRepository.findAll();
    }

    public Optional<BloodInventory> findById(Long id) {
        return bloodInventoryRepository.findById(id);
    }

    public Optional<BloodInventory> findByBloodGroup(BloodGroup bloodGroup) {
        return bloodInventoryRepository.findByBloodGroup(bloodGroup);
    }

    public BloodInventory save(BloodInventory bloodInventory) {
        return bloodInventoryRepository.save(bloodInventory);
    }

    public void deleteById(Long id) {
        bloodInventoryRepository.deleteById(id);
    }

    public List<BloodInventory> findLowStock() {
        return bloodInventoryRepository.findByAvailableUnitsLessThanEqual(5);
    }

    public List<BloodInventory> findAvailableStock() {
        return bloodInventoryRepository.findByAvailableUnitsGreaterThan(0);
    }

    public long count() {
        return bloodInventoryRepository.count();
    }

    public void updateStock(BloodGroup bloodGroup, int units) {
        Optional<BloodInventory> inventoryOpt = findByBloodGroup(bloodGroup);
        if (inventoryOpt.isPresent()) {
            BloodInventory inventory = inventoryOpt.get();
            inventory.setAvailableUnits(inventory.getAvailableUnits() + units);
            save(inventory);
        }
    }
}