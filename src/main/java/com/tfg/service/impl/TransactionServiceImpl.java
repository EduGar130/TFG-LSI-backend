package com.tfg.service.impl;

import com.tfg.entity.*;
import com.tfg.exception.StockInsuficienteException;
import com.tfg.repository.*;
import com.tfg.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final InventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryAlertRepository inventoryAlertRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, InventoryRepository inventoryRepository, WarehouseRepository warehouseRepository, ProductRepository productRepository, UserRepository userRepository, InventoryAlertRepository inventoryAlertRepository) {
        this.transactionRepository = transactionRepository;
        this.inventoryRepository = inventoryRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.inventoryAlertRepository = inventoryAlertRepository;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        Long productId = transaction.getProduct().getId();
        Long warehouseId = transaction.getWarehouse().getId();
        int cantidad = transaction.getQuantity();
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new EntityNotFoundException("Almacen no encontrado con ID " + warehouseId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID " + productId));
        User user = userRepository.findByUsername(transaction.getUser().getUsername())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con username " + transaction.getUser().getUsername()));
        transaction.setWarehouse(warehouse);
        transaction.setProduct(product);
        transaction.setUser(user);

        if (transaction.getType() == TransactionType.SALE || transaction.getType() == TransactionType.REMOVE) {
            // Verifica si hay suficiente stock antes de descontar
            int stockActual = inventoryRepository.tiene(productId, cantidad, warehouseId);
            if (stockActual >= cantidad) {
                List<InventoryAlert> inventoryAlerts = inventoryAlertRepository.findByProductAndWarehouse(product, warehouse);
                Boolean existeAlerta = false;
                for (InventoryAlert inventoryAlert : inventoryAlerts) {
                    if (inventoryAlert.getProduct().getId().equals(productId) && inventoryAlert.getWarehouse().getId().equals(warehouseId)) {
                        existeAlerta = true;
                    }
                }
                if((stockActual - cantidad) <= product.getStockAlertThreshold() && !existeAlerta) {
                    // Crea una alerta de inventario
                    InventoryAlert inventoryAlert = new InventoryAlert();
                    inventoryAlert.setProduct(product);
                    inventoryAlert.setWarehouse(warehouse);
                    inventoryAlert.setAlertType("Stock bajo");
                    inventoryAlert.setMessage("El stock del producto " + product.getName() + " en: " + warehouse.getName() + " está por debajo de: " + product.getStockAlertThreshold() + " unidades.");
                    inventoryAlertRepository.save(inventoryAlert);
                }
                inventoryRepository.restarCantidad(productId, warehouseId, cantidad);
            } else {
                // Lanza una excepción personalizada si no hay suficiente cantidad
                throw new StockInsuficienteException("No hay suficiente stock para el producto ID " + productId + " en el almacén ID " + warehouseId);
            }
        } else if (transaction.getType() == TransactionType.ADD) {
            int stockActual = inventoryRepository.tiene(productId, cantidad, warehouseId);
            if(stockActual <= product.getStockAlertThreshold() && (stockActual + cantidad) > product.getStockAlertThreshold()) {
               //Busca la alerta de inventario y si existe la elimina
                List<InventoryAlert> inventoryAlerts = inventoryAlertRepository.findByProductAndWarehouse(product, warehouse);
                for (InventoryAlert inventoryAlert : inventoryAlerts) {
                    if (inventoryAlert.getProduct().getId().equals(productId) && inventoryAlert.getWarehouse().getId().equals(warehouseId)) {
                        inventoryAlertRepository.delete(inventoryAlert);
                    }
                }
            }
            inventoryRepository.sumarCantidad(productId, warehouseId, cantidad);
        }
        transaction.setId(null);

        return transactionRepository.save(transaction);
    }

}
