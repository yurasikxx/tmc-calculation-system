package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.EquipmentAttributes;
import by.bsuir.tcs.entity.SizAttributes;
import by.bsuir.tcs.entity.TmcItem;
import by.bsuir.tcs.entity.TmcType;
import by.bsuir.tcs.entity.ToolAttributes;
import by.bsuir.tcs.service.TmcItemService;
import by.bsuir.tcs.service.TmcTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tmc-items")
@RequiredArgsConstructor
public class TmcItemController {

    private final TmcItemService tmcItemService;
    private final TmcTypeService tmcTypeService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tmcItems", tmcItemService.findAll());
        return "tmc-items/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("tmcItem", new TmcItem());
        model.addAttribute("tmcTypes", tmcTypeService.findAll());
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
            @RequestParam(required = false) String machineModel) {

        Object attributes = null;
        TmcType type = tmcTypeService.findById(tmcItem.getType().getId());

        if (type.getName().equals("SIZ")) {
            attributes = SizAttributes.builder()
                    .size(size)
                    .wearPeriodMonths(wearPeriodMonths)
                    .protectionClass(protectionClass)
                    .build();
        } else if (type.getName().equals("TOOL")) {
            attributes = ToolAttributes.builder()
                    .material(material)
                    .gostNumber(gostNumber)
                    .measurementRange(measurementRange)
                    .build();
        } else if (type.getName().equals("EQUIPMENT")) {
            attributes = EquipmentAttributes.builder()
                    .drawingNumber(drawingNumber)
                    .maxCycles(maxCycles)
                    .machineModel(machineModel)
                    .build();
        }

        tmcItemService.create(tmcItem, attributes);
        return "redirect:/tmc-items";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        TmcItem tmcItem = tmcItemService.findById(id);
        model.addAttribute("tmcItem", tmcItem);
        model.addAttribute("tmcTypes", tmcTypeService.findAll());

        TmcType type = tmcItem.getType();
        if (type.getName().equals("SIZ")) {
            SizAttributes attrs = tmcItemService.findSizAttributes(id);
            model.addAttribute("attributes", attrs);
            model.addAttribute("attributeType", "SIZ");
        } else if (type.getName().equals("TOOL")) {
            ToolAttributes attrs = tmcItemService.findToolAttributes(id);
            model.addAttribute("attributes", attrs);
            model.addAttribute("attributeType", "TOOL");
        } else if (type.getName().equals("EQUIPMENT")) {
            EquipmentAttributes attrs = tmcItemService.findEquipmentAttributes(id);
            model.addAttribute("attributes", attrs);
            model.addAttribute("attributeType", "EQUIPMENT");
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
            @RequestParam(required = false) String machineModel) {

        Object attributes = null;
        TmcType type = tmcTypeService.findById(tmcItem.getType().getId());

        if (type.getName().equals("SIZ")) {
            attributes = SizAttributes.builder()
                    .size(size)
                    .wearPeriodMonths(wearPeriodMonths)
                    .protectionClass(protectionClass)
                    .build();
        } else if (type.getName().equals("TOOL")) {
            attributes = ToolAttributes.builder()
                    .material(material)
                    .gostNumber(gostNumber)
                    .measurementRange(measurementRange)
                    .build();
        } else if (type.getName().equals("EQUIPMENT")) {
            attributes = EquipmentAttributes.builder()
                    .drawingNumber(drawingNumber)
                    .maxCycles(maxCycles)
                    .machineModel(machineModel)
                    .build();
        }

        tmcItemService.update(id, tmcItem, attributes);
        return "redirect:/tmc-items";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        tmcItemService.delete(id);
        return "redirect:/tmc-items";
    }
}