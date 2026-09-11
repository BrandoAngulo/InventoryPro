package com.example.soft.inventoryPro.Productos.Models;

import java.sql.Date;
import java.time.LocalDate;

public class ProductoResponse {
    private Integer idProductos;
    private String nombre;
    //private String referencia;
    private Long precioventa;
    private LocalDate fechaIngreso;
    private Integer cantidadStock;
    private String observacion;
    private String categoriasConcat;

    public ProductoResponse(Integer idProductos,String nombre,
                           // String referencia,
                            Long precioventa, LocalDate  fechaIngreso, Integer cantidadStock, String observacion, String categoriasConcat) {
        this.idProductos = idProductos;
        this.nombre = nombre;
        //this.referencia = referencia;
        this.precioventa = precioventa;
        this.fechaIngreso = fechaIngreso;
        this.cantidadStock = cantidadStock;
        this.observacion = observacion;
        this.categoriasConcat = categoriasConcat;
    }

    public Integer getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(Integer idProductos) {
        this.idProductos = idProductos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /*public String getReferencia() {
        return referencia;
    }*/

    /*public void setReferencia(String referencia) {
        this.referencia = referencia;
    }*/

    public Long getPrecioVenta() {
        return precioventa;
    }

    public void setPrecioVenta(Long precioventa) {
        this.precioventa = precioventa;
    }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    // Si quieres convertirlo a LocalDate al exponerlo:
   /* public LocalDate getFechaIngresoAsLocalDate() {
        return fechaIngreso != null ? fechaIngreso.toLocalDate() : null;
    } */

    public Integer getCantidadStock() {
        return cantidadStock;
    }

    public void setCantidadStock(Integer cantidadStock) {
        this.cantidadStock = cantidadStock;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getCategoriasConcat() {
        return categoriasConcat;
    }

    public void setCategoriasConcat(String categoriasConcat) {
        this.categoriasConcat = categoriasConcat;
    }
}

