package by.bsuir.tcs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "calculation_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalculationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tmc_id", nullable = false)
    private TmcItem tmcItem;

    @Column(name = "required_quantity", nullable = false)
    private Integer requiredQuantity;

    @Column(name = "period_month", nullable = false)
    private Integer periodMonth;

    @Column(name = "period_year", nullable = false)
    private Integer periodYear;

    @Column(name = "calculation_date", updatable = false)
    private LocalDateTime calculationDate;

    @PrePersist
    protected void onCreate() {
        calculationDate = LocalDateTime.now();
    }
}