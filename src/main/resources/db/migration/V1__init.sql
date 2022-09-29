CREATE TABLE app_user
(
    id              UUID NOT NULL,
    username        VARCHAR(255),
    name            VARCHAR(255),
    pass            VARCHAR(255),
    roles           VARCHAR(255),
    theme           VARCHAR(255),
    profile_picture bytea,
    CONSTRAINT pk_app_user PRIMARY KEY (id)
);

CREATE TABLE category
(
    id          UUID NOT NULL,
    bezeichnung VARCHAR(255),
    info        VARCHAR(1024),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE tax_period
(
    id             UUID    NOT NULL,
    geschaeftsjahr INTEGER NOT NULL,
    von            date,
    bis            date,
    status         VARCHAR(255),
    CONSTRAINT pk_tax_period PRIMARY KEY (id)
);

CREATE TABLE accounting
(
    id              UUID NOT NULL,
    buchungsdatum   date,
    einnahme        DOUBLE PRECISION,
    eingangsdatum   date,
    ausgabe         DOUBLE PRECISION,
    beleg_nr        VARCHAR(255),
    text            VARCHAR(255),
    accounting_type VARCHAR(255),
    category_id     UUID,
    tax_period_id   UUID NOT NULL,
    CONSTRAINT pk_accounting PRIMARY KEY (id)
);

ALTER TABLE accounting
    ADD CONSTRAINT FK_ACCOUNTING_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE accounting
    ADD CONSTRAINT FK_ACCOUNTING_ON_TAX_PERIOD FOREIGN KEY (tax_period_id) REFERENCES tax_period (id);