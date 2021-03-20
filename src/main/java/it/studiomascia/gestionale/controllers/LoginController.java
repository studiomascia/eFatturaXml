/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.User;
import it.studiomascia.gestionale.repository.UserRepository;
import it.studiomascia.gestionale.service.UtenteDetails;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author luigi
 */
@Slf4j
@Controller
public class LoginController {
    
    @Autowired
    UserRepository utenti_repository;
     
    @GetMapping("/login")
    public String Login(HttpServletRequest request, Model model, String error, String logout) 
    {
        System.out.println("it.studiomascia.gestionale.controllers.LoginController.Login().Get");
        if (error!= null)
            model.addAttribute("error","Username e Password non validi");
                   
        if (( request.getParameter("logout") != null && request.getParameter("logout").equals("logout")) || logout!= null){
            model.addAttribute("logout","Il Logout è avvenuto correttamente");
        }
        return "login";
    }
 
    @PostMapping("/login")   
    public String doLogin(Model model, HttpSession session,RedirectAttributes redirectAttributes)
    { 
        System.out.println("it.studiomascia.gestionale.controllers.LoginController.doLogin().Post");
         // read principal out of security context and set it to session
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        validatePrinciple(authentication.getPrincipal());
        User loggedInUser = ((UtenteDetails) authentication.getPrincipal()).getUserDetails();
        model.addAttribute("currentUser", loggedInUser.getUsername());
        session.setAttribute("userId", loggedInUser.getId());
      
        log.info("Login : Username = " + loggedInUser.getUsername());
        //return "/Dashboard";
        session.setAttribute("messaggio", "1");
              return "/Dashboard";


    }
 
 
    
private void validatePrinciple(Object principal) {
        if (!(principal instanceof UtenteDetails)) {
            throw new  IllegalArgumentException("Principal can not be null!");
        }
    }
    
    @GetMapping("/perform_login")
    @PostMapping("/perform_login")
    public String perform_login(HttpServletRequest request, Model model)
    {
  
       
        return "DashboardLogin";
    }

    @GetMapping("/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirAttr) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null){    
        log.info("Logout : Username = " + SecurityContextHolder.getContext().getAuthentication().getName());
        redirAttr.addFlashAttribute("logout","logout");
        new SecurityContextLogoutHandler().logout(request, response, auth);
    }
    return "redirect:/login?logout";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
}
}
