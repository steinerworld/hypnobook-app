CREATE TABLE buchhaltung
(
    id            BIGINT NOT NULL,
    buchungsdatum date,
    einnahme      DOUBLE PRECISION,
    eingangsdatum date,
    ausgabe       DOUBLE PRECISION,
    beleg_nr      VARCHAR(255),
    text          VARCHAR(255),
    CONSTRAINT pk_buchhaltung PRIMARY KEY (id)
);

CREATE TABLE kategorie
(
    id          BIGINT NOT NULL,
    name        VARCHAR(255),
    bezeichnung VARCHAR(255),
    CONSTRAINT pk_kategorie PRIMARY KEY (id)
);

CREATE TABLE steuerperiode
(
    id                BIGINT NOT NULL,
    jahresbezeichnung VARCHAR(255),
    von               date,
    bis               date,
    status            VARCHAR(255),
    CONSTRAINT pk_steuerperiode PRIMARY KEY (id)
);