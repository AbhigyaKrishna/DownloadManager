CREATE TABLE history
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    file_url VARCHAR(255),
    file_name VARCHAR(255),
    file_location VARCHAR(255),
    file_datetime DATETIME,
    file_size BIGINT,
    file_status VARCHAR(20),
    CHECK file_status IN ('Done', 'Error', 'Pending')
);