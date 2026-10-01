SET NAMES utf8mb4;
SET SESSION cte_max_recursion_depth = 100000;

INSERT INTO orders (user_name, product_name, category, amount, status, order_date)
WITH RECURSIVE seq(n) AS (
    SELECT 1 UNION ALL SELECT n + 1 FROM seq WHERE n < 100000
)
SELECT
    CONCAT(ELT(1 + FLOOR(RAND() * 10), '김', '이', '박', '최', '정', '강', '조', '윤', '장', '임'),
           ELT(1 + FLOOR(RAND() * 10), '민준', '서연', '도윤', '지우', '하준', '서윤', '시우', '하은', '주원', '지민')),
    CASE n % 4
        WHEN 0 THEN ELT(1 + (n DIV 4) % 3, '리프트 1일권', '리프트 반일권', '스키 장비 렌탈')
        WHEN 1 THEN ELT(1 + (n DIV 4) % 3, '워터파크 종일권', '워터파크 오후권', '카바나 대여')
        WHEN 2 THEN ELT(1 + (n DIV 4) % 3, '스탠다드 룸', '디럭스 룸', '스위트 룸')
        ELSE        ELT(1 + (n DIV 4) % 3, '골프 18홀', '골프 9홀', '골프 연습장')
    END,
    ELT(1 + n % 4, '스키', '워터파크', '숙박', '골프'),
    (10 + FLOOR(RAND() * 491)) * 1000,
    ELT(1 + FLOOR(RAND() * 3), 'confirmed', 'cancelled', 'pending'),
    TIMESTAMPADD(SECOND, -FLOOR(RAND() * 365 * 24 * 3600), NOW())
FROM seq;
