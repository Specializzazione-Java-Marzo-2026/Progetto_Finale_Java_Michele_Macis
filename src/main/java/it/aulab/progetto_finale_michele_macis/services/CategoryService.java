package it.aulab.progetto_finale_michele_macis.services;

import java.security.Principal;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.modelmapper.ModelMapper;

import it.aulab.progetto_finale_michele_macis.dtos.CategoryDto;
import it.aulab.progetto_finale_michele_macis.models.Article;
import it.aulab.progetto_finale_michele_macis.models.Category;
import it.aulab.progetto_finale_michele_macis.repositories.CategoryRepository;
// import jakarta.transaction.Transactional;

@Service
public class CategoryService implements CrudService <CategoryDto, Category, Long>{

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CategoryDto> readAll() {
        List<CategoryDto> dtos= new ArrayList<CategoryDto>();
        for(Category category: categoryRepository.findAll()){
            dtos.add(modelMapper.map(category, CategoryDto.class));
        }
        return dtos;
    }
    
    @Override
    public CategoryDto read(Long key) {
        return modelMapper.map(categoryRepository.findById(key), CategoryDto.class);
    }
    
    @Override
    public CategoryDto create(Category model , MultipartFile file,Principal principal) {
        return modelMapper.map(categoryRepository.save(model), CategoryDto.class);
    }
    
    @Override
    public CategoryDto update(Long key, Category model, MultipartFile file) {
        if(categoryRepository.existsById(key)){
            model.setId(key);
            return modelMapper.map(categoryRepository.save(model), CategoryDto.class);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    
    }
    
    @Override
    public void delete(Long id) {
        if(categoryRepository.existsById(id)){
            Category category = categoryRepository.findById(id).get();
            if(category.getArticles()!= null){
                Iterable<Article> articles= category.getArticles();
                for(Article article: articles){
                    article.setCategory(null);
                }
            }
            categoryRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        
    }  
}
