package it.aulab.progetto_finale_michele_macis.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.aulab.progetto_finale_michele_macis.models.User;
import it.aulab.progetto_finale_michele_macis.repositories.UserRepository;

import java.util.Collection;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.management.relation.Role;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null){
            throw new UsernameNotFoundException("Credenziali non valide");
        }

        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorithies(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorithies(Collection<Role> roles){ {
        Collection<? extends GrantedAuthority>mapRoles = null;
        if(roles.size()!=0){
            mapRoles = roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
        }
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
    
}
}
