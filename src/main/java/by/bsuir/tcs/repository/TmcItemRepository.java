package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.TmcItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TmcItemRepository extends JpaRepository<TmcItem, Long> {
    Optional<TmcItem> findByCode(String code);

    List<TmcItem> findByTypeId(Long typeId);

    @Query("SELECT i FROM TmcItem i WHERE i.type.name = :typeName")
    List<TmcItem> findByTypeName(@Param("typeName") String typeName);
}