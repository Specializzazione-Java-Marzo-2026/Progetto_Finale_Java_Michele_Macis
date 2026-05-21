package it.aulab.progetto_finale_michele_macis.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import it.aulab.progetto_finale_michele_macis.models.Article;
import it.aulab.progetto_finale_michele_macis.models.Category;
import it.aulab.progetto_finale_michele_macis.models.User;

public interface ArticleRepository extends ListCrudRepository<Article, Long>{
    List<Article> findByCategory(Category category);
    List<Article> findByUser(User user);
    List<Article> findByIsAcceptedTrue();
    List<Article> findByIsAcceptedFalse();
    List<Article> findByIsAcceptedIsNull();
}