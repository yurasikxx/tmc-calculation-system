package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.dto.DashboardStatsDto;
import by.bsuir.tcs.dto.RecentCalculationDto;
import by.bsuir.tcs.service.DashboardService;
import by.bsuir.tcs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final UserService userService;

    @GetMapping("/")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser");

        if (isAuthenticated) {
            DashboardStatsDto stats = dashboardService.getStats();
            List<RecentCalculationDto> recentCalculations = dashboardService.getRecentCalculations();

            String role = userService.getCurrentUserRoleName();
            model.addAttribute("stats", stats);
            model.addAttribute("recentCalculations", recentCalculations);
            model.addAttribute("userRole", role);
            model.addAttribute("userName", userService.getCurrentUserDisplayName());
            model.addAttribute("isAdmin", "ROLE_ADMIN".equals(role));
            model.addAttribute("isMTS", "ROLE_MTS".equals(role));
            model.addAttribute("isOT", "ROLE_OT".equals(role));
            model.addAttribute("isTechnolog", "ROLE_TECHNOLOG".equals(role));
            model.addAttribute("isStorekeeper", "ROLE_STOREKEEPER".equals(role));
        }

        return "index";
    }
}