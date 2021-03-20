/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author luigi
 */
@Entity
@Table(name = "ODA")
public class ODA {

   
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;    

    @Column(name = "numero")
    private  String numero;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data")
    private Date data;
    
    @Column(name = "importo")
    private  BigDecimal importo;
    
    @Column(name = "note")
    private  String note;
    
    @Column(name = "creatore")
    private  String creatore;
    
    @Column(name = "verificato")
    private  Boolean verificato;
    
    
    
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<DBFile> fileODA;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn( name="idAnagraficaSocieta" )
    private AnagraficaSocieta anagraficaSocieta;
 
     /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    
    public AnagraficaSocieta getAnagraficaSocieta() {
        return anagraficaSocieta;
    }
    public void setAnagraficaSocieta(AnagraficaSocieta provider) {
        this.anagraficaSocieta = provider;
    }
    
    
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @param data the datato set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @return the importo 
     */
    public BigDecimal getImporto() {
        return importo;
    }

    /**
     * @param importo the importo to set
     */
    public void setImporto(BigDecimal importo) {
        this.importo = importo;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the verificato
     */
    public Boolean isVerificato() {
        return this.verificato;
    }
    public Boolean getVerificato() {
        return this.verificato;
    }
    public void setVerificato( Boolean b) {
        this.verificato= b;
    }
    /**
     * @param verificato the verificato to set
     */
    public void setSaldata(Boolean x) {
        this.verificato = x;
    }
    
    public Set<DBFile> getFilesODA() {
        return fileODA;
    }

    public List<DBFile> getListaFilesODA() {
        return new ArrayList(fileODA);
    }
     
    public void setFilesODA(Set<DBFile> x) {
        this.fileODA = x;
    }

     


    /**
     * @return the creatore
     */
    public String getCreatore() {
        return creatore;
    }

    /**
     * @param creatore the creatore to set
     */
    public void setCreatore(String creatore) {
        this.creatore = creatore;
    }
     
 
  

}
