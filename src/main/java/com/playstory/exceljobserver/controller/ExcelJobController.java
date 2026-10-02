package com.playstory.exceljobserver.controller;

import com.playstory.exceljobserver.domain.ExcelJob;
import com.playstory.exceljobserver.dto.ExcelJobPage;
import com.playstory.exceljobserver.service.ExcelJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class ExcelJobController {

    private final ExcelJobService service;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ExcelJob generateExcel() {
        return service.generateExcel();
    }

    @GetMapping
    public ExcelJobPage findJobs(@RequestParam(required = false) Long cursor) {
        return service.findJobs(cursor);
    }
}
