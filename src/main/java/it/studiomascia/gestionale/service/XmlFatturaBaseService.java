/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.service;

import it.studiomascia.gestionale.models.Pagamento;
import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.models.XmlFatturaBasePredicate;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import it.studiomascia.gestionale.xml.AnagraficaType;
import it.studiomascia.gestionale.xml.DatiAnagraficiCessionarioType;
import it.studiomascia.gestionale.xml.DatiGeneraliDocumentoType;
import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
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
public class XmlFatturaBaseService {
    
    @Autowired
    private XmlFatturaBaseRepository xmlFatturaBaseRepository;
        
     /* INIZIO Metodi comuni per tutti i mapping */
    private SimpleDateFormat formattaData = new SimpleDateFormat("yyyy-MM-dd");
    
    // Prepara la Map da aggiungere alla view 
    public List<String> getHeaders (){
        
        List<String> headers = new  ArrayList<>();
        headers.add("Id");
        headers.add("TD");
        headers.add("Data Reg.");
        headers.add("N.Reg.");
        headers.add("Data");
        headers.add("Numero");
        headers.add("P.IVA");
        headers.add("Denominazione");
        headers.add("Importo");  
        return headers;
    }

    public List<Map<String, Object>> getRows (int anno){
        //List<XmlFatturaBase> listaFatture = xmlFatturaBaseRepository.findByAttivaFalse();
        List<XmlFatturaBase> listaFatture = xmlFatturaBaseRepository.findByAttivaFalseAndDataFatturaBetween(new Date(anno, 0, 1),new Date(anno, 11,31));
        
//        List<XmlFatturaBase> listaFiltrata = new ArrayList<XmlFatturaBase>();
//        for (XmlFatturaBase xmlFattura : listaFatture){
//              
//            // controlla se la fattura è passiva, se non è pagata e se è stata emessa nell'anno corretto 
//            if (xmlFattura.getFatturaElettronica().getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().getYear()==anno)
//            {
//                listaFiltrata.add(xmlFattura);
//            }
//        }  
        return getRows2 (listaFatture);
    }
    
    public List<Map<String, Object>> getRows (){
        //long inizio = System.currentTimeMillis();
        //System.out.println("xmlFatturaBaseService:: getRows -  xmlFatturaBaseRepository.findByAttivaFalse: " +  (System.currentTimeMillis()-inizio));
        List<XmlFatturaBase> listaFatture = xmlFatturaBaseRepository.findByAttivaFalse();
        //System.out.println("xmlFatturaBaseService:: getRows - xmlFatturaBaseRepository.findByAttivaFalse: " +  (System.currentTimeMillis()-inizio));

        return getRows2 (listaFatture);
    }   
  
    public List<Map<String, Object>> getRows2 (List<XmlFatturaBase> listaFatture)
    {
        long inizio = System.currentTimeMillis();
        List<Map<String, Object>> righe = new ArrayList<Map<String, Object>>();
       
        int conta =0;
        //String strData="";
        //listaFatture.parallelStream().forEach(xmlFattura -> {
        listaFatture.stream().forEach(new Consumer<XmlFatturaBase>() {
            @Override
            public void accept(XmlFatturaBase xmlFattura) {
                System.out.println("xmlFattura.getId()= " + xmlFattura.getId() + " : " + Thread.currentThread().getName());
                String strData = ((xmlFattura.getDataRegistrazione() == null)) ? "N/A" : formattaData.format(xmlFattura.getDataRegistrazione());
                
                Map<String, Object> riga = new HashMap<String, Object>();
                riga.put("Id", xmlFattura.getId());
                riga.put("TD", xmlFattura.getTipoDocumento());
                riga.put("Data Reg.",  strData);
                riga.put("N.Reg.", xmlFattura.getNumeroRegistrazione());
                //riga.put("Data",  xmlFattura.getDataFattura().toString());
                riga.put("Data",  ((xmlFattura.getDataFattura()== null)) ? "N/A" : formattaData.format(xmlFattura.getDataFattura()));
                riga.put("Numero", xmlFattura.getNumeroFattura());
                riga.put("P.IVA",xmlFattura.getPartitaIva() );
                riga.put("Denominazione",xmlFattura.getRagioneSociale() );
                riga.put("Descrizione",xmlFattura.getDescrizione() );
                riga.put("Importo", xmlFattura.getImportoFattura().toString());
                riga.put("Saldata", xmlFattura.getStatoPagamento());
                riga.put("Controllata", xmlFattura.getEsitoUltimoControllo());
                String strData2 = ((xmlFattura.getUltimoControllo().getId() ==  0 ) ? "N/A" : xmlFattura.getUltimoControllo().getCentroDiCosto().getText());
                riga.put("CDC", strData2);
                righe.add(riga);
            }
        });
        
            
        System.out.println("GETROWS:: tempo impiegato: " +  (System.currentTimeMillis()-inizio));
       return righe;
    }
     
