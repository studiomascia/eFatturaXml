/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.DBFile;
import it.studiomascia.gestionale.models.FileDbConfig;
import it.studiomascia.gestionale.models.XmlFatturaBase;
import it.studiomascia.gestionale.models.XmlFatturaBasePredicate;
import it.studiomascia.gestionale.repository.DBFileRepository;
import it.studiomascia.gestionale.repository.XmlFatturaBaseRepository;
import it.studiomascia.gestionale.service.DBFileStorageService;
import it.studiomascia.gestionale.service.FileDbConfigStorageService;
import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.bouncycastle.cms.CMSSignedData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;


/**
 *
 * @author luigi
 */
@Controller
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(xmlStr)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }
    
    private static String convertDocumentToString(Document doc) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
            // "yes");
            StringWriter writer = new StringWriter();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    private static <ProcessingInstructionImpl> Document addingStylesheet(Document doc) 
            throws TransformerConfigurationException, ParserConfigurationException 
    {
        ProcessingInstructionImpl pi = (ProcessingInstructionImpl) doc.createProcessingInstruction("xml-stylesheet","type=\"text/xsl\" href=\"/xsl/foglio.xsl\"");
        Element root = doc.getDocumentElement();
        doc.insertBefore((Node) pi, root);
        //trans.transform(new DOMSource(doc), new StreamResult(new OutputStreamWriter(bout, "utf-8")));
        return doc;
    }
    
    @Autowired
    private DBFileStorageService DBFileStorageService;

    @Autowired
    private XmlFatturaBaseRepository xmlFatturaBaseRepository;

        
    @Autowired
    private DBFileRepository dbFileRepository;

    @Autowired 
    private FileDbConfigStorageService fileDbConfigStorageService;
    
    
    @GetMapping("/Files")
    public String FilesList(HttpServletRequest request,Model model){
        
        //INIZIO:: BLOCCO PER LA PAGINAZIONE
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 100; //default page size is 10
        
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }

        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        //FINE:: BLOCCO PER LA PAGINAZIONE
       Page<DBFile> lista = dbFileRepository.findAll(PageRequest.of(page, size));
//       lista.getPageable().getPageNumber()
        model.addAttribute("lista_files", lista);
        model.addAttribute("currentPage", page);
        model.addAttribute("messaggio", "messaggio da mostrare");
    return "lista_files";
    }
        
    @GetMapping("/downloadFattura/{fileId}")
    public ResponseEntity<Resource> downloadFattura(@PathVariable String fileId) {
        // Load file from database
        Integer id = Integer.valueOf(fileId);
        XmlFatturaBase item = xmlFatturaBaseRepository.findById(id).get();
    
        Document doc2 = convertStringToDocument(item.getXmlData());
        Document doc1 = null;
        try {
            doc1 = addingStylesheet(doc2);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + item.getFileName() + "\"")
                .body(new ByteArrayResource(convertDocumentToString(doc1).getBytes()));
    
    }
    
       
    @GetMapping("/uploadConfigFile")
    public String uploadConfigFile() {
        return "upload_config_file";
    }
    
    @PostMapping("/uploadConfigFile")
    public String uploadConfigFile(@RequestParam("file") MultipartFile file, ModelMap modelMap) {            
        
        FileDbConfig dbFile = fileDbConfigStorageService.storeFile(file);
        List<FileDbConfig> lista = new ArrayList<FileDbConfig>();
        modelMap.addAttribute("file", lista.add(dbFile));
        return "uploaded_config_file";
    }
    
    
    @GetMapping("/uploadFile")
    public String uploadFile() {
        return "uploadFile";
    }
    
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, ModelMap modelMap) {            
        
        DBFile dbFile = DBFileStorageService.storeFile(file);
        List<DBFile> lista = new ArrayList<DBFile>();
        modelMap.addAttribute("file", lista.add(dbFile));
        return "uploaded";
    }

    @PostMapping("/uploadMultipleFiles")
    public String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files,ModelMap modelMap) {
 
        List<DBFile> lista = new ArrayList<DBFile>();
        for (int k=0;k<files.length;k++)
        {
            DBFile dbFile = DBFileStorageService.storeFile(files[k]);
            lista.add(dbFile);
        }
        modelMap.addAttribute("file", lista);
        return "uploaded";
        
    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
        DBFile dbFile = DBFileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }
    
    public byte[] getData(final byte[] p7bytes) throws Exception { 
        ByteArrayOutputStream out = new ByteArrayOutputStream();                 
        try{
            CMSSignedData cms = new CMSSignedData(p7bytes);           
            if(cms.getSignedContent() == null) { 
                //Error!!! 
                return null; 
            } 
            cms.getSignedContent().write(out);           
        }catch (Exception ex){}

        return out.toByteArray(); 
    } 
   

}
