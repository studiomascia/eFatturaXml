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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Admin
 */
@Entity
public class Pagamento {
    
     public static final int PAGAMENTO_SALDO = 2;
     public static final int PAGAMENTO_SISTEMA = 3;
     public static final int PAGAMENTO_ACCONTO = 1;
     public static final int NESSUN_PAGAMENTO = 0;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;    

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_versamento")
    private Date dataVersamento;
    
    @Column(name = "importo_versamento")
    private  BigDecimal importoVersamento;
    
    @Column(name = "note")
    private  String note;
    
    @Column(name = "creatore")
    private  String creatore;
    
    @Column(name = "saldata")
    private  Integer saldata;
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<DBFile> filePagamenti;

    
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the dataVersamento
     */
    public Date getDataVersamento() {
        return dataVersamento;
    }

    /**
     * @param dataVersamento the dataVersamento to set
     */
    public void setDataVersamento(Date dataVersamento) {
        this.dataVersamento = dataVersamento;
    }

    /**
     * @return the importoVersamento
     */
    public BigDecimal getImportoVersamento() {
        return importoVersamento;
    }

    /**
     * @param importoVersamento the importoVersamento to set
     */
    public void setImportoVersamento(BigDecimal importoVersamento) {
        this.importoVersamento = importoVersamento;
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
     * @return the saldata
     */
    public int getStatoPagamento() {
        return this.saldata;
    }

    /**
     * @param saldata the saldata to set
     */
    public void setStatoPagamento(int saldata) {
        this.saldata = saldata;
    }

    /**
     * @return the filePagamenti
     */
    public Set<DBFile> getFilesPagamenti() {
        return filePagamenti;
    }

    public List<DBFile> getListaFilesPagamenti() {
        return new ArrayList(filePagamenti);
    }
    /**
     * @param filePagamento the filePagamenti to set
     */
    public void setFilesPagamenti(Set<DBFile> filesPagamenti) {
        this.filePagamenti = filesPagamenti;
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
     
    public Pagamento()
    {
        this.id=0;
    
    }
    
}
