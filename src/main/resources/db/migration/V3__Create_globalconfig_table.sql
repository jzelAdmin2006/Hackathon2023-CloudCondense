CREATE TABLE global_config
(
    ID INT UNIQUE,
    schedule_rate INT NOT NULL DEFAULT 3600000,
    PRIMARY KEY(ID),
	CHECK (ID = 1)
)

INSERT INTO global_config (ID)
VALUES (1)
