package it.aulab.progetto_finale_michele_macis.controllers;
 
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.progetto_finale_michele_macis.dtos.CategoryDto;
import it.aulab.progetto_finale_michele_macis.dtos.ArticleDto;
import it.aulab.progetto_finale_michele_macis.models.Article;
import it.aulab.progetto_finale_michele_macis.models.Category;
import it.aulab.progetto_finale_michele_macis.repositories.ArticleRepository;
import it.aulab.progetto_finale_michele_macis.services.ArticleService;
import it.aulab.progetto_finale_michele_macis.services.CrudService;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    @Qualifier("categoryService")
    private CrudService<CategoryDto,Category,Long> categoryService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Rotta index degli articoli
    @GetMapping
    public String articlesIndex(Model viewModel){
        viewModel.addAttribute("title","Tutti gli articoli");

        List<ArticleDto> articles= new ArrayList<ArticleDto>();
        for(Article article: articleRepository.findByIsAcceptedTrue()){
            articles.add(modelMapper.map(article, ArticleDto.class));
        }

        Collections.sort(articles, Comparator.comparing(ArticleDto::getPublishDate).reversed());
        viewModel.addAttribute("articles", articles);

        return "article/articles";
    }
    
    // Rotta per la creazione articoli
    @GetMapping("create")
    public String articleCreate(Model viewModel){
        viewModel.addAttribute("title", "Crea un articolo");
        viewModel.addAttribute("article", new Article());
        viewModel.addAttribute("categories", categoryService.readAll());
        return "articles/create";
    }

    // Rotta per lo store di un articolo
    @PostMapping
    public String articleStore(@Valid @ModelAttribute("article") Article article,
                                BindingResult result,
                                RedirectAttributes redirectAttributes, 
                                Principal principal,
                                MultipartFile file,
                                Model viewModel){
        // Controllo degli errori con validazioni
        if (result.hasErrors()) {
            viewModel.addAttribute("title","Crea un articolo");
            viewModel.addAttribute("article", article);
            viewModel.addAttribute("categories", categoryService.readAll());
            return "article/create";
        }

        articleService.create(article, file, principal);
        redirectAttributes.addFlashAttribute("succesMessage","Articolo aggiunto con successo!");

        return "redirect:/";
    }

    // Rotta di dettaglio di un articolo
    @GetMapping("detail/{id}")
    public String detailArticle(@PathVariable("id") Long id, Model viewModel){
        viewModel.addAttribute("title","Article detail");
        viewModel.addAttribute("article", articleService.read(id));
        return "article/detail";
    }

    // Rotta dettaglio di un articolo per il revisore
    @GetMapping("revisor/detail/{id}")
    public String revisorDetailArticle(@PathVariable("id") Long id, Model viewModel){
        viewModel.addAttribute("title","Article detail");
        viewModel.addAttribute("article", articleService.read(id));
        return "revisor/articleDetail";
    }

    // Rotta di revisione articoli
    @PostMapping("/accept")
    public String articleSetAccepted(@RequestParam("action") String action, @RequestParam("id") Long id, RedirectAttributes redirectAttributes){
        if(action.equals("accept")){
            articleService.setIsAccepted(id, true);
            redirectAttributes.addFlashAttribute("resultMessage", "Articolo accettato!");
        } else if(action.equals("reject")){
            articleService.setIsAccepted(id, false);
            redirectAttributes.addFlashAttribute("resultMessage", "Articolo rifiutato!");
        } else {
            redirectAttributes.addFlashAttribute("resultMessage", "Azione non valida!");
        }
        return "redirect:/revisor/dashboard";
    }
}

