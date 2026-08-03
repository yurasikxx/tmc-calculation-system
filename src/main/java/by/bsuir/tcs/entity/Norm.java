package by.bsuir.tcs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "norms", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"tmc_id", "profession_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Norm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tmc_id", nullable = false)
    private TmcItem tmcItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profession_id", nullable = false)
    private Profession profession;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "period_months", nullable = false)
    private Integer periodMonths;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}