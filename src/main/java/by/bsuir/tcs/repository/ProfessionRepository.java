package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.Profession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long> {
    Optional<Profession> findByName(String name);

    boolean existsByName(String name);

    Page<Profession> findByNameContainingIgnoreCase(String name, Pageable pageable);
}