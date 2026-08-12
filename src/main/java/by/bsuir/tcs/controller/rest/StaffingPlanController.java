package by.bsuir.tcs.controller.rest;

import by.bsuir.tcs.dto.StaffingPlanRequest;
import by.bsuir.tcs.entity.StaffingPlan;
import by.bsuir.tcs.service.StaffingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("restStaffingPlanController")
@RequestMapping("/api/staffing-plans")
@RequiredArgsConstructor
public class StaffingPlanController {

    private final StaffingPlanService staffingPlanService;

    @PostMapping("/upload")
    public ResponseEntity<List<StaffingPlan>> upload(@RequestBody List<StaffingPlanRequest> requests) {
        List<StaffingPlan> saved = staffingPlanService.importPlan(requests);
        return ResponseEntity.ok(saved);
    }
}