/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

import java.util.Date;

/**
 *
 * @author luigi
 */
public class FatturaVirtuale {
private Integer id;
   private Date dataReg;
   private String numeroReg;
   private Date dataFatt;
   private String numeroFatt;
   private String pIva;
   private String denominazione;
   private String imponibile;
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
     * @return the dataReg
     */
    public Date getDataReg() {
        return dataReg;
    }

    /**
     * @param dataReg the dataReg to set
     */
    public void setDataReg(Date dataReg) {
        this.dataReg = dataReg;
    }

    /**
     * @return the numeroReg
     */
    public String getNumeroReg() {
        return numeroReg;
    }

    /**
     * @param numeroReg the numeroReg to set
     */
    public void setNumeroReg(String numeroReg) {
        this.numeroReg = numeroReg;
    }

    /**
     * @return the dataFatt
     */
    public Date getDataFatt() {
        return dataFatt;
    }

    /**
     * @param dataFatt the dataFatt to set
     */
    public void setDataFatt(Date dataFatt) {
        this.dataFatt = dataFatt;
    }

    /**
     * @return the numeroFatt
     */
    public String getNumeroFatt() {
        return numeroFatt;
    }

    /**
     * @param numeroFatt the numeroFatt to set
     */
    public void setNumeroFatt(String numeroFatt) {
        this.numeroFatt = numeroFatt;
    }

    /**
     * @return the pIva
     */
    public String getpIva() {
        return pIva;
    }

    /**
     * @param pIva the pIva to set
     */
    public void setpIva(String pIva) {
        this.pIva = pIva;
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
     * @return the imponibile
     */
    public String getImponibile() {
        return imponibile;
    }

    /**
     * @param imponibile the imponibile to set
     */
    public void setImponibile(String imponibile) {
        this.imponibile = imponibile;
    }
   

   
}
