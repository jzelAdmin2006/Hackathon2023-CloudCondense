CREATE TABLE global_config
(
    ID INT UNIQUE,
    schedule_rate INT NOT NULL,
    PRIMARY KEY(ID)
)

INSERT INTO global_config
VALUES (1, 3600000)
