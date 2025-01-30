package com.example.controller.tda.recoridos;

import java.util.HashMap;
import java.util.Map;

import com.example.controller.tda.list.graph.GraphLabelNoDirect;

public class FloydWarshall {

    public static Map<String, Object> obtenerCaminoMinimo(GraphLabelNoDirect<?> graph, int start, int end) {
        int n = graph.nro_vertices();
        float[][] dist = new float[n][n];
        Integer[][] next = new Integer[n][n];

        // Inicialización de la matriz de distancias y predecesores
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                    next[i][j] = i; // Se asigna el mismo nodo
                } else {
                    float weight = graph.getWeight(i + 1, j + 1);
                    if (weight != -1) { // Hay conexión
                        dist[i][j] = weight;
                        next[i][j] = j; // Se inicializa con el destino
                    } else { // No hay conexión
                        dist[i][j] = Float.MAX_VALUE;
                        next[i][j] = null;
                    }
                }
            }
        }

        // Imprimir la matriz de adyacencia inicial
        System.out.println("Matriz de adyacencia inicial:");
        imprimirMatriz(dist);

        // Medir el tiempo de ejecución del algoritmo Floyd-Warshall
        long startTime = System.currentTimeMillis();

        // Algoritmo de Floyd-Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] != Float.MAX_VALUE && dist[k][j] != Float.MAX_VALUE) {
                        float nuevaDistancia = dist[i][k] + dist[k][j];
                        if (nuevaDistancia < dist[i][j]) {
                            dist[i][j] = nuevaDistancia;
                            next[i][j] = next[i][k]; // Actualiza el predecesor
                        }
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de Floyd-Warshall: " + (endTime - startTime) + " ms");

        // Imprimir la matriz de distancias mínimas después de ejecutar Floyd-Warshall
        System.out.println("Matriz de distancias mínimas:");
        imprimirMatriz(dist);

        // Obtener el camino entre los nodos especificados
        String camino = reconstruirCamino(start - 1, end - 1, next);
        Map<String, Object> resultado = new HashMap<>();
        if (camino.equals("No existe camino")) {
            resultado.put("mensaje", camino);
            System.out.println(camino); // Imprime "No existe camino" si no se encuentra
        } else {
            resultado.put("camino", camino);
            resultado.put("distancia", dist[start - 1][end - 1]);
            System.out.println("Camino mínimo: " + camino);
            System.out.println("Distancia: " + dist[start - 1][end - 1]);
        }
        return resultado;
    }

    private static String reconstruirCamino(int start, int end, Integer[][] next) {
        if (next[start][end] == null) {
            return "No existe camino";
        }

        StringBuilder camino = new StringBuilder();
        int current = start;
        while (current != end) {
            if (next[current][end] == null) {
                return "No existe camino"; // Si no hay siguiente nodo, no hay camino válido
            }
            camino.append((current + 1)).append(" -> ");
            current = next[current][end];
        }
        camino.append((end + 1));
        return camino.toString();
    }

    private static void imprimirMatriz(float[][] matriz) {
        for (float[] fila : matriz) {
            for (float valor : fila) {
                System.out.print((valor == Float.MAX_VALUE ? "INF" : valor) + "\t");
            }
            System.out.println();
        }
    }
}



