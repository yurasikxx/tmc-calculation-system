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
    private Long tmcId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "tmc_id")
    private TmcItem tmcItem;

    @Column(name = "drawing_number")
    private String drawingNumber;

    @Column(name = "max_cycles")
    private Integer maxCycles;

    @Column(name = "machine_model")
    private String machineModel;
}