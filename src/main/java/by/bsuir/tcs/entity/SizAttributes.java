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
    private Long tmcId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "tmc_id")
    private TmcItem tmcItem;

    @Column(name = "size")
    private String size;

    @Column(name = "wear_period_months")
    private Integer wearPeriodMonths;

    @Column(name = "protection_class")
    private String protectionClass;
}