/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Admin
 */
@Entity
public class AnagraficaSocieta {

    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
       
    @Column(name = "piva")
    private String piva;

    @Column(name = "denominazione")
    private String denominazione;
    
    @Column(name = "indirizzo")
    private String indirizzo;
    
    @Column(name = "fornitore")
    private boolean fornitore;
    
    @OneToMany( mappedBy = "anagraficaSocieta", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private Set<Ddt> listaDDT;
    
    /**
     * @return the listaDDT
     */
    public Set<Ddt> getListaDDT() {
        return listaDDT;
    }

    /**
     * @param listaDDT the listaDDT to set
     */
    public void setListaDDT(Set<Ddt> listaDDT) {
        this.listaDDT = listaDDT;
    }

    @OneToMany( mappedBy = "anagraficaSocieta", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private Set<ODA> listaODA;
    
    /**
     * @return the listaODA
     */
    public Set<ODA> getListaODA() {
        return listaODA;
    }

    /**
     * @param listaODA the listaODA to set
     */
    public void setListaODA(Set<ODA> listaODA) {
        this.listaODA = listaODA;
    }
    
    
   @OneToMany( mappedBy = "anagraficaSocieta", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private List<XmlFatturaBase> listaXmlFatturaBase;
    
    /**
     * @return the listaDDT
     */
    public List<XmlFatturaBase> getListaXmlFatturaBase() {
        return listaXmlFatturaBase;
    }

    /**
     * @param listaDDT the listaDDT to set
     */
    public void setListaXmlFatturaBase(List<XmlFatturaBase> listaXmlFatturaBase) {
        this.listaXmlFatturaBase= listaXmlFatturaBase;
    }
   
    
    
    /**
     * @return the piva
     */
    public String getPiva() {
        return piva;
    }

    /**
     * @param piva the piva to set
     */
    public void setPiva(String piva) {
        this.piva = piva;
    }

    /**
     * @return the denominazione
     */
    public String getDenominazione() {
        return denominazione;
    }

    /**
     * @param denominazione the denominazione to set
     */
    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    /**
     * @return the indirizzo
     */
    public String getIndirizzo() {
        return indirizzo;
    }

    /**
     * @param indirizzo the indirizzo to set
     */
    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }
    
  

        
    
    public Set<Ddt> getStudenti() {
        return getListaDDT();
    }
    public void setStudenti(Set<Ddt> studenti) {
        this.setListaDDT(studenti);
    }
    
    
    
    
    /**
     * @return the attiva status
     */
    public boolean isFornitore() {
        return this.fornitore;
    }

    /**
     * @param attiva the id to set
     */
    public void seFornitore(boolean x ) {
        this.fornitore = x;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }
    
    //Metodo che restituisce un valore booleano che descrive se ci sono fatture
    //da pagare per questo fornitore
    public Boolean isToPay() {
        Boolean ret =false;
        for (XmlFatturaBase fattura: listaXmlFatturaBase){
        if (fattura.isToPay())ret = true;
            break;
        }
        
        return ret;
    }
    
    public List<XmlFatturaBase> getListaXmlFatturaBaseToPay() {
        List<XmlFatturaBase>  ret = new ArrayList<> ();
        for (XmlFatturaBase item: listaXmlFatturaBase){
           if  (item.isToPay() ) ret.add(item);
        }
        return ret;
    }
    
    public List<XmlFatturaBase> getListaXmlFatturaBaseToPay(Instant dataStart)   {
        Instant dataStop = Instant.now();
        return getListaXmlFatturaBaseToPay(dataStart,dataStop);
    }
        
    public List<XmlFatturaBase> getListaXmlFatturaBaseToPay(Instant dataStart, Instant dataStop) {
        
        List<XmlFatturaBase>  ret = new ArrayList<> ();
        XMLGregorianCalendar dataFattura;
        for (XmlFatturaBase item: listaXmlFatturaBase){
              dataFattura = item.getFatturaElettronica().getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData();
               Calendar calendar = Calendar.getInstance(Locale.ITALY);
               calendar.set(dataFattura.getYear(), dataFattura.getMonth(), dataFattura.getDay());
               Instant istantFattura = calendar.getTime().toInstant();
        
               if  (istantFattura.compareTo(dataStart)>0 && 
                       dataStop.compareTo(istantFattura)>0 && 
                       item.isToPay() ) ret.add(item);
            
        }
        return ret;
    }
    
    public List<XmlFatturaBase> getListaXmlFatturaBase(Instant dataStart) {
        Instant dataStop = Instant.now();
        List<XmlFatturaBase>  ret = new ArrayList<> ();
        XMLGregorianCalendar dataFattura;
        for (XmlFatturaBase item: listaXmlFatturaBase){
              dataFattura = item.getFatturaElettronica().getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData();
               Calendar calendar = Calendar.getInstance(Locale.ITALY);
               calendar.set(dataFattura.getYear(), dataFattura.getMonth(), dataFattura.getDay());
               Instant istantFattura = calendar.getTime().toInstant();
        
               if  (istantFattura.compareTo(dataStart)>0 && 
                       dataStop.compareTo(istantFattura)>0) ret.add(item);
            
        }
        return ret;
    }
  
    public List<XmlFatturaBase> getListaXmlFatturaBase(Instant dataStart, Instant dataStop) {
        List<XmlFatturaBase>  ret = new ArrayList<> ();
        XMLGregorianCalendar dataFattura;
        for (XmlFatturaBase item: listaXmlFatturaBase){
              dataFattura = item.getFatturaElettronica().getFatturaElettronicaBody().get(0).getDatiGenerali().getDatiGeneraliDocumento().getData();
               Calendar calendar = Calendar.getInstance(Locale.ITALY);
               calendar.set(dataFattura.getYear(), dataFattura.getMonth(), dataFattura.getDay());
               Instant istantFattura = calendar.getTime().toInstant();
        
               if  (istantFattura.compareTo(dataStart)>0 && 
                       dataStop.compareTo(istantFattura)>0) ret.add(item);
            
        }
        return ret;
    }
}

