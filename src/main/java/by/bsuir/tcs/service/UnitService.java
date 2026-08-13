package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.Unit;
import by.bsuir.tcs.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitService {

    private final UnitRepository unitRepository;

    public List<Unit> findAll() {
        return unitRepository.findAll();
    }

    public Unit findById(Long id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unit not found with id: " + id));
    }

    public Unit findByCode(String code) {
        return unitRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Unit not found with code: " + code));
    }
}