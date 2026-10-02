package com.playstory.exceljobserver.domain;

import java.time.LocalDateTime;

public record ExcelJob(
        Long id,
        String status,
        LocalDateTime requestedAt,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        String filePath,
        String errorMessage
) {
}
