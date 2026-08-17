package it.aulab.progetto_finale_michele_macis.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.aulab.progetto_finale_michele_macis.models.User;
import it.aulab.progetto_finale_michele_macis.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsernameShouldDefaultToRoleUserWhenRolesAreMissing() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Admin User");
        user.setEmail("admin@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail("admin@example.com")).thenReturn(user);

        CustomUserDetails details = customUserDetailsService.loadUserByUsername("admin@example.com");

        assertEquals("admin@example.com", details.getUsername());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER")));
    }
}
