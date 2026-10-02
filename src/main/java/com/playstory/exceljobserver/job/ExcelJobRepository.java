package com.playstory.exceljobserver.job;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ExcelJobRepository {

    private final JdbcClient jdbcClient;

    public long insertPending() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql("INSERT INTO excel_job (status) VALUES (:status)")
                .param("status", JobStatus.PENDING.value())
                .update(keyHolder);
        return keyHolder.getKey().longValue();
    }

    public void markProcessing(long id) {
        jdbcClient.sql("UPDATE excel_job SET status = :status, started_at = NOW() WHERE id = :id")
                .param("status", JobStatus.PROCESSING.value())
                .param("id", id)
                .update();
    }

    public void markDone(long id, String filePath) {
        jdbcClient.sql("UPDATE excel_job SET status = :status, completed_at = NOW(), file_path = :filePath WHERE id = :id")
                .param("status", JobStatus.DONE.value())
                .param("filePath", filePath)
                .param("id", id)
                .update();
    }

    public void markFailed(long id, String errorMessage) {
        jdbcClient.sql("UPDATE excel_job SET status = :status, completed_at = NOW(), error_message = :errorMessage WHERE id = :id")
                .param("status", JobStatus.FAILED.value())
                .param("errorMessage", errorMessage)
                .param("id", id)
                .update();
    }

    public ExcelJob findById(long id) {
        return jdbcClient.sql("SELECT * FROM excel_job WHERE id = :id")
                .param("id", id)
                .query(ExcelJob.class)
                .single();
    }

    public List<ExcelJob> findBefore(long cursor, int limit) {
        return jdbcClient.sql("SELECT * FROM excel_job WHERE id < :cursor ORDER BY id DESC LIMIT :limit")
                .param("cursor", cursor)
                .param("limit", limit)
                .query(ExcelJob.class)
                .list();
    }
}
