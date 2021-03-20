package it.studiomascia.gestionale.controllers;

import  it.studiomascia.gestionale.models.User;
import it.studiomascia.gestionale.repository.UserRepository;
import  it.studiomascia.gestionale.service.SecurityService;
import  it.studiomascia.gestionale.service.UserService;
import it.studiomascia.gestionale.validator.UserValidator;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    UserRepository userRepository;
    
    @GetMapping("/Admin/SetDefaultUsers")
    public String insertDefaultUsers() {
                
        User u1 = new User();
        u1.setUsername("admin@admin.it");
        u1.setPassword("123123");
        u1.setEnabled(1);
        userService.insertDefaultNewUser(u1);
        
        return "login";
    }
    
    @GetMapping("/Admin/Users")
    public String UtentiList(HttpServletRequest request,Model model){
//        System.out.println("GetMapping(/Admin/Users) INIZIO");
        //INIZIO:: BLOCCO PER LA PAGINAZIONE
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 100; //default page size is 100
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        //FINE:: BLOCCO PER LA PAGINAZIONE
       Page<User> lista = userRepository.findAll(PageRequest.of(page, size));
//       lista.getPageable().getPageNumber()
        model.addAttribute("lista_utenti", lista);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
//        System.out.println("GetMapping(/Admin/Users) FINE");
    return "utente_lista";
    }
  
    @GetMapping("/Admin/User/{id}")
    public String EditUtente(Model model,@PathVariable Long id){
        User x = userRepository.findById(id).get();
        model.addAttribute("utente",x);  
        return "/utente_modifica";
    }
    
    @PostMapping("/Admin/User/{id}")
    public String AggiornaUtente(@Valid @ModelAttribute("utente") User updateUtente, BindingResult bindingResult,Model model, RedirectAttributes redirectAttributes)
    {
//        System.out.println("Updading user.id=" + updateUtente.getId());        
        User vecchioUtente = userRepository.findById(updateUtente.getId()).get();
        
        vecchioUtente.setEnabled(updateUtente.getEnabled());
        redirectAttributes.addFlashAttribute("messaggio","L'utente: " +vecchioUtente.getUsername() + " è stato aggiornato");  
        userRepository.save(vecchioUtente);
        return "redirect:/Admin/Users";
    }
   
    
    
    @GetMapping("Admin/User/New")
    public String NuovoUtente(Model model){
        User x = new User();
        model.addAttribute("utente",x);
        return "/utente_registrazione";
    }
    
    
  
    
    @PostMapping("Admin/User/New")
    public String NuovoUtente(@Valid @ModelAttribute("utente") User utente, BindingResult bindingResult,Model model)
    {
        userValidator.validate(utente, bindingResult);
        model.addAttribute("utente",utente);
        if (bindingResult.hasErrors()) {
            return "/utente_registrazione";
        }else{
//         User u1 = new User();
//        u1.setUsername("admin@admin.it");
//        u1.setPassword("123123");
//        u1.setStato(1);
            userService.insertDefaultNewUser(utente);
            return "redirect:/Admin/Users";
        }
    }
    
    @GetMapping("Account/Password")
    public String Password(Model model, RedirectAttributes redirectAttributes){
        User x = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("utente",x);
//        if (model.containsAttribute("messaggio")) 
//            redirectAttributes.addFlashAttribute("messaggio",model.asMap().get("messaggio"));  

        return "/account_aggiorna_password";
    }
    
    @PostMapping("Account/Password")
    public String aggiornaPassword(@Valid @ModelAttribute("utente") User updateUtente, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes)
    {
        User vecchioUtente = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        vecchioUtente.setPassword(updateUtente.getPassword());
        if (request.getParameter("txtConfirmPassword") == request.getParameter("password") )
        {
            model.addAttribute("messaggio","La password è stata aggiornata. ");  
            redirectAttributes.addFlashAttribute("messaggio", "\"La password è stata aggiornata correttamente");
            userService.EncodeAndSave(vecchioUtente);
            return "redirect:/Dashboard";
        }else{
            model.addAttribute("messaggio","Le password non coincidono. "); 
            return "account_aggiorna_password";
        }
    }
    
   
}