package com.playstory.exceljobserver.job;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<ExcelJob> findAllJobs() {
        return service.findAllJobs();
    }
}
