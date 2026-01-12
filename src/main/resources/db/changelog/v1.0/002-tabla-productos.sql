--liquibase formatted sql

--changeset santiago:002-create-productos-table
CREATE TABLE productos (
                           id BIGSERIAL PRIMARY KEY,
                           tipo_cuenta VARCHAR(20) NOT NULL,
                           numero_cuenta VARCHAR(10) NOT NULL UNIQUE,
                           estado VARCHAR(20) NOT NULL,
                           saldo NUMERIC(15,2) NOT NULL,
                           exenta_gmf BOOLEAN NOT NULL,
                           fecha_creacion TIMESTAMP NOT NULL,
                           fecha_modificacion TIMESTAMP NOT NULL,
                           cliente_id BIGINT NOT NULL,
                           CONSTRAINT fk_productos_cliente FOREIGN KEY (cliente_id)
                               REFERENCES clientes(id) ON DELETE RESTRICT
);

CREATE INDEX idx_productos_numero_cuenta ON productos(numero_cuenta);
CREATE INDEX idx_productos_cliente_id ON productos(cliente_id);

--rollback DROP TABLE productos;