CREATE TABLE global_config
(
    ID INT UNIQUE,
    schedule_rate INT NOT NULL,
    PRIMARY KEY(ID),
	CHECK (ID = 1)
)

INSERT INTO global_config
VALUES (1, 3600000)
