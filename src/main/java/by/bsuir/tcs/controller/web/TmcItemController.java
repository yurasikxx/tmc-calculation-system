package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.*;
import by.bsuir.tcs.service.TmcItemService;
import by.bsuir.tcs.service.TmcTypeService;
import by.bsuir.tcs.service.UnitService;
import lombok.RequiredArgsConstructor;
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
            Model model) {

        List<TmcItem> items;
        if (type != null && !type.isEmpty()) {
            items = tmcItemService.findByTypeName(type);
        } else {
            items = tmcItemService.findAllForCurrentUser();
        }

        model.addAttribute("tmcItems", items);
        model.addAttribute("selectedType", type);
        return "tmc-items/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        TmcItem tmcItem = new TmcItem();
        String role = getCurrentUserRole();

        TmcType defaultType = null;
        String attributeType = null;

        if ("ROLE_OT".equals(role)) {
            defaultType = tmcTypeService.findByName("SIZ");
            attributeType = "SIZ";
        } else if ("ROLE_TECHNOLOG".equals(role)) {
            defaultType = tmcTypeService.findByName("EQUIPMENT");
            attributeType = "EQUIPMENT";
        } else if ("ROLE_STOREKEEPER".equals(role)) {
            defaultType = tmcTypeService.findByName("TOOL");
            attributeType = "TOOL";
        }

        if (defaultType != null) {
            tmcItem.setType(defaultType);
        }

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
            redirectAttributes.addFlashAttribute("success", "ТМЦ успешно создан");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании ТМЦ: " + e.getMessage());
        }
        return "redirect:/tmc-items";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        TmcItem tmcItem = tmcItemService.findById(id);
        TmcType type = tmcItem.getType();
        String attributeType = type.getName();

        model.addAttribute("tmcItem", tmcItem);
        model.addAttribute("attributeType", attributeType);
        model.addAttribute("tmcTypes", tmcTypeService.findAvailableForCurrentUser());
        model.addAttribute("units", unitService.findAll());

        if ("SIZ".equals(attributeType)) {
            SizAttributes attrs = tmcItemService.findSizAttributes(id);
            model.addAttribute("attributes", attrs != null ? attrs : new SizAttributes());
        } else if ("TOOL".equals(attributeType)) {
            ToolAttributes attrs = tmcItemService.findToolAttributes(id);
            model.addAttribute("attributes", attrs != null ? attrs : new ToolAttributes());
        } else if ("EQUIPMENT".equals(attributeType)) {
            EquipmentAttributes attrs = tmcItemService.findEquipmentAttributes(id);
            model.addAttribute("attributes", attrs != null ? attrs : new EquipmentAttributes());
        }

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
            redirectAttributes.addFlashAttribute("success", "ТМЦ успешно обновлён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении ТМЦ: " + e.getMessage());
        }
        return "redirect:/tmc-items";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            tmcItemService.delete(id);
            redirectAttributes.addFlashAttribute("success", "ТМЦ успешно удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении ТМЦ: " + e.getMessage());
        }
        return "redirect:/tmc-items";
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