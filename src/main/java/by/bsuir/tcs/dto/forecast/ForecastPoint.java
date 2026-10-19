package by.bsuir.tcs.dto.forecast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastPoint {
    private Integer step;
    private Double value;
    private Double lowerBound;
    private Double upperBound;
}