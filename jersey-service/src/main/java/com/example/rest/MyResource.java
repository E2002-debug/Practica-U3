package com.example.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import com.example.controller.CoordenadasVeterinarias;
import com.example.controller.dao.services.VeterinariaService;
import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;
import com.example.controller.tda.recoridos.BellmanFord;
import com.example.controller.tda.recoridos.FloydWarshall;
import com.example.models.Veterinaria;



@Path("myresource")
public class MyResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIt() {
        HashMap<String, Object> mapa = new HashMap<>();
        CoordenadasVeterinarias coordenadas = new CoordenadasVeterinarias();
        GraphLabelNoDirect<Veterinaria> graph = new GraphLabelNoDirect<>( 20, Veterinaria.class, coordenadas);

        try {
            VeterinariaService ps = new VeterinariaService();
            LinkedList<Veterinaria> lp = ps.listAll();
        
            // Agregar las veterinarias al HashMap de coordenadas
            for (Veterinaria p : lp.toArray()) {
                coordenadas.agregarVeterinaria(p);
            }
        
            for (Veterinaria p : lp.toArray()) {
                System.out.println(p.getNombre());
            }
        
            // Si hay datos, se configura el grafo
            if (!lp.isEmpty()) {
                graph = new GraphLabelNoDirect<>(lp.getSize(), Veterinaria.class, coordenadas);
                Veterinaria[] m = lp.toArray();
        
                for (int i = 0; i < lp.getSize(); i++) {
                    graph.labelsVerticeL((i + 1), m[i]);
                }
        
                // Generar adyacencias aleatorias
                graph.generarAdyacenciasAleatorias();
		
		       // Insertar algunas adyacencias de ejemplo
                // graph.insertEdgeLD(m[0], m[2]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[1], m[2]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[2], m[3]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[3], m[1]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[1], m[4]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[4], m[5]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[5], m[6]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[6], m[7]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[7], m[8]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[8], m[9]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[9], m[10]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[10], m[11]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[11], m[12]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[12], m[13]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[13], m[14]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[14], m[15]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[15], m[16]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[16], m[17]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[17], m[18]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[18], m[19]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[19], m[0]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[2], m[1]); // Ejemplo de adyacencia
                // graph.insertEdgeLD(m[17], m[8]); // Ejemplo de adyacencia
        
                // Guardar el grafo en formato JSON usando el método de GraphLabelDirect
                graph.guardarGrafoEnJson();
        
                // Dibujar el grafo (opcional)
                graph.drawGraph();
        
                // Ejecutar Floyd-Warshall
                System.out.println("-------------------------------------------------------------------------");
                System.out.println("Ejecutando Floyd-Warshall...");
                FloydWarshall.obtenerCaminoMinimo(graph, 6, 22);
                System.out.println("-------------------------------------------------------------------------");
        
                // Ejecutar Bellman-Ford desde el primer vértice
                System.out.println("\nEjecutando Bellman-Ford desde el primer vértice...");
                BellmanFord.ejecutarBellmanFord(graph, 3, 22);
                
            }
        } catch (Exception e) {
            mapa.put("msg", "Error");
            mapa.put("data", e.toString());
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(mapa).build();
        }
        
        // Retornar respuesta exitosa con grafo como string
        mapa.put("msg", "OK");
        mapa.put("data", graph.toString());
        return Response.ok(mapa).build();
    }
}



