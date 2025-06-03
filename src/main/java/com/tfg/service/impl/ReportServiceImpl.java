package com.tfg.service.impl;

import com.tfg.dto.EstadisticasDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.tfg.service.ReportService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Override
    public byte[] generarEstadisticasPDF(EstadisticasDTO stats) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        // Título principal
        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph titulo = new Paragraph("Reporte de Estadísticas de Inventario", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        document.add(new Paragraph("Fecha de generación: " + LocalDate.now()));
        document.add(new Paragraph(" "));
        // Filtros aplicados
        if (stats.getFiltros() != null && !stats.getFiltros().isBlank()) {
            Paragraph filtros = new Paragraph("Filtros aplicados: \n" + stats.getFiltros(),
                    FontFactory.getFont(FontFactory.HELVETICA, 12));
            document.add(filtros);
        }

        // Sección: Ventas por Categoría
        agregarSeccion("Ventas por Categoría", stats.getVentasPorCategoria(), document);

        // Sección: Movimientos por Tipo
        agregarSeccion("Movimientos por Tipo (Reposiciones, Ventas, Eliminaciones)", stats.getMovimientosPorTipo(), document);

        // Sección: Ventas por Almacén
        agregarSeccion("Ventas por Almacén", stats.getVentasPorAlmacen(), document);

        // Sección: Ranking de Empleados
        agregarSeccion("Ranking de Empleados por Ventas", stats.getRankingEmpleados(), document);

        // Sección: Top 5 Productos Más Vendidos
        agregarSeccion("Top 5 Productos Más Vendidos", stats.getTopProductos(), document);

        // Sección: Ventas Mensuales por SKU (si se ha seleccionado)
        if (stats.getSkuSeleccionado() != null && !stats.getSkuSeleccionado().isBlank()) {
            document.add(new Paragraph(" "));
            Paragraph sub = new Paragraph("Ventas Mensuales para SKU: " + stats.getSkuSeleccionado(),
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
            document.add(sub);

            agregarTabla(stats.getVentasMensuales(), document);
        }

        document.close();
        return baos.toByteArray();
    }

    private void agregarSeccion(String titulo, Map<String, Integer> datos, Document document) throws DocumentException {
        document.add(new Paragraph(" "));
        Paragraph sub = new Paragraph(titulo, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        document.add(sub);
        agregarTabla(datos, document);
    }

    private void agregarTabla(Map<String, Integer> datos, Document document) throws DocumentException {
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(10f);
        tabla.setSpacingAfter(10f);

        Font encabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        PdfPCell celda1 = new PdfPCell(new Phrase("Etiqueta", encabezado));
        PdfPCell celda2 = new PdfPCell(new Phrase("Cantidad", encabezado));
        tabla.addCell(celda1);
        tabla.addCell(celda2);

        for (Map.Entry<String, Integer> entry : datos.entrySet()) {
            tabla.addCell(entry.getKey());
            tabla.addCell(String.valueOf(entry.getValue()));
        }

        document.add(tabla);
    }
}
