CREATE SEQUENCE APP_USER_SEQ START WITH 1 INCREMENT BY 1;
CREATE TABLE app_user
(
    id              BIGINT NOT NULL,
    username        VARCHAR(255),
    name            VARCHAR(255),
    pass            VARCHAR(255),
    roles           VARCHAR(255),
    theme           VARCHAR(255),
    profile_picture bytea,
    CONSTRAINT pk_app_user PRIMARY KEY (id)
);

CREATE SEQUENCE KATEGORIE_SEQ START WITH 1 INCREMENT BY 1;
CREATE TABLE category
(
    id           BIGINT NOT NULL,
    name         VARCHAR(255),
    beschreibung VARCHAR(1024),
    CONSTRAINT pk_kategorie PRIMARY KEY (id)
);

CREATE SEQUENCE PERIODE_SEQ START WITH 1 INCREMENT BY 1;
CREATE TABLE steuerperiode
(
    id                BIGINT NOT NULL,
    jahresbezeichnung VARCHAR(255),
    von               date,
    bis               date,
    status            VARCHAR(255),
    CONSTRAINT pk_steuerperiode PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS buchung_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE accounting
(
    id               BIGINT NOT NULL,
    buchungsdatum    date,
    einnahme         DOUBLE PRECISION,
    eingangsdatum    date,
    ausgabe          DOUBLE PRECISION,
    beleg_nr         VARCHAR(255),
    text             VARCHAR(255),
    buchungtype      VARCHAR(255),
    kategorie_id     BIGINT,
    steuerperiode_id BIGINT NOT NULL,
    CONSTRAINT pk_buchung PRIMARY KEY (id)
);

ALTER TABLE accounting
    ADD CONSTRAINT FK_BUCHUNG_ON_KATEGORIE FOREIGN KEY (kategorie_id) REFERENCES category (id);

ALTER TABLE accounting
    ADD CONSTRAINT FK_BUCHUNG_ON_STEUERPERIODE FOREIGN KEY (steuerperiode_id) REFERENCES steuerperiode (id);

