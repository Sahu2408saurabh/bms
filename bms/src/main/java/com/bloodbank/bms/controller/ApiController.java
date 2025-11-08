package com.bloodbank.bms.controller;

import com.bloodbank.bms.entity.*;
import com.bloodbank.bms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    @Autowired
    private DonorService donorService;
    
    @Autowired
    private BloodInventoryService bloodInventoryService;
    
    @Autowired
    private BloodRequestService bloodRequestService;

    // Public APIs - No authentication required
    @GetMapping("/blood-availability")
    public ResponseEntity<List<BloodInventory>> getBloodAvailability() {
        return ResponseEntity.ok(bloodInventoryService.findAll());
    }

    @GetMapping("/blood-availability/{bloodGroup}")
    public ResponseEntity<BloodInventory> getBloodAvailabilityByGroup(
            @PathVariable String bloodGroup) {
        try {
            BloodGroup group = BloodGroup.valueOf(bloodGroup);
            return bloodInventoryService.findByBloodGroup(group)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/blood-request")
    public ResponseEntity<BloodRequest> submitBloodRequest(@RequestBody BloodRequest request) {
        request.setStatus("PENDING");
        BloodRequest savedRequest = bloodRequestService.save(request);
        return ResponseEntity.ok(savedRequest);
    }

    @PostMapping("/donor/register")
    public ResponseEntity<Donor> registerDonor(@RequestBody Donor donor) {
        donor.setAvailable(true);
        Donor savedDonor = donorService.save(donor);
        return ResponseEntity.ok(savedDonor);
    }

    // Protected APIs (would require JWT authentication in real implementation)
    @GetMapping("/donors")
    public ResponseEntity<List<Donor>> getAllDonors() {
        return ResponseEntity.ok(donorService.findAll());
    }

    @GetMapping("/donors/available")
    public ResponseEntity<List<Donor>> getAvailableDonors() {
        return ResponseEntity.ok(donorService.findAvailableDonors());
    }

    @GetMapping("/requests/pending")
    public ResponseEntity<List<BloodRequest>> getPendingRequests() {
        return ResponseEntity.ok(bloodRequestService.findPendingRequests());
    }

    // Statistics API
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getSystemStats() {
        Map<String, Object> stats = Map.of(
            "totalDonors", donorService.count(),
            "availableDonors", donorService.countAvailableDonors(),
            "pendingRequests", bloodRequestService.countPendingRequests(),
            "totalBloodUnits", bloodInventoryService.findAll().stream()
                    .mapToInt(BloodInventory::getAvailableUnits).sum()
        );
        return ResponseEntity.ok(stats);
    }
}