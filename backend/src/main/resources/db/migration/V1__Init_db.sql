-- Enable Case-insensitive text

CREATE EXTENSION IF NOT EXISTS citext;

-- BEFORE insert function for trigger

CREATE OR REPLACE FUNCTION set_created() RETURNS TRIGGER
AS
$BODY$
BEGIN
    new.created := NOW();
    new.updated := new.created;
    RETURN new;
END;
$BODY$
    LANGUAGE plpgsql;

-- BEFORE update function for trigger

CREATE OR REPLACE FUNCTION set_updated() RETURNS TRIGGER
AS
$BODY$
BEGIN
    new.updated := NOW();
    RETURN new;
END;
$BODY$
    LANGUAGE plpgsql;

-- Create base schema


-- Create table users
CREATE SEQUENCE users_seq INCREMENT 50;

CREATE TABLE users
(
    id       BIGINT               DEFAULT NEXTVAL('users_seq') NOT NULL,
    created  timestamptz NOT NULL,
    deleted  timestamptz,
    updated  timestamptz NOT NULL,
    password TEXT        NOT NULL,
    username citext      NOT NULL,
    active   bool        NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_users__id PRIMARY KEY (id),
    CONSTRAINT uk_users__username UNIQUE (username)
);

CREATE TRIGGER tgr_users_set_created
    BEFORE INSERT
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE set_created();

CREATE TRIGGER tgr_users_set_updated
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE set_updated();

-- Create table roles

CREATE TYPE USER_ROLE AS ENUM (
    'ADMINISTRATOR',
    'SERVICE',
    'DOCTOR',
    'NURSE'
    );

CREATE SEQUENCE roles_seq INCREMENT 50;

CREATE TABLE roles
(
    id      BIGINT DEFAULT NEXTVAL('roles_seq') NOT NULL,
    created timestamptz                         NOT NULL,
    deleted timestamptz,
    updated timestamptz                         NOT NULL,
    name    USER_ROLE                           NOT NULL,
    CONSTRAINT pk_roles__id PRIMARY KEY (id),
    CONSTRAINT uk_roles__name UNIQUE (name)
);

CREATE TRIGGER tgr_roles_set_created
    BEFORE INSERT
    ON roles
    FOR EACH ROW
EXECUTE PROCEDURE set_created();

CREATE TRIGGER tgr_roles_set_updated
    BEFORE UPDATE
    ON roles
    FOR EACH ROW
EXECUTE PROCEDURE set_updated();

-- Create table user_roles

CREATE SEQUENCE user_roles_seq INCREMENT 50;

CREATE TABLE user_roles
(
    id      BIGINT DEFAULT NEXTVAL('user_roles_seq') NOT NULL,
    created timestamptz                              NOT NULL,
    deleted timestamptz,
    updated timestamptz                              NOT NULL,
    user_id BIGINT                                   NOT NULL,
    role_id BIGINT                                   NOT NULL,
    CONSTRAINT pk_user_roles__id PRIMARY KEY (id),
    CONSTRAINT fk_user_roles__users__user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_user_roles__roles__role_id FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uk_user_roles__user_id__role_id UNIQUE (user_id, role_id)
);

CREATE TRIGGER tgr_user_roles_set_created
    BEFORE INSERT
    ON user_roles
    FOR EACH ROW
EXECUTE PROCEDURE set_created();

CREATE TRIGGER tgr_user_roles_set_updated
    BEFORE UPDATE
    ON user_roles
    FOR EACH ROW
EXECUTE PROCEDURE set_updated();

-- Create password reset table

CREATE SEQUENCE password_resets_seq INCREMENT 50;

CREATE TABLE password_resets
(
    id          BIGINT DEFAULT NEXTVAL('password_resets_seq') NOT NULL,
    created     timestamptz                                   NOT NULL,
    deleted     timestamptz,
    updated     timestamptz                                   NOT NULL,
    token       TEXT                                          NOT NULL,
    valid_until timestamptz                                   NOT NULL,
    user_id     BIGINT                                        NOT NULL,
    CONSTRAINT pk_password_resets__id PRIMARY KEY (id),
    CONSTRAINT fk_password_resets__users__user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uk_password_resets__user_id__token UNIQUE (token)
);

CREATE TRIGGER tgr_password_resets_set_created
    BEFORE INSERT
    ON password_resets
    FOR EACH ROW
EXECUTE PROCEDURE set_created();

CREATE TRIGGER tgr_password_resets_set_updated
    BEFORE UPDATE
    ON password_resets
    FOR EACH ROW
EXECUTE PROCEDURE set_updated();

-- Insert roles

INSERT INTO roles (name)
VALUES ('ADMINISTRATOR');
INSERT INTO roles (name)
VALUES ('SERVICE');
INSERT INTO roles (name)
VALUES ('DOCTOR');
INSERT INTO roles (name)
VALUES ('NURSE');
