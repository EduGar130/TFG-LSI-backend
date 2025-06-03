package com.tfg.service.impl;

import com.tfg.dto.EstadisticasDTO;
import com.tfg.entity.Category;
import com.tfg.entity.Transaction;
import com.tfg.entity.Warehouse;
import com.tfg.repository.CategoryRepository;
import com.tfg.repository.TransactionRepository;
import com.tfg.repository.WarehouseRepository;
import com.tfg.service.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class EstadisticasServiceImpl implements EstadisticasService {

    private final TransactionRepository transactionRepository;
    private final WarehouseRepository warehouseRepository;
    private final CategoryRepository categoryRepository;

    public EstadisticasServiceImpl(TransactionRepository transactionRepository, WarehouseRepository warehouseRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.warehouseRepository = warehouseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public EstadisticasDTO obtenerEstadisticas(String sku, String almacenId, String categoriaId, String fechaInicio, String fechaFin) {
        List<Transaction> transacciones = transactionRepository.findAll();

        // Filtrar transacciones por almacén, categoría y fechas si se proporcionan
        if (almacenId != null && !almacenId.isEmpty()) {
            transacciones.removeIf(t -> !almacenId.equals(t.getWarehouse().getId().toString()));
        }
        if (categoriaId != null && !categoriaId.isEmpty())
        {
            transacciones.removeIf(t -> !categoriaId.equals(t.getProduct().getCategory().getId().toString()));
        }
        if (fechaInicio != null && !fechaInicio.isEmpty() && fechaFin != null && !fechaFin.isEmpty()) {
            LocalDate inicio = LocalDate.parse(fechaInicio);
            LocalDate fin = LocalDate.parse(fechaFin);
            transacciones.removeIf(t -> t.getCreatedAt().toLocalDate().isBefore(inicio) || t.getCreatedAt().toLocalDate().isAfter(fin));
        }

        Map<String, Integer> ventasPorCategoria = new HashMap<>();
        Map<String, Integer> movimientosPorTipo = new HashMap<>();
        Map<String, Integer> ventasPorAlmacen = new HashMap<>();
        Map<String, Integer> rankingEmpleados = new HashMap<>();
        Map<String, Integer> topProductos = new HashMap<>();
        Map<String, Integer> ventasMensuales = new HashMap<>();

        for (Transaction t : transacciones) {
            String tipo = t.getType().toString();

            // Conteo por tipo
            movimientosPorTipo.merge(tipo, 1, Integer::sum);

            if ("SALE".equals(tipo)) {
                // Categoría
                String categoria = Optional.ofNullable(t.getProduct().getCategory())
                        .map(c -> c.getName()).orElse("Sin categoría");
                ventasPorCategoria.merge(categoria, t.getQuantity(), Integer::sum);

                // Almacén
                String almacen = t.getWarehouse().getName();
                ventasPorAlmacen.merge(almacen, t.getQuantity(), Integer::sum);

                // Empleado
                String empleado = t.getUser().getUsername();
                rankingEmpleados.merge(empleado, t.getQuantity(), Integer::sum);

                // Producto
                String nombreProducto = t.getProduct().getName();
                topProductos.merge(nombreProducto, t.getQuantity(), Integer::sum);

                // Ventas mensuales por SKU (si aplica)
                if (sku != null && sku.equals(t.getProduct().getSku())) {
                    LocalDate fecha = t.getCreatedAt().toLocalDate();
                    String mes = fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es")).toUpperCase();
                    ventasMensuales.merge(mes, t.getQuantity(), Integer::sum);
                }
            }
        }

        // Limitar a top 5 productos
        topProductos = ordenarYReducir(topProductos, 5);
        StringBuilder filtros = new StringBuilder();
        if (almacenId != null && !almacenId.isEmpty()) {
            Optional<Warehouse> almacen = warehouseRepository.findById(Long.parseLong(almacenId));
            filtros.append("Almacén: ").append(almacen.map(Warehouse::getName).orElse("Desconocido")).append("\n");
        }
        if (categoriaId != null && !categoriaId.isEmpty()) {
            Optional<Category> categoria = categoryRepository.findById(Long.parseLong(categoriaId));
            filtros.append("Categoría: ").append(categoria.map(Category::getName).orElse("Desconocida")).append("\n");
        }
        if (fechaInicio != null && !fechaInicio.isEmpty() && fechaFin != null && !fechaFin.isEmpty()) {
            filtros.append("Fecha Inicio: ").append(fechaInicio).append("\n");
            filtros.append("Fecha Fin: ").append(fechaFin).append("\n");
        }

        EstadisticasDTO dto = new EstadisticasDTO();
        dto.setVentasPorCategoria(ventasPorCategoria);
        dto.setMovimientosPorTipo(movimientosPorTipo);
        dto.setVentasPorAlmacen(ventasPorAlmacen);
        dto.setRankingEmpleados(ordenar(rankingEmpleados));
        dto.setTopProductos(topProductos);
        dto.setVentasMensuales(ventasMensuales);
        dto.setSkuSeleccionado(sku);
        dto.setFiltros(filtros.toString());
        return dto;
    }

    private Map<String, Integer> ordenar(Map<String, Integer> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        Map::putAll);
    }

    private Map<String, Integer> ordenarYReducir(Map<String, Integer> map, int limite) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limite)
                .collect(LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        Map::putAll);
    }
}
