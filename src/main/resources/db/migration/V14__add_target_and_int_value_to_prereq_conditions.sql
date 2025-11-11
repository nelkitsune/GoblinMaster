ALTER TABLE prereq_conditions
    ADD COLUMN target VARCHAR(255) NULL,
    ADD COLUMN int_value INT NULL;

