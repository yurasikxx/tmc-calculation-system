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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model) {

        Pageable pageable = PageRequest.of(
                page, size,
                Sort.by(Sort.Direction.fromString(sortDir), sortBy)
        );

        Page<Norm> normsPage;

        if (type != null && !type.isEmpty() && search != null && !search.isEmpty()) {
            normsPage = normService.findByTypeAndSearchForCurrentUser(type, search, pageable);
        } else if (type != null && !type.isEmpty()) {
            normsPage = normService.findByTypeForCurrentUser(type, pageable);
        } else if (search != null && !search.isEmpty()) {
            normsPage = normService.findBySearchForCurrentUser(search, pageable);
        } else {
            normsPage = normService.findAllForCurrentUser(pageable);
        }

        model.addAttribute("norms", normsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", normsPage.getTotalPages());
        model.addAttribute("totalItems", normsPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("selectedType", type);
        model.addAttribute("searchQuery", search);
        model.addAttribute("availableTypes", normService.getAvailableTypeNames());

        return "norms/list";
    }

    @GetMapping("/create")
    public String createForm(Model model, RedirectAttributes redirectAttributes) {
        if (!canEditNorms(getCurrentUserRole())) {
            redirectAttributes.addFlashAttribute("error",
                    "У вас нет прав на создание норм");
            return "redirect:/norms";
        }
        model.addAttribute("norm", new Norm());
        model.addAttribute("tmcItems", tmcItemService.findAllAvailableForCurrentUser());
        model.addAttribute("professions", professionService.findAll());
        return "norms/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Norm norm, RedirectAttributes redirectAttributes) {
        try {
            normService.create(norm);
            redirectAttributes.addFlashAttribute("success", "Норма создана");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/norms";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model,
                           RedirectAttributes redirectAttributes) {
        if (!canEditNorms(getCurrentUserRole())) {
            redirectAttributes.addFlashAttribute("error",
                    "У вас нет прав на редактирование норм");
            return "redirect:/norms";
        }
        model.addAttribute("norm", normService.findById(id));
        model.addAttribute("tmcItems", tmcItemService.findAllAvailableForCurrentUser());
        model.addAttribute("professions", professionService.findAll());
        return "norms/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Norm norm,
                         RedirectAttributes redirectAttributes) {
        try {
            normService.update(id, norm);
            redirectAttributes.addFlashAttribute("success", "Норма обновлена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/norms";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            normService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Норма удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: " + e.getMessage());
        }
        return "redirect:/norms";
    }

    private boolean canEditNorms(String role) {
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