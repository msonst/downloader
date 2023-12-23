CREATE SCHEMA IF NOT EXISTS cs;
SET SCHEMA cs;
CREATE TABLE download (id INT auto_increment, url VARCHAR(256), cookie VARCHAR(50), file_path VARCHAR(256), status INT);