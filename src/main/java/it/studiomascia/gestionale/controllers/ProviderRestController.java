package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.AnagraficaSocieta;
import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.models.XmlFatturaBasePredicate;
import it.studiomascia.gestionale.repository.AnagraficaSocietaRepository;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import it.studiomascia.gestionale.service.AnagraficaSocietaService;
import  it.studiomascia.gestionale.service.XmlFatturaBaseService;
import it.studiomascia.gestionale.xml.AnagraficaType;
import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProviderRestController {
    
    @Autowired
    AnagraficaSocietaService  providerService;
    
    @Autowired
    private AnagraficaSocietaRepository  anagraficaSocietaRepository;
    
    @Autowired
    private XmlFatturaBaseRepository xmlFatturaBaseRepository;
    @Autowired
    private XmlFatturaBaseService xmlFatturaBaseService;


    // recupera la lista delle fatture passive e da questa
    // ricava l'elenco delle anagrafiche delle varie societa 
    @RequestMapping(path="/ProviderShortList", method=RequestMethod.GET)
    public List<AnagraficaSocieta> ProviderShortList(){
        
       
        //FINE:: BLOCCO PER LA PAGINAZIONE
        List<XmlFatturaBase> listaFatture = XmlFatturaBasePredicate.filterXmlFatturaBase(xmlFatturaBaseRepository.findAll(), XmlFatturaBasePredicate.isPassiva());
        //List<AnagraficaSocieta> listaAnagrafica = new ArrayList<AnagraficaSocieta>();
        ArrayList<AnagraficaSocieta> listaAnagrafica = new ArrayList<>();

        String  strData = null;
        byte[] byteArr;
        try {
            StringWriter sw = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
            // Unmarshaller serve per convertire il file in un oggetto
            Unmarshaller jaxbUnMarshaller = context.createUnmarshaller();
            // Marshaller serve per convertire l'oggetto ottenuto dal file in una stringa xml
            Marshaller jaxbMarshaller = context.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
       
            for (XmlFatturaBase xmlFattura:listaFatture) {
                byteArr = xmlFattura.getXmlData().getBytes("UTF-8");
                sw = new StringWriter();
            
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                FatturaElettronicaType item = root.getValue();
                jaxbMarshaller.marshal(root, sw);

                
                String partitaIVA = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice().toString();
                String denominazione = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione();
                if (item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione()!=null)
                {
                    denominazione=item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione();
                }else{
                    AnagraficaType  anagrafica = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica();
                    if (anagrafica.getTitolo()!=null)  denominazione+= anagrafica.getTitolo() +" ";
                    if (anagrafica.getNome()!=null)  denominazione+= anagrafica.getNome() +" ";
                    if (anagrafica.getCognome()!=null)  denominazione+= anagrafica.getCognome() +" ";
                }
                
                
                String indirizzo = item.getFatturaElettronicaHeader().getCedentePrestatore().getSede().getIndirizzo();
                String civico = item.getFatturaElettronicaHeader().getCedentePrestatore().getSede().getNumeroCivico();
                String cap = item.getFatturaElettronicaHeader().getCedentePrestatore().getSede().getCAP();
                String comune = item.getFatturaElettronicaHeader().getCedentePrestatore().getSede().getComune();

                AnagraficaSocieta riga = new AnagraficaSocieta();
                riga.setPiva(partitaIVA );
                riga.setDenominazione(denominazione );
                riga.seFornitore(true);
                riga.setIndirizzo(indirizzo +","+ civico+";"+ cap +";"+ comune);
                listaAnagrafica.add(riga); 
            }
            Set<AnagraficaSocieta> newListaAnagrafica = new LinkedHashSet<>(listaAnagrafica);
            listaAnagrafica.clear();
            listaAnagrafica.addAll(newListaAnagrafica);

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return listaAnagrafica;
    }

    // aggiorna la tabella delle anagrafiche sul DB
    // a partire dall'elenco delle anagrafiche che si ottengono dalle fatture ricevute
    @RequestMapping(path="/AnagraficaUpdateList", method=RequestMethod.GET)
    public void AnagraficaUpdateList(){

        List<AnagraficaSocieta> lista = ProviderShortList();
        
        for (AnagraficaSocieta var : lista) 
        { 
            if (anagraficaSocietaRepository.findByPiva(var.getPiva()) == null )
            {
                anagraficaSocietaRepository.save(var);}
            } 
    }
}