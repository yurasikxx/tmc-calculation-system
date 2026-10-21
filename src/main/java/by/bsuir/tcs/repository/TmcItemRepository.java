package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.TmcItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TmcItemRepository extends JpaRepository<TmcItem, Long> {

    Optional<TmcItem> findByCode(String code);

    List<TmcItem> findAllByOrderByIdDesc();

    List<TmcItem> findByTypeNameOrderByIdDesc(String typeName);

    @Query("SELECT i FROM TmcItem i WHERE i.type.name = :typeName")
    Page<TmcItem> findByTypeName(@Param("typeName") String typeName, Pageable pageable);

    @Query("SELECT i FROM TmcItem i WHERE i.type.name = :typeName")
    List<TmcItem> findByTypeName(@Param("typeName") String typeName);

    Page<TmcItem> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(String code, String name, Pageable pageable);

    @Query("SELECT i FROM TmcItem i WHERE i.type.name = :typeName AND (i.code LIKE %:search% OR i.name LIKE %:search%)")
    Page<TmcItem> findByTypeNameAndCodeContainingIgnoreCaseOrNameContainingIgnoreCase(
            @Param("typeName") String typeName,
            @Param("search") String search1,
            @Param("search") String search2,
            Pageable pageable);

    @Query("SELECT t.type.name, COUNT(t) FROM TmcItem t GROUP BY t.type.name")
    List<Object[]> countByType();

    @Query("""
            SELECT t FROM TmcItem t
            WHERE t.type.name IN :typeNames
            """)
    Page<TmcItem> findByTypeNameIn(
            @Param("typeNames") List<String> typeNames,
            Pageable pageable);

    @Query("""
            SELECT t FROM TmcItem t
            WHERE t.type.name IN :typeNames
              AND (LOWER(t.name) LIKE CONCAT('%', :search, '%')
                   OR LOWER(t.code) LIKE CONCAT('%', :search, '%'))
            """)
    Page<TmcItem> findByTypeNameInAndSearch(
            @Param("typeNames") List<String> typeNames,
            @Param("search") String search,
            Pageable pageable);
}