package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.User;
import by.bsuir.tcs.entity.UserRole;
import by.bsuir.tcs.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void loadUserByUsername_success() {
        User user = User.builder()
                .username("admin").passwordHash("$2a$10$hash")
                .isActive(true)
                .role(UserRole.builder().name("ROLE_ADMIN").build())
                .build();
        when(userRepository.findByUsernameWithRole("admin")).thenReturn(Optional.of(user));

        UserDetails details = userService.loadUserByUsername("admin");

        assertEquals("admin", details.getUsername());
        assertEquals("$2a$10$hash", details.getPassword());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN")));
    }

    @Test
    void loadUserByUsername_inactive_throwsDisabled() {
        User user = User.builder()
                .username("blocked").passwordHash("hash").isActive(false)
                .role(UserRole.builder().name("ROLE_OT").build()).build();
        when(userRepository.findByUsernameWithRole("blocked")).thenReturn(Optional.of(user));

        assertThrows(org.springframework.security.authentication.DisabledException.class,
                () -> userService.loadUserByUsername("blocked"));
    }

    @Test
    void findByUsername_notFound_throwsException() {
        when(userRepository.findByUsernameWithRole("ghost")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("ghost"));
    }
}