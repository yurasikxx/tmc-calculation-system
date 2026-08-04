package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.SizAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizAttributesRepository extends JpaRepository<SizAttributes, Long> {
}