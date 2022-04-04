package uz.pdp.cinemaroomrestfullservice.controller.adminController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.cinemaroomrestfullservice.payload.AdminStats;
import uz.pdp.cinemaroomrestfullservice.service.admin.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @GetMapping("/dashboard-stats")
    public HttpEntity<?> getStatistics(){
        AdminStats statistics = adminService.getStatistics();
        return ResponseEntity.status(200).body(statistics);
    }

}
