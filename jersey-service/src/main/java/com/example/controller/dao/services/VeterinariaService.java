package com.example.controller.dao.services;

import java.util.HashMap;

import com.example.controller.CoordenadasVeterinarias;
import com.example.controller.dao.VeterinariaDao;
import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;
import com.example.models.Veterinaria;

public class VeterinariaService {

    private VeterinariaDao obj;
    private LinkedList<Veterinaria> veterinarias;
    public VeterinariaService() {
        obj = new VeterinariaDao();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public LinkedList listAll() {
        return obj.getListAll();
    }

    public Veterinaria getVeterinaria() {
        return obj.getVeterinaria();
    }

    public void setVeterinaria(Veterinaria veterinaria) {
        obj.setVeterinaria(veterinaria);
    }

    public Veterinaria get(Integer id) throws Exception {
        return obj.get(id);
    }

    public Veterinaria findByName(String nombre) {
        for (Veterinaria v : veterinarias) {
            if (v.getNombre().equalsIgnoreCase(nombre)) {
                return v; // Retorna la veterinaria si el nombre coincide
            }
        }
        return null; // Si no se encuentra, retorna null
    }

    

}
