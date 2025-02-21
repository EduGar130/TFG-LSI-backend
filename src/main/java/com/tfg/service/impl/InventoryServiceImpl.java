package com.tfg.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.tfg.entity.Category;
import com.tfg.entity.Inventory;
import com.tfg.entity.Product;
import com.tfg.entity.Warehouse;
import com.tfg.repository.CategoryRepository;
import com.tfg.repository.InventoryRepository;
import com.tfg.repository.ProductRepository;
import com.tfg.repository.WarehouseRepository;
import com.tfg.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository,
                                WarehouseRepository warehouseRepository,
                                ProductRepository productRepository,
                                CategoryRepository categoryRepository) {
        this.inventoryRepository = inventoryRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    @Override
    public List<Inventory> getInventoryByWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + warehouseId));
        return inventoryRepository.findByWarehouse(warehouse);
    }

    @Override
    public void deleteInventoryById(Long id) {
        inventoryRepository.deleteById(id);
    }

    @Override
    public Inventory addInventory(Inventory inventory) {
        inventory.setId(null);
        if(inventory.getProduct().getId() == -1) {
            inventory.getProduct().setId(null);
        }

        Warehouse warehouse = warehouseRepository.findById(inventory.getWarehouse().getId())
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + inventory.getWarehouse().getId()));

        // --- Paso 1: Guardar el producto sin SKU para obtener el ID ---
        if (inventory.getProduct().getId() != null) {
            Product existingProduct = productRepository.findById(inventory.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + inventory.getProduct().getId()));
            inventory.setProduct(existingProduct);
        }else {
            Product product = new Product();
            product.setName(inventory.getProduct().getName());
            product.setDescription(inventory.getProduct().getDescription());
            product.setPrice(inventory.getProduct().getPrice());
            product.setStockAlertThreshold(inventory.getProduct().getStockAlertThreshold());

            Long categoryId = inventory.getProduct().getCategory().getId();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
            product.setCategory(category);

            product = productRepository.save(product);
            // --- Paso 2: Generar el SKU y actualizar el producto ---
            String sku = createSku(product);
            product.setSku(sku);
            product = productRepository.save(product); // actualización del SKU

            // --- Paso 3: Guardar el inventario con referencias completas ---
            inventory.setProduct(product);
        }
        inventory.setWarehouse(warehouse);
        return inventoryRepository.save(inventory);
    }


    @Override
    public Inventory updateInventory(Long id, Inventory inventory) {
        Inventory existingInventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
        Long productId = inventory.getProduct().getId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        product.setName(inventory.getProduct().getName());
        product.setDescription(inventory.getProduct().getDescription());
        product.setPrice(inventory.getProduct().getPrice());
        product.setStockAlertThreshold(inventory.getProduct().getStockAlertThreshold());
        Long categoryId = inventory.getProduct().getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        product.setCategory(category);
        existingInventory.setProduct(product);
        Long warehouseId = inventory.getWarehouse().getId();
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with id: " + warehouseId));
        existingInventory.setWarehouse(warehouse);
        existingInventory.setQuantity(inventory.getQuantity());
        return inventoryRepository.save(existingInventory);
    }

    @Override
    public byte[] generateCsvFile() {
        try {
            List<Inventory> inventoryList = inventoryRepository.findAll(); // o usa un DTO si quieres menos info

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
            CSVWriter csvWriter = new CSVWriter(streamWriter);

            // Cabecera del CSV
            String[] header = {"ID Inventario", "Producto", "SKU", "Precio", "Cantidad", "CategoriaNombre", "Almacén"};
            csvWriter.writeNext(header);

            // Cuerpo del CSV
            for (Inventory item : inventoryList) {
                String[] row = {
                        String.valueOf(item.getId()),
                        item.getProduct().getName(),
                        item.getProduct().getSku(),
                        String.valueOf(item.getProduct().getPrice()),
                        String.valueOf(item.getQuantity()),
                        item.getProduct().getCategory().getName(),
                        item.getWarehouse().getName()
                };
                csvWriter.writeNext(row);
            }

            csvWriter.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error al generar el archivo CSV", e);
        }
    }

    @Override
    @Transactional
    public void importInventoryFromCsv(MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVReader csvReader = new CSVReader(reader)) {

            String[] linea;
            boolean cabecera = true;

            while ((linea = csvReader.readNext()) != null) {
                if (cabecera) {
                    cabecera = false;
                    continue; // saltamos la cabecera
                }

                Long productId = Long.parseLong(linea[0].replaceAll("\"", ""));
                String productName = linea[1].replaceAll("\"", "");
                String sku = linea[2].replaceAll("\"", "");
                BigDecimal precio = new BigDecimal(linea[3].replaceAll("\"", ""));
                int cantidad = Integer.parseInt(linea[4].replaceAll("\"", ""));
                String categoriaNombre = linea[5].replaceAll("\"", "");
                String almacenNombre = linea[6].replaceAll("\"", "");

                Product product = productRepository.findBySku(sku)
                        .orElseGet(() -> {
                            Product nuevo = new Product();
                            nuevo.setName(productName);
                            nuevo.setSku(sku);
                            nuevo.setDescription("Importado desde CSV");
                            nuevo.setPrice(precio);
                            nuevo.setStockAlertThreshold(1);
                            nuevo.setCategory(categoryRepository.findByName(categoriaNombre)
                                    .orElseGet(() -> {
                                        Category nuevaC = new Category();
                                        nuevaC.setName(categoriaNombre);
                                        nuevaC.setDescription("Importado desde CSV");
                                        return categoryRepository.save(nuevaC);
                                    }));
                            return productRepository.save(nuevo);
                        });

                Warehouse warehouse = warehouseRepository.findByName(almacenNombre)
                        .orElseThrow(() -> new RuntimeException("No se encontró el almacén: " + almacenNombre));

                boolean yaExiste = warehouseRepository.checkProductInWarehouse(warehouse.getId(), product.getId()) != 0;
                if (!yaExiste) {
                    Inventory inventory = new Inventory();
                    inventory.setProduct(product);
                    inventory.setWarehouse(warehouse);
                    inventory.setQuantity(cantidad);
                    inventoryRepository.save(inventory);
                } else {
                    // Sumamos cantidad
                    Inventory existingInventory = inventoryRepository.findByProductAndWarehouse(product, warehouse)
                            .orElseThrow(() -> new RuntimeException("No se encontró el inventario para el producto y almacén especificados"));
                    existingInventory.setQuantity(existingInventory.getQuantity() + cantidad);
                    inventoryRepository.save(existingInventory);
                }
            }
        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Genera un SKU único basado en el producto y el almacén.
     * Formato: NOM-LOC-PID-WID
     * Ejemplo: LAP-MAD-15-2
     */
    private String createSku(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Producto no puede ser nulo.");
        }

        String nombre = sanitize(product.getName());

        String nombrePart = extractPart(nombre, 3);

        String categoria = sanitize(product.getCategory().getName());

        String categoriaPart = extractPart(categoria, 3);

        return String.format("%s-%d-%s-%d",
                nombrePart.toUpperCase(),
                product.getId(),
                categoriaPart.toUpperCase(),
                product.getCategory().getId());
    }

    /**
     * Limpia una cadena eliminando espacios adicionales y caracteres no alfanuméricos.
     */
    private String sanitize(String input) {
        return input == null ? "" : input.replaceAll("[^a-zA-Z0-9]", "").trim();
    }

    /**
     * Extrae los primeros 'n' caracteres o completa con 'X' si es muy corta.
     */
    private String extractPart(String input, int length) {
        if (input.length() >= length) {
            return input.substring(0, length);
        }
        return String.format("%-" + length + "s", input).replace(' ', 'X'); // rellena con X
    }
}
