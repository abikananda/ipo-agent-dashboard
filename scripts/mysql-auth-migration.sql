-- Run as a MySQL administrator when upgrading an existing database to MySQL 8.4.
-- Adjust the host if the application user is not registered as 'ipo_user'@'%'.
ALTER USER 'ipo_user'@'%' IDENTIFIED WITH caching_sha2_password BY 'ipo_password';
GRANT ALL PRIVILEGES ON ipo_analysis.* TO 'ipo_user'@'%';
FLUSH PRIVILEGES;

