package it.aulab.progetto_finale_michele_macis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import it.aulab.progetto_finale_michele_macis.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}