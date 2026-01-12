--liquibase formatted sql

--changeset santiago:003-create-transacciones-table
CREATE TABLE transacciones (
                               id BIGSERIAL PRIMARY KEY,
                               tipo_transaccion VARCHAR(20) NOT NULL,
                               monto NUMERIC(15,2) NOT NULL,
                               descripcion VARCHAR(255),
                               fecha_creacion TIMESTAMP NOT NULL,
                               cuenta_origen_id BIGINT,
                               cuenta_destino_id BIGINT,
                               numero_cuenta_origen VARCHAR(10),
                               numero_cuenta_destino VARCHAR(10),
                               CONSTRAINT fk_transacciones_cuenta_origen FOREIGN KEY (cuenta_origen_id)
                                   REFERENCES productos(id) ON DELETE RESTRICT,
                               CONSTRAINT fk_transacciones_cuenta_destino FOREIGN KEY (cuenta_destino_id)
                                   REFERENCES productos(id) ON DELETE RESTRICT
);

CREATE INDEX idx_transacciones_cuenta_origen ON transacciones(cuenta_origen_id);
CREATE INDEX idx_transacciones_cuenta_destino ON transacciones(cuenta_destino_id);
CREATE INDEX idx_transacciones_fecha_creacion ON transacciones(fecha_creacion);

--rollback DROP TABLE transacciones;