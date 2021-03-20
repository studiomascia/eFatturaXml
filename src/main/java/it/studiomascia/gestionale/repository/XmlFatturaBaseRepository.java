/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.repository;

import it.studiomascia.gestionale.models.XmlFatturaBase;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author luigi
 */
@Repository
public interface XmlFatturaBaseRepository extends JpaRepository<XmlFatturaBase, Integer> ,JpaSpecificationExecutor<XmlFatturaBase>  {
//    List<XmlFatturaBase> findAllPassive();
    List<XmlFatturaBase> findPassiveNotRegistered();
//    List<XmlFatturaBase> findAllAttive();
    List<XmlFatturaBase> findAttiveNotRegistered();
    List<XmlFatturaBase> findAllByOrderByIdDesc();
    List<XmlFatturaBase> findByAttivaTrue();
    List<XmlFatturaBase> findByAttivaFalse();
    List<XmlFatturaBase> findAllByDataFatturaBetween(Date dataInizio, Date dataFine);
    List<XmlFatturaBase> findByAttivaFalseAndDataFatturaBetween(Date dataInizio, Date dataFine);
}
