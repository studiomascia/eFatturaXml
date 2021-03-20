/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.apache.poi.hssf.record.PageBreakRecord;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Admin
 */
@Entity
public class ControlloFattura {

    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;    

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_controllo")
    private Date dataControllo;
      
    @Column(name = "note")
    private  String note;
    
//    @Column(name = "centro_di_costo")
//    private  String centroDiCosto;
      
    @Column(name = "controllata")
    private  int controllata;
    
    @Column(name = "creatore")
    private  String creatore;
    
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<DBFile> fileControlloFattura;
    
    @ManyToOne
    @JoinColumn(name="CentroDiCosto", nullable=false)
    private CentroDiCosto centroDiCosto;
    
    public static final int VALIDATA = 1;
    public static final int PENDING  = 2;
    public static final int BLOCCATA = 3;
    
    
    
    /**
     * @return the centroDiCosto
     */
    public CentroDiCosto getCentroDiCosto() {

        return centroDiCosto;
    }

    /**
     * @param centroDiCosto the centroDiCosto to set
     */
    public void setCentroDiCosto(CentroDiCosto centroDiCosto) {
        this.centroDiCosto = centroDiCosto;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the dataControllo
     */
    public Date getDataControllo() {
        return dataControllo;
    }

    /**
     * @param dataControllo the dataControllo to set
     */
    public void setDataControllo(Date dataControllo) {
        this.dataControllo = dataControllo;
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
     * @param controllata the controllata to set
     */
    public void setEsitoControllo(int x) {
        this.setControllata(x);
    }
    /**
     * @return the controllata
     */
    public int getEsitoControllo() {
        return getControllata();
    }
    
     public Boolean isControlloOK() {
        Boolean ret=false;
        if (getControllata()== ControlloFattura.VALIDATA) ret =true;
     return ret;   
    }
     public Boolean isControlloNOK() {
        Boolean ret=false;
        if (getControllata()== ControlloFattura.BLOCCATA) ret =true;
     return ret;   
    }
     public Boolean isControlloWAIT() {
        Boolean ret=false;
        if (getControllata() == ControlloFattura.PENDING) ret =true;
     return ret;   
    }
     public String getDescrizioneEsitoControllo()
     {
         String ret="";
         int esito = getEsitoControllo();
         switch (esito)
         {
            case VALIDATA:
               ret="VALIDATA";
               break;
            case BLOCCATA:
               ret="BLOCCATA";
               break;
            case PENDING:
               ret="PENDING";
               break;
         }
         return ret;
     }
    
    /**
     * @return the fileControlloFattura
     */
    public Set<DBFile> getFilesControlloFattura() {
        return fileControlloFattura;
    }

    public List<DBFile> getListaFilesControlloFattura() {
        return new ArrayList(fileControlloFattura);
    }
    /**
     * @param fileControlloFattura the fileControlloFattura to set
     */
    public void setFilesControlloFattura(Set<DBFile> filesControlloFattura) {
        this.fileControlloFattura = filesControlloFattura;
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

    /**
     * @return the controllata
     */
    public int getControllata() {
        return controllata;
    }

    /**
     * @param controllata the controllata to set
     */
    public void setControllata(int controllata) {
        this.controllata = controllata;
    }

    
}
