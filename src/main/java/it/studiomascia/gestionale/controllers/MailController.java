package it.studiomascia.gestionale.controllers;

import  it.studiomascia.gestionale.SmtpMailSender;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MailController {
  
    @Autowired
    private SmtpMailSender smtpMailSender;
    
    @GetMapping("/Mail/Test")
    public String MailTest(HttpServletRequest request,Model model){
         
        String destinatario = "luigi.mascia@gruppostatuto.it";
        String oggetto = "oggetto del messaggio";
        String testo = "<h1>testo del messaggio</h1></br><h2>messaggio di prova in html</h2>";
        model.addAttribute("destinatario", destinatario);
        model.addAttribute("oggetto", oggetto);
        model.addAttribute("testo", testo);
        String risultato ="OK";
        try{
//            MailService.sendEmailWithAttachment(destinatario, oggetto, testo);
        
            smtpMailSender.send(destinatario,oggetto, testo);
        }catch(Exception e){
            risultato= e.getMessage();
        }
        model.addAttribute("risultato", risultato);
        
     return "mail";
    }
  
   
    
   
}