/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import java.time.Instant;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

/**
 *
 * @author luigi
 */

@Controller
public class HomeController {

    
    @Autowired
    BuildProperties buildProperties;
    
    /** 
     * The controller map the get on the view 'login'
     * @return vista login
     */
    @GetMapping("/")
    public String home(){
            return "login";
    }

    @GetMapping("/index")
    public String home2(){
            return "index";
    } 

    @GetMapping("/vuoto")
    public String vuoto(){
            return "template_vuoto";
    }
    @GetMapping("/testpage")
    public String testpage(){   

            return "testpage";
    }
    @GetMapping("/version")
    public String getVersion(Model model){   

       Properties prop = new Properties();
        //need to explicitly loop over all entries, just returning the BuildProperties object only contains the specific fields (artificact, group, name, time and version)
        buildProperties.forEach(entry -> prop.put(entry.getKey(),entry.getValue()));
        //proper date formatting for time
        prop.put("time", Instant.ofEpochMilli(Long.parseLong(prop.getProperty("time"))).toString());
        model.addAttribute("testo", prop.toString());
        return "testpage";
    }
}
