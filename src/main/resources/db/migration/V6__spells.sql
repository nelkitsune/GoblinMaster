-- V3__spells.sql

CREATE TABLE spell_schools (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               code VARCHAR(64) NOT NULL UNIQUE,  -- e.g. EVOCATION
                               name VARCHAR(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE spell_classes (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               code VARCHAR(64) NOT NULL UNIQUE,  -- e.g. WIZARD, SORCERER, MAGUS, OCCULTIST, BLOODRAGER
                               name VARCHAR(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE spells (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(256) NOT NULL,              -- "Bola de fuego"
                        original_name VARCHAR(256) NULL,         -- "Fireball"
                        school_id BIGINT NOT NULL,
                        casting_time VARCHAR(128) NULL,          -- "1 acción estándar"
                        range_text VARCHAR(128) NULL,            -- "largo (400 pies + 40 pies/nível)" (texto)
                        area_text VARCHAR(128) NULL,             -- "20 pies de radio"
                        duration_text VARCHAR(128) NULL,         -- "instantánea"
                        saving_throw VARCHAR(128) NULL,          -- "Reflejos mitad"
                        spell_resistance TINYINT(1) NOT NULL DEFAULT 0,  -- si/no
                        components_v TINYINT(1) NOT NULL DEFAULT 0,
                        components_s TINYINT(1) NOT NULL DEFAULT 0,
                        components_m TINYINT(1) NOT NULL DEFAULT 0,
                        material_desc VARCHAR(512) NULL,         -- "bolita de guano de murciélago y azufre"
                        source VARCHAR(128) NULL,                -- "Reglas Básicas pág. 250"
                        description MEDIUMTEXT NULL,
                        CONSTRAINT fk_spells_school FOREIGN KEY (school_id) REFERENCES spell_schools(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_spells_school ON spells(school_id);
CREATE INDEX idx_spells_name ON spells(name);

-- N:M con nivel por clase
CREATE TABLE spell_class_levels (
                                    spell_id BIGINT NOT NULL,
                                    class_id BIGINT NOT NULL,
                                    level TINYINT NOT NULL,  -- 0..9
                                    PRIMARY KEY (spell_id, class_id),
                                    CONSTRAINT fk_scl_spell FOREIGN KEY (spell_id) REFERENCES spells(id),
                                    CONSTRAINT fk_scl_class FOREIGN KEY (class_id) REFERENCES spell_classes(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Semillas mínimas
INSERT INTO spell_schools(code, name) VALUES
                                          ('EVOCATION','Evocación'), ('ABJURATION','Abjuración'),
                                          ('CONJURATION','Conjuración'), ('DIVINATION','Adivinación'),
                                          ('ENCHANTMENT','Encantamiento'), ('ILLUSION','Ilusión'),
                                          ('NECROMANCY','Nigromancia'), ('TRANSMUTATION','Transmutación');

INSERT INTO spell_classes(code, name) VALUES
                                            ('WIZARD','Mago'), ('SORCERER','Hechicero'),
                                            ('MAGUS','Magus'), ('OCCULTIST','Ocultista'),
                                            ('BLOODRAGER','Rabioso de sangre'),('BARD','Bardo'),
                                            ('CLERIC','Clérigo'), ('DRUID','Druida'),
                                            ('INQUISITOR','Inquisidor'), ('SHAMAN','Chamán'),
                                            ('ALCHEMIST','Alquimista'), ('SUMMONER','Convocador'),
                                            ('WITCH','Brujo'), ('PSIONICIST','Psiónico'),
                                            ('ANTIPALADIN','Antipaladín'), ('PALADIN','Paladín'),
                                            ('RANGER','Explorador');
