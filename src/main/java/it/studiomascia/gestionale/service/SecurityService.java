package it.studiomascia.gestionale.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autoLogin(String username, String password);
}