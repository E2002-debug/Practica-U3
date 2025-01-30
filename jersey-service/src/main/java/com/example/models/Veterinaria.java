package com.example.models;

public class Veterinaria {
    private Integer id;
    private String nombre;
    private String direccion;
    private String telefono;
    private String nombre_Veterinario;
    private Double latitud;
    private Double longitud;
    

    public Veterinaria() {
    }

    public Veterinaria(Integer id, String nombre, String direccion, String telefono, String nombre_Veterinario, Double latitud, Double longitud) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nombre_Veterinario = nombre_Veterinario;
        this.latitud = latitud;
        this.longitud = longitud;
    }
    

    public Integer getId() {
      return this.id;
    }
    public void setId(Integer value) {
      this.id = value;
    }

    public String getNombre() {
      return this.nombre;
    }
    public void setNombre(String value) {
      this.nombre = value;
    }

    public String getDireccion() {
      return this.direccion;
    }
    public void setDireccion(String value) {
      this.direccion = value;
    }

    public String getTelefono() {
      return this.telefono;
    }
    public void setTelefono(String value) {
      this.telefono = value;
    }


    public String getNombre_Veterinario() {
      return this.nombre_Veterinario;
    }
    public void setNombre_Veterinario(String value) {
      this.nombre_Veterinario = value;
    }

    public Double getLatitud() {
      return this.latitud;
    }
    public void setLatitud(Double value) {
      this.latitud = value;
    }

    public Double getLongitud() {
      return this.longitud;
    }
    public void setLongitud(Double value) {
      this.longitud = value;
    }

    public String toString() {
      return nombre;
    }
}
