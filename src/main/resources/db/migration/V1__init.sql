CREATE SEQUENCE BUCHHALTUNG_SEQ START WITH 1000 INCREMENT BY 50;

CREATE TABLE buchhaltung
(
    id               BIGINT NOT NULL,
    buchungsdatum    date,
    einnahme         DOUBLE PRECISION,
    eingangsdatum    date,
    ausgabe          DOUBLE PRECISION,
    beleg_nr         VARCHAR(255),
    text             VARCHAR(255),
    kategorie_id     BIGINT,
    steuerperiode_id BIGINT NOT NULL,
    CONSTRAINT pk_buchhaltung PRIMARY KEY (id)
);

CREATE SEQUENCE KATEGORIE_SEQ START WITH 1000 INCREMENT BY 50;

CREATE TABLE kategorie
(
    id          BIGINT NOT NULL,
    name        VARCHAR(255),
    bezeichnung VARCHAR(255),
    CONSTRAINT pk_kategorie PRIMARY KEY (id)
);

CREATE SEQUENCE PERIODE_SEQ START WITH 1000 INCREMENT BY 50;

CREATE TABLE steuerperiode
(
    id                BIGINT NOT NULL,
    jahresbezeichnung VARCHAR(255),
    von               date,
    bis               date,
    status            VARCHAR(255),
    CONSTRAINT pk_steuerperiode PRIMARY KEY (id)
);

ALTER TABLE buchhaltung
    ADD CONSTRAINT FK_BUCHHALTUNG_ON_KATEGORIE FOREIGN KEY (kategorie_id) REFERENCES kategorie (id);

ALTER TABLE buchhaltung
    ADD CONSTRAINT FK_BUCHHALTUNG_ON_STEUERPERIODE FOREIGN KEY (steuerperiode_id) REFERENCES steuerperiode (id);
