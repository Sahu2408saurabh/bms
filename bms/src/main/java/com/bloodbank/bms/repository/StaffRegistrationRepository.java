package com.bloodbank.bms.repository;

import com.bloodbank.bms.entity.RegistrationStatus;
import com.bloodbank.bms.entity.StaffRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRegistrationRepository extends JpaRepository<StaffRegistration, Long> {
    List<StaffRegistration> findByStatus(RegistrationStatus status);
    List<StaffRegistration> findByStatusOrderByRegistrationDateDesc(RegistrationStatus status);
    Optional<StaffRegistration> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByStatus(RegistrationStatus status);
}