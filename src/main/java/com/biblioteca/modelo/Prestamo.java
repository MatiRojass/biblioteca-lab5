/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.modelo;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Carlo_s Paz Cast
 */
@Entity
@Table(name = "prestamos")
public class Prestamo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    private LocalDate fecha;
    
    // Puede ser nulo si el socio todavía no devolvió los libros
    private LocalDate fecha_devuelve; 
    
    private Float multa;

    // Relación: Muchos préstamos pueden pertenecer a Un socio
    @ManyToOne
    @JoinColumn(name = "socios_id")
    private Socio socio;

    //  Mapeamos la tabla intermedia "librosprestamos" 
    @ManyToMany
    @JoinTable(
        name = "librosprestamos", 
        joinColumns = @JoinColumn(name = "prestamos_id"), 
        inverseJoinColumns = @JoinColumn(name = "libros_id")
    )
    private List<Libro> libros = new ArrayList<>();

    public Prestamo() {
        this.multa = 0f; // Inicializamos sin multa
    }

    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalDate getFecha_devuelve() { return fecha_devuelve; }
    public void setFecha_devuelve(LocalDate fecha_devuelve) { this.fecha_devuelve = fecha_devuelve; }

    public Float getMulta() { return multa; }
    public void setMulta(Float multa) { this.multa = multa; }

    public Socio getSocio() { return socio; }
    public void setSocio(Socio socio) { this.socio = socio; }

    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }
}
