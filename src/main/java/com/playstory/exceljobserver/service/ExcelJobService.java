package com.playstory.exceljobserver.service;

import com.playstory.exceljobserver.domain.ExcelJob;
import com.playstory.exceljobserver.dto.ExcelJobPage;
import com.playstory.exceljobserver.repository.ExcelJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelJobService {

    private static final int MAX_ERROR_MESSAGE_LENGTH = 1000;
    private static final int PAGE_SIZE = 15;

    private final ExcelJobRepository repository;
    private final ExcelFileGenerator generator;
    private final ThreadPoolTaskExecutor excelJobExecutor;

    public ExcelJob generateExcel() {
        long id = repository.insertPending();
        ExcelJob job = repository.findById(id);
        excelJobExecutor.execute(() -> executeExcelJob(id));

        return job;
    }

    public ExcelJobPage findJobs(Long cursor) {
        List<ExcelJob> excelJobs = repository.findBefore(cursor == null ? Long.MAX_VALUE : cursor, PAGE_SIZE + 1);
        if (excelJobs.size() <= PAGE_SIZE) {
            return new ExcelJobPage(excelJobs, null);
        }

        List<ExcelJob> items = excelJobs.subList(0, PAGE_SIZE);

        return new ExcelJobPage(items, items.getLast().id());
    }

    private void executeExcelJob(long id) {
        try {
            repository.markProcessing(id);
            log.info("excel job {} started", id);

            Path file = generator.generate(id);
            repository.markDone(id, file.toString());

            log.info("excel job {} done: {}", id, file);
        } catch (Exception e) {
            log.error("excel job {} failed", id, e);
            repository.markFailed(id, truncateErrorMessage(String.valueOf(e.getMessage())));
        }
    }

    private String truncateErrorMessage(String message) {
        return message.length() > MAX_ERROR_MESSAGE_LENGTH ? message.substring(0, MAX_ERROR_MESSAGE_LENGTH) : message;
    }
}
