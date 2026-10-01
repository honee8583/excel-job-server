package com.playstory.exceljobserver.job;

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
