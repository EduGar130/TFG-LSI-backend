package com.tfg.repository;

import com.tfg.entity.Inventory;
import com.tfg.entity.Product;
import com.tfg.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductAndWarehouse(Product product, Warehouse warehouse);
    List<Inventory> findByProduct(Product product);
    List<Inventory> findByWarehouse(Warehouse warehouse);

    @Query("""
        SELECT i.quantity
        FROM Inventory i
        WHERE i.product.id = :productId AND i.warehouse.id = :warehouseId AND i.quantity >= :quantity
    """)
    int tiene(@Param("productId") Long productId,
                  @Param("quantity") Integer quantity,
                  @Param("warehouseId") Long warehouseId);

    @Modifying
    @Transactional
    @Query("""
    UPDATE Inventory i
    SET i.quantity = i.quantity + :cantidad
    WHERE i.product.id = :productId AND i.warehouse.id = :warehouseId
""")
    int sumarCantidad(@Param("productId") Long productId,
                      @Param("warehouseId") Long warehouseId,
                      @Param("cantidad") int cantidad);

    @Modifying
    @Transactional
    @Query("""
    UPDATE Inventory i
    SET i.quantity = i.quantity - :cantidad
    WHERE i.product.id = :productId AND i.warehouse.id = :warehouseId AND i.quantity >= :cantidad
""")
    int restarCantidad(@Param("productId") Long productId,
                       @Param("warehouseId") Long warehouseId,
                       @Param("cantidad") int cantidad);

}
