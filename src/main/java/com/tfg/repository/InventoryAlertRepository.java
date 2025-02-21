package com.tfg.repository;

import com.tfg.entity.InventoryAlert;
import com.tfg.entity.Product;
import com.tfg.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryAlertRepository extends JpaRepository<InventoryAlert, Long> {
    List<InventoryAlert> findByProductId(Long productId);

    List<InventoryAlert> findByProductAndWarehouse(Product product, Warehouse warehouse);
}
