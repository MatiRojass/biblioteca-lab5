/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.controlador;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import com.biblioteca.modelo.Administrador;
import com.biblioteca.repositorio.AdministradorDAO;
/**
 *
 * @author Carlo_s Paz Cast
 */
@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String password;
    
    
    private Administrador adminLogueado;

    @Inject
    private AdministradorDAO adminDao;

    // Método para procesar el formulario de Login
    public String login() {
        // Vamos a la base de datos a preguntar si existe
        Administrador admin = adminDao.validarLogin(username, password);

        if (admin != null) {
            
            adminLogueado = admin;
            return "ver-consultas?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o contraseña incorrectos", null));
            return null;
        }
    }

    // Método para cerrar sesión
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        adminLogueado = null;
        
        // Lo mandamos de vuelta al inicio
        return "index?faces-redirect=true";
    }

    //las vistas pregunten si hay alguien logueado
    public boolean isLogueado() {
        return adminLogueado != null;
    }

    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Administrador getAdminLogueado() { return adminLogueado; }
    public void setAdminLogueado(Administrador adminLogueado) { this.adminLogueado = adminLogueado; }
}
