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
import com.biblioteca.modelo.Consulta;

/**
 *
 * @author Carlo_s Paz Cast
 */

@ApplicationScoped
public class ConsultaDAO {
    @PersistenceContext(unitName = "BibliotecaPU")
    private EntityManager em;

    @Transactional
    public void guardar(Consulta consulta) {
        em.persist(consulta);
    }
    
    @Transactional
    public void eliminar(Integer id) {
        Consulta c = em.find(Consulta.class, id);
        if (c != null) {
            em.remove(c);
        }
    }

    public List<Consulta> listarTodas() {
        return em.createQuery("SELECT c FROM Consulta c ORDER BY c.id DESC", Consulta.class)
                 .getResultList();
    }
}
