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
                siz.setTmcId(saved.getId());
                sizAttributesRepository.save(siz);
            } else if (type.getName().equals("TOOL") && attributes instanceof ToolAttributes) {
                ToolAttributes tool = (ToolAttributes) attributes;
                tool.setTmcId(saved.getId());
                toolAttributesRepository.save(tool);
            } else if (type.getName().equals("EQUIPMENT") && attributes instanceof EquipmentAttributes) {
                EquipmentAttributes equipment = (EquipmentAttributes) attributes;
                equipment.setTmcId(saved.getId());
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
            if (saved.getType().getName().equals("SIZ") && attributes instanceof SizAttributes) {
                sizAttributesRepository.findById(saved.getId())
                        .ifPresent(sizAttributesRepository::delete);
                SizAttributes siz = (SizAttributes) attributes;
                siz.setTmcId(saved.getId());
                sizAttributesRepository.save(siz);
            } else if (saved.getType().getName().equals("TOOL") && attributes instanceof ToolAttributes) {
                toolAttributesRepository.findById(saved.getId())
                        .ifPresent(toolAttributesRepository::delete);
                ToolAttributes tool = (ToolAttributes) attributes;
                tool.setTmcId(saved.getId());
                toolAttributesRepository.save(tool);
            } else if (saved.getType().getName().equals("EQUIPMENT") && attributes instanceof EquipmentAttributes) {
                equipmentAttributesRepository.findById(saved.getId())
                        .ifPresent(equipmentAttributesRepository::delete);
                EquipmentAttributes equipment = (EquipmentAttributes) attributes;
                equipment.setTmcId(saved.getId());
                equipmentAttributesRepository.save(equipment);
            } else {
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
}