package com.example.rest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.example.controller.CoordenadasVeterinarias;
import com.example.controller.dao.services.VeterinariaService;
import com.example.controller.tda.list.LinkedList;
import com.example.controller.tda.list.graph.GraphLabelNoDirect;
import com.example.controller.tda.recoridos.BellmanFord;
import com.example.controller.tda.recoridos.FloydWarshall;
import com.example.models.Veterinaria;
import com.google.gson.Gson;

@Path("vet")
public class VeterinariaApi {

    private GraphLabelNoDirect<Veterinaria> graph;
    private CoordenadasVeterinarias coordenadas = new CoordenadasVeterinarias();

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons() {
        HashMap map = new HashMap<>();
        VeterinariaService ps = new VeterinariaService();
        map.put("msg", "OK");
        map.put("data", ps.listAll().toArray());
        if (ps.listAll().isEmpty()) {
            map.put("data", new Object[] {});
        }

        return Response.ok(map).build();
    }

    @Path("/get/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPerson(@PathParam("id") Integer id) {
        HashMap map = new HashMap<>();
        VeterinariaService ps = new VeterinariaService();
        try {
            ps.setVeterinaria(ps.get(id));
        } catch (Exception e) {
            // TODO: handle exception
        }
        map.put("msg", "OK");
        map.put("data", ps.getVeterinaria());
        if (ps.getVeterinaria().getId() == null) {
            map.put("data", "No existe la veterinaria con ese identificador");
            return Response.status(Status.BAD_REQUEST).entity(map).build();
        }
        return Response.ok(map).build();
    }

