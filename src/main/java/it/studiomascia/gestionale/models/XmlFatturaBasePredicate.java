/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;

/**
 *
 * @author luigi
 */

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.data.jpa.domain.Specification;
 
public class XmlFatturaBasePredicate
{
    public static Predicate<XmlFatturaBase> isAttiva() {
        
        return p -> p.isAttiva()==true;
    }
     
    public static Predicate<XmlFatturaBase> isPassiva() {
        return p -> p.isAttiva()==false;
    }
    
      public static Predicate<XmlFatturaBase> isNotRegistered() {
        return p -> p.getNumeroRegistrazione() == null ;
    }
    
    public static List<XmlFatturaBase> filtraFatturePassive(List<XmlFatturaBase> fatture)
    {
      return fatture.stream().filter((fattura)-> fattura.isAttiva() == false).collect(Collectors.<XmlFatturaBase>toList());
    }
     public static List<XmlFatturaBase> filtraFattureNonRegistrate(List<XmlFatturaBase> fatture)
    {
      return fatture.stream().filter((fattura)-> fattura.getNumeroRegistrazione()== null).collect(Collectors.<XmlFatturaBase>toList());
    }
     
    public static List<XmlFatturaBase> filterXmlFatturaBase (List<XmlFatturaBase> fatture, Predicate<XmlFatturaBase> predicate)
    {
        return fatture.stream()
                    .filter( predicate )
                    .collect(Collectors.<XmlFatturaBase>toList());
    }
    
     
   
    
    
}  