ALTER TABLE prereq_conditions
    ADD COLUMN value VARCHAR(255),
    ADD COLUMN featId BIGINT,
    ADD CONSTRAINT fk_prereq_conditions_feat FOREIGN KEY (featId) REFERENCES feats(id);