     public List<Map<String, Object>> getListaFattureAttive ()
    {
        List<XmlFatturaBase> listaFatture = xmlFatturaBaseRepository.findByAttivaTrue();

        long inizio = System.currentTimeMillis();
        List<Map<String, Object>> righe = new ArrayList<Map<String, Object>>();
       
        int conta =0;
        //String strData="";
        //listaFatture.parallelStream().forEach(xmlFattura -> {
        listaFatture.stream().forEach(xmlFattura -> {
            System.out.println("xmlFattura.getId()= " + xmlFattura.getId() + " : " + Thread.currentThread().getName()); 
            
            Map<String, Object> riga = new HashMap<String, Object>();
            riga.put("Id", xmlFattura.getId());   
            riga.put("TD", xmlFattura.getTipoDocumento());   
            riga.put("Numero", xmlFattura.getNumeroFattura());
            riga.put("Data",  ((xmlFattura.getDataFattura()== null)) ? "N/A" : formattaData.format(xmlFattura.getDataFattura()));
            riga.put("P.IVA",xmlFattura.getPartitaIva() );
            riga.put("Denominazione",xmlFattura.getRagioneSociale() );
            riga.put("Causale",xmlFattura.getDescrizione() );
            riga.put("Importo", xmlFattura.getImportoFattura().toString());
            righe.add(riga); 
        
        
        });
        
            
        System.out.println("getListaFattureAttive:: tempo impiegato: " +  (System.currentTimeMillis()-inizio));
       return righe;
    }
   
    
    public void UpdateFieldsTable(XmlFatturaBase xmlFattura)
    {
            //Definisce oggetti item e anagrafica per comodità visto che vengono utilizzati spesso
            DatiGeneraliDocumentoType item = xmlFattura.getFatturaElettronica().getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento();
            AnagraficaType anagraficaFornitore = xmlFattura.getFatturaElettronica().getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica();
            AnagraficaType anagraficaCliente = xmlFattura.getFatturaElettronica().getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici().getAnagrafica();
            
            //Setta le parti comuni a tutti i tipi di documenti
            xmlFattura.setDataFattura(item.getData().toGregorianCalendar().getTime());
            xmlFattura.setNumeroFattura(item.getNumero());
            xmlFattura.setImportoFattura(Optional.ofNullable(item.getImportoTotaleDocumento()).orElse(BigDecimal.ZERO));
            xmlFattura.setTipoDocumento((item.getTipoDocumento() != null) ? item.getTipoDocumento().value() : "TD00");
            xmlFattura.setDescrizione( (item.getCausale()!=null ) ?  xmlFattura.getFatturaElettronica().getFatturaElettronicaBody().get(0).getDatiBeniServizi().getDettaglioLinee().get(0).getDescrizione() : "N/A" );
            
            //IL CAMPO ANAGRAFICA DIPENDE DAL TIPO DI FATTURA, SE ATTIVA O PASSIVA
            //NEL CASO DI FATTURA ATTIVA RAPPRESENTA IL CLIENTE
            //NEL CASO DI FATTURA PASSIVA RAPPRESENTA IL FORNITORE
            
            String denominazione="";
            if (!xmlFattura.isAttiva()){
                xmlFattura.setPartitaIva(xmlFattura.getFatturaElettronica().getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice());
                if (anagraficaFornitore.getDenominazione()!=null)
                {
                    denominazione=anagraficaFornitore.getDenominazione();
                }else{
                    if (anagraficaFornitore.getTitolo()!=null)  denominazione+= anagraficaFornitore.getTitolo() +" ";
                    if (anagraficaFornitore.getNome()!=null)  denominazione+= anagraficaFornitore.getNome() +" ";
                    if (anagraficaFornitore.getCognome()!=null)  denominazione+= anagraficaFornitore.getCognome() +" ";
                }
            }else{
                DatiAnagraficiCessionarioType committente = xmlFattura.getFatturaElettronica().getFatturaElettronicaHeader().getCessionarioCommittente().getDatiAnagrafici();
                xmlFattura.setPartitaIva(
                        committente.getIdFiscaleIVA()==null ? committente.getCodiceFiscale() : committente.getIdFiscaleIVA().getIdCodice()
                );
                if (anagraficaCliente.getDenominazione()!=null)
                {
                    denominazione=anagraficaCliente.getDenominazione();
                }else{
                    if (anagraficaCliente.getTitolo()!=null)  denominazione+= anagraficaCliente.getTitolo() +" ";
                    if (anagraficaCliente.getNome()!=null)  denominazione+= anagraficaCliente.getNome() +" ";
                    if (anagraficaCliente.getCognome()!=null)  denominazione+= anagraficaCliente.getCognome() +" ";
                }
            }
            xmlFattura.setRagioneSociale(denominazione);  
            
            xmlFattura = xmlFatturaBaseRepository.save(xmlFattura);
            
    }

/*   
    public List<Map<String, Object>> getRows (List<XmlFatturaBase> listaFatture){
        
        long inizio = System.currentTimeMillis();

 
        List<Map<String, Object>> righe = new ArrayList<Map<String, Object>>();
        String  strData = null;
        byte[] byteArr;
        try {
            StringWriter sw = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
            Unmarshaller jaxbUnMarshaller = context.createUnmarshaller();
            Marshaller jaxbMarshaller = context.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            int conta =0;
            
            for (XmlFatturaBase xmlFattura:listaFatture) {

                byteArr = xmlFattura.getXmlData().getBytes("UTF-8");
                strData = "";
                sw = new StringWriter();
            
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                FatturaElettronicaType item = root.getValue();
                jaxbMarshaller.marshal(root, sw);
                Date dataFattura = item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData().toGregorianCalendar().getTime();
                String numeroFattura= item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getNumero();
                String partitaIVA = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getIdFiscaleIVA().getIdCodice().toString();
                String denominazione="";
                if (item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione()!=null)
                {
                    denominazione=item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica().getDenominazione();
                }else{
                    AnagraficaType  anagrafica = item.getFatturaElettronicaHeader().getCedentePrestatore().getDatiAnagrafici().getAnagrafica();
                    if (anagrafica.getTitolo()!=null)  denominazione+= anagrafica.getTitolo() +" ";
                    if (anagrafica.getNome()!=null)  denominazione+= anagrafica.getNome() +" ";
                    if (anagrafica.getCognome()!=null)  denominazione+= anagrafica.getCognome() +" ";
                }
                String importoFattura= "0";
                    if (item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento() !=null){
                        importoFattura = item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getImportoTotaleDocumento().toString();
                    }
                strData = ((xmlFattura.getDataRegistrazione() == null)) ? "N/A" : formattaData.format(xmlFattura.getDataRegistrazione());

                String descrizione = "N/A";
                if (item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getCausale()!=null ){
                   descrizione= item.getFatturaElettronicaBody().get(0).getDatiBeniServizi().getDettaglioLinee().get(0).getDescrizione();
                }

                Map<String, Object> riga = new HashMap<String, Object>();
                riga.put("Id", xmlFattura.getId());   
                riga.put("TD", item.getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getTipoDocumento().value() );   
                riga.put("Data Reg.",  strData);
                riga.put("N.Reg.", xmlFattura.getNumeroRegistrazione());
                riga.put("Data",  formattaData.format(dataFattura));
                riga.put("Numero", numeroFattura);
                riga.put("P.IVA",partitaIVA );
                riga.put("Denominazione",denominazione );
                riga.put("Descrizione",descrizione );
                riga.put("Importo", importoFattura);
                riga.put("Saldata", xmlFattura.getStatoPagamento());
                riga.put("Controllata", xmlFattura.getEsitoUltimoControllo());
                righe.add(riga); 
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } 
        System.out.println("GETROWS:: tempo impiegato: " +  (System.currentTimeMillis()-inizio));

       return righe;
    }

*/
}
