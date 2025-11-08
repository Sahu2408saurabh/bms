package com.bloodbank.bms.controller;

import com.bloodbank.bms.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Controller
@RequestMapping("/admin/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public String reportsDashboard(Model model) {
        Map<String, Object> comprehensiveReport = reportService.getComprehensiveReport();
        model.addAttribute("report", comprehensiveReport);
        model.addAttribute("title", "System Reports - Admin");
        return "admin-reports";
    }

    @GetMapping("/monthly")
    public String monthlyReport(@RequestParam(defaultValue = "2024") int year,
                               @RequestParam(defaultValue = "10") int month,
                               Model model) {
        Map<String, Object> monthlyReport = reportService.getMonthlyDonationVsUsageReport(year, month);
        model.addAttribute("report", monthlyReport);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("title", "Monthly Report - Admin");
        return "monthly-report";
    }

    @GetMapping("/blood-demand")
    public String bloodDemandReport(Model model) {
        Map<String, Object> demandReport = reportService.getMostDemandedBloodGroups();
        model.addAttribute("report", demandReport);
        model.addAttribute("title", "Blood Demand Analysis - Admin");
        return "blood-demand-report";
    }

    @GetMapping("/top-donors")
    public String topDonorsReport(Model model) {
        Map<String, Object> topDonorsReport = reportService.getTopActiveDonors(10);
        model.addAttribute("report", topDonorsReport);
        model.addAttribute("title", "Top Donors Report - Admin");
        return "top-donors-report";
    }

    @GetMapping("/inventory")
    public String inventoryReport(Model model) {
        Map<String, Object> inventoryReport = reportService.getInventoryStatusReport();
        model.addAttribute("report", inventoryReport);
        model.addAttribute("title", "Inventory Status Report - Admin");
        return "inventory-report";
    }

    @GetMapping("/comprehensive")
    public String comprehensiveReport(Model model) {
        Map<String, Object> comprehensiveReport = reportService.getComprehensiveReport();
        model.addAttribute("report", comprehensiveReport);
        model.addAttribute("title", "Comprehensive System Report - Admin");
        return "comprehensive-report";
    }
}