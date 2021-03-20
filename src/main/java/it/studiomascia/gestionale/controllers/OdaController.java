/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.DBFile;
import it.studiomascia.gestionale.models.Ddt;
import it.studiomascia.gestionale.models.ODA;
import it.studiomascia.gestionale.repository.AnagraficaSocietaRepository;
import it.studiomascia.gestionale.repository.OdaRepository;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import it.studiomascia.gestionale.service.DBFileStorageService;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 *
 * @author luigi
 */
@Controller
public class OdaController {
    
    private static final Logger logger = LoggerFactory.getLogger(OdaController.class);

    private SimpleDateFormat formattaData = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    private DBFileStorageService DBFileStorageService;

    @Autowired
    private OdaRepository odaRepository;
    
    @Autowired
    private XmlFatturaBaseRepository xmlFatturaRepository;
    
    @Autowired
    private AnagraficaSocietaRepository anagraficaSocietaRepository;
    
    @GetMapping("/ODA/{id}/ModalAttachment")
    public String ModalAddDdtAttachment (ModelMap model,@PathVariable Integer id){
        
        ODA oda = odaRepository.findById(id).get();
        model.addAttribute("IdProvider",oda.getAnagraficaSocieta().getId());  
        model.addAttribute("oda",oda);  
        return "modalContents :: attachmentOda";
        
        
        
        
    }
 
    
    @PostMapping("/ODA/{idDdt}/ModalAttachment")
    public String postModalAddAttachmentToDdt(@ModelAttribute("oda") Ddt d,@PathVariable Integer idDdt, RedirectAttributes redirectAttributes, @RequestParam("files") MultipartFile[] files, HttpServletRequest request)
    {
        String idProvider="0";
        if (request.getParameterMap().get("txtDescription") != null && request.getParameterMap().get("txtDescription").length > 0) {
            
            ODA oda = odaRepository.findById(idDdt).get();
            idProvider = oda.getAnagraficaSocieta().getId().toString();
            for (int k=0;k<files.length;k++)
            {
                DBFile dbFile = DBFileStorageService.storeFile(files[k],request.getParameter("txtDescription").toString());
                 oda.getFilesODA().add(dbFile);
                 odaRepository.save(oda);
            }
        }
        return "redirect:/Provider/"+ idProvider +"/ODA";
    }
   

    
    
    

}
