////*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
package it.studiomascia.gestionale.repository;

import it.studiomascia.gestionale.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author luigi
 */
public interface RoleRepository extends JpaRepository<Authority, Long>{

}