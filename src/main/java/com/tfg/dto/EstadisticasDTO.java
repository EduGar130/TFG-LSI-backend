package com.tfg.dto;

import lombok.Data;

import java.util.Map;

@Data
public class EstadisticasDTO {
    private Map<String, Integer> ventasPorCategoria;
    private Map<String, Integer> movimientosPorTipo;
    private Map<String, Integer> ventasPorAlmacen;
    private Map<String, Integer> rankingEmpleados;
    private Map<String, Integer> topProductos;
    private Map<String, Integer> ventasMensuales;
    private String skuSeleccionado;
    private String filtros;
}