    @Path("/save")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap map) {
        // TODO
        // VALIDATION ---- BAD REQUEST

        HashMap res = new HashMap<>();
        Gson g = new Gson();
        String a = g.toJson(map);
        System.out.println("******* " + a);
        try {
            VeterinariaService ps = new VeterinariaService();
            ps.getVeterinaria().setNombre(map.get(("nombre")).toString());
            ps.getVeterinaria().setDireccion(map.get(("direccion")).toString());
            ps.getVeterinaria().setTelefono(map.get(("telefono")).toString());
            ps.getVeterinaria().setNombre_Veterinario(map.get(("nombre_Veterinario")).toString());
            ps.getVeterinaria().setLatitud(Double.parseDouble(map.get(("latitud")).toString()));
            ps.getVeterinaria().setLongitud(Double.parseDouble(map.get(("longitud")).toString()));

            ps.save();
            res.put("msg", "OK");
            res.put("data", "Veterinaria registrada correctamente");
            return Response.ok(res).build();
        } catch (Exception e) {
            System.out.println("Error en sav data " + e.toString());
            res.put("msg", "Error");
            // res.put("msg", "ERROR");
            res.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
            // TODO: handle exception
        }

    }

    @Path("/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(HashMap map) {
        HashMap res = new HashMap<>();

        try {
            VeterinariaService ps = new VeterinariaService();
            ps.setVeterinaria(ps.get(Integer.parseInt(map.get("id").toString())));
            ps.getVeterinaria().setNombre(map.get(("nombre")).toString());
            ps.getVeterinaria().setDireccion(map.get(("direccion")).toString());
            ps.getVeterinaria().setTelefono(map.get(("telefono")).toString());
            ps.getVeterinaria().setNombre_Veterinario(map.get(("nombre_Veterinario")).toString());
            ps.getVeterinaria().setLatitud(Double.parseDouble(map.get(("latitud")).toString()));
            ps.getVeterinaria().setLongitud(Double.parseDouble(map.get(("longitud")).toString()));

            ps.update();
            res.put("msg", "OK");
            res.put("data", "Veterinaria registrada correctamente");
            return Response.ok(res).build();

        } catch (Exception e) {
            System.out.println("Error en sav data " + e.toString());
            res.put("msg", "Error");
            // res.put("msg", "ERROR");
            res.put("data", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
            // TODO: handle exception
        }

    }

    @Path("/grafo")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response getGrafoHtml() {
        // Ruta absoluta al archivo grafo.html
        String filePath = "C:\\Users\\lisbe\\Desktop\\Practica3\\jersey-service\\resources\\graph\\grafo.html";
        File file = new File(filePath);

        if (!file.exists()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Archivo grafo.html no encontrado").build();
        }

        try {
            // Leer el contenido del archivo
            String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            return Response.ok(content).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al leer el archivo grafo.html: " + e.getMessage()).build();
        }
    }

    @Path("/adyacencias/manual")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response agregarAdyacenciaManual(HashMap<String, Object> params) {
        HashMap<String, Object> res = new HashMap<>();
        try {
            // Validar parámetros
            String v1Nombre = params.get("v1").toString();
            String v2Nombre = params.get("v2").toString();

            if (v1Nombre == null || v2Nombre == null) {
                res.put("msg", "Debe proporcionar los nombres de las dos veterinarias.");
                return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
            }

            VeterinariaService veterinariaService = new VeterinariaService();
            Veterinaria v1 = veterinariaService.findByName(v1Nombre);
            Veterinaria v2 = veterinariaService.findByName(v2Nombre);

            if (v1 == null || v2 == null) {
                res.put("msg", "Una o ambas veterinarias no existen.");
                return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
            }

            // Inicializar grafo si no existe
            if (graph == null) {
                LinkedList<Veterinaria> listaVeterinarias = veterinariaService.listAll();
                graph = new GraphLabelNoDirect<>(listaVeterinarias.getSize(), Veterinaria.class, coordenadas);

                for (int i = 0; i < listaVeterinarias.getSize(); i++) {
                    graph.labelsVerticeL(i + 1, listaVeterinarias.get(i));
                }
            }

            // Agregar arista manualmente
            graph.insertEdgeLD(v1, v2);
            res.put("msg", "Adyacencia agregada exitosamente.");
            return Response.ok(res).build();

        } catch (Exception e) {
            res.put("msg", "Error al agregar la adyacencia manual.");
            res.put("error", e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @Path("/adyacencias/aleatorias")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response generarAdyacenciasAleatorias() {
        HashMap<String, Object> res = new HashMap<>();
        try {
            VeterinariaService veterinariaService = new VeterinariaService();
            LinkedList<Veterinaria> listaVeterinarias = veterinariaService.listAll();

            if (listaVeterinarias.isEmpty()) {
                res.put("msg", "No hay veterinarias para generar adyacencias.");
                return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
            }

            // Inicializar grafo si no existe
            if (graph == null) {
                graph = new GraphLabelNoDirect<>(listaVeterinarias.getSize(), Veterinaria.class, coordenadas);

                for (int i = 0; i < listaVeterinarias.getSize(); i++) {
                    graph.labelsVerticeL(i + 1, listaVeterinarias.get(i));
                }
            }

            // Generar adyacencias aleatorias
            graph.generarAdyacenciasAleatorias();
            res.put("msg", "Adyacencias aleatorias generadas exitosamente.");
            return Response.ok(res).build();

        } catch (Exception e) {
            res.put("msg", "Error al generar adyacencias aleatorias.");
            res.put("error", e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @Path("/graph")
    public class GrafoController {

        private GraphLabelNoDirect<Veterinaria> graph;
        private CoordenadasVeterinarias coordenadas;

        public GrafoController() {
            cargarGrafo(); // Cargar el grafo en el constructor
        }

        /**
         * Método para inicializar y cargar el grafo con los datos de las veterinarias.
         */
        private void cargarGrafo() {
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

                // Agregar las veterinarias al HashMap de coordenadas
                coordenadas = new CoordenadasVeterinarias();
                for (Veterinaria p : lp) {
                    coordenadas.agregarVeterinaria(p);
                    System.out.println("Veterinaria agregada: " + p.getNombre()); // Verificar que se agregan
                                                                                  // correctamente
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
                    System.out.println("Grafo cargado correctamente.");
                }
            } catch (Exception e) {
                e.printStackTrace(); // Para obtener detalles más completos del error
                System.out.println("Error al cargar el grafo.");
            }
        }
    }

    /**
     * Endpoint para ejecutar el algoritmo de Floyd-Warshall.
     */
    @GET
    @Path("/floyd/{origen}/{destino}")
    public Response ejecutarFloydWarshall(@PathParam("origen") int origen, @PathParam("destino") int destino) {
        HashMap<String, Object> mapa = new HashMap<>();
        try {
            

            // Validación de nodos
            System.out.println("Número de vértices: " + graph.nro_vertices());
            if (origen <= 0 || destino <= 0 || origen > graph.nro_vertices() || destino > graph.nro_vertices()) {
                mapa.put("msg", "Error: Nodos fuera de rango.");
                return Response.status(Response.Status.BAD_REQUEST).entity(mapa).build();
            }

            Map<String, Object> resultado = FloydWarshall.obtenerCaminoMinimo(graph, origen, destino);
            System.out.println("Resultado de Floyd-Warshall: " + resultado); // Verificar resultado
            mapa.put("msg", "OK");
            mapa.put("data", resultado);
            return Response.ok(mapa).build();

        } catch (Exception e) {
            mapa.put("msg", "Error");
            mapa.put("data", e.toString());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(mapa).build();
        }
    }

    /**
     * Endpoint para ejecutar el algoritmo de Bellman-Ford.
     */
    @GET
    @Path("/bellman/{origen}/{destino}")
    public Response ejecutarBellmanFord(@PathParam("origen") int origen, @PathParam("destino") int destino) {
        HashMap<String, Object> mapa = new HashMap<>();
        try {

            // Validación de nodos
            System.out.println("Número de vértices: " + graph.nro_vertices());
            if (origen <= 0 || destino <= 0 || origen > graph.nro_vertices() || destino > graph.nro_vertices()) {
                mapa.put("msg", "Error: Nodos fuera de rango.");
                return Response.status(Response.Status.BAD_REQUEST).entity(mapa).build();
            }

            Map<String, Object> resultado = BellmanFord.ejecutarBellmanFord(graph, origen, destino);
            System.out.println("Resultado de Bellman-Ford: " + resultado); // Verificar resultado
            mapa.put("msg", "OK");
            mapa.put("data", resultado);
            return Response.ok(mapa).build();

        } catch (Exception e) {
            mapa.put("msg", "Error");
            mapa.put("data", e.toString());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(mapa).build();
        }
    }

}
