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
import com.biblioteca.modelo.Categoria;

/**
 *
 * @author Carlo_s Paz Cast
 */
@ApplicationScoped
public class CategoriaDAO {
    @PersistenceContext(unitName = "BibliotecaPU")
    private EntityManager em;

    // Método para traer todas las categorías (usado en el selectOneMenu)
    public List<Categoria> listarTodas() {
        return em.createQuery("SELECT c FROM Categoria c ORDER BY c.nombre", Categoria.class)
                 .getResultList();
    }

    public Categoria buscar(Integer id) {
        return em.find(Categoria.class, id);
    }

    @Transactional
    public void guardar(Categoria categoria) {
        em.persist(categoria);
    }

    @Transactional
    public void actualizar(Categoria categoria) {
        em.merge(categoria);
    }

    @Transactional
    public void eliminar(Integer id) {
        Categoria c = em.find(Categoria.class, id);
        if (c != null) {
            em.remove(c);
        }
    }
}
