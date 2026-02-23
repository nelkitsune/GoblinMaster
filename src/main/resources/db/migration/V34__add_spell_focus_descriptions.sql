-- V34__add_spell_focus_descriptions.sql

-- Agrega columnas focus_desc y divine_focus_desc a la tabla spells
ALTER TABLE spells
    ADD COLUMN focus_desc VARCHAR(512) NULL,
    ADD COLUMN divine_focus_desc VARCHAR(512) NULL;

-- No índices adicionales; columnas NULL por compatibilidad con datos existentes

