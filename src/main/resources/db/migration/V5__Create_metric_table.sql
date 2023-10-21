CREATE TABLE metric
(
    ID INT UNIQUE,
    saved_disk_space NUMERIC(10, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY(ID),
    CHECK (ID = 1)
)

INSERT INTO metric (ID)
VALUES (1)
