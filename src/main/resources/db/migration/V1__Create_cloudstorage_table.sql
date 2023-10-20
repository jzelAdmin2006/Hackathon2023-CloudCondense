CREATE TABLE cloud_storage
(
    ID INT IDENTITY,
    [name] VARCHAR(MAX) NOT NULL,
    [type] VARCHAR(100) NOT NULL,
    username VARCHAR(MAX) NOT NULL,
    [password] VARCHAR(MAX) NOT NULL,
    created DATETIME DEFAULT GETDATE(),
    PRIMARY KEY(ID)
)
