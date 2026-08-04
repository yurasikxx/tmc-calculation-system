package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.TmcType;
import by.bsuir.tcs.repository.TmcTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TmcTypeService {

    private final TmcTypeRepository tmcTypeRepository;

    public List<TmcType> findAll() {
        return tmcTypeRepository.findAll();
    }

    public TmcType findById(Long id) {
        return tmcTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TmcType not found"));
    }
}