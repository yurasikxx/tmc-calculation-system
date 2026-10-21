package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.*;
import by.bsuir.tcs.service.TmcItemService;
import by.bsuir.tcs.service.TmcTypeService;
import by.bsuir.tcs.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/tmc-items")
@RequiredArgsConstructor
public class TmcItemController {

    private final TmcItemService tmcItemService;
    private final TmcTypeService tmcTypeService;
    private final UnitService unitService;

    @GetMapping
    public String list(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {

        List<String> availableTypes = tmcTypeService.findAvailableTypeNames();

        if (type != null && !type.isEmpty() && !availableTypes.contains(type)) {
            type = null;
        }

        Pageable pageable = PageRequest.of(
                page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy)
        );

        Page<TmcItem> itemPage;

        if (type != null && !type.isEmpty() && search != null && !search.isEmpty()) {
            itemPage = tmcItemService.findByTypeAndSearch(type, search, pageable);
        } else if (type != null && !type.isEmpty()) {
            itemPage = tmcItemService.findByType(type, pageable);
        } else if (search != null && !search.isEmpty()) {
            itemPage = tmcItemService.findBySearchForTypes(availableTypes, search, pageable);
        } else {
            itemPage = tmcItemService.findByTypes(availableTypes, pageable);
        }

        model.addAttribute("tmcItems", itemPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", itemPage.getTotalPages());
        model.addAttribute("totalItems", itemPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("selectedType", type);
        model.addAttribute("searchQuery", search);
        model.addAttribute("availableTypes", availableTypes);
        model.addAttribute("tmcTypeService", tmcTypeService);

        return "tmc-items/list";
    }

    @GetMapping("/create")
    public String createForm(Model model, RedirectAttributes redirectAttributes) {
        String role = getCurrentUserRole();

        if (!canCreateTmc(role)) {
            redirectAttributes.addFlashAttribute("error",
                    "У вас нет прав на создание ТМЦ");
            return "redirect:/tmc-items";
        }

        TmcItem tmcItem = new TmcItem();

        TmcType defaultType;
        String attributeType;
        if ("ROLE_OT".equals(role)) {
            defaultType = tmcTypeService.findByName("SIZ");
            attributeType = "SIZ";
        } else if ("ROLE_TECHNOLOG".equals(role)) {
            defaultType = tmcTypeService.findByName("TOOL");
            attributeType = "TOOL";
        } else {
            defaultType = tmcTypeService.findByName("SIZ");
            attributeType = "SIZ";
        }

        tmcItem.setType(defaultType);

        model.addAttribute("tmcItem", tmcItem);
        model.addAttribute("attributeType", attributeType);
        model.addAttribute("tmcTypes", tmcTypeService.findAvailableForCurrentUser());
        model.addAttribute("units", unitService.findAll());

        model.addAttribute("sizAttributes", new SizAttributes());
        model.addAttribute("toolAttributes", new ToolAttributes());
        model.addAttribute("equipmentAttributes", new EquipmentAttributes());

        return "tmc-items/form";
    }

    @PostMapping("/create")
    public String create(
            @ModelAttribute TmcItem tmcItem,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) Integer wearPeriodMonths,
            @RequestParam(required = false) String protectionClass,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) String gostNumber,
            @RequestParam(required = false) String measurementRange,
            @RequestParam(required = false) String drawingNumber,
            @RequestParam(required = false) Integer maxCycles,
            @RequestParam(required = false) String machineModel,
            RedirectAttributes redirectAttributes) {

        try {
            if (tmcItem.getType() == null || tmcItem.getType().getId() == null) {
                throw new RuntimeException("Тип ТМЦ не выбран");
            }

            Object attributes = null;
            TmcType type = tmcTypeService.findById(tmcItem.getType().getId());

            if ("SIZ".equals(type.getName())) {
                attributes = SizAttributes.builder()
                        .size(size)
                        .wearPeriodMonths(wearPeriodMonths)
                        .protectionClass(protectionClass)
                        .build();
            } else if ("TOOL".equals(type.getName())) {
                attributes = ToolAttributes.builder()
                        .material(material)
                        .gostNumber(gostNumber)
                        .measurementRange(measurementRange)
                        .build();
            } else if ("EQUIPMENT".equals(type.getName())) {
                attributes = EquipmentAttributes.builder()
                        .drawingNumber(drawingNumber)
                        .maxCycles(maxCycles)
                        .machineModel(machineModel)
                        .build();
            }

            tmcItemService.create(tmcItem, attributes);
            redirectAttributes.addFlashAttribute("success", "ТМЦ успешно создана");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании ТМЦ: " + e.getMessage());
        }
        return "redirect:/tmc-items";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        String role = getCurrentUserRole();

        if (!canCreateTmc(role)) {
            redirectAttributes.addFlashAttribute("error",
                    "У вас нет прав на редактирование ТМЦ");
            return "redirect:/tmc-items";
        }

        TmcItem tmcItem = tmcItemService.findById(id);
        TmcType type = tmcItem.getType();
        String attributeType = type.getName();

        model.addAttribute("tmcItem", tmcItem);
        model.addAttribute("attributeType", attributeType);
        model.addAttribute("tmcTypes", tmcTypeService.findAvailableForCurrentUser());
        model.addAttribute("units", unitService.findAll());

        SizAttributes sizAttrs = tmcItemService.findSizAttributes(id);
        ToolAttributes toolAttrs = tmcItemService.findToolAttributes(id);
        EquipmentAttributes equipAttrs = tmcItemService.findEquipmentAttributes(id);

        model.addAttribute("sizAttributes", sizAttrs != null ? sizAttrs : new SizAttributes());
        model.addAttribute("toolAttributes", toolAttrs != null ? toolAttrs : new ToolAttributes());
        model.addAttribute("equipmentAttributes", equipAttrs != null ? equipAttrs : new EquipmentAttributes());

        return "tmc-items/form";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute TmcItem tmcItem,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) Integer wearPeriodMonths,
            @RequestParam(required = false) String protectionClass,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) String gostNumber,
            @RequestParam(required = false) String measurementRange,
            @RequestParam(required = false) String drawingNumber,
            @RequestParam(required = false) Integer maxCycles,
            @RequestParam(required = false) String machineModel,
            RedirectAttributes redirectAttributes) {

        try {
            if (tmcItem.getType() == null || tmcItem.getType().getId() == null) {
                throw new RuntimeException("Тип ТМЦ не выбран");
            }

            Object attributes = null;
            TmcType type = tmcTypeService.findById(tmcItem.getType().getId());

            if ("SIZ".equals(type.getName())) {
                attributes = SizAttributes.builder()
                        .size(size)
                        .wearPeriodMonths(wearPeriodMonths)
                        .protectionClass(protectionClass)
                        .build();
            } else if ("TOOL".equals(type.getName())) {
                attributes = ToolAttributes.builder()
                        .material(material)
                        .gostNumber(gostNumber)
                        .measurementRange(measurementRange)
                        .build();
            } else if ("EQUIPMENT".equals(type.getName())) {
                attributes = EquipmentAttributes.builder()
                        .drawingNumber(drawingNumber)
                        .maxCycles(maxCycles)
                        .machineModel(machineModel)
                        .build();
            }

            tmcItemService.update(id, tmcItem, attributes);
            redirectAttributes.addFlashAttribute("success", "ТМЦ успешно обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении ТМЦ: " + e.getMessage());
        }
        return "redirect:/tmc-items";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tmcItemService.delete(id);
            redirectAttributes.addFlashAttribute("success", "ТМЦ успешно удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении ТМЦ: " + e.getMessage());
        }
        return "redirect:/tmc-items";
    }

    private boolean canCreateTmc(String role) {
        return "ROLE_OT".equals(role)
                || "ROLE_TECHNOLOG".equals(role)
                || "ROLE_ADMIN".equals(role);
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