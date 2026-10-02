package com.playstory.exceljobserver.dto;

import com.playstory.exceljobserver.domain.ExcelJob;

import java.util.List;

public record ExcelJobPage(
        List<ExcelJob> items,
        Long nextCursor
) {
}
