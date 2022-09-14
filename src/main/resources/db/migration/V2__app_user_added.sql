CREATE SEQUENCE APP_USER_SEQ START WITH 1000 INCREMENT BY 50;

CREATE TABLE app_user
(
    id              BIGINT NOT NULL,
    username        VARCHAR(255),
    name            VARCHAR(255),
    pass            VARCHAR(255),
    roles           VARCHAR(255),
    profile_picture bytea,
    CONSTRAINT pk_app_user PRIMARY KEY (id)
);