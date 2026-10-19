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
import com.biblioteca.modelo.Consulta;
import com.biblioteca.repositorio.ConsultaDAO;
/**
 *
 * @author Carlo_s Paz Cast
 */
@Named("consultaBean")
@ViewScoped
public class ConsultaBean implements Serializable{
    private Consulta nuevaConsulta;
    private List<Consulta> listaConsultas;
    private String mensajeExito; // Para avisarle al usuario que se envió bien

    @Inject
    private ConsultaDAO consultaDao;

    @PostConstruct
    public void init() {
        nuevaConsulta = new Consulta();
        cargarConsultas();
    }

    public void enviarConsulta() {
        consultaDao.guardar(nuevaConsulta);
        nuevaConsulta = new Consulta(); // Limpiamos el formulario
        mensajeExito = "¡Tu consulta fue enviada con éxito!";
        cargarConsultas(); // Refrescamos la lista para la vista del personal
    }
    
    public void eliminarConsulta(Integer id) {
        consultaDao.eliminar(id);
        cargarConsultas(); // Volvemos a consultar a la BD para actualizar la vista
    }

    private void cargarConsultas() {
        listaConsultas = consultaDao.listarTodas();
    }

    
    public Consulta getNuevaConsulta() { return nuevaConsulta; }
    public void setNuevaConsulta(Consulta nuevaConsulta) { this.nuevaConsulta = nuevaConsulta; }

    public List<Consulta> getListaConsultas() { return listaConsultas; }
    public void setListaConsultas(List<Consulta> listaConsultas) { this.listaConsultas = listaConsultas; }

    public String getMensajeExito() { return mensajeExito; }
    public void setMensajeExito(String mensajeExito) { this.mensajeExito = mensajeExito; }
}
