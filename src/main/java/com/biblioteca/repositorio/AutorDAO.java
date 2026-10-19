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
import com.biblioteca.modelo.Autor;

/**
 *
 * @author Carlo_s Paz Cast
 */
@ApplicationScoped
public class AutorDAO {
    
    @PersistenceContext(unitName = "BibliotecaPU")
    private EntityManager em;

    // Método para traer todos los autores (usado en el selectOneMenu)
    public List<Autor> listarTodos() {
        return em.createQuery("SELECT a FROM Autor a ORDER BY a.apellido, a.nombres", Autor.class)
                 .getResultList();
    }

    public Autor buscar(Integer id) {
        return em.find(Autor.class, id);
    }

    @Transactional
    public void guardar(Autor autor) {
        em.persist(autor);
    }

    @Transactional
    public void actualizar(Autor autor) {
        em.merge(autor);
    }

    @Transactional
    public void eliminar(Integer id) {
        Autor a = em.find(Autor.class, id);
        if (a != null) {
            em.remove(a);
        }
    }
}
