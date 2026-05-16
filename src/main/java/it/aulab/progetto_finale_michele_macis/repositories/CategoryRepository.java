package it.aulab.progetto_finale_michele_macis.repositories;

import org.springframework.data.repository.ListCrudRepository;

import it.aulab.progetto_finale_michele_macis.models.Category;

public interface CategoryRepository extends ListCrudRepository<Category, Long> {
    
}
