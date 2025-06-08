package com.tfg.service.impl;

import com.lowagie.text.pdf.PdfPTable;
import com.tfg.dto.EstadisticasDTO;
import com.lowagie.text.DocumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lowagie.text.Document;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportServiceImplTest {

    private ReportServiceImpl reportService;

    @BeforeEach
    void setUp() {
        reportService = new ReportServiceImpl();
    }

    @Test
    void testGenerarEstadisticasPDF() throws DocumentException {
        // Datos simulados
        EstadisticasDTO stats = new EstadisticasDTO();
        stats.setFiltros("Filtro: Ventas del último mes");

        Map<String, Integer> ventasPorCategoria = new HashMap<>();
        ventasPorCategoria.put("Categoría 1", 100);
        ventasPorCategoria.put("Categoría 2", 200);
        stats.setVentasPorCategoria(ventasPorCategoria);

        Map<String, Integer> movimientosPorTipo = new HashMap<>();
        movimientosPorTipo.put("Venta", 150);
        movimientosPorTipo.put("Reposición", 50);
        stats.setMovimientosPorTipo(movimientosPorTipo);

        Map<String, Integer> ventasPorAlmacen = new HashMap<>();
        ventasPorAlmacen.put("Almacén 1", 300);
        stats.setVentasPorAlmacen(ventasPorAlmacen);

        Map<String, Integer> rankingProductos = new HashMap<>();
        rankingProductos.put("Producto A", 120);
        stats.setTopProductos(rankingProductos);

        Map<String, Integer> rankingEmpleados = new HashMap<>();
        rankingEmpleados.put("Empleado 1", 80);
        stats.setRankingEmpleados(rankingEmpleados);

        Map<String, Integer> ventasMensualesPorSKU = new HashMap<>();
        ventasMensualesPorSKU.put("SKU123", 50);
        stats.setVentasMensuales(ventasMensualesPorSKU);

        // Ejecución del método
        byte[] pdfBytes = reportService.generarEstadisticasPDF(stats);

        // Verificación
        assertNotNull(pdfBytes);
        // Puedes agregar más verificaciones, como comprobar el tamaño del PDF generado
    }

    private void agregarTabla(Document document, Map<String, Integer> datos) throws DocumentException {
        if (datos == null || datos.isEmpty()) {
            throw new IllegalArgumentException("El mapa de datos no puede ser null o vacío");
        }

        PdfPTable table = new PdfPTable(2); // Ejemplo: 2 columnas
        table.addCell("Categoría");
        table.addCell("Cantidad");

        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            table.addCell(entry.getKey());
            table.addCell(String.valueOf(entry.getValue()));
        }

        document.add(table);
    }
}