package by.bsuir.tcs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "siz_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SizAttributes {

    @Id
    @Column(name = "tmc_id")
    private Long tmcId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "tmc_id")
    private TmcItem tmcItem;

    @Column(name = "size", length = 20)
    private String size;

    @Column(name = "wear_period_months")
    private Integer wearPeriodMonths;

    @Column(name = "protection_class", length = 50)
    private String protectionClass;
}