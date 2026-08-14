package by.bsuir.tcs.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecentCalculationDto {
    private String period;
    private String tmcName;
    private String tmcCode;
    private Integer requiredQuantity;
    private String formattedDate;
}