DROP TABLE IF EXISTS comment;

DROP TABLE IF EXISTS til_tag;

DROP TABLE IF EXISTS tag;

DROP TABLE IF EXISTS til;

DROP TABLE IF EXISTS oauth;

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

CREATE TABLE oauth
(
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    profile_id         BIGINT       NOT NULL,
    uuid               CHAR(36)     NOT NULL,
    identifier         VARCHAR(255) NOT NULL,
    provider           VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_date DATETIME     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (profile_id)
        REFERENCES profile (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    UNIQUE INDEX (uuid),
    UNIQUE INDEX (identifier)
) ENGINE=InnoDB;

CREATE TABLE til
(
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    profile_id         BIGINT       NOT NULL,
    uuid               CHAR(36)     NOT NULL,
    title              VARCHAR(255) NOT NULL,
    content            LONGTEXT     NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_date DATETIME     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (profile_id)
        REFERENCES profile (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    UNIQUE INDEX (uuid)
) ENGINE=InnoDB;

CREATE TABLE tag
(
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    uuid               CHAR(36)     NOT NULL,
    name               VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_date DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX (uuid),
    UNIQUE INDEX (name)
) ENGINE=InnoDB;

CREATE TABLE til_tag
(
    til_id             BIGINT   NOT NULL,
    tag_id             BIGINT   NOT NULL,
    uuid               CHAR(36) NOT NULL,
    created_date       DATETIME NOT NULL,
    last_modified_date DATETIME NOT NULL,
    PRIMARY KEY (til_id, tag_id),
    FOREIGN KEY (til_id)
        REFERENCES til (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (tag_id)
        REFERENCES tag (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    UNIQUE INDEX (uuid)
) ENGINE=InnoDB;

CREATE TABLE comment
(
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    profile_id         BIGINT       NOT NULL,
    til_id             BIGINT       NOT NULL,
    uuid               CHAR(36)     NOT NULL,
    content            VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_date DATETIME     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (profile_id)
        REFERENCES profile (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (til_id)
        REFERENCES til (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    UNIQUE INDEX (uuid)
) ENGINE=InnoDB;