package com.playstory.exceljobserver.service;

import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExcelFileGenerator {

    private static final String[] HEADERS = {"id", "user_name", "product_name", "category", "amount", "status", "order_date"};
    private static final int CHUNK_SIZE = 1_000;

    private final JdbcClient jdbcClient;
    private final Path storageDir;

    public ExcelFileGenerator(JdbcClient jdbcClient, @Value("${excel.storage-dir}") String storageDir) {
        this.jdbcClient = jdbcClient;
        this.storageDir = Path.of(storageDir);
    }

    public Path generate(long jobId) throws IOException {
        Files.createDirectories(storageDir);
        Path target = storageDir.resolve("orders_job_" + jobId + ".xlsx");
        Path temp = storageDir.resolve(target.getFileName() + ".tmp");

        try {
            try (OutputStream os = Files.newOutputStream(temp);
                 Workbook workbook = new Workbook(os, "excel-job-server", "1.0")) {
                Worksheet sheet = workbook.newWorksheet("orders");
                sheet.width(6, 20);
                for (int col = 0; col < HEADERS.length; col++) {
                    sheet.value(0, col, HEADERS[col]);
                }
                writeOrders(sheet);
            }
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            return target;
        } finally {
            Files.deleteIfExists(temp);
        }
    }

    private void writeOrders(Worksheet sheet) throws IOException {
        long lastId = 0;
        int row = 0;
        List<OrderRow> chunk;
        do {
            chunk = jdbcClient.sql("""
                            SELECT id, user_name, product_name, category, amount, status, order_date
                            FROM orders
                            WHERE id > :lastId
                            ORDER BY id
                            LIMIT :limit
                            """)
                    .param("lastId", lastId)
                    .param("limit", CHUNK_SIZE)
                    .query(OrderRow.class)
                    .list();

            for (OrderRow order : chunk) {
                row++;
                sheet.value(row, 0, order.id());
                sheet.value(row, 1, order.userName());
                sheet.value(row, 2, order.productName());
                sheet.value(row, 3, order.category());
                sheet.value(row, 4, order.amount());
                sheet.value(row, 5, order.status());
                sheet.value(row, 6, order.orderDate());
                sheet.style(row, 6).format("yyyy-mm-dd hh:mm:ss").set();
            }
            sheet.flush();

            if (!chunk.isEmpty()) {
                lastId = chunk.getLast().id();
            }
        } while (chunk.size() == CHUNK_SIZE);
    }

    private record OrderRow(
            long id,
            String userName,
            String productName,
            String category,
            int amount,
            String status,
            LocalDateTime orderDate
    ) {
    }
}
