package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.StaffingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StaffingPlanRepository extends JpaRepository<StaffingPlan, Long> {
    List<StaffingPlan> findByEmployeeId(Long employeeId);
    List<StaffingPlan> findByEffectiveDateBetween(LocalDate start, LocalDate end);
}