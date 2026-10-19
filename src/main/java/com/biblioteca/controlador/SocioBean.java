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
import com.biblioteca.modelo.Socio;
import com.biblioteca.repositorio.SocioDAO;

/**
 *
 * @author Carlo_s Paz Cast
 */

@Named("socioBean")
@ViewScoped
public class SocioBean implements Serializable{
    
    private Socio socio;
    private List<Socio> listaSocios;
    private boolean editado = false;
    
    @Inject
    private SocioDAO socioDao;

    @PostConstruct
    public void init() {
        socio = new Socio();
        cargarSocios();
    }

    public void guardar() {
        if (editado) {
            socioDao.actualizar(socio); // Si la bandera es true, hace un UPDATE
        } else {
            socioDao.guardar(socio);    // Si es false, hace un INSERT
        }
        
        // Limpiamos el formulario y reseteamos la bandera
        socio = new Socio();
        editado = false;
        
        // Refrescamos la lista para que la vista se actualice
        cargarSocios();
    }
    
    public void seleccionar(Socio socioSeleccionado) {
        // Clonamos o asignamos el socio a editar para que el formulario se llene con sus datos
        this.socio = socioSeleccionado;
        editado = true; // Cambiamos la bandera porque ahora vamos a modificar
    }

    public void eliminar(Integer id) {
        socioDao.eliminar(id);
 
        socio = new Socio();
        editado = false;
        
        cargarSocios();
    }

    private void cargarSocios() {
        listaSocios = socioDao.listarTodos();
    }

    
    public Socio getSocio() { return socio; }
    public void setSocio(Socio socio) { this.socio = socio; }
    public List<Socio> getListaSocios() { return listaSocios; }

    public void setListaSocios(List<Socio> listaSocios) {
        this.listaSocios = listaSocios;
    }

    public boolean isEditado() {
        return editado;
    }

    public void setEditado(boolean editado) {
        this.editado = editado;
    }
    
}
    

