package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.IssuedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IssuedItemRepository extends JpaRepository<IssuedItem, Long> {

    List<IssuedItem> findByEmployeeId(Long employeeId);

    @Query("""
            SELECT i FROM IssuedItem i
            WHERE (:status IS NULL OR i.status = :status)
              AND (:tmcType IS NULL OR i.tmcItem.type.name = :tmcType)
            ORDER BY i.issueDate DESC
            """)
    Page<IssuedItem> findAllWithFilters(
            @Param("status") String status,
            @Param("tmcType") String tmcType,
            Pageable pageable);

    @Query("""
            SELECT i FROM IssuedItem i
            WHERE i.expectedReturnDate <= :date AND i.status = 'ISSUED'
            """)
    List<IssuedItem> findExpiringItems(@Param("date") LocalDate date);

    @EntityGraph(attributePaths = {"employee", "tmcItem", "tmcItem.type"})
    @Query("""
            SELECT i FROM IssuedItem i
            WHERE (:status IS NULL OR i.status = :status)
              AND (:tmcType IS NULL OR i.tmcItem.type.name = :tmcType)
              AND (:search IS NULL
                   OR LOWER(i.employee.fullName) LIKE :search
                   OR LOWER(i.tmcItem.name)     LIKE :search
                   OR LOWER(i.tmcItem.code)     LIKE :search)
            """)
    Page<IssuedItem> findAllFiltered(
            @Param("status") String status,
            @Param("tmcType") String tmcType,
            @Param("search") String search,
            Pageable pageable);

    @Query("""
            SELECT i FROM IssuedItem i
            JOIN FETCH i.tmcItem
            WHERE i.status = 'ISSUED'
              AND i.expectedReturnDate >= :date
            """)
    List<IssuedItem> findActiveByDate(@Param("date") LocalDate date);

    @Query("""
            SELECT i FROM IssuedItem i
            JOIN FETCH i.tmcItem
            WHERE i.status = 'ISSUED'
              AND i.expectedReturnDate >= :from
              AND i.expectedReturnDate <  :to
            """)
    List<IssuedItem> findExpiringBetween(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
}