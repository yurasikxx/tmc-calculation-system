package by.bsuir.tcs.dto.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastPoint {
    private Integer step;
    private Double value;

    @JsonProperty("lower_bound")
    private Double lowerBound;

    @JsonProperty("upper_bound")
    private Double upperBound;

    @JsonProperty("period_year")
    private Integer periodYear;

    @JsonProperty("period_month")
    private Integer periodMonth;

    private String label;

    private String shortLabel;
}