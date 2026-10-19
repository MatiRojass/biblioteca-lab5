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
import com.biblioteca.modelo.Socio;
import com.biblioteca.repositorio.SocioDAO;
/**
 *
 * @author Carlo_s Paz Cast
 */

@Named("loginSocioBean")
@SessionScoped
public class LoginSocioBean implements Serializable {

    private Integer dni;
    private String password;
    
    // Si esta variable tiene datos, el socio está logueado
    private Socio socioLogueado;

    @Inject
    private SocioDAO socioDao;

    public String login() {
        Socio socio = socioDao.validarLogin(dni, password);

        if (socio != null) {
            socioLogueado = socio;
            return "index?faces-redirect=true"; 
        } else {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "DNI o contraseña incorrectos", null));
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        socioLogueado = null;
        return "index?faces-redirect=true";
    }

    public boolean isLogueado() {
        return socioLogueado != null;
    }

   
    public Integer getDni() { return dni; }
    public void setDni(Integer dni) { this.dni = dni; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Socio getSocioLogueado() { return socioLogueado; }
    public void setSocioLogueado(Socio socioLogueado) { this.socioLogueado = socioLogueado; }
}
