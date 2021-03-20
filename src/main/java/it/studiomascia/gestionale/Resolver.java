/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.xslt.XsltView;
import org.springframework.web.servlet.view.xslt.XsltViewResolver;

/**
 *
 * @author luigi
 */
@Configuration
@EnableWebMvc
public class Resolver{
    
    public static final String RESOURCES_DIR;
    public static final String OUTPUT_DIR;

    static {
        RESOURCES_DIR = "./resources/";
        OUTPUT_DIR = "./resources/output/";
    }
    
    
     @Bean
    public ViewResolver getXSLTViewResolver(){
         
        XsltViewResolver xsltResolover = new XsltViewResolver();
        xsltResolover.setOrder(1);
        xsltResolover.setSourceKey("xmlSource");
         
        xsltResolover.setViewClass(XsltView.class);
        xsltResolover.setViewNames(new String[] {"XSLTView"});
//        xsltResolover.setPrefix("/WEB-INF/xsl/");
        xsltResolover.setPrefix("classpath:/xsl/" );

        xsltResolover.setSuffix(".xsl");
         
        return xsltResolover;
    }
    
    
    
}
