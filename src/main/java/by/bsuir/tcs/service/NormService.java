package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.Norm;
import by.bsuir.tcs.entity.Profession;
import by.bsuir.tcs.entity.TmcItem;
import by.bsuir.tcs.repository.NormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NormService {

    private final NormRepository normRepository;
    private final TmcItemService tmcItemService;
    private final ProfessionService professionService;

    @Transactional(readOnly = true)
    public List<Norm> findAll() {
        return normRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Norm findById(Long id) {
        return normRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Norm not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Norm> findByProfession(Long professionId) {
        return normRepository.findByProfessionId(professionId);
    }

    @Transactional(readOnly = true)
    public List<Norm> findByTmcItem(Long tmcItemId) {
        return normRepository.findByTmcItemId(tmcItemId);
    }

    @Transactional
    public Norm create(Norm norm) {
        if (norm.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be positive");
        }

        if (norm.getPeriodMonths() <= 0) {
            throw new RuntimeException("Period months must be positive");
        }

        TmcItem tmcItem = tmcItemService.findById(norm.getTmcItem().getId());
        Profession profession = professionService.findById(norm.getProfession().getId());

        if (normRepository.existsByTmcItemIdAndProfessionId(tmcItem.getId(), profession.getId())) {
            throw new RuntimeException("Norm already exists for this TmcItem and Profession");
        }

        norm.setTmcItem(tmcItem);
        norm.setProfession(profession);

        return normRepository.save(norm);
    }

    @Transactional
    public Norm update(Long id, Norm updatedNorm) {
        Norm existing = findById(id);

        if (updatedNorm.getQuantity() != null && updatedNorm.getQuantity() > 0) {
            existing.setQuantity(updatedNorm.getQuantity());
        }

        if (updatedNorm.getPeriodMonths() != null && updatedNorm.getPeriodMonths() > 0) {
            existing.setPeriodMonths(updatedNorm.getPeriodMonths());
        }

        if (updatedNorm.getTmcItem() != null && updatedNorm.getTmcItem().getId() != null) {
            TmcItem tmcItem = tmcItemService.findById(updatedNorm.getTmcItem().getId());
            existing.setTmcItem(tmcItem);
        }

        if (updatedNorm.getProfession() != null && updatedNorm.getProfession().getId() != null) {
            Profession profession = professionService.findById(updatedNorm.getProfession().getId());
            existing.setProfession(profession);
        }

        return normRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Norm norm = findById(id);
        normRepository.delete(norm);
    }
}