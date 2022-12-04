CREATE USER `pap22Z_z03`@`localhost` IDENTIFIED WITH caching_sha2_password BY 'pap.2022.PAP';
GRANT ALL PRIVILEGES ON *.* TO 'pap22Z_z03'@'localhost' WITH GRANT OPTION;
ALTER USER 'pap22Z_z03'@'localhost' REQUIRE NONE
    WITH MAX_QUERIES_PER_HOUR 0
    MAX_CONNECTIONS_PER_HOUR 0
    MAX_UPDATES_PER_HOUR 0
    MAX_USER_CONNECTIONS 0;
CREATE DATABASE IF NOT EXISTS `pap22Z_z03`;
GRANT ALL PRIVILEGES ON `pap22Z_z03`.* TO 'pap22Z_z03'@'localhost';