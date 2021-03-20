/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.repository;

import it.studiomascia.gestionale.models.AnagraficaSocieta;
import it.studiomascia.gestionale.models.Ddt;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Admin
 */
@Repository

public interface AnagraficaSocietaRepository extends JpaRepository<AnagraficaSocieta, Integer> {
     AnagraficaSocieta findByPiva(String piva);
    // List<Ddt> findByProvider(int Provider);
//     Boolean existByPiva(String piva);
}
