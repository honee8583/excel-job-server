package com.playstory.exceljobserver.job;

import java.util.List;

public record ExcelJobPage(
        List<ExcelJob> items,
        Long nextCursor
) {
}
