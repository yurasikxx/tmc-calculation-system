package by.bsuir.tcs.controller.web;

import by.bsuir.tcs.entity.User;
import by.bsuir.tcs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;

    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @ModelAttribute("currentUserRoleName")
    public String getCurrentUserRoleName() {
        User user = userService.getCurrentUser();
        if (user == null || user.getRole() == null) {
            return "Аноним";
        }
        return UserService.getRussianRoleName(user.getRole().getName());
    }
}