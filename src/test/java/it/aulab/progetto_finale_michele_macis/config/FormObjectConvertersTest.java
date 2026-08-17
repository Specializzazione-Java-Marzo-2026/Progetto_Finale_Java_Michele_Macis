package it.aulab.progetto_finale_michele_macis.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import it.aulab.progetto_finale_michele_macis.models.Category;
import it.aulab.progetto_finale_michele_macis.models.Role;
import it.aulab.progetto_finale_michele_macis.repositories.CategoryRepository;
import it.aulab.progetto_finale_michele_macis.repositories.RoleRepository;

@ExtendWith(MockitoExtension.class)
class FormObjectConvertersTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void categoryConverterShouldResolveCategoryById() {
        Category category = new Category();
        category.setId(7L);
        category.setName("Tecnologia");

        when(categoryRepository.findById(7L)).thenReturn(Optional.of(category));

        CategoryConverter converter = new CategoryConverter(categoryRepository);

        assertEquals(category, converter.convert("7"));
        verify(categoryRepository).findById(7L);
    }

    @Test
    void roleConverterShouldResolveRoleById() {
        Role role = new Role();
        role.setId(3L);
        role.setName("ROLE_REVISOR");

        when(roleRepository.findById(3L)).thenReturn(Optional.of(role));

        RoleConverter converter = new RoleConverter(roleRepository);

        assertEquals(role, converter.convert("3"));
        verify(roleRepository).findById(3L);
    }
}
