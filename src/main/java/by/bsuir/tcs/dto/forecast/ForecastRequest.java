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
public class ForecastRequest {
    private List<Double> history;
    private Integer steps;
}