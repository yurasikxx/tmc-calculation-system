package by.bsuir.tcs.repository;

import by.bsuir.tcs.entity.Norm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NormRepository extends JpaRepository<Norm, Long> {
    List<Norm> findByProfessionId(Long professionId);
    List<Norm> findByTmcItemId(Long tmcItemId);
    boolean existsByTmcItemIdAndProfessionId(Long tmcItemId, Long professionId);
}