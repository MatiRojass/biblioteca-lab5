/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.repositorio;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import com.biblioteca.modelo.Prestamo;

/**
 *
 * @author Carlo_s Paz Cast
 */
@ApplicationScoped
public class PrestamoDAO {

    @PersistenceContext(unitName = "BibliotecaPU")
    private EntityManager em;

    @Transactional
    public void guardar(Prestamo prestamo) {
        em.persist(prestamo);
    }

    @Transactional
    public void actualizar(Prestamo prestamo) {
        // Usamos merge para actualizar la fecha de devolución y la multa cuando el socio traiga los libros
        em.merge(prestamo);
    }

    
    public boolean tienePrestamosVencidos(Integer socioId) {
        // Calculamos la fecha límite 
        LocalDate fechaLimite = LocalDate.now().minusDays(7);
        
        
        Long cantidadVencidos = em.createQuery(
            "SELECT COUNT(p) FROM Prestamo p WHERE p.socio.id = :socioId AND p.fecha_devuelve IS NULL AND p.fecha < :limite", Long.class)
            .setParameter("socioId", socioId)
            .setParameter("limite", fechaLimite)
            .getSingleResult();
            
        // Si el conteo es mayor a 0, significa que es moroso 
        return cantidadVencidos > 0;
    }

    //  Límite de 3 libros por socio
    public Long cantidadLibrosSinDevolver(Integer socioId) {
        
        Long cantidad = em.createQuery(
            "SELECT COUNT(l) FROM Prestamo p JOIN p.libros l WHERE p.socio.id = :socioId AND p.fecha_devuelve IS NULL", Long.class)
            .setParameter("socioId", socioId)
            .getSingleResult();
            
        return cantidad != null ? cantidad : 0L;
    }
    
    public Prestamo buscarPorId(Integer id) { return em.find(Prestamo.class, id); }
    
    
    public List<Prestamo> listarPrestamosActivos() {
        return em.createQuery("SELECT p FROM Prestamo p WHERE p.fecha_devuelve IS NULL", Prestamo.class)
                 .getResultList();
    }
}
