package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.EquipmentAttributes;
import by.bsuir.tcs.entity.SizAttributes;
import by.bsuir.tcs.entity.TmcItem;
import by.bsuir.tcs.entity.TmcType;
import by.bsuir.tcs.entity.ToolAttributes;
import by.bsuir.tcs.repository.EquipmentAttributesRepository;
import by.bsuir.tcs.repository.SizAttributesRepository;
import by.bsuir.tcs.repository.TmcItemRepository;
import by.bsuir.tcs.repository.TmcTypeRepository;
import by.bsuir.tcs.repository.ToolAttributesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TmcItemService {

    private final TmcItemRepository tmcItemRepository;
    private final TmcTypeRepository tmcTypeRepository;
    private final SizAttributesRepository sizAttributesRepository;
    private final ToolAttributesRepository toolAttributesRepository;
    private final EquipmentAttributesRepository equipmentAttributesRepository;

    @Transactional(readOnly = true)
    public List<TmcItem> findAll() {
        return tmcItemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<TmcItem> findAllForCurrentUser() {
        String role = getCurrentUserRole();
        return switch (role) {
            case "ROLE_OT" -> tmcItemRepository.findByTypeName("SIZ");
            case "ROLE_TECHNOLOG" -> tmcItemRepository.findByTypeName("EQUIPMENT");
            case "ROLE_STOREKEEPER" -> tmcItemRepository.findByTypeName("TOOL");
            case "ROLE_ADMIN", "ROLE_LABOR", "ROLE_MTS" -> tmcItemRepository.findAll();
            default -> List.of();
        };
    }

    @Transactional(readOnly = true)
    public TmcItem findById(Long id) {
        return tmcItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TmcItem not found with id: " + id));
    }

    @Transactional
    public TmcItem create(TmcItem tmcItem, Object attributes) {
        if (tmcItem.getCode() == null || tmcItem.getCode().isEmpty()) {
            throw new RuntimeException("Code must not be empty");
        }

        if (tmcItemRepository.findByCode(tmcItem.getCode()).isPresent()) {
            throw new RuntimeException("TmcItem with code '" + tmcItem.getCode() + "' already exists");
        }

        TmcType type = tmcTypeRepository.findById(tmcItem.getType().getId())
                .orElseThrow(() -> new RuntimeException("TmcType not found"));

        tmcItem.setType(type);

        TmcItem saved = tmcItemRepository.save(tmcItem);

        if (attributes != null) {
            if (type.getName().equals("SIZ") && attributes instanceof SizAttributes) {
                SizAttributes siz = (SizAttributes) attributes;
                siz.setTmcItem(saved);
                sizAttributesRepository.save(siz);
            } else if (type.getName().equals("TOOL") && attributes instanceof ToolAttributes) {
                ToolAttributes tool = (ToolAttributes) attributes;
                tool.setTmcItem(saved);
                toolAttributesRepository.save(tool);
            } else if (type.getName().equals("EQUIPMENT") && attributes instanceof EquipmentAttributes) {
                EquipmentAttributes equipment = (EquipmentAttributes) attributes;
                equipment.setTmcItem(saved);
                equipmentAttributesRepository.save(equipment);
            } else {
                throw new RuntimeException("Invalid attribute type for TmcType: " + type.getName());
            }
        }

        return saved;
    }

    @Transactional
    public TmcItem update(Long id, TmcItem updatedTmcItem, Object attributes) {
        TmcItem existing = findById(id);

        if (updatedTmcItem.getCode() != null && !updatedTmcItem.getCode().isEmpty()) {
            if (!existing.getCode().equals(updatedTmcItem.getCode()) &&
                    tmcItemRepository.findByCode(updatedTmcItem.getCode()).isPresent()) {
                throw new RuntimeException("TmcItem with code '" + updatedTmcItem.getCode() + "' already exists");
            }
            existing.setCode(updatedTmcItem.getCode());
        }

        if (updatedTmcItem.getName() != null) {
            existing.setName(updatedTmcItem.getName());
        }

        if (updatedTmcItem.getUnit() != null) {
            existing.setUnit(updatedTmcItem.getUnit());
        }

        if (updatedTmcItem.getServiceLifeMonths() != null) {
            existing.setServiceLifeMonths(updatedTmcItem.getServiceLifeMonths());
        }

        if (updatedTmcItem.getType() != null && updatedTmcItem.getType().getId() != null) {
            TmcType type = tmcTypeRepository.findById(updatedTmcItem.getType().getId())
                    .orElseThrow(() -> new RuntimeException("TmcType not found"));
            existing.setType(type);
        }

        TmcItem saved = tmcItemRepository.save(existing);

        if (attributes != null) {
            switch (attributes) {
                case SizAttributes siz when saved.getType().getName().equals("SIZ") -> {
                    sizAttributesRepository.findById(saved.getId())
                            .ifPresent(sizAttributesRepository::delete);
                    siz.setTmcId(saved.getId());
                    sizAttributesRepository.save(siz);
                }
                case ToolAttributes tool when saved.getType().getName().equals("TOOL") -> {
                    toolAttributesRepository.findById(saved.getId())
                            .ifPresent(toolAttributesRepository::delete);
                    tool.setTmcId(saved.getId());
                    toolAttributesRepository.save(tool);
                }
                case EquipmentAttributes equipment when saved.getType().getName().equals("EQUIPMENT") -> {
                    equipmentAttributesRepository.findById(saved.getId())
                            .ifPresent(equipmentAttributesRepository::delete);
                    equipment.setTmcId(saved.getId());
                    equipmentAttributesRepository.save(equipment);
                }
                default ->
                        throw new RuntimeException("Invalid attribute type for TmcType: " + saved.getType().getName());
            }
        }

        return saved;
    }

    @Transactional
    public void delete(Long id) {
        TmcItem tmcItem = findById(id);
        tmcItemRepository.delete(tmcItem);
    }

    public boolean hasAccessToTmcItem(TmcItem tmcItem) {
        String role = getCurrentUserRole();
        return switch (role) {
            case "ROLE_OT" -> tmcItem.getType().getName().equals("SIZ");
            case "ROLE_TECHNOLOG" -> tmcItem.getType().getName().equals("EQUIPMENT");
            case "ROLE_STOREKEEPER" -> tmcItem.getType().getName().equals("TOOL");
            case "ROLE_ADMIN", "ROLE_LABOR", "ROLE_MTS" -> true;
            default -> false;
        };
    }

    @Transactional(readOnly = true)
    public SizAttributes findSizAttributes(Long tmcId) {
        return sizAttributesRepository.findById(tmcId).orElse(null);
    }

    @Transactional(readOnly = true)
    public ToolAttributes findToolAttributes(Long tmcId) {
        return toolAttributesRepository.findById(tmcId).orElse(null);
    }

    @Transactional(readOnly = true)
    public EquipmentAttributes findEquipmentAttributes(Long tmcId) {
        return equipmentAttributesRepository.findById(tmcId).orElse(null);
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