package it.aulab.progetto_finale_michele_macis.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LegacyPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return bcryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null) {
            return false;
        }

        if (encodedPassword.startsWith("$2") || encodedPassword.startsWith("{bcrypt}")) {
            return bcryptPasswordEncoder.matches(rawPassword, encodedPassword);
        }

        return rawPassword != null && rawPassword.toString().equals(encodedPassword);
    }
}
