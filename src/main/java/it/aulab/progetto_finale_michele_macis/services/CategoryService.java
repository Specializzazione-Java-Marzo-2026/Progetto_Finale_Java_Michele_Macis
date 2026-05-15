package it.aulab.progetto_finale_michele_macis.services;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.aulab.progetto_finale_michele_macis.dtos.CategoryDto;
import it.aulab.progetto_finale_michele_macis.models.Category;

@Service
public class CategoryService implements CrudService<CategoryDto, Category, Long>{

        @Override
        public List<CategoryDto> readAll() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'readAll'");
        }
    
        @Override
        public CategoryDto read(Long key) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'read'");
        }
    
        @Override
        public CategoryDto create(Category model, Principal principal, MultipartFile file) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'create'");
        }
    
        @Override
        public CategoryDto update(Long key, Category model, MultipartFile file) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'update'");
        }
    
        @Override
        public void delete(Long key) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'delete'");
        }
    
}
 