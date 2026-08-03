package by.bsuir.tcs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tool_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToolAttributes {

    @Id
    @Column(name = "tmc_id")
    private Long tmcId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "tmc_id")
    private TmcItem tmcItem;

    @Column(name = "material", length = 100)
    private String material;

    @Column(name = "gost_number", length = 50)
    private String gostNumber;

    @Column(name = "measurement_range", length = 50)
    private String measurementRange;
}