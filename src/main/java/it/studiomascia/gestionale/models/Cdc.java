/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
 
/**
 *
 * @author Admin
 */
@Entity
@Table(name = "cdc")
public class Cdc {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private Integer id;

    @NotNull
    private String text;

    @OneToMany
    @OrderColumn
    @JoinColumn(name = "parent_id")
    private List<Cdc> nodes = new LinkedList<Cdc>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_id",insertable=false,updatable=false)
    private Cdc parent;
    
    
    private Boolean attivo;

//    public Cdc getTree(){ 
//        entityManager.createNamedQuery("findAllNodesWithTheirChildren").getResultList(); 
//        Cdc root = entityManager.find(Cdc.class, 1); 
//   return root;
//  } 
       
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
     * @param descrizione the text to set
     */
    public void setText(String descrizione) {
        this.text = descrizione;
    }

    /**
     * @return the nodes
     */
    public List<Cdc> getNodes() {
        return nodes;
    }

    /**
     * @param children the nodes to set
     */
    public void setNodes(List<Cdc> children) {
        this.nodes = children;
    }

    /**
     * @param children the nodes to set
     */
    public void addNode(Cdc child) {
        this.nodes.add(child);
    }
    
    /**
     * @return the parent
     */
    public Cdc getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Cdc parent) {
        this.parent = parent;
    }

    /**
     * @return the attivo
     */
    public Boolean isAttivo() {
        return attivo;
    }

    /**
     * @param attivo the attivo to set
     */
    public void setAttivo(Boolean attivo) {
        this.attivo = attivo;
    }

    public String toJson()
    { 
        return "['text':"+this.text +", 'nodes': " + nodes.toString() + " ]";

    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[{");
        sb.append("'text' : ").append(text);
        sb.append(", 'nodes' : ").append(nodes);
        sb.append("}]");
        return sb.toString();
    }
    
}