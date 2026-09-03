-- Applied only when Docker initializes a brand-new MySQL data volume.
-- MYSQL_USER is normally created with MySQL 8.4's default caching_sha2_password;
-- this makes the expected authentication method explicit.
ALTER USER 'ipo_user'@'%' IDENTIFIED WITH caching_sha2_password BY 'ipo_password';
GRANT ALL PRIVILEGES ON ipo_analysis.* TO 'ipo_user'@'%';

