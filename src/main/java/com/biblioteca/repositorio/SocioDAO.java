/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.repositorio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import com.biblioteca.modelo.Socio;

/**
 *
 * @author Carlo_s Paz Cast
 */
@ApplicationScoped
public class SocioDAO {
    @PersistenceContext(unitName = "BibliotecaPU")
    private EntityManager em;

    @Transactional
    public void guardar(Socio socio) {
        em.persist(socio);
    }

    @Transactional
    public void actualizar(Socio socio) {
        em.merge(socio);
    }
    
    @Transactional
    public void eliminar(Integer id) {
        Socio s = em.find(Socio.class, id);
        if (s != null) { 
            em.remove(s); 
        }
    }
    
    public Socio buscar(Integer id) {
        return em.find(Socio.class, id);
    }
    
    public Socio validarLogin(Integer dni, String password) {
        try {
            return em.createQuery("SELECT s FROM Socio s WHERE s.dni = :dni AND s.password = :pass", Socio.class)
                     .setParameter("dni", dni)
                     .setParameter("pass", password)
                     .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
    
    public List<Socio> listarTodos() {
        return em.createQuery(
            "SELECT s FROM Socio s ORDER BY s.apellido, s.nombres",
            Socio.class)
            .getResultList();
    }
}
