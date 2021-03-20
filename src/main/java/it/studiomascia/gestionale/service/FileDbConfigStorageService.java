/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.service;

import it.studiomascia.gestionale.exception.FileStorageException;
import it.studiomascia.gestionale.exception.MyFileNotFoundException;
import it.studiomascia.gestionale.models.DBFile;
import it.studiomascia.gestionale.models.FileDbConfig;
import it.studiomascia.gestionale.repository.FileDbConfigRepository;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
                                         

/**
 *
 * @author luigi
 */
@Service
public class FileDbConfigStorageService {

    @Autowired
    private FileDbConfigRepository  fileDbConfigRepository;

    public FileDbConfig storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            FileDbConfig dbFile = new FileDbConfig(fileName, file.getBytes());

            return fileDbConfigRepository.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public FileDbConfig getFile(String fileId) {
        return fileDbConfigRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }
}

