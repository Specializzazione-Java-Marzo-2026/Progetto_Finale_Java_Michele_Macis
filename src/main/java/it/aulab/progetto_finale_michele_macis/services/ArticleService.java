package it.aulab.progetto_finale_michele_macis.services;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import it.aulab.progetto_finale_michele_macis.dtos.ArticleDto;
import it.aulab.progetto_finale_michele_macis.models.Article;
import it.aulab.progetto_finale_michele_macis.models.User;
import it.aulab.progetto_finale_michele_macis.repositories.ArticleRepository;
import it.aulab.progetto_finale_michele_macis.repositories.UserRepository;

@Service
public class ArticleService implements CrudService<ArticleDto, Article, Long> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ArticleDto> readAll(){
        // TODO Auto-generated stub
        throw new UnsupportedOperationException("Unimplemented method 'readAll'");
    }
    
    @Override
    public ArticleDto read(Long key){
        // TODO Auto-generated stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }

    @Override
    public ArticleDto create(Article article, Principal principal, MultipartFile multipartFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
            User user = (userRepository.findById(userDetails.getId())).get();
            article.setUser(user);
        }

        ArticleDto dto = modelMapper.map(articleRepository.save(article), Article.class);

        return dto;
    }

    @Override
    public ArticleDto update(Long key, Article article, MultipartFile file){
        // TODO Auto-generated stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Long key){
        // TODO Auto-generated stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
    
}