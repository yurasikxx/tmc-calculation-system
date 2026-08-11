package by.bsuir.tcs.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RecentCalculationDto {
    private String period;
    private String tmcName;
    private Integer requiredQuantity;
    private LocalDateTime calculationDate;
}