package it.aulab.progetto_finale_michele_macis.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;

import it.aulab.progetto_finale_michele_macis.dtos.ArticleDto;
import it.aulab.progetto_finale_michele_macis.models.Article;
import it.aulab.progetto_finale_michele_macis.models.Category;
import it.aulab.progetto_finale_michele_macis.models.User;
import it.aulab.progetto_finale_michele_macis.repositories.ArticleRepository;
import it.aulab.progetto_finale_michele_macis.repositories.UserRepository;
// import it.aulab.progetto_finale_michele_macis.repositories.CategoryRepository;
// import it.aulab.progetto_finale_michele_macis.services.CustomUserDetails;

@Service
public class ArticleService implements CrudService<ArticleDto, Article, Long> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ArticleDto> readAll(){
        List<ArticleDto> dtos= new ArrayList<ArticleDto>();
        for(Article article: articleRepository.findAll()){
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }
    
    @Override
    public ArticleDto read(Long key){
        Optional<Article> optArticle = articleRepository.findById(key);
        if(optArticle.isPresent()){
            return modelMapper.map(optArticle.get(), ArticleDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author id" + key + " not found");
        }
    }

    @Override
    public ArticleDto create(Article article, MultipartFile file, Principal principal){
        String url= "";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
            User user = ( userRepository.findById(userDetails.getId())).get();
            article.setUser(user);
        }

        if(!file.isEmpty()){
            try{
                CompletableFuture<String> futureUrl = imageService.saveImageOnCloud(file);
                url = futureUrl.get();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        article.setIsAccepted(null);
    
        ArticleDto dto = modelMapper.map(articleRepository.save(article),ArticleDto.class);
        if(!file.isEmpty()){
            imageService.saveImageOnDB(url, article);
        }
        return dto;
    }

    @Override
    public ArticleDto update(Long key, Article updatedArticle, MultipartFile file){
        String url="";
        // Controllo esistenza di un articolo in base all'id
        if(articleRepository.existsById(key)){
            // Assegno all'articolo del form lo stesso dell'originale
            updatedArticle.setId(key);
            // Recupero articolo orginale non modificato
            Article article = articleRepository.findById(key).get();
            // Imposto l'utente dell'articolo del form con lo stesso dell'originale
            updatedArticle.setUser(article.getUser());
            // faccio un controllo sull'esistenza del file per capire se bisogna modificare l'immagine
            if(!file.isEmpty()){
                try{
                    // Eiliminare l'immagine precedente
                    imageService.deleteImage(article.getImage().getPath());
                    try{
                        // Salvo nuova immagine
                        CompletableFuture<String> futureUrl = imageService.saveImageOnCloud(file);
                        url= futureUrl.get();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    // Salvo il nuovo path nel db
                    imageService.saveImageOnDB(url, updatedArticle);

                    // Avendo effettuato una modifica l'articolo torna in revisione
                    updatedArticle.setIsAccepted(null);
                    return modelMapper.map(articleRepository.save(updatedArticle), ArticleDto.class);
                } catch(Exception e){
                    e.printStackTrace();
                }
            } else if(article.getImage()== null){ 
                // se l'articolo originale non ha un'immagine e nemmeno quello da modificare allora non è stata fatta nessuna modifica
                updatedArticle.setIsAccepted(article.getIsAccepted());
            } else{
                // se l'immagine non è stata modifica devo fare un check degli altri campi, se diversi torna in revisione, e posso anche impostare sull'articolo modificato quella originale
                updatedArticle.setImage(article.getImage());
                if(updatedArticle.equals(article)==false){
                    updatedArticle.setIsAccepted(null);
                }else{
                    updatedArticle.setIsAccepted(article.getIsAccepted());
                }
                return modelMapper.map(articleRepository.save(updatedArticle), ArticleDto.class);
            }
        } else  {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @Override
    public void delete(Long key){
        if(articleRepository.existsById(key)){
            Article article = articleRepository.findById(key).get();
            try{
                String path = article.getImage().getPath();
                article.getImage().setArticle(null);
                imageService.deleteImage(path);
            } catch(Exception e){
                e.printStackTrace();
            }
            articleRepository.deleteById(key);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public List<ArticleDto> searchByCategory(Category category){
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for(Article article: articleRepository.findByCategory(category)){
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }

    public List<ArticleDto> searchByAuthor(User user){
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for(Article article: articleRepository.findByUser(user)){
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }

    public void setIsAccepted(Boolean result, Long id){
        Article article = articleRepository.findById(id).get();
        article.setIsAccepted(result);
        articleRepository.save(article);
    }

    public List<ArticleDto> search(String searchTerm){
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for(Article article: articleRepository.search(searchTerm)){
            dtos.add(modelMapper.map(article, ArticleDto.class));
        }
        return dtos;
    }
}