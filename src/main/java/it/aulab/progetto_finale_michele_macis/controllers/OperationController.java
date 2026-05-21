package it.aulab.progetto_finale_michele_macis.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.progetto_finale_michele_macis.models.CareerRequest;
import it.aulab.progetto_finale_michele_macis.models.Role;
import it.aulab.progetto_finale_michele_macis.models.User;
import it.aulab.progetto_finale_michele_macis.repositories.RoleRepository;
import it.aulab.progetto_finale_michele_macis.repositories.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import it.aulab.progetto_finale_michele_macis.services.CareerRequestService;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/operations")
public class OperationController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CareerRequestService careerRequestService;

    // Rotta creazione per richiesta di ruolo
    @GetMapping("/career/request")
    public String carrerRequest(Model viewModel) {
        viewModel.addAttribute("title","Inserisci la tua richiesta");
        viewModel.addAttribute("careerRequest", new CareerRequest());

        List<Role> roles = roleRepository.findAll();
        // Eliminare il ruolo user dalla select
        roles.removeIf(e -> e.getName().equals("ROLE_USER"));
        viewModel.addAttribute("roles", roles);

        return "career/requestForm";
    }

    // Rotta salvataggio di un richiesta di ruolo
    @PostMapping("/csreer/request/save")
    public String careerRequestStore(@ModelAttribute("careerRequest") CareerRequest careerRequest, Principal principal, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(principal.getName());

        if(careerRequestService.isRoleAlreadyAssigned(user, careerRequest)){
            redirectAttributes.addFlashAttribute("errorMessage", "Ti è già stato assegnato questo ruolo");
            return "redirect:/";
        }

        careerRequestService.save(careerRequest, user);

        redirectAttributes.addFlashAttribute("successMessage", "Richiesta inviata con successo");
        
        return "redirect:/";
    }

    // Rotta per il request detail
    @GetMapping("/career/request/detail/{id}")
    public String careerRequestDetail(@PathVariable("id")Long id, Model viewModel) {
        viewModel.addAttribute("title", "Dettagli richiesta");
        viewModel.addAttribute("request", careerRequestService.find(id));
        return "career/requestDetail";
    }

    // Rotta accettazione request
    @PostMapping("/career/request/accept/{requestId}")
    public String careerRequestAccpet(@PathVariable Long requestId, RedirectAttributes redirectAttributes) {
        careerRequestService.careerAccept(requestId);
        redirectAttributes.addFlashAttribute("successMessage", "Ruolo abilitato per l'utente");
        return "redirect:/admin/dashboard";
    }
    
    
    
}
