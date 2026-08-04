package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.Norm;
import by.bsuir.tcs.service.NormService;
import by.bsuir.tcs.service.ProfessionService;
import by.bsuir.tcs.service.TmcItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/norms")
@RequiredArgsConstructor
public class NormController {

    private final NormService normService;
    private final TmcItemService tmcItemService;
    private final ProfessionService professionService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("norms", normService.findAll());
        return "norms/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("norm", new Norm());
        model.addAttribute("tmcItems", tmcItemService.findAll());
        model.addAttribute("professions", professionService.findAll());
        return "norms/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Norm norm) {
        normService.create(norm);
        return "redirect:/norms";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("norm", normService.findById(id));
        model.addAttribute("tmcItems", tmcItemService.findAll());
        model.addAttribute("professions", professionService.findAll());
        return "norms/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Norm norm) {
        normService.update(id, norm);
        return "redirect:/norms";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        normService.delete(id);
        return "redirect:/norms";
    }
}