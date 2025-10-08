-- Crear las bases de datos para todos los microservicios
CREATE DATABASE IF NOT EXISTS microservice_manager;
CREATE DATABASE IF NOT EXISTS microservice_news;
CREATE DATABASE IF NOT EXISTS microservice_project;
CREATE DATABASE IF NOT EXISTS microservice_voluntary;

-- Otorgar permisos al usuario root
GRANT ALL PRIVILEGES ON microservice_manager.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON microservice_news.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON microservice_project.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON microservice_voluntary.* TO 'root'@'%';

FLUSH PRIVILEGES;