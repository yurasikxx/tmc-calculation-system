package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.Norm;
import by.bsuir.tcs.entity.Profession;
import by.bsuir.tcs.entity.TmcItem;
import by.bsuir.tcs.repository.NormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public Page<Norm> findAllForCurrentUser(Pageable pageable) {
        String role = getCurrentUserRole();
        return switch (role) {
            case "ROLE_OT" -> normRepository.findByTmcTypeName("SIZ", pageable);
            case "ROLE_TECHNOLOG" -> normRepository.findByTmcTypeName("EQUIPMENT", pageable);
            case "ROLE_STOREKEEPER" -> normRepository.findByTmcTypeName("TOOL", pageable);
            case "ROLE_ADMIN", "ROLE_LABOR", "ROLE_MTS" -> normRepository.findAll(pageable);
            default -> Page.empty(pageable);
        };
    }

    @Transactional(readOnly = true)
    public Page<Norm> findByProfession(String professionName, Pageable pageable) {
        return normRepository.findByProfessionName(professionName, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Norm> findByTmc(String tmcName, Pageable pageable) {
        return normRepository.findByTmcName(tmcName, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Norm> findByProfessionAndTmc(String professionName, String tmcName, Pageable pageable) {
        return normRepository.findByProfessionNameAndTmcName(professionName, tmcName, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Norm> findBySearch(String search, Pageable pageable) {
        return normRepository.findBySearch(search, pageable);
    }

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

        if (!tmcItemService.hasAccessToTmcItem(tmcItem)) {
            throw new RuntimeException("You don't have permission to create norms for this type of TMC");
        }

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

        if (!tmcItemService.hasAccessToTmcItem(existing.getTmcItem())) {
            throw new RuntimeException("You don't have permission to update norms for this type of TMC");
        }

        if (updatedNorm.getQuantity() != null && updatedNorm.getQuantity() > 0) {
            existing.setQuantity(updatedNorm.getQuantity());
        }

        if (updatedNorm.getPeriodMonths() != null && updatedNorm.getPeriodMonths() > 0) {
            existing.setPeriodMonths(updatedNorm.getPeriodMonths());
        }

        if (updatedNorm.getTmcItem() != null && updatedNorm.getTmcItem().getId() != null) {
            TmcItem tmcItem = tmcItemService.findById(updatedNorm.getTmcItem().getId());
            if (!tmcItemService.hasAccessToTmcItem(tmcItem)) {
                throw new RuntimeException("You don't have permission to use this TMC item");
            }
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

        if (!tmcItemService.hasAccessToTmcItem(norm.getTmcItem())) {
            throw new RuntimeException("You don't have permission to delete norms for this type of TMC");
        }

        normRepository.delete(norm);
    }

    private String getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return "ROLE_ANONYMOUS";
        }
        return auth.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_ANONYMOUS");
    }
}