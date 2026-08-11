package by.bsuir.tcs.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsDto {
    private long departmentsCount;
    private long professionsCount;
    private long tmcItemsCount;
    private long normsCount;
    private long calculationsCount;
}