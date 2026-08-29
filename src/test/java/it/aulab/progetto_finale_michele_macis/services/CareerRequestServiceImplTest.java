package it.aulab.progetto_finale_michele_macis.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.aulab.progetto_finale_michele_macis.models.CareerRequest;
import it.aulab.progetto_finale_michele_macis.models.Role;
import it.aulab.progetto_finale_michele_macis.models.User;
import it.aulab.progetto_finale_michele_macis.repositories.CareerRequestRepository;
import it.aulab.progetto_finale_michele_macis.repositories.RoleRepository;
import it.aulab.progetto_finale_michele_macis.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class CareerRequestServiceImplTest {

    @Mock
    private CareerRequestRepository careerRequestRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private CareerRequestServiceImpl careerRequestService;

    @Test
    void careerRejectShouldMarkRequestAsCheckedAndNotifyUser() {
        User user = new User();
        user.setEmail("user@example.com");

        Role role = new Role();
        role.setName("ROLE_WRITER");

        CareerRequest request = new CareerRequest();
        request.setId(1L);
        request.setUser(user);
        request.setRole(role);
        request.setIsChecked(false);

        when(careerRequestRepository.findById(1L)).thenReturn(Optional.of(request));

        careerRequestService.careerReject(1L);

        assertTrue(Boolean.TRUE.equals(request.getIsChecked()));
        assertEquals("ROLE_WRITER", request.getRole().getName());
        verify(careerRequestRepository).save(request);
        verify(emailService).sendSimpleEmail(
                eq("user@example.com"),
                eq("Richiesta di ruolo rifiutata"),
                contains("rifiutata")
        );
    }
}
