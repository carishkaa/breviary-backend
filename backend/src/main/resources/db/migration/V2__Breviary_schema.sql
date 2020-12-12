-- Breviary schema

CREATE TYPE SEX AS ENUM (
    'MALE',
    'FEMALE'
    );

-- Create table patients

CREATE SEQUENCE patients_seq INCREMENT 50;

CREATE TABLE patients
(
    id            BIGINT               DEFAULT NEXTVAL('patients_seq') NOT NULL,
    first_name    TEXT,
    last_name     TEXT,
    date_of_birth DATE,
    height        NUMERIC,
    sex           SEX,
    active        BOOLEAN     NOT NULL DEFAULT FALSE, -- default false, false means the patient is in IKEM DB but not in app DB
    other_params  jsonb,
    created       timestamptz NOT NULL,
    deleted       timestamptz,
    updated       timestamptz NOT NULL,
    created_by    BIGINT      NOT NULL,
    CONSTRAINT pk_patients__id PRIMARY KEY (id),
    CONSTRAINT fk_patients__users__user_id FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TRIGGER tgr_patients_set_created
    BEFORE INSERT
    ON patients
    FOR EACH ROW
EXECUTE PROCEDURE set_created();

CREATE TRIGGER tgr_patients_set_updated
    BEFORE UPDATE
    ON patients
    FOR EACH ROW
EXECUTE PROCEDURE set_updated();

-- Create table heparin_patients

CREATE SEQUENCE heparin_patients_seq INCREMENT 50;

CREATE TABLE heparin_patients
(
    id                  BIGINT DEFAULT NEXTVAL('heparin_patients_seq') NOT NULL,
    target_aptt_low     NUMERIC                                        NOT NULL,
    target_aptt_high    NUMERIC                                        NOT NULL,
    solution_heparin_iu NUMERIC                                        NOT NULL,
    solution_ml         NUMERIC                                        NOT NULL,
    weight              NUMERIC                                        NOT NULL,
    patient_id          BIGINT                                         NOT NULL,
    created             timestamptz                                    NOT NULL,
    deleted             timestamptz,
    updated             timestamptz                                    NOT NULL,
    created_by          BIGINT                                         NOT NULL,
    CONSTRAINT pk_heparin_patients__id PRIMARY KEY (id),
    CONSTRAINT fk_heparin_patients__patients__patient_id FOREIGN KEY (patient_id) REFERENCES patients (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_heparin_patients__users__user_id FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TRIGGER tgr_heparin_patients_set_created
    BEFORE INSERT
    ON heparin_patients
    FOR EACH ROW
EXECUTE PROCEDURE set_created();

CREATE TRIGGER tgr_heparin_patients_set_updated
    BEFORE UPDATE
    ON heparin_patients
    FOR EACH ROW
EXECUTE PROCEDURE set_updated();

-- Create table insulin_patients

CREATE SEQUENCE insulin_patients_seq INCREMENT 50;

CREATE TABLE insulin_patients
(
    id              BIGINT DEFAULT NEXTVAL('insulin_patients_seq') NOT NULL,
    tddi            NUMERIC                                        NOT NULL,
    target_glycemia NUMERIC                                        NOT NULL,
    patient_id      BIGINT                                         NOT NULL,
    created         timestamptz                                    NOT NULL,
    deleted         timestamptz,
    updated         timestamptz                                    NOT NULL,
    created_by      BIGINT                                         NOT NULL,
    CONSTRAINT pk_insulin_patients__id PRIMARY KEY (id),
    CONSTRAINT fk_insulin_patients__patients__patient_id FOREIGN KEY (patient_id) REFERENCES patients (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_insulin_patients__users__user_id FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TRIGGER tgr_insulin_patients_set_created
    BEFORE INSERT
    ON insulin_patients
    FOR EACH ROW
EXECUTE PROCEDURE set_created();

CREATE TRIGGER tgr_insulin_patients_set_updated
    BEFORE UPDATE
    ON insulin_patients
    FOR EACH ROW
EXECUTE PROCEDURE set_updated();

-- Create table aptt_values

CREATE SEQUENCE aptt_values_seq INCREMENT 50;

CREATE TABLE aptt_values
(
    id                 BIGINT DEFAULT NEXTVAL('aptt_values_seq') NOT NULL,
    heparin_patient_id BIGINT                                    NOT NULL,
    value              NUMERIC                                   NOT NULL,
    created            timestamptz                               NOT NULL,
    deleted            timestamptz,
    updated            timestamptz                               NOT NULL,
    created_by         BIGINT                                    NOT NULL,
    CONSTRAINT pk_aptt_values__id PRIMARY KEY (id),
    CONSTRAINT fk_aptt_values__heparin_patients__patient_id FOREIGN KEY (heparin_patient_id) REFERENCES heparin_patients (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_aptt_values__users__user_id FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TRIGGER tgr_aptt_values_set_created
    BEFORE INSERT
    ON aptt_values
    FOR EACH ROW
EXECUTE PROCEDURE set_created();

CREATE TRIGGER tgr_aptt_values_set_updated
    BEFORE UPDATE
    ON aptt_values
    FOR EACH ROW
EXECUTE PROCEDURE set_updated();

-- Create table heparin_dosages

CREATE SEQUENCE heparin_dosages_seq INCREMENT 50;

CREATE TABLE heparin_dosages
(
    id                        BIGINT DEFAULT NEXTVAL('heparin_dosages_seq') NOT NULL,
    heparin_patient_id        BIGINT                                        NOT NULL,
    dosage_heparin_continuous NUMERIC                                       NOT NULL,
    dosage_heparin_bolus      NUMERIC                                       NOT NULL,
    created                   timestamptz                                   NOT NULL,
    deleted                   timestamptz,
    updated                   timestamptz                                   NOT NULL,
    created_by                BIGINT                                        NOT NULL,
    CONSTRAINT pk_heparin_dosages__id PRIMARY KEY (id),
    CONSTRAINT fk_heparin_dosages__heparin_patients__patient_id FOREIGN KEY (heparin_patient_id) REFERENCES heparin_patients (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_heparin_dosages__users__user_id FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TRIGGER tgr_heparin_dosages_set_created
    BEFORE INSERT
    ON heparin_dosages
    FOR EACH ROW
EXECUTE PROCEDURE set_created();

CREATE TRIGGER tgr_heparin_dosages_set_updated
    BEFORE UPDATE
    ON heparin_dosages
    FOR EACH ROW
EXECUTE PROCEDURE set_updated();

-- Create table glycemia_values

CREATE SEQUENCE glycemia_values_seq INCREMENT 50;

CREATE TABLE glycemia_values
(
    id                 BIGINT DEFAULT NEXTVAL('glycemia_values_seq') NOT NULL,
    insulin_patient_id BIGINT                                        NOT NULL,
    value              NUMERIC                                       NOT NULL,
    created            timestamptz                                   NOT NULL,
    deleted            timestamptz,
    updated            timestamptz                                   NOT NULL,
    created_by         BIGINT                                        NOT NULL,
    CONSTRAINT pk_glycemia_values__id PRIMARY KEY (id),
    CONSTRAINT fk_glycemia_values__insulin_patients__patient_id FOREIGN KEY (insulin_patient_id) REFERENCES insulin_patients (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_glycemia_values__users__user_id FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TRIGGER tgr_glycemia_values_set_created
    BEFORE INSERT
    ON glycemia_values
    FOR EACH ROW
EXECUTE PROCEDURE set_created();

CREATE TRIGGER tgr_glycemia_values_set_updated
    BEFORE UPDATE
    ON glycemia_values
    FOR EACH ROW
EXECUTE PROCEDURE set_updated();

-- Create table carbohydrate_intake_values

CREATE SEQUENCE carbohydrate_intake_values_seq INCREMENT 50;

CREATE TABLE carbohydrate_intake_values
(
    id                 BIGINT DEFAULT NEXTVAL('carbohydrate_intake_values_seq') NOT NULL,
    insulin_patient_id BIGINT                                                   NOT NULL,
    value              NUMERIC                                                  NOT NULL,
    created            timestamptz                                              NOT NULL,
    deleted            timestamptz,
    updated            timestamptz                                              NOT NULL,
    created_by         BIGINT                                                   NOT NULL,
    CONSTRAINT pk_carbohydrate_intake_values__id PRIMARY KEY (id),
    CONSTRAINT fk_carbohydrate_intake_values__insulin_patients__patient_id FOREIGN KEY (insulin_patient_id) REFERENCES insulin_patients (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_carbohydrate_intake_values__users__user_id FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TRIGGER tgr_carbohydrate_intake_values_set_created
    BEFORE INSERT
    ON carbohydrate_intake_values
    FOR EACH ROW
EXECUTE PROCEDURE set_created();

CREATE TRIGGER tgr_carbohydrate_intake_values_set_updated
    BEFORE UPDATE
    ON carbohydrate_intake_values
    FOR EACH ROW
EXECUTE PROCEDURE set_updated();

-- Create table insulin_dosages

CREATE SEQUENCE insulin_dosages_seq INCREMENT 50;

CREATE TABLE insulin_dosages
(
    id                 BIGINT DEFAULT NEXTVAL('insulin_dosages_seq') NOT NULL,
    insulin_patient_id BIGINT                                        NOT NULL,
    dosage_insulin     NUMERIC                                       NOT NULL,
    created            timestamptz                                   NOT NULL,
    deleted            timestamptz,
    updated            timestamptz                                   NOT NULL,
    created_by         BIGINT                                        NOT NULL,
    CONSTRAINT pk_insulin_dosages__id PRIMARY KEY (id),
    CONSTRAINT fk_insulin_dosages__insulin_patients__patient_id FOREIGN KEY (insulin_patient_id) REFERENCES insulin_patients (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_insulin_dosages__users__user_id FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TRIGGER tgr_insulin_dosages_set_created
    BEFORE INSERT
    ON insulin_dosages
    FOR EACH ROW
EXECUTE PROCEDURE set_created();

CREATE TRIGGER tgr_insulin_dosages_set_updated
    BEFORE UPDATE
    ON insulin_dosages
    FOR EACH ROW
EXECUTE PROCEDURE set_updated();
