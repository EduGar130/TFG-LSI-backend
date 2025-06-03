package com.tfg.repository;

import com.tfg.entity.Warehouse;
import jakarta.activation.DataHandler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.warehouse.id = :warehouseId AND i.product.id = :productId")
    int checkProductInWarehouse(@Param("warehouseId") Long warehouseId, @Param("productId") Long productId);

    Optional<Warehouse> findByName(String almacenNombre);

    Warehouse getWarehouseByName(String almacénCentral);
}
