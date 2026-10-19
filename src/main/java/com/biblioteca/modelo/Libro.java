/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.biblioteca.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Carlo_s Paz Cast
 */

@Entity
@Table(name = "libros")
public class Libro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;
    
    @Column(name = "\"year\"")
    private Integer year;
    
    private Integer edicion; 
    
    private String editorial;
    
    private String descripcion;
    
    private Integer disponibles;
    
    @ManyToMany(mappedBy = "libros")
    private List<Prestamo> prestamos = new ArrayList<>();
    
    
    public List<Prestamo> getPrestamos() { return prestamos; }
    public void setPrestamos(List<Prestamo> prestamos) { this.prestamos = prestamos; }

    @Column(name = "categorias_id")
    private Integer categoriasId;

    @Column(name = "autores_id")
    private Integer autoresId;

    public Libro() {
    }

    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Integer getEdicion() { return edicion; }
    public void setEdicion(Integer edicion) { this.edicion = edicion; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getDisponibles() { return disponibles; }
    public void setDisponibles(Integer disponibles) { this.disponibles = disponibles; }

    public Integer getCategoriasId() { return categoriasId; }
    public void setCategoriasId(Integer categoriasId) { this.categoriasId = categoriasId; }

    public Integer getAutoresId() { return autoresId; }
    public void setAutoresId(Integer autoresId) { this.autoresId = autoresId; }
}
