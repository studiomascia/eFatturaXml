/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.service;


import java.io.IOException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public  class MailService {
    
    @Autowired
    private static JavaMailSender javaMailSender;

     public  static void sendEmailWithAttachment(String destinatario, String oggetto, String messaggio) throws MessagingException {
        
        final JavaMailSenderImpl sender = new JavaMailSenderImpl();
        final MimeMessage message = sender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message);
        
//        MimeMessage msg = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
helper.setValidateAddresses(false);
helper.setTo(destinatario);
        helper.setSubject(oggetto);
        helper.setFrom("gestionale@gruppostatuto.it");
        // true = text/html
        helper.setText(messaggio, true);

        // hard coded a file path
        //FileSystemResource file = new FileSystemResource(new File("path/android.png"));
        //helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));
        
//        javaMailSender.send(message);
        sender.send(message);
    }
    
        public  static void sendEmailWithAttachment(String destinatario, String oggetto, String messaggio, String filename, InputStreamSource file ) throws MessagingException, IOException {

        MimeMessage msg = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(destinatario);
        helper.setSubject(oggetto);

        // true = text/html
        helper.setText(messaggio, true);

        // hard coded a file path
        //FileSystemResource file = new FileSystemResource(new File("path/android.png"));
        helper.addAttachment(filename,file);

        javaMailSender.send(msg);

    }

}
