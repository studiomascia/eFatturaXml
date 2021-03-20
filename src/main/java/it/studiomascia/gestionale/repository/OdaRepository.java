////*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package it.studiomascia.gestionale.repository;

import it.studiomascia.gestionale.models.ODA;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author luigi
 */
public interface OdaRepository extends JpaRepository<ODA, Integer>{
//    @EntityGraph(value = "Ddt.AnagraficaSocieta", type = EntityGraphType.FETCH)
//    public List<Ddt> findByIdAnagraficaSocieta(int id);
//    
//    @Query("select u from ddt u where u.idAnagraficaSocieta= ?1")
//  List<Ddt> findByAnagraficaSocieta(int x);
}