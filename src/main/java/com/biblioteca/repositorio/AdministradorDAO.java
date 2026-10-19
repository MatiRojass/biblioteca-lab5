/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.repositorio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import com.biblioteca.modelo.Administrador;
/**
 *
 * @author Carlo_s Paz Cast
 */

@ApplicationScoped
public class AdministradorDAO {

    @PersistenceContext(unitName = "BibliotecaPU")
    private EntityManager em;

    // Método que devuelve el Administrador si lo encuentra, o null si la clave es incorrecta
    public Administrador validarLogin(String username, String password) {
        try {
            return em.createQuery("SELECT a FROM Administrador a WHERE a.username = :user AND a.password = :pass", Administrador.class)
                     .setParameter("user", username)
                     .setParameter("pass", password)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
