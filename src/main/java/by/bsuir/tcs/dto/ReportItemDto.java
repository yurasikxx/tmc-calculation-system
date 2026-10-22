package by.bsuir.tcs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportItemDto {
    private String tmcCode;
    private String tmcName;
    private String tmcType;
    private String unit;
    private Integer normBaseQuantity;
    private Integer hireQuantity;
    private Integer replacementQuantity;
    private Integer totalQuantity;
}