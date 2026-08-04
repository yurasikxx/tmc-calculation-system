package by.bsuir.tcs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffingPlanRequest {
    private String fullName;
    private String professionName;
    private String departmentName;
    private String actionType;
    private LocalDate effectiveDate;
    private String newProfessionName;
    private String newDepartmentName;
}