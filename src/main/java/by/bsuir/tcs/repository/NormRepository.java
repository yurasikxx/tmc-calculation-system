package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.Norm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NormRepository extends JpaRepository<Norm, Long> {
    List<Norm> findByProfessionId(Long professionId);

    List<Norm> findByTmcItemId(Long tmcItemId);

    boolean existsByTmcItemIdAndProfessionId(Long tmcItemId, Long professionId);

    @Query("SELECT n FROM Norm n WHERE n.tmcItem.type.name = :typeName")
    List<Norm> findByTmcTypeName(@Param("typeName") String typeName);

    @Query("SELECT n FROM Norm n WHERE n.tmcItem.type.name = :typeName")
    Page<Norm> findByTmcTypeName(@Param("typeName") String typeName, Pageable pageable);

    @Query("SELECT n FROM Norm n WHERE n.profession.name LIKE %:professionName%")
    Page<Norm> findByProfessionName(@Param("professionName") String professionName, Pageable pageable);

    @Query("SELECT n FROM Norm n WHERE n.tmcItem.name LIKE %:tmcName% OR n.tmcItem.code LIKE %:tmcName%")
    Page<Norm> findByTmcName(@Param("tmcName") String tmcName, Pageable pageable);

    @Query("SELECT n FROM Norm n WHERE n.profession.name LIKE %:professionName% AND (n.tmcItem.name LIKE %:tmcName% OR n.tmcItem.code LIKE %:tmcName%)")
    Page<Norm> findByProfessionNameAndTmcName(@Param("professionName") String professionName, @Param("tmcName") String tmcName, Pageable pageable);

    @Query("SELECT n FROM Norm n WHERE n.profession.name LIKE %:search% OR n.tmcItem.name LIKE %:search% OR n.tmcItem.code LIKE %:search%")
    Page<Norm> findBySearch(@Param("search") String search, Pageable pageable);
}