package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.TmcType;
import by.bsuir.tcs.repository.TmcTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public TmcType findByName(String name) {
        return tmcTypeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("TmcType not found: " + name));
    }

    public List<TmcType> findAvailableForCurrentUser() {
        String role = getCurrentUserRole();
        return switch (role) {
            case "ROLE_OT" -> tmcTypeRepository.findByNameIn(List.of("SIZ"));
            case "ROLE_TECHNOLOG" -> tmcTypeRepository.findByNameIn(List.of("EQUIPMENT"));
            case "ROLE_STOREKEEPER" -> tmcTypeRepository.findByNameIn(List.of("TOOL"));
            case "ROLE_ADMIN", "ROLE_LABOR", "ROLE_MTS" -> tmcTypeRepository.findAll();
            default -> List.of();
        };
    }

    public static String getRussianName(String typeName) {
        return switch (typeName) {
            case "SIZ" -> "СИЗ";
            case "TOOL" -> "Инструмент";
            case "EQUIPMENT" -> "Оснастка";
            default -> typeName;
        };
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