/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.controlador;

import com.biblioteca.modelo.Categoria;
import com.biblioteca.modelo.Revista;
import com.biblioteca.repositorio.CategoriaDAO;
import com.biblioteca.repositorio.RevistaDAO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

import com.biblioteca.modelo.Categoria;

import com.biblioteca.modelo.Revista;
import com.biblioteca.repositorio.CategoriaDAO;

import com.biblioteca.repositorio.RevistaDAO;

/**
 *
 * @author Carlo_s Paz Cast
 */

@Named("revistaBean")
@ViewScoped
public class RevistaBean implements Serializable {
     private Revista revista;
    private List<Revista> revistas;
    private boolean editado = false;
    
    private List<Categoria> categorias;
    

    @Inject
    private RevistaDAO revistaDao;
    
    @Inject
    private CategoriaDAO categoriaDao;
    
    
    @PostConstruct
    public void init() {
        revista = new Revista();
        cargarLibros();
        
        categorias = categoriaDao.listarTodas();
        
    }
    
    
     public void guardar() {
        if (editado) {
            revistaDao.actualizar(revista);
        } else {
            revistaDao.guardar(revista);
        }
        
        // Limpiar el form y refrescar la tabla
        revista = new Revista();
        editado = false;
        cargarLibros();
    }

    public void seleccionar(Revista libroSeleccionado) {
        this.revista = libroSeleccionado;
        editado = true;
    }

    public void eliminar(Integer id) {
        revistaDao.eliminar(id);
        
        revista = new Revista();
        editado = false;
        cargarLibros();
    }

    private void cargarLibros() {
        revistas = revistaDao.listarTodos();
    }

    public Revista getRevista() {
        return revista;
    }

    public void setRevista(Revista revista) {
        this.revista = revista;
    }

    public List<Revista> getRevistas() {
        return revistas;
    }

    public void setRevistas(List<Revista> revistas) {
        this.revistas = revistas;
    }

    public boolean isEditado() {
        return editado;
    }

    public void setEditado(boolean editado) {
        this.editado = editado;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public RevistaDAO getRevistaDao() {
        return revistaDao;
    }

    public void setRevistaDao(RevistaDAO revistaDao) {
        this.revistaDao = revistaDao;
    }

    public CategoriaDAO getCategoriaDao() {
        return categoriaDao;
    }

    public void setCategoriaDao(CategoriaDAO categoriaDao) {
        this.categoriaDao = categoriaDao;
    }
    
    
    

    
}
