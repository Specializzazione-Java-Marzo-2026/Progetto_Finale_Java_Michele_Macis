package it.aulab.progetto_finale_michele_macis.services;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.aulab.progetto_finale_michele_macis.models.CareerRequest;
import it.aulab.progetto_finale_michele_macis.models.Role;
import it.aulab.progetto_finale_michele_macis.models.User;
import it.aulab.progetto_finale_michele_macis.repositories.CareerRequestRepository;
import it.aulab.progetto_finale_michele_macis.repositories.RoleRepository;
import it.aulab.progetto_finale_michele_macis.repositories.UserRepository;

import java.util.List;

public class CareerRequestServiceImpl implements CareerRequestService {

    @Autowired
    private CareerRequestRepository careerRequestRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    
    @Transactional
    public boolean isRoleAlreadyAssigned(User user,CareerRequest careerRequest){
        List<Long> allUserIds = careerRequestRepository.findAllUserIds();

        if(!allUserIds.contains(user.getId())){
            return false;
        }

        List<Long> requests = careerRequestRepository.findUserById(user.getId());

        return requests.stream().anyMatch(roleId-> roleId.equals(careerRequest.getRole().getId()));
    }

    public void save(CareerRequest careerRequest, User user){
        careerRequest.setUser(user);
        careerRequest.setIsChecked(false);
        careerRequestRepository.save(careerRequest);
        // Invio mail di richiesta x il ruolo di admin
        emailService.sendSimpleEmail("mail che non ricordo", "Richiesta per ruolo:" + careerRequest.getRole().getName(), "C'è una nuova richiesta di collaborazione da parte di " + user.getUsername());
    }

    @Override
    public void careerAccept(Long requestId){
        CareerRequest request = careerRequestRepository.findById(requestId).get();

        User user = request.getUser();
        Role role = request.getRole();

        List<Role> rolesUser = user.getRoles();
        Role newRole = roleRepository.findByName(role.getName());
        rolesUser.add(newRole);

        user.setRoles(rolesUser);
        userRepository.save(user);
        request.setIsChecked(true);
        careerRequestRepository.save(request);

        emailService.sendSimpleEmail( user.getEmail(), "Ruolo abilitato", "Ciao, la tua richiesta di collaborazione è stata accettata dalla nostra amministrazione");
    }
    
    @Override
    public CareerRequest find(Long id){
        return careerRequestRepository.findById(id).get();
    }
    
}
