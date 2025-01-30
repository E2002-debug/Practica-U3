package com.example.controller.tda.list.graph;

import com.example.controller.excepcion.LabelException;
import com.example.controller.tda.list.LinkedList;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.example.controller.CoordenadasVeterinarias;

public class GraphLabelNoDirect<E> extends GraphLabelDirect<E> {

    

    public GraphLabelNoDirect(Integer nro_vertices, Class<E> clazz, CoordenadasVeterinarias coordenadas) {
        super(nro_vertices, clazz, coordenadas); // Llamada al constructor de la clase base
    }
    //Adyacencias Manuales 
    
    public void insertEdgeLD(E v1, E v2) throws Exception {
        if (isLabelsGraph()) {
            // Obtener la distancia geográfica entre las dos veterinarias usando el HashMap de coordenadas
            double distancia = coordenadas.calcularDistancia(v1.toString(), v2.toString());

            // Si la distancia es válida, insertar la arista con la distancia calculada
            if (distancia >= 0) {
                // Se inserta la arista en ambas direcciones para grafo no dirigido
                add_edge(getVerticeL(v1), getVerticeL(v2), (float) distancia);
                add_edge(getVerticeL(v2), getVerticeL(v1), (float) distancia);

            }
        } else {
            throw new LabelException("Grafo no etiquetado");
        }
        System.err.println(coordenadas.calcularDistancia(v1.toString(), v2.toString()));
    }

    @Override
    public E[] getLabels() {
        return labels; // Devuelve el array de etiquetas
    }

    @Override
    public LinkedList<Adycencia> adycencias(Integer vertex) {
        try {
            return super.adycencias(vertex); // Llama al método de la superclase
        } catch (RuntimeException e) {
            throw e; // Re-lanza la excepción no comprobada si es crítico
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo adyacencias", e);
        }
    }

    public Float getWeight(Integer from, Integer to) {
        try {
            return weight_edge(from, to); // Obtiene el peso de la arista
        } catch (RuntimeException e) {
            throw e; // Re-lanza la excepción no comprobada si es crítico
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo peso de la arista", e);
        }
    }

    public void generarAdyacenciasAleatorias() throws Exception {
    Random random = new Random();

    for (int i = 1; i <= this.nro_vertices(); i++) {
        Set<Integer> conexiones = new HashSet<>(); // Usamos un Set para evitar duplicados

        while (conexiones.size() < 3) {
            int destino = random.nextInt(this.nro_vertices()) + 1; // Genera un nodo aleatorio entre 1 y nro_vertices

            // Asegurarse de que no sea el mismo nodo y que no haya conexión previa
            if (destino != i && !conexiones.contains(destino)) {
                conexiones.add(destino);

                // Insertar arista bidireccional
                add_edge(i, destino, (float) coordenadas.calcularDistancia(getLabelL(i).toString(), getLabelL(destino).toString()));
                add_edge(destino, i, (float) coordenadas.calcularDistancia(getLabelL(destino).toString(), getLabelL(i).toString()));
            }
        }

        // Imprimir las conexiones generadas para cada nodo
        System.out.println("Nodo " + i + " conectado a: " + conexiones);
    }
}


    
}


