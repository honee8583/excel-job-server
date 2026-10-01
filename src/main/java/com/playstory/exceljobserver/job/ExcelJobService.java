package com.playstory.exceljobserver.job;

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

    private final ExcelJobRepository repository;
    private final ExcelFileGenerator generator;
    private final ThreadPoolTaskExecutor excelJobExecutor;

    public ExcelJob generateExcel() {
        long id = repository.insertPending();
        ExcelJob job = repository.findById(id);
        excelJobExecutor.execute(() -> executeExcelJob(id));

        return job;
    }

    // TODO 페이징 적용
    public List<ExcelJob> findAllJobs() {
        return repository.findAll();
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
