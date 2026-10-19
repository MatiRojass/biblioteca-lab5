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
import com.biblioteca.modelo.Libro;

/**
 *
 * @author Carlo_s Paz Cast
 */
@ApplicationScoped
public class LibroDAO {
    @PersistenceContext(unitName = "BibliotecaPU")
    
    private EntityManager em;

    @Transactional
    public void guardar(Libro libro) {
        em.persist(libro);
    }

    @Transactional
    public void actualizar(Libro libro) {
        em.merge(libro);
    }
    
    @Transactional
    public void eliminar(Integer id) {
        Libro l = em.find(Libro.class, id);
        if (l != null) { 
            em.remove(l); 
        }
    }
    
    public Libro buscar(Integer id) {
        return em.find(Libro.class, id);
    }
    
    public List<Libro> listarTodos() {
        // Ordenamos los libros por título alfabéticamente
        return em.createQuery(
            "SELECT l FROM Libro l ORDER BY l.titulo",
            Libro.class)
            .getResultList();
    }
    
    
    public List<Libro> buscarPorTitulo(String titulo) {
        return em.createQuery("SELECT l FROM Libro l WHERE LOWER(l.titulo) LIKE LOWER(:titulo)", Libro.class)
                .setParameter("titulo", "%" + titulo + "%")
                .getResultList();
    }
    
}
