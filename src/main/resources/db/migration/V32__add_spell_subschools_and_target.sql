-- V32__add_spell_subschools_and_target.sql

-- A) Nueva tabla spell_subschools
CREATE TABLE spell_subschools (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    school_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT fk_subschools_school FOREIGN KEY (school_id) REFERENCES spell_schools(id),
    UNIQUE KEY uq_subschools_school_name (school_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE INDEX idx_subschools_school ON spell_subschools(school_id);

-- B) Alterar tabla spells: agregar subschool_id y target
ALTER TABLE spells
    ADD COLUMN subschool_id BIGINT NULL,
    ADD COLUMN target VARCHAR(255) NULL;

ALTER TABLE spells
    ADD CONSTRAINT fk_spells_subschool FOREIGN KEY (subschool_id) REFERENCES spell_subschools(id);

CREATE INDEX idx_spells_subschool ON spells(subschool_id);

-- C) Seeds: insertar sub-escuelas para las escuelas indicadas (siempre que existan)
-- Divination: Escudriñamiento
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Escudriñamiento' FROM spell_schools WHERE code = 'DIVINATION' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Escudriñamiento'
);

-- Conjuration: Llamada, Creación, Curación, Convocación, Teletransporte
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Llamada' FROM spell_schools WHERE code = 'CONJURATION' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Llamada'
);
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Creación' FROM spell_schools WHERE code = 'CONJURATION' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Creación'
);
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Curación' FROM spell_schools WHERE code = 'CONJURATION' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Curación'
);
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Convocación' FROM spell_schools WHERE code = 'CONJURATION' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Convocación'
);
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Teletransporte' FROM spell_schools WHERE code = 'CONJURATION' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Teletransporte'
);

-- Encantamiento: Hechizo, Compulsión
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Hechizo' FROM spell_schools WHERE code = 'ENCHANTMENT' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Hechizo'
);
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Compulsión' FROM spell_schools WHERE code = 'ENCHANTMENT' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Compulsión'
);

-- Ilusión: Quimera, Engaño, Pauta, Fantasmagoría, Sombra
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Quimera' FROM spell_schools WHERE code = 'ILLUSION' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Quimera'
);
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Engaño' FROM spell_schools WHERE code = 'ILLUSION' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Engaño'
);
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Pauta' FROM spell_schools WHERE code = 'ILLUSION' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Pauta'
);
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Fantasmagoría' FROM spell_schools WHERE code = 'ILLUSION' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Fantasmagoría'
);
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Sombra' FROM spell_schools WHERE code = 'ILLUSION' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Sombra'
);

-- Transmutación: Polimorfia
INSERT INTO spell_subschools (school_id, name)
SELECT id, 'Polimorfia' FROM spell_schools WHERE code = 'TRANSMUTATION' AND NOT EXISTS (
    SELECT 1 FROM spell_subschools ss WHERE ss.school_id = spell_schools.id AND ss.name = 'Polimorfia'
);

