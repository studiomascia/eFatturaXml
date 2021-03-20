/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.Utility;
import it.studiomascia.gestionale.models.AnagraficaSocieta;
import it.studiomascia.gestionale.models.CentroDiCosto;
import it.studiomascia.gestionale.models.ControlloFattura;
import it.studiomascia.gestionale.models.DBFile;
import it.studiomascia.gestionale.models.Ddt;
import it.studiomascia.gestionale.models.Pagamento;
import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.repository.AnagraficaSocietaRepository;
import it.studiomascia.gestionale.repository.CentroDiCostoRepository;
import it.studiomascia.gestionale.repository.ControlloFatturaRepository;
import it.studiomascia.gestionale.repository.DdtRepository;
import it.studiomascia.gestionale.repository.PagamentoRepository;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import it.studiomascia.gestionale.service.DBFileStorageService;
import it.studiomascia.gestionale.service.XmlFatturaBaseService;
import it.studiomascia.gestionale.xml.DatiTrasmissioneType;
import it.studiomascia.gestionale.xml.DatiTrasportoType;
import it.studiomascia.gestionale.xml.DettaglioPagamentoType;
import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import it.studiomascia.gestionale.xml.IdFiscaleType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.xmlgraphics.util.MimeConstants;
import org.bouncycastle.cms.CMSSignedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 

/**
 *
 * 
 * @author Admin
 */
@Controller
@Slf4j
public class InvoiceController {
     
    @Value("${max.rows.table}")
    private int MAX_ROWS_TABLE;
    
    @Autowired
    private XmlFatturaBaseRepository xmlFatturaBaseRepository;
    
    @Autowired
    private XmlFatturaBaseService xmlFatturaBaseService;
    
    @Autowired
    private PagamentoRepository pagamentoRepository;
    
    @Autowired
    private ControlloFatturaRepository controlloFatturaRepository;
    
    @Autowired
    private DBFileStorageService DBFileStorageService;
    
    @Autowired
    private CentroDiCostoRepository centroDiCostoRepository;
    
    @Autowired
    private AnagraficaSocietaRepository  anagraficaSocietaRepository;
    
    @Autowired
    private DdtRepository  ddtRepository;
    
    /* INIZIO Metodi comuni per tutti i mapping */
    private SimpleDateFormat formattaData = new SimpleDateFormat("dd-MM-yyyy");
    
    public byte[] getData( byte[] p7bytes) throws Exception { 
        ByteArrayOutputStream out = new ByteArrayOutputStream();                 
        try{
            CMSSignedData cms = new CMSSignedData(p7bytes);           
            if(cms.getSignedContent() == null) { 
                //Error!!! 
                return null; 
            } 
            cms.getSignedContent().write(out);           
        }catch (Exception ex){
            p7bytes = org.bouncycastle.util.encoders.Base64.decode(p7bytes);
            try{
                CMSSignedData cms = new CMSSignedData(p7bytes);           
                if(cms.getSignedContent() == null) { 
                    //Error!!! 
                    return null; 
                } 
                cms.getSignedContent().write(out);           
            }catch (Exception e){}
        }

        return out.toByteArray(); 
    } 
    
    
    /* METODO CHE PERMETTE DI VISUALIZZARE LA FATTURA SIA IN CHE OUT */
    @GetMapping("/Invoice/Download/{fileId}")
    public ModelAndView downloadFattura(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileId) throws IOException {
        
        // Load file from database
        Integer id = Integer.valueOf(fileId); 
        XmlFatturaBase item = xmlFatturaBaseRepository.findById(id).get();
    
       byte[] byteArr = item.getXmlData().getBytes("UTF-8");
       Source source = new StreamSource(new ByteArrayInputStream(byteArr) );
 
        // adds the XML source file to the model so the XsltView can detect
        ModelAndView model = new ModelAndView("XSLTView");
        model.addObject("xmlSource", source);
         
        return model;
    }
        
      
    @GetMapping("/downloadFatturaPdf/{fileId}")
    public void downloadFatturaPdf(HttpServletRequest request, HttpServletResponse response,@PathVariable String fileId)
         throws ServletException, IOException{
        try
        {
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());

            //Setup a buffer to obtain the content length
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource("C:\\WORKSPACE\\foglio.xsl"));
            //Make sure the XSL transformation's result is piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            //Setup input
            Source src = new StreamSource(new File("c:\\workspace\\foglio.xml"));

