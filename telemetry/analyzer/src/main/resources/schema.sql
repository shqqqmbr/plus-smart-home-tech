DROP TABLE IF EXISTS scenario_actions CASCADE;
DROP TABLE IF EXISTS scenario_conditions CASCADE;
DROP TABLE IF EXISTS actions CASCADE;
DROP TABLE IF EXISTS conditions CASCADE;
DROP TABLE IF EXISTS sensors CASCADE;
DROP TABLE IF EXISTS scenarios CASCADE;

CREATE TABLE IF NOT EXISTS scenarios
(
    id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hub_id VARCHAR,
    name   VARCHAR,
    UNIQUE (hub_id, name)
);

CREATE TABLE IF NOT EXISTS sensors
(
    id     VARCHAR PRIMARY KEY,
    hub_id VARCHAR
);

CREATE TABLE IF NOT EXISTS conditions
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type      VARCHAR,
    operation VARCHAR,
    value     INTEGER
);

CREATE TABLE IF NOT EXISTS actions
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type  VARCHAR,
    value INTEGER
);

CREATE TABLE IF NOT EXISTS scenario_conditions
(
    scenario_id  BIGINT REFERENCES scenarios (id),
    sensor_id    VARCHAR REFERENCES sensors (id),
    condition_id BIGINT REFERENCES conditions (id),
    PRIMARY KEY (scenario_id, sensor_id, condition_id)
);

CREATE TABLE IF NOT EXISTS scenario_actions
(
    scenario_id BIGINT REFERENCES scenarios (id),
    sensor_id   VARCHAR REFERENCES sensors (id),
    action_id   BIGINT REFERENCES actions (id),
    PRIMARY KEY (scenario_id, sensor_id, action_id)
);