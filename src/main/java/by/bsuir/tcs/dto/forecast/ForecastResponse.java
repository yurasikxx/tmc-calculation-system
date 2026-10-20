package by.bsuir.tcs.dto.forecast;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastResponse {
    private List<ForecastPoint> forecast;
    private String method;
    private String reliability;
    private ForecastDiagnostics diagnostics;
}