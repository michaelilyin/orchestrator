-- liquibase formatted sql

--changeset michael:initial context:main
SELECT 1;

--changeset michael:create-network-state-table context:main
CREATE TABLE IF NOT EXISTS network_state
(
    type   VARCHAR PRIMARY KEY,
    status VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS network_state_check_log
(
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR NOT NULL REFERENCES network_state(type),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
