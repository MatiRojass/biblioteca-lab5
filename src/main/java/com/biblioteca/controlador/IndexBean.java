/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.controlador;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import com.biblioteca.modelo.Libro;
import com.biblioteca.repositorio.LibroDAO;

/**
 *
 * @author Carlo_s Paz Cast
 */
@Named("indexBean")
@ViewScoped
public class IndexBean implements Serializable{
    private String terminoBusqueda; // Lo que el usuario escribe en el input
    private List<Libro> librosMostrados; // Las tarjetas que se van a renderizar

    @Inject
    private LibroDAO libroDao;

    @PostConstruct
    public void init() {
        
        librosMostrados = libroDao.listarTodos();
    }
    
    public void buscar() {
        if (terminoBusqueda != null && !terminoBusqueda.trim().isEmpty()) {
            
            librosMostrados = libroDao.buscarPorTitulo(terminoBusqueda);
        } else {
            // Si le dio a buscar con el cuadro vacío, volvemos a mostrar todos
            librosMostrados = libroDao.listarTodos();
        }
    }
    
    public String getTerminoBusqueda() { return terminoBusqueda; }
    public void setTerminoBusqueda(String terminoBusqueda) { this.terminoBusqueda = terminoBusqueda; }

    public List<Libro> getLibrosMostrados() { return librosMostrados; }
    public void setLibrosMostrados(List<Libro> librosMostrados) { this.librosMostrados = librosMostrados; }
}
