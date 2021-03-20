/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import it.studiomascia.gestionale.models.DBFile;
import it.studiomascia.gestionale.models.Ddt;
import it.studiomascia.gestionale.models.Pagamento;
import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.repository.AnagraficaSocietaRepository;
import it.studiomascia.gestionale.repository.DdtRepository;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import it.studiomascia.gestionale.service.DBFileStorageService;
import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 *
 * @author luigi
 */
@Controller
public class DdtController {
    
    private static final Logger logger = LoggerFactory.getLogger(DdtController.class);

    private SimpleDateFormat formattaData = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    private DBFileStorageService DBFileStorageService;

    @Autowired
    private DdtRepository ddtRepository;
    
    @Autowired
    private XmlFatturaBaseRepository xmlFatturaRepository;
    
    @Autowired
    private AnagraficaSocietaRepository anagraficaSocietaRepository;
    
    @GetMapping("/InvoiceIn/{fatturaId}/DDT")
    public String DdtFattura(Model model, @PathVariable String fatturaId){
        List<String> headers = new  ArrayList<>();
            headers.add("Id");
            headers.add("Registro IVA");
            headers.add("N. Fattura");
            headers.add("Data Fattura");
            headers.add("P.IVA");
            headers.add("Denominazione");
            headers.add("Imponibile");
            
            String strData="N/A";
            Integer id = Integer.valueOf(fatturaId);
            XmlFatturaBase xmlFattura = xmlFatturaRepository.findById(id).get();
            try {
                byte[] byteArr = xmlFattura.getXmlData().getBytes("UTF-8");
                StringWriter sw = new StringWriter();
                JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
                // Unmarshaller serve per convertire il file in un oggetto
                Unmarshaller jaxbUnMarshaller = context.createUnmarshaller();
                // Marshaller serve per convertire l'oggetto ottenuto dal file in una stringa xml
                Marshaller jaxbMarshaller = context.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                FatturaElettronicaType item = root.getValue();
                jaxbMarshaller.marshal(root, sw);

                Date dataFattura = item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().toGregorianCalendar().getTime();
                String numeroFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero();
                String importoFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().toString();
                String partitaIVA =  item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice();
                String denominazione = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione();
            
                Map<String, Object> riga = new HashMap<String, Object>();
                riga.put("Id", xmlFattura.getId());   
                strData = ((xmlFattura.getDataRegistrazione() == null)) ? "N/A" : formattaData.format(xmlFattura.getDataRegistrazione());
                riga.put("Registro IVA",xmlFattura.getNumeroRegistrazione()+ " - " +  strData);
                riga.put("N. Fattura", numeroFattura);
                riga.put("Data Fattura", formattaData.format(dataFattura));
                riga.put("P.IVA",partitaIVA );
                riga.put("Denominazione",denominazione );
                riga.put("Imponibile", importoFattura);
             
                model.addAttribute("fattura", riga);
                model.addAttribute("headers", headers);
 
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }     
            
        Set<Ddt> listaDDT= xmlFattura.getDDT();
        
        List<DBFile> listaFile = new ArrayList<DBFile>();
        for (Ddt x : listaDDT){

            listaFile.addAll(x.getFilesDDT());
        	
        }
        
        List lista = new ArrayList(listaDDT);
        model.addAttribute("listafiles", listaFile);
        model.addAttribute("listapagamenti", lista);
        return "lista_ddt_fattura";
    }
    
    @GetMapping("/Ddt/{id}/ModalAttachment")
    public String ModalAddDdtAttachment (ModelMap model,@PathVariable Integer id){
        
        Ddt ddt = ddtRepository.findById(id).get();
        model.addAttribute("IdProvider",ddt.getAnagraficaSocieta().getId());  
        model.addAttribute("ddt",ddt);  
        return "modalContents :: attachmentDdt";
        
        
        
        
    }
 
    
    @PostMapping("/Ddt/{idDdt}/ModalAttachment")
    public String postModalAddAttachmentToDdt(@ModelAttribute("ddt") Ddt d,@PathVariable Integer idDdt, RedirectAttributes redirectAttributes, @RequestParam("files") MultipartFile[] files, HttpServletRequest request)
    {
        String idProvider="0";
        if (request.getParameterMap().get("txtDescription") != null && request.getParameterMap().get("txtDescription").length > 0) {
            
            Ddt ddt = ddtRepository.findById(idDdt).get();
            idProvider = ddt.getAnagraficaSocieta().getId().toString();
            for (int k=0;k<files.length;k++)
            {
                DBFile dbFile = DBFileStorageService.storeFile(files[k],request.getParameter("txtDescription").toString());
                 ddt.getFilesDDT().add(dbFile);
                 ddtRepository.save(ddt);
            }
        }
        return "redirect:/Provider/"+ idProvider +"/DDT";
    }
   

    
    
    

}
