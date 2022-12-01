DROP TABLE IF EXISTS verification;

DROP TABLE IF EXISTS account;

DROP TABLE IF EXISTS profile;

CREATE TABLE profile
(
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    uuid               CHAR(36)     NOT NULL,
    nickname           VARCHAR(20)  NOT NULL,
    picture            VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_date DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX (uuid),
    UNIQUE INDEX (nickname)
) ENGINE=InnoDB;

CREATE TABLE account
(
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    profile_id         BIGINT       NOT NULL,
    uuid               CHAR(36)     NOT NULL,
    email              VARCHAR(255) NOT NULL,
    password           VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_date DATETIME     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (profile_id)
        REFERENCES profile (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    UNIQUE INDEX (uuid),
    UNIQUE INDEX (email)
) ENGINE=InnoDB;

CREATE TABLE verification
(
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    uuid               CHAR(36)     NOT NULL,
    email              VARCHAR(255) NOT NULL,
    code               VARCHAR(6)   NOT NULL,
    state              BIT(1)       NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_date DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX (uuid),
    UNIQUE INDEX (email)
) ENGINE=InnoDB;