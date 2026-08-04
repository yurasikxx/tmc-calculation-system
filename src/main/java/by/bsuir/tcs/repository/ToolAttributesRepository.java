package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.ToolAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolAttributesRepository extends JpaRepository<ToolAttributes, Long> {
}