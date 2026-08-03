package by.bsuir.tcs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipment_attributes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentAttributes {

    @Id
    @Column(name = "tmc_id")
    private Long tmcId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "tmc_id")
    private TmcItem tmcItem;

    @Column(name = "drawing_number", length = 50)
    private String drawingNumber;

    @Column(name = "max_cycles")
    private Integer maxCycles;

    @Column(name = "machine_model", length = 100)
    private String machineModel;
}