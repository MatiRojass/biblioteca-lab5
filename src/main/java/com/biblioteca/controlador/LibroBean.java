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
import com.biblioteca.modelo.Autor;
import com.biblioteca.modelo.Categoria;
import com.biblioteca.modelo.Libro;
import com.biblioteca.repositorio.AutorDAO;
import com.biblioteca.repositorio.CategoriaDAO;
import com.biblioteca.repositorio.LibroDAO;

/**
 *
 * @author Carlo_s Paz Cast
 */
@Named("libroBean")
@ViewScoped
public class LibroBean implements Serializable {

    private Libro libro;
    private List<Libro> libros;
    private boolean editado = false;
    
    private List<Categoria> categorias;
    private List<Autor> autores;

    @Inject
    private LibroDAO libroDao;
    
    @Inject
    private CategoriaDAO categoriaDao;
    @Inject
    private AutorDAO autorDao;

    @PostConstruct
    public void init() {
        libro = new Libro();
        cargarLibros();
        
        categorias = categoriaDao.listarTodas();
        autores = autorDao.listarTodos();
    }

    public void guardar() {
        if (editado) {
            libroDao.actualizar(libro);
        } else {
            libroDao.guardar(libro);
        }
        
        // Limpiar el form y refrescar la tabla
        libro = new Libro();
        editado = false;
        cargarLibros();
    }

    public void seleccionar(Libro libroSeleccionado) {
        this.libro = libroSeleccionado;
        editado = true;
    }

    public void eliminar(Integer id) {
        libroDao.eliminar(id);
        
        libro = new Libro();
        editado = false;
        cargarLibros();
    }

    private void cargarLibros() {
        libros = libroDao.listarTodos();
    }

    
    public Libro getLibro() { return libro; }
    public void setLibro(Libro libro) { this.libro = libro; }

    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }

    public boolean isEditado() { return editado; }
    public void setEditado(boolean editado) { this.editado = editado; }
    
    
    public List<Categoria> getCategorias() { return categorias; }
    public void setCategorias(List<Categoria> categorias) { this.categorias = categorias; }

    public List<Autor> getAutores() { return autores; }
    public void setAutores(List<Autor> autores) { this.autores = autores; }
    
}
