package com.tfg.service;

import com.tfg.dto.EstadisticasDTO;

public interface ReportService {
    byte[] generarEstadisticasPDF(EstadisticasDTO dto);
}
