package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.AnagraficaSocieta;
import  it.studiomascia.gestionale.models.Ddt;
import it.studiomascia.gestionale.models.ODA;
import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.repository.AnagraficaSocietaRepository;
import it.studiomascia.gestionale.repository.DdtRepository;
import it.studiomascia.gestionale.repository.OdaRepository;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import it.studiomascia.gestionale.service.AnagraficaSocietaService;
import it.studiomascia.gestionale.service.XmlFatturaBaseService;
import it.studiomascia.gestionale.xml.DettaglioLineeType;
import it.studiomascia.gestionale.xml.FatturaElettronicaBodyType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.IterableUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProviderController {
    
    private static final Logger logger = LoggerFactory.getLogger(PagamentoController.class);
    private SimpleDateFormat formattaData = new SimpleDateFormat("dd-MM-yyyy");
    
    @Autowired
    private AnagraficaSocietaService  providerService;

    @Autowired
    private AnagraficaSocietaRepository  providerRepository;

       @Autowired
    private XmlFatturaBaseRepository xmlFatturaBaseRepository;
    
    @Autowired
    private XmlFatturaBaseService xmlFatturaBaseService;
    
    @Autowired
    private DdtRepository ddtRepository;
    
    @Autowired
    private AnagraficaSocietaRepository anagraficaSocietaRepository;
     
    @Autowired
    private OdaRepository odaRepository;
     
    @GetMapping("/ProvidersList")
    public String providersList(HttpServletRequest request,Model model){
        model.addAttribute("headers", providerService.getHeaders());
        model.addAttribute("rows", providerService.getRows());

        return "providers_lista";
    }
   
    @GetMapping("/Provider/{id}/DDT")
    public String DddtListByProviderId(Model model, @PathVariable String id){

        Integer idProvider = Integer.valueOf(id);
        AnagraficaSocieta provider = providerRepository.findById(idProvider).get();
        
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("Creatore");
        headers.add("Numero");
        headers.add("Data");
        headers.add("Importo");
        headers.add("Note");
        headers.add("Verificato");

        Set<Ddt> lista= provider.getListaDDT();
        
        model.addAttribute("headerProvider", providerService.getHeaders());
        model.addAttribute("provider", provider);
        model.addAttribute("headers", headers);
        model.addAttribute("listaddt", lista);       

        return"lista_ddt_fornitore";
        
        
    }
    
    
    @GetMapping("/Provider/{idProvider}/ModalDdt")
    public String ModalAddDdtProvider (ModelMap model,@PathVariable Integer idProvider){
        
        AnagraficaSocieta provider = anagraficaSocietaRepository.findById(idProvider).get();
        Ddt ddt = new Ddt();
        model.addAttribute("ddt",ddt);  
        model.addAttribute("provider",provider);  
        return "modalContents :: ddtProvider";
    }
 
    
    @PostMapping("/Provider/{idProvider}/ModalDdt")
    public String registraNuovoPagamentoFatturaIn( @ModelAttribute("ddt") Ddt ddt, Model model,@PathVariable Integer idProvider, RedirectAttributes redirectAttributes)
    {
        AnagraficaSocieta provider = anagraficaSocietaRepository.findById(idProvider).get();
        ddt.setCreatore(SecurityContextHolder.getContext().getAuthentication().getName());
        ddt.setAnagraficaSocieta(provider);
        
        ddtRepository.save(ddt);

        //provider.getListaDDT().add(ddt);
        
        //anagraficaSocietaRepository.save(provider);
        return "redirect:/Provider/"+ idProvider +"/DDT";
    }
    
    @GetMapping("/Provider/{id}/ODA")
    public String OdatListByProviderId(Model model, @PathVariable String id){

        Integer idProvider = Integer.valueOf(id);
        AnagraficaSocieta provider = providerRepository.findById(idProvider).get();
        
        ODA x = new ODA();
        
        
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("Creatore");
        headers.add("Numero");
        headers.add("Data");
        headers.add("Importo");
        headers.add("Note");
        headers.add("Verificato");

        Set<ODA> lista= provider.getListaODA();
        
        model.addAttribute("headerProvider", providerService.getHeaders());
        model.addAttribute("provider", provider);
        model.addAttribute("headers", headers);
        model.addAttribute("listaoda", lista);       

        return"lista_oda_fornitore";
        
        
    }
    
    
    @GetMapping("/Provider/{idProvider}/ModalODA")
    public String ModalAddOdaProvider (ModelMap model,@PathVariable Integer idProvider){
        
        AnagraficaSocieta provider = anagraficaSocietaRepository.findById(idProvider).get();
        ODA x = new ODA();
        model.addAttribute("oda",x);  
        model.addAttribute("provider",provider);  
        return "modalContents :: odaProvider";
    }
 
    
    @PostMapping("/Provider/{idProvider}/ModalODA")
    public String registraNuovoODA( @ModelAttribute("oda") ODA oda, Model model,@PathVariable Integer idProvider, RedirectAttributes redirectAttributes)
    {
        AnagraficaSocieta provider = anagraficaSocietaRepository.findById(idProvider).get();
        oda.setCreatore(SecurityContextHolder.getContext().getAuthentication().getName());
        oda.setAnagraficaSocieta(provider);
        
        odaRepository.save(oda);

        //provider.getListaDDT().add(ddt);
        
        //anagraficaSocietaRepository.save(provider);
        return "redirect:/Provider/"+ idProvider +"/ODA";
    }
   
    @GetMapping("/Provider/{id}/Invoices")
    public String InvoicestListByProviderId(Model model, @PathVariable String id){

        Integer idProvider = Integer.valueOf(id);
        AnagraficaSocieta provider = providerRepository.findById(idProvider).get();
        
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("Creatore");
        headers.add("Numero");
        headers.add("Data");
        headers.add("Importo");
        headers.add("Note");
        headers.add("Verificato");

        List<XmlFatturaBase> lista= provider.getListaXmlFatturaBase();

        model.addAttribute("headerProvider", providerService.getHeaders());
        model.addAttribute("provider", provider);
        model.addAttribute("headers", headers);
        

        model.addAttribute("header2", xmlFatturaBaseService.getHeaders());
        model.addAttribute("rows", xmlFatturaBaseService.getRows2(lista.stream().collect(Collectors.toList())));
  
        
        return"lista_fatture_fornitore";
        
        
    }
    
    
      @GetMapping("/Provider/{id}/InvoicesDetails")
    public String InvoicesDetailsByProviderId(Model model, @PathVariable String id){

        Integer idProvider = Integer.valueOf(id);
        AnagraficaSocieta provider = providerRepository.findById(idProvider).get();
        
        List<XmlFatturaBase> lista= provider.getListaXmlFatturaBase();
        List<String> headers = new  ArrayList<>();
        headers.add("IdFattura");
        headers.add("TD");
        headers.add("Data");
        headers.add("Numero");
        headers.add("P.IVA");
        headers.add("NumeroLinea");
        headers.add("CodiceArticolo");
        headers.add("Descrizione");
        headers.add("PrezzoUnitario");
        headers.add("U.M.");
        headers.add("Quantità");
        headers.add("PrezzoTotale");


        List<Map<String, Object>> righe = new ArrayList<Map<String, Object>>();
       
        lista.stream().forEach(xmlFattura -> {
            List<DettaglioLineeType> elencoProdotti = xmlFattura.getFatturaElettronica().getFatturaElettronicaBody().get(0).getDatiBeniServizi().getDettaglioLinee();
            if (xmlFattura.getId()==1938)
            {
                System.out.println("InvoicesDetails:: xmlFattura.getId()= " + xmlFattura.getId() + " : " + Thread.currentThread().getName()); 
            }
            elencoProdotti.forEach(item ->  {
               
                Map<String, Object> riga = new HashMap<>();
                riga.put("IdFattura", xmlFattura.getId());  
                riga.put("TD", xmlFattura.getTipoDocumento());   
                riga.put("Data",  ((xmlFattura.getDataFattura()== null)) ? "N/A" : formattaData.format(xmlFattura.getDataFattura()));
                riga.put("Numero", xmlFattura.getNumeroFattura());
                riga.put("P.IVA",xmlFattura.getPartitaIva() );
                riga.put("NumeroLinea",item.getNumeroLinea());
                String temp= "N/A" ;
                if (item.getCodiceArticolo().size()>0) 
                {
                    temp = (item.getCodiceArticolo().get(0).getCodiceValore() != null) ? item.getCodiceArticolo().get(0).getCodiceValore() : "-";
                    
                }
                riga.put("CodiceArticolo",temp);
                riga.put("Descrizione",item.getDescrizione());
                riga.put("U.M.", item.getUnitaMisura());
                riga.put("PrezzoUnitario", item.getPrezzoUnitario());
                riga.put("Quantità", item.getQuantita());
                riga.put("PrezzoTotale", item.getPrezzoTotale());
                righe.add(riga); 
            });
        });
        model.addAttribute("headerProvider", providerService.getHeaders());
        model.addAttribute("provider", provider);
        model.addAttribute("headers", headers);
        model.addAttribute("rows", righe);
  
        
        return"lista_dettaglio_linee_fatture_fornitore";
        
        
    }
    
}