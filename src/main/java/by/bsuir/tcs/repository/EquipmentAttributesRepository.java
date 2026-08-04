package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.EquipmentAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentAttributesRepository extends JpaRepository<EquipmentAttributes, Long> {
}