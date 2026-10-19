/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.controlador;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import com.biblioteca.modelo.Libro;
import com.biblioteca.modelo.Prestamo;
import com.biblioteca.repositorio.LibroDAO;
import com.biblioteca.repositorio.PrestamoDAO;
/**
 *
 * @author Carlo_s Paz Cast
 */
@Named("prestamoBean")
@SessionScoped
public class PrestamoBean implements Serializable {

    
    @Inject
    private PrestamoDAO prestamoDao;
    @Inject
    private LibroDAO libroDao;
    @Inject
    private LoginSocioBean loginSocioBean; // para saber quién es el que reserva

    //variables reservar
    private List<Libro> carritoReserva;
    private String mensajeReserva;

    // var devolver
    private Integer prestamoIdBusqueda;
    private Prestamo prestamoEncontrado;
    private String mensajeDevolucion;
    
    private Float multaEstimada = 0f;

    @PostConstruct
    public void init() {
        carritoReserva = new ArrayList<>();
    }

    

    // prestamos
    public String agregarAlCarrito(Libro libro) {
        
        
        if (!loginSocioBean.isLogueado()) {
            
            return "login-socio?faces-redirect=true"; 
        }

        Integer socioId = loginSocioBean.getSocioLogueado().getId();

        
        if (prestamoDao.tienePrestamosVencidos(socioId)) {
            mostrarMensaje("No puedes realizar préstamos. Tienes libros vencidos sin devolver.");
            return null; 
        }

        
        Long librosEnCasa = prestamoDao.cantidadLibrosSinDevolver(socioId);
        if ((librosEnCasa + carritoReserva.size()) >= 3) {
            mostrarMensaje("Límite alcanzado. Solo puedes tener hasta 3 libros prestados en total.");
            return null;
        }

        // Validar stock
        if (libro.getDisponibles() <= 0) {
            mostrarMensaje("No hay stock disponible de este libro.");
            return null;
        }

        // Agregamos
        if (!carritoReserva.contains(libro)) {
            carritoReserva.add(libro);
            mostrarMensaje("Libro agregado a tu reserva.");
        }
        
        return null; // Finaliza y se queda en el index para que siga mirando libros
    }

    public void confirmarPrestamo() {
        if (carritoReserva.isEmpty()) {
            mostrarMensaje("Tu carrito está vacío.");
            return;
        }

        // Armamos ticket
        Prestamo nuevoPrestamo = new Prestamo();
        nuevoPrestamo.setSocio(loginSocioBean.getSocioLogueado());
        nuevoPrestamo.setFecha(LocalDate.now()); // Fecha de hoy
        nuevoPrestamo.setLibros(new ArrayList<>(carritoReserva));

        // Descontamos el stock 
        for (Libro lib : carritoReserva) {
            lib.setDisponibles(lib.getDisponibles() - 1);
            libroDao.actualizar(lib);
        }

        // Guardamos en la BD
        prestamoDao.guardar(nuevoPrestamo);

        // Limpiamos la pantalla
        carritoReserva.clear();
        mensajeReserva = "¡Préstamo confirmado! Tienes 7 días corridos para devolverlos.";
    }

    

    //devolucion
    public void buscarTicket() {
        prestamoEncontrado = prestamoDao.buscarPorId(prestamoIdBusqueda);
        
        if (prestamoEncontrado == null || prestamoEncontrado.getFecha_devuelve() != null) {
            mostrarMensaje("No se encontró un préstamo activo con ese número.");
            prestamoEncontrado = null;
            multaEstimada = 0f;
        } else {
            // Calculamos la multa v
            LocalDate fechaHoy = LocalDate.now();
            LocalDate fechaVencimiento = prestamoEncontrado.getFecha().plusDays(7);
            long diasAtraso = ChronoUnit.DAYS.between(fechaVencimiento, fechaHoy);

            if (diasAtraso > 0) {
                multaEstimada = diasAtraso * 500f * prestamoEncontrado.getLibros().size();
            } else {
                multaEstimada = 0f; 
            }
        }
    }

    public void registrarDevolucion() {
        if (prestamoEncontrado != null) {
            LocalDate fechaHoy = LocalDate.now();
            prestamoEncontrado.setFecha_devuelve(fechaHoy);

            //  Calcular Multa de $500 por día por libro
            LocalDate fechaVencimiento = prestamoEncontrado.getFecha().plusDays(7);
            
            
            long diasAtraso = ChronoUnit.DAYS.between(fechaVencimiento, fechaHoy);

            if (diasAtraso > 0) {
                float totalMulta = diasAtraso * 500f * prestamoEncontrado.getLibros().size();
                prestamoEncontrado.setMulta(totalMulta);
                mensajeDevolucion = "Devolución tardía registrada. Multa a cobrar: $" + totalMulta;
            } else {
                prestamoEncontrado.setMulta(0f);
                mensajeDevolucion = "Devolución en término. ¡Gracias!";
            }

            // Devolvemos el stock a los libros
            for (Libro lib : prestamoEncontrado.getLibros()) {
                lib.setDisponibles(lib.getDisponibles() + 1);
                libroDao.actualizar(lib); // Actualizamos la BD
            }

            
            prestamoDao.actualizar(prestamoEncontrado);
            prestamoEncontrado = null; // Limpiamos pantalla
            prestamoIdBusqueda = null;
        }
    }

    
    private void mostrarMensaje(String msj) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(msj));
    }

    
    public List<Libro> getCarritoReserva() { return carritoReserva; }
    public void setCarritoReserva(List<Libro> carritoReserva) { this.carritoReserva = carritoReserva; }
    

    public PrestamoDAO getPrestamoDao() {
        return prestamoDao;
    }

    public void setPrestamoDao(PrestamoDAO prestamoDao) {
        this.prestamoDao = prestamoDao;
    }

    public LibroDAO getLibroDao() {
        return libroDao;
    }

    public void setLibroDao(LibroDAO libroDao) {
        this.libroDao = libroDao;
    }

    public LoginSocioBean getLoginSocioBean() {
        return loginSocioBean;
    }

    public void setLoginSocioBean(LoginSocioBean loginSocioBean) {
        this.loginSocioBean = loginSocioBean;
    }

    public String getMensajeReserva() {
        return mensajeReserva;
    }

    public void setMensajeReserva(String mensajeReserva) {
        this.mensajeReserva = mensajeReserva;
    }

    public Integer getPrestamoIdBusqueda() {
        return prestamoIdBusqueda;
    }

    public void setPrestamoIdBusqueda(Integer prestamoIdBusqueda) {
        this.prestamoIdBusqueda = prestamoIdBusqueda;
    }

    public Prestamo getPrestamoEncontrado() {
        return prestamoEncontrado;
    }

    public void setPrestamoEncontrado(Prestamo prestamoEncontrado) {
        this.prestamoEncontrado = prestamoEncontrado;
    }

    public String getMensajeDevolucion() {
        return mensajeDevolucion;
    }

    public void setMensajeDevolucion(String mensajeDevolucion) {
        this.mensajeDevolucion = mensajeDevolucion;
    }
    
    public Float getMultaEstimada() { return multaEstimada; }
    public void setMultaEstimada(Float multaEstimada) { this.multaEstimada = multaEstimada; }
    
    
}
