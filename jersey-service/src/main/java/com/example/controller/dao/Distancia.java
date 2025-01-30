package com.example.controller.dao;

public class Distancia {
    
    private static final double RADIO_TIERRA = 6371.0; // Radio de la Tierra en kilómetros

    // Método para calcular la distancia usando la fórmula del Haversine
    public static double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        // Convertir las coordenadas de grados a radianes
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Diferencias de latitud y longitud
        double dlat = lat2Rad - lat1Rad;
        double dlon = lon2Rad - lon1Rad;

        // Fórmula del Haversine
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distancia final
        return RADIO_TIERRA * c;
    }
    
}
