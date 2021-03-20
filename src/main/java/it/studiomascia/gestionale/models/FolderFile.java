/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author luigi
 */
@Entity
@Table(name = "folderfiles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FolderFile.findAll", query = "SELECT f FROM FolderFile f"),
    @NamedQuery(name = "FolderFile.findById", query = "SELECT f FROM FolderFile f WHERE f.id = :id"),
    @NamedQuery(name = "FolderFile.findRootFolder", query = "SELECT f FROM FolderFile f WHERE f.parentId IS NULL")})
public class FolderFile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    
    @Size(max = 255)
    @Column(name = "folder_file_name")
    private String folderFileName;
    
    @OneToMany(mappedBy = "parentId")
    private List<FolderFile> children;
    
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @ManyToOne
    private FolderFile parentId;

    public FolderFile() {
    }

    public FolderFile(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFolderFileName() {
        return folderFileName;
    }

    public void setFolderFileName(String folderFileName) {
        this.folderFileName = folderFileName;
    }

    @XmlTransient
    public List<FolderFile> getChildren() {
        return children;
    }

    public void setChildren(List<FolderFile> folderFile2List) {
        this.children = folderFile2List;
    }

    public FolderFile getParentId() {
        return parentId;
    }

    public void setParentId(FolderFile parentId) {
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
        if (!(object instanceof FolderFile)) {
            return false;
        }
        FolderFile other = (FolderFile) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.studiomascia.gestionale.models.FolderFile[ id=" + id + " ]";
    }
    
}
