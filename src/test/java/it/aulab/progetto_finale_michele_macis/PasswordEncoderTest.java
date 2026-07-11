package it.aulab.progetto_finale_michele_macis;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

class PasswordEncoderTest {

    @Test
    void shouldAcceptLegacyPlainTextPasswords() {
        PasswordEncoder encoder = new ProgettoFinaleMicheleMacisApplication().passwordEncoder();

        assertTrue(encoder.matches("Password123!", "Password123!"));
    }
}
