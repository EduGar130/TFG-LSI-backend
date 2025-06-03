package com.tfg.service;

import com.tfg.dto.EstadisticasDTO;

public interface EstadisticasService {
    EstadisticasDTO obtenerEstadisticas(String sku, String almacen, String categoria, String fechaInicio, String fechaFin);
}
