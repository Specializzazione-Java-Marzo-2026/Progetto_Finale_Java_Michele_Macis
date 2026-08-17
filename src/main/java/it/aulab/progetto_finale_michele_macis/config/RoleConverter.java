package it.aulab.progetto_finale_michele_macis.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.aulab.progetto_finale_michele_macis.models.Role;
import it.aulab.progetto_finale_michele_macis.repositories.RoleRepository;

@Component
public class RoleConverter implements Converter<String, Role> {

    private final RoleRepository roleRepository;

    public RoleConverter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return roleRepository.findById(Long.parseLong(source)).orElse(null);
    }
}
