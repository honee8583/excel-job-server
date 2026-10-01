SET NAMES utf8mb4;

CREATE TABLE orders (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    user_name    VARCHAR(50)  NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    category     VARCHAR(50)  NOT NULL,
    amount       INT          NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    order_date   TIMESTAMP    NOT NULL
);

CREATE TABLE excel_job (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    status       VARCHAR(20)  NOT NULL,
    requested_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at   TIMESTAMP    NULL,
    completed_at TIMESTAMP    NULL,
    file_path    VARCHAR(255) NULL,
    error_message VARCHAR(1000) NULL
);
