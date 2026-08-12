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
    private Long tmcId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "tmc_id")
    private TmcItem tmcItem;

    @Column(name = "material")
    private String material;

    @Column(name = "gost_number")
    private String gostNumber;

    @Column(name = "measurement_range")
    private String measurementRange;
}