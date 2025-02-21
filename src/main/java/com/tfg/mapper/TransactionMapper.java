package com.tfg.mapper;

import com.tfg.dto.TransactionDTO;
import com.tfg.entity.Transaction;
import com.tfg.entity.TransactionType;
import jakarta.validation.Valid;

public class TransactionMapper {

    public static TransactionDTO toDto(Transaction transaction) {
        if (transaction == null) return null;
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setProduct(ProductMapper.toDto(transaction.getProduct()));
        dto.setWarehouse(WarehouseMapper.toDto(transaction.getWarehouse()));
        dto.setUser(UserMapper.toDto(transaction.getUser()));
        dto.setType(transaction.getType());
        dto.setQuantity(transaction.getQuantity());
        dto.setDescription(transaction.getDescription());
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }

    public static @Valid Transaction toEntity(@Valid TransactionDTO dto) {
        if (dto == null) return null;
        Transaction entity = new Transaction();
        entity.setId(dto.getId());
        entity.setProduct(ProductMapper.toEntity(dto.getProduct()));
        entity.setWarehouse(WarehouseMapper.toEntity(dto.getWarehouse()));
        entity.setUser(UserMapper.toEntity(dto.getUser()));
        entity.setType(dto.getType());
        entity.setQuantity(dto.getQuantity());
        entity.setDescription(dto.getDescription());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }
}
