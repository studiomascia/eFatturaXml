/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author Admin
 */
    
@Entity
@Table(name = "dipendente_contratto")
public class DipendenteContratto {

    /**
     * @return the fileContratto
     */
    public Set<DBFile> getFileContratto() {
        return fileContratto;
    }

    /**
     * @param fileContratto the fileContratto to set
     */
    public void setFileContratto(Set<DBFile> fileContratto) {
        this.fileContratto = fileContratto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;    

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "data_inizio")
    private Date dataInizio;
    
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "data_fine")
    private Date dataFine;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "data_cessazione")
    private Date dataCessazione;

    @ManyToOne
    @JoinColumn
    private Dipendente dipendente;
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<DBFile> fileContratto;
     
    
    
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

    /**
     * @return the dataInizio
     */
    public Date getDataInizio() {
        return dataInizio;
    }

    /**
     * @param dataInizio the dataInizio to set
     */
    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    /**
     * @return the dataFine
     */
    public Date getDataFine() {
        return dataFine;
    }

    /**
     * @param dataFine the dataFine to set
     */
    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    /**
     * @return the dataCessazione
     */
    public Date getDataCessazione() {
        return dataCessazione;
    }

    /**
     * @param dataCessazione the dataCessazione to set
     */
    public void setDataCessazione(Date dataCessazione) {
        this.dataCessazione = dataCessazione;
    }

    /**
     * @return the dipendente
     */
    public Dipendente getDipendente() {
        return dipendente;
    }

    /**
     * @param dipendente the dipendente to set
     */
    public void setDipendente(Dipendente dipendente) {
        this.dipendente = dipendente;
    }
    

    
    
}