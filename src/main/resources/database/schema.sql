CREATE TABLE profile
(
    id                 BIGINT      NOT NULL AUTO_INCREMENT,
    nickname           VARCHAR(20) NOT NULL,
    created_date       DATETIME    NOT NULL,
    last_modified_date DATETIME    NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX (nickname)
) ENGINE=InnoDB;

CREATE TABLE account
(
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    profile_id         BIGINT       NOT NULL,
    email              VARCHAR(255) NOT NULL,
    password           VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_date DATETIME     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (profile_id)
        REFERENCES profile (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    UNIQUE INDEX (email)
) ENGINE=InnoDB;