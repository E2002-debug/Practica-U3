package com.example.controller;

import java.util.HashMap;
import com.example.models.Veterinaria;
import com.example.controller.Haversine;

import java.util.HashMap;

public class CoordenadasVeterinarias {
    private HashMap<String, Veterinaria> veterinarias;

    public CoordenadasVeterinarias() {
        veterinarias = new HashMap<>();
    }

    public void agregarVeterinaria(Veterinaria veterinaria) {
        veterinarias.put(veterinaria.getNombre(), veterinaria);
    }

    public Veterinaria obtenerVeterinaria(String nombre) {
        return veterinarias.get(nombre);
    }

    public double calcularDistancia(String nombre1, String nombre2) {
        Veterinaria vet1 = veterinarias.get(nombre1);
        Veterinaria vet2 = veterinarias.get(nombre2);
    
        if (vet1 != null && vet2 != null) {
            return Haversine.calcularDistancia(vet1.getLatitud(), vet1.getLongitud(), vet2.getLatitud(), vet2.getLongitud());
        }
        return -1; // Si alguna veterinaria no existe
    }
}    

