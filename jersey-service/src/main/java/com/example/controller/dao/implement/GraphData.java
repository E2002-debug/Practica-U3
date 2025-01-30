package com.example.controller.dao.implement;


import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.Adycencia;

public class GraphData<E> {
    public E[] labels; // Etiquetas de los vértices
    public LinkedList<Adycencia>[] adjacencyList; // Lista de adyacencias

    public GraphData(E[] labels, LinkedList<Adycencia>[] adjacencyList) {
        this.labels = labels;
        this.adjacencyList = adjacencyList;
    }
}
