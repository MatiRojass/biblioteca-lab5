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
import com.biblioteca.modelo.Revista;


/**
 *
 * @author Carlo_s Paz Cast
 */
@ApplicationScoped
public class RevistaDAO {
    @PersistenceContext(unitName = "BibliotecaPU")
    private EntityManager em;

    @Transactional
    public void guardar(Revista revista) {
        em.persist(revista);
    }

    @Transactional
    public void actualizar(Revista revista) {
        em.merge(revista);
    }
    
    @Transactional
    public void eliminar(Integer id) {
        Revista s = em.find(Revista.class, id);
        if (s != null) { 
            em.remove(s); 
        }
    }
    
    public Revista buscar(Integer id) {
        return em.find(Revista.class, id);
    }
    
    public List<Revista> listarTodos() {
        return em.createQuery(
            "SELECT r FROM Revista r ORDER BY r.nombre",
            Revista.class)
            .getResultList();
    }
}
