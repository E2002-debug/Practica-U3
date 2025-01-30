package com.example.controller.tda.recoridos;

import java.util.HashMap;
import java.util.Map;

import com.example.controller.tda.list.graph.Adycencia;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;

public class BellmanFord {

    public static Map<String, Object> ejecutarBellmanFord(GraphLabelNoDirect<?> graph, int source, int destino) {
        int n = graph.nro_vertices();
        float[] dist = new float[n];
        Integer[] predecesor = new Integer[n];

        // Inicialización de distancias y predecesores
        for (int i = 0; i < n; i++) {
            dist[i] = Float.MAX_VALUE;
            predecesor[i] = null;
        }
        dist[source - 1] = 0;

        // Relajación de aristas
        long startTime = System.currentTimeMillis(); // Inicia la medición de tiempo

        for (int i = 1; i < n; i++) { // Ejecutar n-1 veces
            for (int u = 1; u <= n; u++) {
                for (Adycencia edge : (Iterable<Adycencia>) graph.adycencias(u)) {
                    int v = edge.getDestination();
                    float peso = edge.getWeight();
                    if (dist[u - 1] != Float.MAX_VALUE && dist[u - 1] + peso < dist[v - 1]) {
                        dist[v - 1] = dist[u - 1] + peso;
                        predecesor[v - 1] = u - 1; // Guardar el predecesor
                    }
                }
            }
        }

        // Comprobación de ciclos negativos
        for (int u = 1; u <= n; u++) {
            for (Adycencia edge : (Iterable<Adycencia>) graph.adycencias(u)) {
                int v = edge.getDestination();
                float peso = edge.getWeight();
                if (dist[u - 1] != Float.MAX_VALUE && dist[u - 1] + peso < dist[v - 1]) {
                    System.err.println("El grafo contiene un ciclo negativo");
                    return Map.of("error", "El grafo contiene un ciclo negativo");
                }
            }
        }

        long endTime = System.currentTimeMillis(); // Finaliza la medición de tiempo
        System.out.println("Tiempo de ejecución de Bellman-Ford: " + (endTime - startTime) + " ms");

        // Imprimir las distancias mínimas desde el vértice fuente
        imprimirDistancias(dist, source);

        // Reconstruir el camino mínimo desde la fuente al destino
        String camino = reconstruirCamino(predecesor, source - 1, destino - 1);
        Map<String, Object> resultado = new HashMap<>();
        if (camino.equals("No existe camino")) {
            resultado.put("mensaje", camino);
            System.out.println(camino); // Imprimir "No existe camino" si no se encuentra
        } else {
            resultado.put("camino", camino);
            resultado.put("distancia", dist[destino - 1]);
            System.out.println("Camino mínimo: " + camino); // Imprimir el camino mínimo
            System.out.println("Distancia: " + dist[destino - 1]); // Imprimir la distancia
        }
        return resultado;
    }

    private static String reconstruirCamino(Integer[] predecesor, int source, int destino) {
        if (predecesor[destino] == null) {
            return "No existe camino";
        }
        StringBuilder camino = new StringBuilder();
        int actual = destino;
        while (actual != source) {
            camino.insert(0, " -> " + (actual + 1));
            actual = predecesor[actual];
        }
        camino.insert(0, (source + 1));
        return camino.toString();
    }

    private static void imprimirDistancias(float[] dist, int source) {
        System.out.println("Distancias mínimas desde el vértice " + source + ":");
        for (int i = 0; i < dist.length; i++) {
            System.out.println("Vértice " + (i + 1) + ": " + (dist[i] == Float.MAX_VALUE ? "INF" : dist[i]));
        }
    }
}
