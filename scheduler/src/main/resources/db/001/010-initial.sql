-- liquibase formatted sql

--changeset michael:initial context:main
SELECT 1;

--changeset michael:create-schedule-table
CREATE TABLE IF NOT EXISTS schedules (
  id BIGSERIAL PRIMARY KEY,
  title VARCHAR NOT NULL,
  code VARCHAR NOT NULL UNIQUE
);

INSERT INTO schedules (title, code)
VALUES ('Internal network check', 'INTERNAL_NETWORK_CHECK'),
       ('Media volumes check', 'MEDIA_VOLUMES_CHECK');
