package it.studiomascia.gestionale.service;

import it.studiomascia.gestionale.models.User;

public interface UserService {
    void insertDefaultNewUser (User user);
    void EncodeAndSave (User user);
    User findByUsername(String username);
}