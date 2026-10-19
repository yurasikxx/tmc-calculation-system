package by.bsuir.tcs.dto.forecast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastResponse {
    private List<ForecastPoint> forecast;
    private String method;
}