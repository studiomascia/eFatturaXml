/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.repository.AnagraficaSocietaRepository;
import it.studiomascia.gestionale.repository.UserRepository;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author luigi
 */
@Controller
public class UserDashboardController {
 
   @Autowired
   UserRepository utenti_repository;
   
   @Autowired
   AnagraficaSocietaRepository  fornitori_repository;
   
   @Autowired
   XmlFatturaBaseRepository xmlfattura_repository;
   
    @GetMapping("/Dashboard")
    public String Dashboard(Model model, HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes){
//        System.out.println("currentUser: is ADMIN: "+ request.isUserInRole("ROLE_ADMIN") );
//        System.out.println("currentUser: is USER: "+ request.isUserInRole("ROLE_USER") );

//        if (session.getAttribute("primoAccesso")== null)
//        {
//            session.setAttribute("primoAccesso",true); 
//            String msg="primo accesso";
//            model.addAttribute("buongiorno",msg); 
//        }

        model.addAttribute("numUtenti", utenti_repository.findAll().size());
        model.addAttribute("nFornitori", fornitori_repository.findAll().size());
        model.addAttribute("nFatturePassive", xmlfattura_repository.findByAttivaFalse().size());
        model.addAttribute("nFattureAttive", xmlfattura_repository.findByAttivaTrue().size());  

        model.addAttribute("nFatturePassiveNR", xmlfattura_repository.findPassiveNotRegistered().size());
        model.addAttribute("nFattureAttiveNR", xmlfattura_repository.findAttiveNotRegistered().size());

        return "user_dashboard";
    }
    
}
   
   

