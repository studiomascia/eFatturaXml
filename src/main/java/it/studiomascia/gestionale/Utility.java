/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale;

import it.studiomascia.gestionale.xml.DettaglioPagamentoType;
import it.studiomascia.gestionale.xml.ModalitaPagamentoType;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author luigi
 */
public class Utility {
    
    public static void main(String[] args) 
    {
    }
    
    public static boolean CheckInvoicePayed (List<DettaglioPagamentoType> lista)
    {
        boolean ret= false;
         for (DettaglioPagamentoType x:lista)
         { 
            ModalitaPagamentoType y = x.getModalitaPagamento();
            if ((y!=null) && (  ( y == ModalitaPagamentoType.MP_01) || 
                                ( y == ModalitaPagamentoType.MP_03) || 
                                ( y == ModalitaPagamentoType.MP_04) || 
                                ( y == ModalitaPagamentoType.MP_08) || 
                                ( y == ModalitaPagamentoType.MP_09) || 
                                ( y == ModalitaPagamentoType.MP_10) || 
                                ( y == ModalitaPagamentoType.MP_11) || 
                                ( y == ModalitaPagamentoType.MP_12) || 
                                ( y == ModalitaPagamentoType.MP_16) || 
                                ( y == ModalitaPagamentoType.MP_17) || 
                                ( y == ModalitaPagamentoType.MP_19) || 
                                ( y == ModalitaPagamentoType.MP_20 ) 
            ))  ret = true;
         }
        return ret;
    }
    
    public static String ConvertBigDecimaltoString(BigDecimal numero){
        
        String ret="";
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.ITALY);
        numberFormat.setMinimumFractionDigits(2);
        try{
            ret = numberFormat.format(numero);
        }catch (Exception e){
        }
        return ret;
    }
    
    
     public static ByteArrayResource  listToExcel(String nomefile, List<String> header, List< Map<String, Object>> righe) throws IOException {
        byte[] bytes = new byte[1024];

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Fatture Passive");
        
        //Crea l'intestazione della tabella
        int rowIdx = 1;
        Row rowHeader = sheet.createRow(rowIdx++);
        for (int i = 0; i < header.size(); i++) {
            rowHeader.createCell(i).setCellValue(header.get(i));
//            rowHeader.createCell(i).setCellValue("test");
        }
        String valoreCella="";
        //aggiunge le righe della tabella
        for (int i = 0; i < righe.size(); i++) 
        {
            Row row = sheet.createRow(rowIdx++);
            for (int col = 0; col < header.size(); col++) {
                try{
                    valoreCella= righe.get(i).get(header.get(col)).toString();
//                    valoreCella= "prova";
                }catch (Exception e){
                    valoreCella ="errore righe(i)=" + i + " col="+col;
                }
                row.createCell(col).setCellValue(valoreCella);
                
            }
        }
        
        FileOutputStream fos = new FileOutputStream(nomefile);
        workbook.write(fos);
        ByteArrayResource ret =new ByteArrayResource(bytes);
        fos.write(bytes);
        //fos.close();
        
      return  ret;
        
    }    
    
}
