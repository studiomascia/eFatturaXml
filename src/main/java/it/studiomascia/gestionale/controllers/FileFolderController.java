/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.controllers;

import it.studiomascia.gestionale.models.FolderFile;
import it.studiomascia.gestionale.repository.FolderFileRepository;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author luigi
 */
@Controller
public class FileFolderController {

    @Autowired
    FolderFileRepository folderFileRepository;
    
    @GetMapping("/FolderFile")
    public String getAlbero(HttpServletRequest request,Model model){
        FolderFile rootCategory = folderFileRepository.findRootFolder().get();
        List<FolderFile> folders =  new ArrayList<FolderFile>();
        folders.add(rootCategory);
         model.addAttribute("folders", folders);
    return "folder_file";
    }
    
    
    public void recursiveTree(FolderFile cat) {
        System.out.println(cat.getFolderFileName());
        if (cat.getChildren().size() > 0) {
            for (FolderFile c : cat.getChildren()) {
                recursiveTree(c);
            }
        }
    }
    
}
