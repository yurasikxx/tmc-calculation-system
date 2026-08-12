package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.dto.StaffingPlanRequest;
import by.bsuir.tcs.entity.StaffingPlan;
import by.bsuir.tcs.service.StaffingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

@Controller("webStaffingPlanController")
@RequestMapping("/staffing-plans")
@RequiredArgsConstructor
public class StaffingPlanController {

    private final StaffingPlanService staffingPlanService;
    private final ObjectMapper objectMapper;

    @GetMapping("/upload")
    public String uploadForm() {
        return "staffing-plans/upload";
    }

    @PostMapping("/upload")
    public String upload(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        try {
            String json = new String(file.getBytes());
            List<StaffingPlanRequest> requests = Arrays.asList(
                    objectMapper.readValue(json, StaffingPlanRequest[].class)
            );

            List<StaffingPlan> saved = staffingPlanService.importPlan(requests);
            redirectAttributes.addFlashAttribute("success",
                    "Загружено " + saved.size() + " кадровых событий");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка при загрузке: " + e.getMessage());
        }

        return "redirect:/staffing-plans/upload";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("plans", staffingPlanService.findAll());
        return "staffing-plans/list";
    }
}