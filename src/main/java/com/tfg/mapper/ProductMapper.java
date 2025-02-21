package com.tfg.mapper;

import com.tfg.dto.ProductDTO;
import com.tfg.entity.Product;

public class ProductMapper {

    public static ProductDTO toDto(Product product) {
        if (product == null) return null;
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSku(product.getSku());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockAlertThreshold(product.getStockAlertThreshold());
        dto.setCategory(CategoryMapper.toDto(product.getCategory()));
        return dto;
    }

    public static Product toEntity(ProductDTO product) {
        if (product == null) return null;
        Product entity = new Product();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setSku(product.getSku());
        entity.setDescription(product.getDescription());
        entity.setPrice(product.getPrice());
        entity.setStockAlertThreshold(product.getStockAlertThreshold());
        entity.setCategory(CategoryMapper.toEntity(product.getCategory()));
        return entity;
    }
}
