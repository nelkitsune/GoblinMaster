-- Alinear nombre de columna con convención snake_case
-- Quitar FK existente sobre featId, renombrar columna y volver a crear la FK
ALTER TABLE prereq_conditions
    DROP FOREIGN KEY fk_prereq_conditions_feat;

ALTER TABLE prereq_conditions
    CHANGE featId feat_id BIGINT;

ALTER TABLE prereq_conditions
    ADD CONSTRAINT fk_prereq_conditions_feat FOREIGN KEY (feat_id) REFERENCES feats(id);

