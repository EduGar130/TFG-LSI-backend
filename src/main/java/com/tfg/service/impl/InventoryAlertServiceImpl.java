package com.tfg.service.impl;

import com.tfg.entity.InventoryAlert;
import com.tfg.repository.InventoryAlertRepository;
import com.tfg.service.InventoryAlertService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryAlertServiceImpl implements InventoryAlertService {

    private final InventoryAlertRepository alertRepository;

    public InventoryAlertServiceImpl(InventoryAlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public List<InventoryAlert> getAllAlerts() {
        return alertRepository.findAll();
    }
}
