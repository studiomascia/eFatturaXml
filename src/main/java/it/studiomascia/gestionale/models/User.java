/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
//    $2a$10$J4Njbzt7u0j1.Z96Mh705e/ZG92Mh5ymMj3/ozZ47.HsftBLTz./i
//      $2a$10$YLFL3mWnwXl0Nrk6KIWOBuG.j00BomF1rbqKDkcWbTRg8tyYsSZWa
//    123123
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private int enabled;

    private String shortname;

    @ManyToMany (fetch=FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinTable(name="users_authority", 
            joinColumns = { @JoinColumn(name="users_id")}, 
            inverseJoinColumns ={@JoinColumn(name="authority_id")} )
    
    private Set<Authority> authorities = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int x) {
        this.enabled = x;
    }

    /**
     * @return the authorities
     */
    public List<String> getRolesAsString() {
        List<String> ret;
        ret = new ArrayList();
        for (Authority aut :authorities)
        {
            ret.add(aut.getName()+" ");
        }
        
        return ret;
    }


    public Set<Authority> getAuthorities() {
        return authorities;
    }

    /**
     * @param authorities the authorities to set
     */
    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    /**
     * @return the shortname
     */
    public String getShortname() {
        return shortname;
    }

    /**
     * @param shortname the shortname to set
     */
    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

}