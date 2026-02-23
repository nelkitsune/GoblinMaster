-- V33__add_spell_focus_components.sql

-- Agrega columnas components_f y components_df a la tabla spells
ALTER TABLE spells
    ADD COLUMN components_f TINYINT(1) NOT NULL DEFAULT 0,
    ADD COLUMN components_df TINYINT(1) NOT NULL DEFAULT 0;

-- No índices adicionales; defaults 0 mantienen compatibilidad con datos existentes

