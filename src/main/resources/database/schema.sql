DROP TABLE IF EXISTS comment;

DROP TABLE IF EXISTS til_tag;

DROP TABLE IF EXISTS tag;

DROP TABLE IF EXISTS til;

DROP TABLE IF EXISTS til_sequence;

DROP TABLE IF EXISTS auth_token;

DROP TABLE IF EXISTS account;

DROP TABLE IF EXISTS profile;

CREATE TABLE profile
(
    id                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
    nickname           VARCHAR(20)  NOT NULL,
    picture_url        VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_date DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (nickname)
) ENGINE=InnoDB;

CREATE TABLE account
(
    profile_id         BIGINT(20)   NOT NULL,
    email              VARCHAR(255) NOT NULL,
    password           VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_date DATETIME     NOT NULL,
    PRIMARY KEY (profile_id),
    FOREIGN KEY (profile_id)
        REFERENCES profile (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    UNIQUE (email)
) ENGINE=InnoDB;

CREATE TABLE auth_token
(
    id                 BIGINT(20) NOT NULL AUTO_INCREMENT,
    profile_id         BIGINT(20) NOT NULL,
    refresh_token      TEXT       NOT NULL,
    is_revoked         BOOLEAN    NOT NULL,
    created_date       DATETIME   NOT NULL,
    last_modified_date DATETIME   NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (profile_id)
        REFERENCES profile (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    UNIQUE (refresh_token)
) ENGINE=InnoDB;

CREATE TABLE til_sequence
(
    profile_id         BIGINT(20) NOT NULL,
    sequence           BIGINT(20) NOT NULL,
    version            INT(11)    NOT NULL,
    created_date       DATETIME   NOT NULL,
    last_modified_date DATETIME   NOT NULL,
    PRIMARY KEY (profile_id),
    FOREIGN KEY (profile_id)
        REFERENCES profile (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE til
(
    id                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
    profile_id         BIGINT(20)   NOT NULL,
    sequence           BIGINT(20)   NOT NULL,
    title              VARCHAR(255) NOT NULL,
    content_markdown   MEDIUMTEXT   NOT NULL,
    hits               INT(11)      NOT NULL,
    is_deleted         BOOLEAN      NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_date DATETIME     NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (profile_id)
        REFERENCES profile (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE tag
(
    id                 BIGINT(20)   NOT NULL AUTO_INCREMENT,
    name               VARCHAR(255) NOT NULL,
    created_date       DATETIME     NOT NULL,
    last_modified_date DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
) ENGINE=InnoDB;

CREATE TABLE til_tag
(
    til_id             BIGINT(20) NOT NULL,
    tag_id             BIGINT(20) NOT NULL,
    created_date       DATETIME   NOT NULL,
    last_modified_date DATETIME   NOT NULL,
    PRIMARY KEY (til_id, tag_id),
    FOREIGN KEY (til_id)
        REFERENCES til (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (tag_id)
        REFERENCES tag (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE comment
(
    id                 BIGINT(20) NOT NULL AUTO_INCREMENT,
    profile_id         BIGINT(20) NOT NULL,
    til_id             BIGINT(20) NOT NULL,
    content            TEXT       NOT NULL,
    is_deleted         BOOLEAN    NOT NULL,
    created_date       DATETIME   NOT NULL,
    last_modified_date DATETIME   NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (profile_id)
        REFERENCES profile (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (til_id)
        REFERENCES til (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB;
