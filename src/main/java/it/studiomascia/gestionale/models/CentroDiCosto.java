/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author luigi
 */
@Entity
@XmlRootElement
@Table(name = "centrodicosto")
@NamedQueries({
@NamedQuery(name = "CentroDiCosto.findRootFolder", query = "SELECT f FROM CentroDiCosto f WHERE f.parentId IS NULL")})
public class CentroDiCosto implements Serializable {

    /**
     * @return the controlloFattura
     */
    public Set<ControlloFattura> getControlloFattura() {
        return controlloFattura;
    }

    /**
     * @param controlloFattura the controlloFattura to set
     */
    public void setControlloFattura(Set<ControlloFattura> controlloFattura) {
        this.controlloFattura = controlloFattura;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    private String text;
    
    private Boolean attivo;
    
    @OneToMany(mappedBy = "parentId")
    private List<CentroDiCosto> children;
    
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne
    private CentroDiCosto parentId;
    
    @OneToMany(mappedBy="centroDiCosto")
    private Set<ControlloFattura> controlloFattura;
    
    public CentroDiCosto() {
    }

    public CentroDiCosto(Integer id) {
        this.id = id;
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

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the attivo
     */
    public Boolean getAttivo() {
        return attivo;
    }

    /**
     * @param attivo the attivo to set
     */
    public void setAttivo(Boolean attivo) {
        this.attivo = attivo;
    }

    @XmlTransient
    public List<CentroDiCosto> getChildren() {
        return children;
    }

    public void setChildren(List<CentroDiCosto> folderFile2List) {
        this.children = folderFile2List;
    }

    public CentroDiCosto getParentId() {
        return parentId;
    }

    public void setParentId(CentroDiCosto parentId) {
        this.parentId = parentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CentroDiCosto)) {
            return false;
        }
        CentroDiCosto other = (CentroDiCosto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.studiomascia.gestionale.models.CentroDiCosto[ id=" + id + " ]";
    }
    
    
    
}
