package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.TmcType;
import by.bsuir.tcs.repository.TmcTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TmcTypeService {

    private final TmcTypeRepository tmcTypeRepository;

    @Transactional(readOnly = true)
    public List<TmcType> findAll() {
        return tmcTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TmcType findById(Long id) {
        return tmcTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тип ТМЦ не найден: " + id));
    }

    @Transactional(readOnly = true)
    public TmcType findByName(String name) {
        return tmcTypeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Тип ТМЦ не найден: " + name));
    }

    @Transactional(readOnly = true)
    public List<TmcType> findAvailableForCurrentUser() {
        String role = getCurrentUserRole();

        return switch (role) {
            case "ROLE_OT" -> List.of(findByName("SIZ"));
            case "ROLE_TECHNOLOG" -> List.of(findByName("TOOL"));
            case "ROLE_STOREKEEPER", "ROLE_ADMIN" -> findAll();
            default -> List.of();
        };
    }

    @Transactional(readOnly = true)
    public List<String> findAvailableTypeNames() {
        return findAvailableForCurrentUser().stream()
                .map(TmcType::getName)
                .toList();
    }

    public String getRussianName(String typeName) {
        return switch (typeName) {
            case "SIZ" -> "СИЗ";
            case "TOOL" -> "Инструмент";
            default -> typeName;
        };
    }

    private String getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            return "ROLE_ANONYMOUS";
        }
        return auth.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_ANONYMOUS");
    }
}