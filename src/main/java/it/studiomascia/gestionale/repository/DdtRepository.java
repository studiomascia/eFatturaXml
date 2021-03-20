////*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package it.studiomascia.gestionale.repository;

import it.studiomascia.gestionale.models.AnagraficaSocieta;
import it.studiomascia.gestionale.models.Ddt;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author luigi
 */
public interface DdtRepository extends JpaRepository<Ddt, Integer>{
//    @EntityGraph(value = "Ddt.AnagraficaSocieta", type = EntityGraphType.FETCH)
//    public List<Ddt> findByIdAnagraficaSocieta(int id);
//    
  @Query("select u from Ddt u where u.anagraficaSocieta = ?1 and  u.xmlFatturaBase is NULL")
  List<Ddt> findByProviderNotAssigned(AnagraficaSocieta x);
}