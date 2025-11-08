package com.bloodbank.bms.service;

import com.bloodbank.bms.entity.Appointment;
import com.bloodbank.bms.entity.AppointmentStatus;
import com.bloodbank.bms.entity.Donor;
import com.bloodbank.bms.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private EmailService emailService;

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findByDonor(Donor donor) {
        return appointmentRepository.findByDonor(donor);
    }

    public List<Appointment> findUpcomingAppointments() {
        return appointmentRepository.findByAppointmentDateAfter(LocalDateTime.now());
    }

    public List<Appointment> findByStatus(AppointmentStatus status) {
        return appointmentRepository.findByStatus(status);
    }

    public Appointment scheduleAppointment(Donor donor, LocalDateTime dateTime, String purpose, String notes) {
        Appointment appointment = new Appointment(donor, dateTime, purpose);
        appointment.setNotes(notes);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        
        Appointment savedAppointment = save(appointment);
        
        // Send confirmation email
        emailService.sendAppointmentConfirmation(
            donor.getEmail(),
            donor.getFullName(),
            dateTime.toString(),
            purpose
        );
        
        return savedAppointment;
    }

    public void deleteById(Long id) {
        appointmentRepository.deleteById(id);
    }

    public long countByStatus(AppointmentStatus status) {
        return appointmentRepository.countByStatus(status);
    }
}