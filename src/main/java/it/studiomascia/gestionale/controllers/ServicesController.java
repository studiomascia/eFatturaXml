package it.studiomascia.gestionale.controllers;

import  it.studiomascia.gestionale.SmtpMailSender;
import it.studiomascia.gestionale.Utility;
import it.studiomascia.gestionale.models.AnagraficaSocieta;
import it.studiomascia.gestionale.models.Pagamento;
import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.repository.AnagraficaSocietaRepository;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import it.studiomascia.gestionale.service.XmlFatturaBaseService;
import it.studiomascia.gestionale.xml.AnagraficaType;
import it.studiomascia.gestionale.xml.CedentePrestatoreType;
import it.studiomascia.gestionale.xml.DatiGeneraliDocumentoType;
import it.studiomascia.gestionale.xml.DettaglioPagamentoType;
import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;
import javax.mail.search.DateTerm;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ServicesController {
  
     @Autowired
    private AnagraficaSocietaRepository  anagraficaSocietaRepository;
     
    @Autowired
    private XmlFatturaBaseRepository xmlFatturaBaseRepository;
   
    @Autowired
    private XmlFatturaBaseService xmlFatturaBaseService;
    
    @GetMapping("/SetOldPayment")
    public String SetOldPayment(HttpServletRequest request, ModelMap modelMap) {        
        Date dataPagamentoFittizio = new Date(System.currentTimeMillis());
       
        List<XmlFatturaBase> listaEsistenti =xmlFatturaBaseRepository.findAll();
  
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        
        
        List<Map<String, Object>> righe = new ArrayList<Map<String, Object>>();
        BigDecimal importo  = new BigDecimal("0.00");        
        for (XmlFatturaBase xmlFattura : listaEsistenti){
              
            // controlla se la fattura è passiva, se non è pagata e se è stata emessa nel 2019
            if (!xmlFattura.isAttiva() && xmlFattura.isToPay() && xmlFattura.getFatturaElettronica().getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().getYear()==2019)
            {
                Pagamento p = new Pagamento();
                p.setDataVersamento(dataPagamentoFittizio);
                
                if (xmlFattura.getFatturaElettronica().getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento() !=null) importo = xmlFattura.getFatturaElettronica().getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento();
                p.setImportoVersamento(importo);
                p.setNote("pagamento impostato automaticamente");
                p.setCreatore("System");
                p.setStatoPagamento(Pagamento.PAGAMENTO_SISTEMA);
 
                xmlFattura.getPagamenti().add(p);
                xmlFattura = xmlFatturaBaseRepository.save(xmlFattura);
                xmlFatturaBaseRepository.flush();
                System.out.println(" Inserimento pagamento fittizio fattura id = "  + xmlFattura.getId());
            }
                    

            
                    // Perpara la Map da aggiungere alla view 
                    Map<String, Object> riga = new HashMap<String, Object>(4);
                    riga.put("Id", xmlFattura.getId());   
                    
                    righe.add(riga);
                 
           
            modelMap.addAttribute("headers", headers);
            modelMap.addAttribute("rows", righe);
        }  
        return "fatture_passive_caricate";
    }
   
    @GetMapping("/UpdateInvoicesFields")
    public String UpdateInvoicesDate(HttpServletRequest request, ModelMap modelMap){
        
        long inizio = System.currentTimeMillis();
        
        //int contatore=0;
        //int contatoreMax = listaFatture.size();
        
        List<XmlFatturaBase> listaFatture =xmlFatturaBaseRepository.findAll();
        List<String> headers = new  ArrayList<>();
        List<Map<String, Object>> righe = new ArrayList<Map<String, Object>>();

        headers.add("Id");
        headers.add("Data");
        headers.add("Numero");
        headers.add("Partita IVA");
        headers.add("Ragione sociale");
        headers.add("Importo");
        headers.add("TD");
        headers.add("Descrizione");
        
        
        listaFatture.parallelStream().forEach(xmlFattura -> {
            
            xmlFatturaBaseService.UpdateFieldsTable(xmlFattura);
            //System.out.println(contatore++ + "/" + contatoreMax +" Aggiornamento fattura id = "  + xmlFattura.getId());
            // Perpara la Map da aggiungere alla view 
            Map<String, Object> riga = new HashMap<>(5);
            riga.put("Id", xmlFattura.getId());   
            riga.put("Data", xmlFattura.getDataFattura().toString()); 
            riga.put("Numero", xmlFattura.getNumeroFattura()); 
            riga.put("Partita IVA",xmlFattura.getPartitaIva());
            riga.put("Ragione sociale", xmlFattura.getRagioneSociale()); 
            riga.put("Importo", xmlFattura.getImportoFattura().toString()); 
            riga.put("TD",xmlFattura.getTipoDocumento()); 
            riga.put("Descrizione", xmlFattura.getDescrizione()); 
            righe.add(riga);
        });

        
        modelMap.addAttribute("titolo", "Elenco fatture aggiornate");
        modelMap.addAttribute("headers", headers);
        modelMap.addAttribute("rows", righe);
        xmlFatturaBaseRepository.flush();
        
        System.out.println(" Aggiornamento durato " +  (System.currentTimeMillis() - inizio   ));

        return "fatture_passive_elaborate";
    }
    
    
}