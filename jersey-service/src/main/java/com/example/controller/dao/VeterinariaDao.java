package com.example.controller.dao;

import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.Graph;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;

import java.io.File;
import java.io.FileWriter;

import com.example.controller.dao.implement.AdapterDao;
import com.example.models.Veterinaria;

public class VeterinariaDao extends AdapterDao<Veterinaria> {
    private Veterinaria veterinaria;
    private LinkedList<Veterinaria> listAll;

    public VeterinariaDao() {
        super(Veterinaria.class);
    }
    public Veterinaria getVeterinaria() {
        if (veterinaria == null) {
            veterinaria = new Veterinaria();
        }
        return this.veterinaria;
    }

    public void setVeterinaria(Veterinaria veterinaria) {
        this.veterinaria = veterinaria;
    }
    
    public LinkedList getListAll() {
        if(listAll == null){
            this.listAll = (LinkedList<Veterinaria>) listAll();
        }
        return listAll;
    }

    public Boolean save() throws Exception {
        Integer id = getListAll().getSize()+1;
        veterinaria.setId(id);
        this.persist(this.veterinaria);
        return true;
    }

    public Boolean update() throws Exception {
        this.merge(getVeterinaria(), getVeterinaria().getId());
        return true;
    }

    public LinkedList<Veterinaria> buscar_nombre(String texto) {
        LinkedList<Veterinaria> lista = new LinkedList<>();
        LinkedList<Veterinaria> listita = listAll();
        if (!listita.isEmpty()) {
            Veterinaria[] aux = listita.toArray();
            for (int i = 0; i < aux.length; i++) {

                if (aux[i].getNombre().toLowerCase().startsWith(texto.toLowerCase())) {
                    //System.out.println("**** "+aux[i].get);
                    lista.add(aux[i]);
                }
            }
        }
        return lista;
    }


    



        
}
