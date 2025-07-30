package com.personal.library.library_api.dto.error;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        String message,
        int status,
        LocalDateTime timestamp,
        String path
) {}
