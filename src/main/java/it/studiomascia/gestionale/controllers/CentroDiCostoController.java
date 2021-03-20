/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.CentroDiCosto;
import it.studiomascia.gestionale.repository.CentroDiCostoRepository;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


/**
 *
 * @author luigi
 */
@Controller
public class CentroDiCostoController {
    
    private static final Logger logger = LoggerFactory.getLogger(CentroDiCostoController.class);

    private SimpleDateFormat formattaData = new SimpleDateFormat("dd-MM-yyyy");

    
    @Autowired
    private CentroDiCostoRepository centroDiCostoRepository;
    
    @GetMapping("/CentroDiCosto")
    public String CentroDiCostoList(Model model){
        
        CentroDiCosto rootCategory = centroDiCostoRepository.findRootFolder().get();
        List<CentroDiCosto> lista =  new ArrayList<CentroDiCosto>();
        lista.add(rootCategory);
         model.addAttribute("lista_cdc", lista);
        
        return "lista_cdc";
    }
    
 

    
    

}
