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
public class ForecastDiagnostics {
    private Double aic;
    private Boolean converged;

    @JsonProperty("ljung_box_pvalue")
    private Double ljungBoxPvalue;

    @JsonProperty("intervals_capped")
    private Boolean intervalsCapped;

    @JsonProperty("n_obs")
    private Integer nObs;

    @JsonProperty("smoothing_level")
    private Double smoothingLevel;

    @JsonProperty("smoothing_trend")
    private Double smoothingTrend;

    @JsonProperty("smoothing_seasonal")
    private Double smoothingSeasonal;
}