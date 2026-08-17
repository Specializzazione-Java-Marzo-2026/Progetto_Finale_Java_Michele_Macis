package it.aulab.progetto_finale_michele_macis.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import it.aulab.progetto_finale_michele_macis.models.Category;
import it.aulab.progetto_finale_michele_macis.repositories.CategoryRepository;

@Component
public class CategoryConverter implements Converter<String, Category> {

    private final CategoryRepository categoryRepository;

    public CategoryConverter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return categoryRepository.findById(Long.parseLong(source)).orElse(null);
    }
}
