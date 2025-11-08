package com.bloodbank.bms.repository;

import com.bloodbank.bms.entity.BloodGroup;
import com.bloodbank.bms.entity.BloodInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, Long> {
    Optional<BloodInventory> findByBloodGroup(BloodGroup bloodGroup);
    List<BloodInventory> findByStatus(String status);
    List<BloodInventory> findByAvailableUnitsGreaterThan(int units);
    List<BloodInventory> findByAvailableUnitsLessThanEqual(int units);
}