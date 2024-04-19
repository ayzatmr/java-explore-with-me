DROP TABLE IF EXISTS endpointhits;

CREATE TABLE IF NOT EXISTS endpointhits
(
    id
    BIGINT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL
    PRIMARY
    KEY,
    app
    VARCHAR
(
    255
) NOT NULL,
    uri VARCHAR
(
    255
) NOT NULL,
    ip VARCHAR
(
    11
) NOT NULL,
    created TIMESTAMP
    );