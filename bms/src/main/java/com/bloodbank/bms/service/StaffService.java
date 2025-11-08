package com.bloodbank.bms.service;

import com.bloodbank.bms.entity.Staff;
import com.bloodbank.bms.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    public Optional<Staff> findById(Long id) {
        return staffRepository.findById(id);
    }

    public Optional<Staff> findByStaffId(String staffId) {
        return staffRepository.findByStaffId(staffId);
    }

    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }

    public void deleteById(Long id) {
        staffRepository.deleteById(id);
    }

    public List<Staff> findByDepartment(String department) {
        return staffRepository.findByDepartment(department);
    }

    public List<Staff> findActiveStaff() {
        return staffRepository.findByActiveTrue();
    }

    public long countActiveStaff() {
        return staffRepository.countByActiveTrue();
    }
}