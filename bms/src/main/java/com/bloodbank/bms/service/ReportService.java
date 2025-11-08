package com.bloodbank.bms.service;

import com.bloodbank.bms.entity.*;
import com.bloodbank.bms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private DonorRepository donorRepository;
    
    @Autowired
    private DonationRecordRepository donationRecordRepository;
    
    @Autowired
    private BloodRequestRepository bloodRequestRepository;
    
    @Autowired
    private BloodInventoryRepository bloodInventoryRepository;

    // Monthly donation vs usage report
    public Map<String, Object> getMonthlyDonationVsUsageReport(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        
        // Get donations in the month
        List<DonationRecord> monthlyDonations = donationRecordRepository
            .findByDonationDateBetween(startDate, endDate);
        
        // Get blood requests in the month
        List<BloodRequest> monthlyRequests = bloodRequestRepository.findAll().stream()
            .filter(request -> request.getRequestDate() != null &&
                             !request.getRequestDate().isBefore(startDate) &&
                             !request.getRequestDate().isAfter(endDate))
            .toList();
        
        int totalDonations = monthlyDonations.size();
        int totalRequests = monthlyRequests.size();
        int totalDonatedML = monthlyDonations.stream()
            .mapToInt(d -> d.getBloodVolumeML() != null ? d.getBloodVolumeML() : 0)
            .sum();
        
        int totalRequestedUnits = monthlyRequests.stream()
            .mapToInt(r -> r.getUnits() != null ? r.getUnits() : 0)
            .sum();
        
        Map<String, Object> report = new HashMap<>();
        report.put("period", startDate.getMonth().toString() + " " + year);
        report.put("totalDonations", totalDonations);
        report.put("totalRequests", totalRequests);
        report.put("totalDonatedML", totalDonatedML);
        report.put("totalRequestedUnits", totalRequestedUnits);
        report.put("donationEfficiency", totalRequests > 0 ? 
            (double) totalDonations / totalRequests * 100 : 0);
        
        return report;
    }

    // Most demanded blood groups
    public Map<String, Object> getMostDemandedBloodGroups() {
        List<BloodRequest> allRequests = bloodRequestRepository.findAll();
        
        Map<BloodGroup, Long> bloodGroupDemand = allRequests.stream()
            .collect(Collectors.groupingBy(
                BloodRequest::getBloodGroup,
                Collectors.counting()
            ));
        
        Map<String, Object> report = new HashMap<>();
        report.put("bloodGroupDemand", bloodGroupDemand);
        report.put("mostDemanded", bloodGroupDemand.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(entry -> entry.getKey().getDisplayName())
            .orElse("N/A"));
        
        return report;
    }

    // Top active donors
    public Map<String, Object> getTopActiveDonors(int limit) {
        List<Donor> activeDonors = donorRepository.findByIsAvailableTrue();
        
        Map<String, Integer> donorDonationCounts = activeDonors.stream()
            .collect(Collectors.toMap(
                Donor::getFullName,
                donor -> donationRecordRepository.findByDonor(donor).size()
            ));
        
        List<Map.Entry<String, Integer>> topDonors = donorDonationCounts.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(limit)
            .toList();
        
        Map<String, Object> report = new HashMap<>();
        report.put("topDonors", topDonors);
        report.put("totalActiveDonors", activeDonors.size());
        
        return report;
    }

    // Blood inventory status report
    public Map<String, Object> getInventoryStatusReport() {
        List<BloodInventory> inventory = bloodInventoryRepository.findAll();
        
        int totalUnits = inventory.stream()
            .mapToInt(BloodInventory::getAvailableUnits)
            .sum();
        
        int criticalStockCount = (int) inventory.stream()
            .filter(item -> "CRITICAL".equals(item.getStatus()))
            .count();
        
        Map<String, Object> report = new HashMap<>();
        report.put("totalBloodGroups", inventory.size());
        report.put("totalUnits", totalUnits);
        report.put("criticalStockCount", criticalStockCount);
        report.put("inventoryDetails", inventory);
        
        return report;
    }

    // Comprehensive system report
    public Map<String, Object> getComprehensiveReport() {
        Map<String, Object> report = new HashMap<>();
        
        // Basic statistics
        report.put("totalDonors", donorRepository.count());
        report.put("availableDonors", donorRepository.countByIsAvailableTrue());
        report.put("totalRequests", bloodRequestRepository.count());
        report.put("pendingRequests", bloodRequestRepository.countByStatus("PENDING"));
        
        // Current month report
        LocalDate now = LocalDate.now();
        report.put("monthlyReport", getMonthlyDonationVsUsageReport(now.getYear(), now.getMonthValue()));
        
        // Blood group demand
        report.put("demandAnalysis", getMostDemandedBloodGroups());
        
        // Top donors
        report.put("topDonors", getTopActiveDonors(5));
        
        // Inventory status
        report.put("inventoryStatus", getInventoryStatusReport());
        
        report.put("generatedAt", LocalDateTime.now());
        
        return report;
    }
}