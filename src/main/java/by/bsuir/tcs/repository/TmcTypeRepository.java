package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.TmcType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TmcTypeRepository extends JpaRepository<TmcType, Long> {
    Optional<TmcType> findByName(String name);
    List<TmcType> findByNameIn(List<String> names);
}