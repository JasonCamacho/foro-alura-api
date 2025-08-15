package com.alura.foro.dto;

import java.time.LocalDateTime;

public record DatosListadoTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        String status,
        String autor,
        String curso
) {
    // Compatibilidad con el código existente que usa solo 3 argumentos
    public DatosListadoTopico(Long id, String titulo, String mensaje) {
        this(id, titulo, mensaje, null, null, null, null);
    }
}