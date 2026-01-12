--liquibase formatted sql

--changeset santiago:001-create-clientes-table
CREATE TABLE clientes (
                          id BIGSERIAL PRIMARY KEY,
                          tipo_identificacion VARCHAR(20) NOT NULL,
                          numero_identificacion VARCHAR(20) NOT NULL UNIQUE,
                          nombres VARCHAR(100) NOT NULL,
                          apellido VARCHAR(100) NOT NULL,
                          correo_electronico VARCHAR(100) NOT NULL,
                          fecha_nacimiento DATE NOT NULL,
                          fecha_creacion TIMESTAMP NOT NULL,
                          fecha_modificacion TIMESTAMP NOT NULL
);

CREATE INDEX idx_clientes_numero_identificacion ON clientes(numero_identificacion);

--rollback DROP TABLE clientes;