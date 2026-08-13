package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.User;
import by.bsuir.tcs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameWithRole(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!user.getIsActive()) {
            throw new DisabledException("Ваша учетная запись заблокирована. Обратитесь к администратору.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName()))
        );
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsernameWithRole(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            return null;
        }
        return findByUsername(auth.getName());
    }

    public static String getRussianRoleName(String role) {
        return switch (role) {
            case "ROLE_OT" -> "Инженер по охране труда";
            case "ROLE_TECHNOLOG" -> "Инженер-технолог";
            case "ROLE_STOREKEEPER" -> "Кладовщик ИРК";
            case "ROLE_LABOR" -> "Инженер по ОиНТ";
            case "ROLE_MTS" -> "Инженер по МТС";
            case "ROLE_ADMIN" -> "Администратор";
            default -> role;
        };
    }
}