/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import org.springframework.stereotype.Controller;

/**
 *
 * @author luigi
 */
@Controller
public class ErrorController {
    public String error(){
    return "error";
    }
}
