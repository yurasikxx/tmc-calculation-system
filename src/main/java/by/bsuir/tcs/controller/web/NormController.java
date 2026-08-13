package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.Norm;
import by.bsuir.tcs.service.NormService;
import by.bsuir.tcs.service.ProfessionService;
import by.bsuir.tcs.service.TmcItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/norms")
@RequiredArgsConstructor
public class NormController {

    private final NormService normService;
    private final TmcItemService tmcItemService;
    private final ProfessionService professionService;

    @GetMapping
    public String list(
            @RequestParam(required = false) String profession,
            @RequestParam(required = false) String tmc,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        Page<Norm> normPage;

        if (profession != null && !profession.isEmpty() && tmc != null && !tmc.isEmpty()) {
            normPage = normService.findByProfessionAndTmc(profession, tmc, pageable);
        } else if (profession != null && !profession.isEmpty()) {
            normPage = normService.findByProfession(profession, pageable);
        } else if (tmc != null && !tmc.isEmpty()) {
            normPage = normService.findByTmc(tmc, pageable);
        } else if (search != null && !search.isEmpty()) {
            normPage = normService.findBySearch(search, pageable);
        } else {
            normPage = normService.findAllForCurrentUser(pageable);
        }

        model.addAttribute("norms", normPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", normPage.getTotalPages());
        model.addAttribute("totalItems", normPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("selectedProfession", profession);
        model.addAttribute("selectedTmc", tmc);
        model.addAttribute("searchQuery", search);
        model.addAttribute("allProfessions", professionService.findAll());
        model.addAttribute("allTmcItems", tmcItemService.findAllForCurrentUser(Pageable.unpaged()).getContent());

        return "norms/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("norm", new Norm());
        model.addAttribute("tmcItems", tmcItemService.findAllForCurrentUser(Pageable.unpaged()).getContent());
        model.addAttribute("professions", professionService.findAll());
        return "norms/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Norm norm, RedirectAttributes redirectAttributes) {
        try {
            normService.create(norm);
            redirectAttributes.addFlashAttribute("success", "Норма выдачи успешно создана");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании нормы: " + e.getMessage());
        }
        return "redirect:/norms";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("norm", normService.findById(id));
        model.addAttribute("tmcItems", tmcItemService.findAllForCurrentUser(Pageable.unpaged()).getContent());
        model.addAttribute("professions", professionService.findAll());
        return "norms/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Norm norm, RedirectAttributes redirectAttributes) {
        try {
            normService.update(id, norm);
            redirectAttributes.addFlashAttribute("success", "Норма выдачи успешно обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении нормы: " + e.getMessage());
        }
        return "redirect:/norms";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            normService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Норма выдачи успешно удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении нормы: " + e.getMessage());
        }
        return "redirect:/norms";
    }
}