            //Start the transformation and rendering process
            transformer.transform(src, res);
   
            
             //Prepare response
            response.setContentType("application/pdf");
            response.setContentLength(out.size());

            //Send content to Browser
            response.getOutputStream().write(out.toByteArray());
            response.getOutputStream().flush();
            
            
            /*
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test.pdf\"")
                .body(new ByteArrayResource(out.toByteArray()));
             */       
        }catch(Exception e){
            e.printStackTrace();
            //return null;
        }
    }

    /* METODI PER LE FATTURE IN */
         
    @GetMapping("/InvoicesIn")
    public String FatturePassiveList(HttpServletRequest request,Model model){
        model.addAttribute("headers", xmlFatturaBaseService.getHeaders());
        model.addAttribute("rows", xmlFatturaBaseService.getRows());
        model.addAttribute("titoloPagina", "Elenco completo delle fatture passive");

        return "fatture_passive_lista";
        //return FatturePassiveList(request, model, 2020);
    }

    @GetMapping("/InvoicesIn/{anno}")
    public String FatturePassiveList(HttpServletRequest request,Model model, @PathVariable(required = false) Integer anno){
        //default time zone
	ZoneId defaultZoneId = ZoneId.systemDefault();
	//creating the instance of LocalDate using the day, month, year info
        LocalDate inizioAnno = LocalDate.of(anno, 1, 1);        
        LocalDate fineAnno = LocalDate.of(anno, 12, 31);        
        //local date + atStartOfDay() + default time zone + toInstant() = Date
        
         
//        Calendar inizio = Calendar.getInstance(); 
//        inizio.set(anno,0 , 1);
//        Calendar fine = Calendar.getInstance(); 
//        fine.set(anno,11,31);

        model.addAttribute("headers", xmlFatturaBaseService.getHeaders());
        
        if (anno != null) {
            System.out.println("InvoicesIn/anno :: " + inizioAnno + " - "  +fineAnno);
            model.addAttribute("rows",
                    xmlFatturaBaseService.getRows2(
                            xmlFatturaBaseRepository.findAllByDataFatturaBetween(Date.from(inizioAnno.atStartOfDay(defaultZoneId).toInstant()),Date.from(fineAnno.atStartOfDay(defaultZoneId).toInstant()))
                    )
            );
            model.addAttribute("titoloPagina", "Elenco delle fatture passive anno: "+ anno);

        } else {
            System.out.println("InvoicesIn/" );
            model.addAttribute("rows", xmlFatturaBaseService.getRows2(xmlFatturaBaseRepository.findByAttivaFalse()));
            model.addAttribute("titoloPagina", "Elenco completo delle fatture passive");

        }
        
        return "fatture_passive_lista";
    }
    
    public List<XmlFatturaBase> findXmlFatturaBase(XmlFatturaBase probe) {
        return xmlFatturaBaseRepository.findAll(Example.of(probe));
    }
    
    @GetMapping("/InvoicesIn/New")
    public String nuovaFatturaIn() {
        return "fatture_passive_new";
        
    }
    
    @PostMapping("/InvoicesIn/New")
    public String nuovaFatturaIn(@RequestParam("files") MultipartFile[] files, ModelMap modelMap) {            
        return uploadFatture(false,  files,  modelMap);
    }
   
    @GetMapping("/InvoicesOut/New")
    public String nuovaFatturaOut() {
        return "fatture_attive_new";
    }
    
    @PostMapping("/InvoicesOut/New")
    public String nuovaFatturaOut(@RequestParam("files") MultipartFile[] files, ModelMap modelMap) {            
     return uploadFatture(true,  files,  modelMap);
    }
    
    
    public String uploadFatture(Boolean isAttiva, @RequestParam("files") MultipartFile[] files, ModelMap modelMap) {            
        long inizio = System.currentTimeMillis();
        List<XmlFatturaBase> listaEsistenti =xmlFatturaBaseRepository.findAll();
        ArrayList<FatturaElettronicaType> listaFattureElettroniche = new ArrayList<>();
        for (int k=0; k<listaEsistenti.size();k++){
            listaFattureElettroniche.add(listaEsistenti.get(k).getFatturaElettronica());
        }
        listaEsistenti.sort(Comparator.comparing(XmlFatturaBase::getId));
        log.info("Ho ordinato le liste.... durata " + (System.currentTimeMillis() - inizio) );
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("P.IVA");
        headers.add("Data");
        headers.add("Denominazione");
        headers.add("Numero"); 
        headers.add("Importo");     
        List<Map<String, Object>> righe = new ArrayList<>();
        
        for (int k=0;k<files.length;k++){
            log.info(" Inizio Elaborazione file n. " +k);
       
            try {
                //log.info("Carico file= " + files[k].getOriginalFilename());
                
                byte[] byteArr = files[k].getBytes();
                if (files[k].getOriginalFilename().endsWith("p7m")) {
                    byteArr=getData(files[k].getBytes());
                }
                StringWriter sw = new StringWriter();
                JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
                // Unmarshaller serve per convertire il file in un oggetto
                Unmarshaller jaxbUnMarshaller = context.createUnmarshaller();
                // Marshaller serve per convertire l'oggetto ottenuto dal file in una stringa xml
                Marshaller jaxbMarshaller = context.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                FatturaElettronicaType itemFile = root.getValue();
                jaxbMarshaller.marshal(root, sw);
                //log.info("File caricato e convertito - Durata:" + (System.currentTimeMillis() - inizio) );

                // Controllo se il file esiste gia nella tabella, se esiste skip
                int i = 0;
                Boolean trovato =false;
                //log.info("Inizio controllo esistenza fattura");
                DatiTrasmissioneType  txFile = itemFile.getFatturaElettronicaHeader().getDatiTrasmissione();
                String txFileIdCodice = txFile.getIdTrasmittente().getIdCodice();
                String txFileProgessivoInvio = txFile.getProgressivoInvio();
                
		while (i < listaFattureElettroniche.size() && !trovato) {
                    //devo controllare se esiste per lo stessa partita iva la stessa fattura
                    //log.info("ciclo while iterazione n. "+ i +" - inizio " + (System.currentTimeMillis() - inizio) );
                    DatiTrasmissioneType  trEsistente = listaFattureElettroniche.get(i).getFatturaElettronicaHeader().getDatiTrasmissione();
                    //log.info("     --> set trEsistente - Durata " + (System.currentTimeMillis() - inizio) );
                    if (trEsistente.getProgressivoInvio().equals(txFileProgessivoInvio) 
                         && trEsistente.getIdTrasmittente().getIdCodice().equals(txFileIdCodice ) )
                    {
                        //log.info("--> Primo controllo OK ");
                        if (listaEsistenti.get(i).getFatturaElettronica().getFatturaElettronicaHeader().getDatiTrasmissione().getProgressivoInvio().equals(txFileProgessivoInvio) 
                         && listaEsistenti.get(i).getFatturaElettronica().getFatturaElettronicaHeader().getDatiTrasmissione().getIdTrasmittente().getIdCodice().equals(txFileIdCodice ) )
                        {
                            //log.info("--> Secondo controllo OK ");
                            //log.info("--> Fattura esistente ID = " + listaEsistenti.get(i).getId());
                            trovato =true;
                            listaEsistenti.get(i).setXmlData(sw.toString());
                            //log.info("-->                        Aggiorno XML");
                            listaEsistenti.set(i, xmlFatturaBaseRepository.save(listaEsistenti.get(i)));

                            xmlFatturaBaseService.UpdateFieldsTable(listaEsistenti.get(i));
                            //log.info("-->                        Tabella DB aggiornata");
                            xmlFatturaBaseRepository.flush();
                            log.info("--> Fattura  esistente ---> aggiornamento terminato al ciclo " + i +"- Durata:" + (System.currentTimeMillis() - inizio) );

                        }
                    }
                    //log.info("ciclo while iterazione n. "+ i +" - Durata" + (System.currentTimeMillis() - inizio) );
                    i++;
                }                
                //log.info("Fine controllo esistenza fattura - Durata" + (System.currentTimeMillis() - inizio) );

                
                if (!trovato){
                    log.info("--> Fattura non esistente ---> creo ed inserisco la nuova fattura  - Durata:" + (System.currentTimeMillis() - inizio) );

                    XmlFatturaBase xmlFattura = new XmlFatturaBase(isAttiva);

                    Date dataFattura = itemFile.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().toGregorianCalendar().getTime();
                    //Modified to insert invoice date into field table
                    xmlFattura.setDataFattura(dataFattura);
                    String numeroFattura= itemFile.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero();
                    //Modified to insert invoice number into field table
                    xmlFattura.setNumeroFattura(numeroFattura);

                    String importoFattura= "0";
                    // Alcune fatture con importo 0 hanno un valore null 
                    if (itemFile.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento() !=null){
                        xmlFattura.setImportoFattura(itemFile.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento());
                        importoFattura = xmlFattura.getImportoFattura().toString();
                    }
                    
                    String partitaIVA = itemFile.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice();
                    String denominazione = itemFile.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione();
                    xmlFattura.setRagioneSociale(denominazione);
                    // Aggiungere un controllo sulla partita iva:
                    // Se nella tabella fornitori non esiste questa partita IVA allora va inserito il nuovo fornitore
                    // e il relativo id va inserito nella chiave esterna IdFornitore
                    if (!isAttiva){
                        AnagraficaSocieta var = anagraficaSocietaRepository.findByPiva(partitaIVA);
                        if ( var== null )
                        {
                            var = new AnagraficaSocieta();
                            var.setPiva(partitaIVA );
                            var.setDenominazione(denominazione );
                            var.seFornitore(true);
                            String indirizzo = itemFile.getFatturaElettronicaHeader().getCedentePrestatore().getSede().getIndirizzo();
                            String civico = itemFile.getFatturaElettronicaHeader().getCedentePrestatore().getSede().getNumeroCivico();
                            String cap = itemFile.getFatturaElettronicaHeader().getCedentePrestatore().getSede().getCAP();
                            String comune = itemFile.getFatturaElettronicaHeader().getCedentePrestatore().getSede().getComune();
                            var.setIndirizzo(indirizzo +","+ civico+";"+ cap +";"+ comune);
                            anagraficaSocietaRepository.saveAndFlush(var);
                        }
                        xmlFattura.setAnagraficaSocieta(var);
                        
                        if(itemFile.getFatturaElettronicaBody().get(0).getDatiPagamento().size()>0){
                            List<DettaglioPagamentoType>  dettaglioPagamento =  itemFile.getFatturaElettronicaBody().get(0).getDatiPagamento().get(0).getDettaglioPagamento();
                            if (Utility.CheckInvoicePayed(dettaglioPagamento)){
                                    Set<Pagamento> setp = new HashSet<Pagamento>();
                                    Pagamento p = new Pagamento();
                                    p.setDataVersamento(dataFattura);
                                    p.setImportoVersamento(itemFile.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento());
                                    p.setNote("pagamento impostato automaticamente");
                                    p.setCreatore("System");
                                    p.setStatoPagamento(Pagamento.PAGAMENTO_SISTEMA);
                                    setp.add(p);
                                    xmlFattura.setPagamenti(setp);
                            }
                        }
                    }
                    
                    // fine aggiunta 


                    xmlFattura.setDataInserimento(new Date());
                   
                    xmlFattura.setFileName(files[k].getOriginalFilename());
                    xmlFattura.setCreatore(SecurityContextHolder.getContext().getAuthentication().getName());
                    xmlFattura.setXmlData(sw.toString());

                    
                    xmlFattura = xmlFatturaBaseRepository.save(xmlFattura);
                    xmlFatturaBaseService.UpdateFieldsTable(xmlFattura);
                    xmlFatturaBaseRepository.flush();
                    
                    
                    // Perpara la Map da aggiungere alla view 
                    Map<String, Object> riga = new HashMap<String, Object>(4);
                    riga.put("Id", xmlFattura.getId());   
                    riga.put("P.IVA",partitaIVA );
                    riga.put("Denominazione",denominazione );
                    riga.put("Data",  LocalDateTime.ofInstant(dataFattura.toInstant(), ZoneId.systemDefault()).toLocalDate());
                    riga.put("Numero", numeroFattura);
                    riga.put("Importo", importoFattura);
                    righe.add(riga);
                    log.info("       Fattura Inserita = "  + files[k].getOriginalFilename() + " - Durata:" + (System.currentTimeMillis() - inizio) );

                }
                
            } catch (JAXBException e) {
                log.info("ERRORE CARICAMENTO FILE - JAXBException)");
                //e.printStackTrace();
            } catch (IOException e) {
                log.info("ERRORE CARICAMENTO FILE - IOException");
                //e.printStackTrace();
            } catch (Exception e) {
                log.info("ERRORE CARICAMENTO FILE - Exception");
                //e.printStackTrace();
            }
            modelMap.addAttribute("headers", headers);
            modelMap.addAttribute("rows", righe);
            log.info("Fine elaborazione file n. " + k + " - Durata:" + (System.currentTimeMillis() - inizio) );
        } //end for
        
        
        return (isAttiva) ? "fatture_attive_caricate" : "fatture_passive_caricate";
    }
   
    /* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */ 
    
    @GetMapping("/InvoiceIn/ModalRegister/{id}")
    public String ModalEditFatturaIn(ModelMap model,@PathVariable Integer id){
        XmlFatturaBase x = xmlFatturaBaseRepository.findById(id).get(); 
        model.addAttribute("fattura",x);  
        return "modalContents :: registerInvoice";
    }
    
    @GetMapping("/InvoiceIn/Register/{id}")
    public String EditFatturaIn(Model model,@PathVariable Integer id){
        XmlFatturaBase x = xmlFatturaBaseRepository.findById(id).get();
        model.addAttribute("fattura",x);  
        model.addAttribute("dataReg",x.getDataRegistrazione());  
        return "fatture_passive_registra";
    }
     
    @PostMapping("/InvoiceIn/Register/{id}")
    public String aggiornaFatturaIn(@Valid @ModelAttribute("fattura") XmlFatturaBase updateFattura, BindingResult bindingResult,Model model, RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors()) { return "fatture_passive_registra"; }
        XmlFatturaBase vecchiaFattura = xmlFatturaBaseRepository.findById(updateFattura.getId()).get();
        
        vecchiaFattura.setNumeroRegistrazione(updateFattura.getNumeroRegistrazione());
        vecchiaFattura.setDataRegistrazione(updateFattura.getDataRegistrazione());
        redirectAttributes.addFlashAttribute("messaggio","La fattura: è stata registrata");  
        xmlFatturaBaseRepository.save(vecchiaFattura);
        //return "redirect:/InvoiceIn/Register/"+updateFattura.getId();
        return "redirect:/InvoicesIn";
    }
   
    /* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */ 
    
    @GetMapping("/Invoice/{id}/ModalPayment")
    public String ModalAddPaymentFatturaIn(ModelMap model,@PathVariable Integer id){
        XmlFatturaBase x = xmlFatturaBaseRepository.findById(id).get();
        Pagamento p = new Pagamento();
        model.addAttribute("fattura",x);  
        model.addAttribute("pagamento",p);  
        return "modalContents :: paymentInvoice";
    }
    
    @PostMapping("/Invoice/{id}/ModalPayment")
    public String registraNuovoPagamentoFatturaIn( @ModelAttribute("pagamento") Pagamento pagamento, Model model,@PathVariable Integer id, HttpServletRequest request, RedirectAttributes redirectAttributes)
    {
        XmlFatturaBase vecchiaFattura = xmlFatturaBaseRepository.findById(id).get();
        pagamento.setCreatore(SecurityContextHolder.getContext().getAuthentication().getName());
        int temp = Integer.parseInt(request.getParameterValues("cbSaldata")[0]);
       
        pagamento.setStatoPagamento(temp);
        vecchiaFattura.getPagamenti().add(pagamento);
        
        xmlFatturaBaseRepository.save(vecchiaFattura);
//        redirectAttributes.addFlashAttribute("messaggio","Pagamento inserito");  
        return "redirect:/InvoiceIn/"+ id +"/Payments";
    }
    
    @GetMapping("/Invoice/{IdFattura}/Payment/{id}/ModalAttachment")
    public String ModalAddAttachmentToPaymentFatturaIn(ModelMap model,@PathVariable Integer IdFattura,@PathVariable Integer id){
//        XmlFatturaBase x = xmlFatturaBaseRepository.findById(id).get();
        Pagamento p = pagamentoRepository.findById(id).get();
        model.addAttribute("IdFattura",IdFattura);  
        model.addAttribute("pagamento",p);  
        return "modalContents :: attachmentPaymentInvoice";
    }
    
    @PostMapping("/Payment/{id}/ModalAttachment")
    public String postModalAddAttachmentToPaymentFatturaIn( @ModelAttribute("pagamento") Pagamento pagamento,@PathVariable Integer id, RedirectAttributes redirectAttributes, @RequestParam("files") MultipartFile[] files, HttpServletRequest request)
    {
        if (request.getParameterMap().get("txtDescription") != null && request.getParameterMap().get("txtDescription").length > 0) {
            
            Pagamento p = pagamentoRepository.findById(id).get();
            for (int k=0;k<files.length;k++)
            {
               
                DBFile dbFile = DBFileStorageService.storeFile(files[k],request.getParameter("txtDescription").toString());
                 p.getFilesPagamenti().add(dbFile);
                 pagamentoRepository.save(p);
            }
        }
        return "redirect:/InvoiceIn/"+ request.getParameter("IdFattura").toString() +"/Payments";
    }
    
    /* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */ 
   
    @GetMapping("/InvoiceIn/{id}/ModalCheck")
    public String ModalAddCheckFatturaIn(ModelMap model,@PathVariable Integer id){
        XmlFatturaBase x = xmlFatturaBaseRepository.findById(id).get();
        ControlloFattura p = new ControlloFattura();
        List<CentroDiCosto> l =  centroDiCostoRepository.findAll();
        model.addAttribute("fattura",x);  
        model.addAttribute("controllo",p);  
        model.addAttribute("listaCdc",l);  
        return "modalContents :: checkInvoice";
    }
    
    @PostMapping("/InvoiceIn/{id}/ModalCheck")
    public String registraNuovoCheckFatturaIn( @ModelAttribute("controllo") ControlloFattura controllo,@PathVariable Integer id, HttpServletRequest request)
    {
        XmlFatturaBase vecchiaFattura = xmlFatturaBaseRepository.findById(id).get();
        controllo.setDataControllo(new Date());
        controllo.setCreatore(SecurityContextHolder.getContext().getAuthentication().getName());
        if (request.getParameter("ddlCentroDiCosto") != null) {
            int id2 = Integer.parseInt(request.getParameter("ddlCentroDiCosto"));
            //controllo.setCentroDiCosto(centroDiCostoRepository.findById(id2).get().getText());
            controllo.setCentroDiCosto(centroDiCostoRepository.findById(id2).get());
        }
        if (request.getParameter("cbControllata") != null) {
            int id3 = Integer.parseInt(request.getParameter("cbControllata"));
            controllo.setEsitoControllo(id3);
        }


        vecchiaFattura.getControlli().add(controllo);
        
        xmlFatturaBaseRepository.save(vecchiaFattura);
//        redirectAttributes.addFlashAttribute("messaggio","Pagamento inserito");  
        return "redirect:/InvoiceIn/"+ id +"/Checks";
    }
    
    @GetMapping("/InvoiceIn/{IdFattura}/Check/{id}/ModalAttachment")
    public String ModalAddAttachmentToCheckFatturaIn(ModelMap model,@PathVariable Integer IdFattura,@PathVariable Integer id){
//        XmlFatturaBase x = xmlFatturaBaseRepository.findById(id).get();
        ControlloFattura p = controlloFatturaRepository.findById(id).get();
        model.addAttribute("IdFattura",IdFattura);  
        model.addAttribute("controllo",p);  
        return "modalContents :: attachmentCheckInvoice";
    }
    
    @PostMapping("/Check/{id}/ModalAttachment")
    public String postModalAddAttachmentToCheckFatturaIn( @ModelAttribute("controllo") ControlloFattura controllo,@PathVariable Integer id, RedirectAttributes redirectAttributes, @RequestParam("files") MultipartFile[] files, HttpServletRequest request)
    {
        if (request.getParameterMap().get("txtDescription") != null && request.getParameterMap().get("txtDescription").length > 0) {
            
            ControlloFattura p = controlloFatturaRepository.findById(id).get();
            for (int k=0;k<files.length;k++)
            {
               
                DBFile dbFile = DBFileStorageService.storeFile(files[k],request.getParameter("txtDescription").toString());
                 p.getFilesControlloFattura().add(dbFile);
                 controlloFatturaRepository.save(p);
            }
        }
        return "redirect:/InvoiceIn/"+ request.getParameter("IdFattura").toString() +"/Checks";
    }
    
    @GetMapping("/InvoiceIn/{fatturaId}/Checks")
    public String ControlliFattura(Model model, @PathVariable String fatturaId){
        List<String> headers = new  ArrayList<>();
            headers.add("Id");
            headers.add("Registro IVA");
            headers.add("N. Fattura");
            headers.add("Data Fattura");
            headers.add("P.IVA");
            headers.add("Denominazione");
            headers.add("Importo");
            
            String strData="N/A";
            Integer id = Integer.valueOf(fatturaId);
            XmlFatturaBase xmlFattura = xmlFatturaBaseRepository.findById(id).get();
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
//                String importoFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().toString();
                 String importoFattura= "0";
                    // Alcune fatture con importo 0 hanno un valore null 
                    if (item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento() !=null){
                        importoFattura = item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().toString();
                    }
                
                
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
                riga.put("Importo", importoFattura);
             
                model.addAttribute("fattura", riga);
                model.addAttribute("headers", headers);
 
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }     
            
        Set<ControlloFattura> listacontrolli= xmlFattura.getControlli();
        
        List<DBFile> listaFile = new ArrayList<DBFile>();
        for (ControlloFattura x : listacontrolli){
            listaFile.addAll(x.getFilesControlloFattura());
        }
        
        List lista = new ArrayList(listacontrolli);
        model.addAttribute("listafiles", listaFile);
        model.addAttribute("listacontrolli", lista);
        model.addAttribute("listaddt", xmlFattura.getDDT());
        
        return "lista_controlli_fattura";
    }
    
    /* METODI PER LE FATTURE OUT */
    
    @GetMapping("/InvoicesOut")
    public String FattureAttiveList(HttpServletRequest request,Model model){
        
//        List<XmlFatturaBase> listaFatture = XmlFatturaBasePredicate.filterXmlFatturaBase(xmlFatturaBaseRepository.findAllByOrderByIdDesc(), XmlFatturaBasePredicate.isAttiva());
        List<XmlFatturaBase>  listaFatture= xmlFatturaBaseRepository.findByAttivaTrue();
        // Prepara la Map da aggiungere alla view 
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("TD");
        headers.add("Numero");
        headers.add("Data");
        headers.add("P.IVA");
        headers.add("Denominazione");
        headers.add("Causale");
//        headers.add("Descrizione");
        headers.add("Importo");     
       

       
       
       model.addAttribute("headers", headers);
        model.addAttribute("rows",  xmlFatturaBaseService.getListaFattureAttive());
    return "fatture_attive_lista";
      
    
    }
    
      
    @GetMapping("/InvoiceOut/{fatturaId}/Payments")
    public String PagamentiFatturaOut(Model model, @PathVariable String fatturaId){
        List<String> headers = new  ArrayList<>();
            headers.add("Id");
            headers.add("N. Fattura");
            headers.add("Data Fattura");
            headers.add("P.IVA");
            headers.add("Denominazione");
            headers.add("Importo");
            
            String strData="N/A";
            Integer id = Integer.valueOf(fatturaId);
            XmlFatturaBase xmlFattura = xmlFatturaBaseRepository.findById(id).get();
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
                String partitaIVA =  item.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice();
                String denominazione = item.getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().getAnagrafica().getDenominazione();
        
                
                
                
                Map<String, Object> riga = new HashMap<String, Object>();
                riga.put("Id", xmlFattura.getId());   
                riga.put("N. Fattura", numeroFattura);
                riga.put("Data Fattura", formattaData.format(dataFattura));
                riga.put("P.IVA",partitaIVA );
                riga.put("Denominazione",denominazione );
                riga.put("Importo", importoFattura);
             
                model.addAttribute("fattura", riga);
                model.addAttribute("headers", headers);
 
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }     
            
        Set<Pagamento> listapagamenti= xmlFattura.getPagamenti();
        
        List<DBFile> listaFile = new ArrayList<DBFile>();
        for (Pagamento x : listapagamenti){
//            List<DBFile> files = dbFileRepository.findAll();
//            int k = files.size();
//            for (DBFile f:  x.getFilesPagamenti())
//            {
//                listaFile.add(f);
//            }
            listaFile.addAll(x.getFilesPagamenti());
        	
        }
        
        List lista = new ArrayList(listapagamenti);
        model.addAttribute("listafiles", listaFile);
        model.addAttribute("listapagamenti", lista);
        return "lista_pagamenti_fattura_out";
    }
    
      
    /* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */ 
   
    @GetMapping("/InvoiceIn/{idFattura}/ModalAssignDDT")
    public String AssociaDDTaFatturaIn(ModelMap model, @PathVariable Integer idFattura, HttpServletRequest request)
    {
        XmlFatturaBase xml = xmlFatturaBaseRepository.findById(idFattura).get();

        List<Ddt> lista = ddtRepository.findByProviderNotAssigned(xml.getAnagraficaSocieta());
    
        model.addAttribute("idFattura",idFattura);  
        model.addAttribute("ddt_non_assegnati",lista);  
        return "modalContents :: assignDDT";
    }
    
    @PostMapping("/InvoiceIn/{idFattura}/ModalAssignDDT")
    public String AssociaDDTaFatturaIn( @RequestParam("listaId") List<String> lista, @PathVariable Integer idFattura, HttpServletRequest request)
    {
        if(lista != null)
        {
            XmlFatturaBase fattura = xmlFatturaBaseRepository.findById(idFattura).get();

            for(String item : lista){
                int idDDT = Integer.parseInt(item);
                Ddt ddt= ddtRepository.findById(idDDT).get();
                ddt.setXmlFatturaBase(fattura);
                ddtRepository.save(ddt);
            } 
        }
        return "redirect:/InvoiceIn/"+ idFattura +"/Checks";
    }
   
    
    
    /* ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- ----- */ 
   
    @GetMapping("/AssociaFatturaTest")
    public String AssociaFatturaTest() {
        XmlFatturaBase xml = xmlFatturaBaseRepository.findById(116).get();
        Ddt ddt = ddtRepository.findById(10).get();
        ddt.setXmlFatturaBase(xml);
        ddtRepository.save(ddt);
        
        return "fatture_attive_new";
    }
   
}
