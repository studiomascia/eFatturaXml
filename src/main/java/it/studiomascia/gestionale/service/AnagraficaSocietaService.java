/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.service;

import it.studiomascia.gestionale.models.AnagraficaSocieta;
import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.repository.AnagraficaSocietaRepository;
import it.studiomascia.gestionale.xml.AnagraficaType;
import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
@Slf4j
public class AnagraficaSocietaService {
    
    @Autowired
    private AnagraficaSocietaRepository anagraficaSocietaRepository;
    
     /* INIZIO Metodi comuni per tutti i mapping */
    private SimpleDateFormat formattaData = new SimpleDateFormat("dd-MM-yyyy");
    
    // Prepara la Map da aggiungere alla view 
    public List<String> getHeaders (){
        
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("P.IVA");
        headers.add("Denominazione");
        headers.add("Indirizzo");  
        return headers;
    }

        
        
    public List<Map<String, Object>> getRows (){
        
        List<AnagraficaSocieta> elencoSocieta = anagraficaSocietaRepository.findAll();
 
        List<Map<String, Object>> righe = new ArrayList<Map<String, Object>>();
            
            for (AnagraficaSocieta item:elencoSocieta) {

                Map<String, Object> riga = new HashMap<String, Object>();
                riga.put("Id", item.getId());   
                riga.put("P.IVA",item.getPiva() );
                riga.put("Denominazione",item.getDenominazione() );
                riga.put("Indirizzo", item.getIndirizzo());
                righe.add(riga); 
            }

       return righe;
    }
        
    

        
        
        
        
        
        
        
        
        
        
        
        
}
