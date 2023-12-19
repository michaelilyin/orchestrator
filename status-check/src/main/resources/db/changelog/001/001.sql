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
  id         BIGSERIAL PRIMARY KEY,
  type       VARCHAR                  NOT NULL REFERENCES network_state (type),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

--changeset michael:add-status-column-to-network-log context:main
ALTER TABLE network_state_check_log
  ADD COLUMN IF NOT EXISTS status VARCHAR;

--changeset michael:rename-table context:main splitStatements:false

-- take into account that deploy will be on single node with downtime (no back compatibility is required)
ALTER TABLE IF EXISTS network_state
  RENAME TO check_state;
ALTER TABLE IF EXISTS network_state_check_log
  RENAME TO check_log;

MERGE INTO check_state (type, status)
  KEY (type)
  SELECT 'NETWORK_LOCAL', status
  FROM check_state
  WHERE type = 'LOCAL';

DELETE
FROM check_state
WHERE type = 'LOCAL';

UPDATE check_log
SET type = 'NETWORK_LOCAL'
WHERE type = 'LOCAL';

--changeset michael:log-details context:main
CREATE TABLE IF NOT EXISTS check_log_details
(
  id     BIGSERIAL PRIMARY KEY,
  log_id BIGINT REFERENCES check_log (id),
  data JSON
);
