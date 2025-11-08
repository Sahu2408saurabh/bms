package com.bloodbank.bms.repository;

import com.bloodbank.bms.entity.Appointment;
import com.bloodbank.bms.entity.AppointmentStatus;
import com.bloodbank.bms.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDonor(Donor donor);
    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);
    List<Appointment> findByAssignedStaff(String staffName);
    long countByStatus(AppointmentStatus status);
	List<Appointment> findByAppointmentDateAfter(LocalDateTime now);
}