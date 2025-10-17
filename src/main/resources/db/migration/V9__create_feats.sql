CREATE TABLE feats (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,
                       original_name VARCHAR(255) NOT NULL UNIQUE,
                       code VARCHAR(255) NOT NULL,
                       source VARCHAR(255) NOT NULL,
                       Benefit VARCHAR(255) NOT NULL,
                       Special VARCHAR(255),
                       tipo VARCHAR(50),
                       CONSTRAINT chk_tipo CHECK (tipo IN (
                                                           'ARTISTICAS', 'COOPERATIVAS', 'AGALLAS', 'COMBATE', 'ESTILO',
                                                           'CREACION_DE_OBJETOS', 'CRITICO', 'METAMAGICAS', 'RACIAL'
                           ))
);

CREATE TABLE prereq_groups (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               feat_id BIGINT NOT NULL,
                               group_index INT NOT NULL,
                               CONSTRAINT fk_prereqgroup_feat FOREIGN KEY (feat_id) REFERENCES feats(id)
);

CREATE TABLE prereq_conditions (
                                   id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                   prereq_group_id BIGINT NOT NULL,
                                   kind VARCHAR(50),
                                   CONSTRAINT fk_condition_group FOREIGN KEY (prereq_group_id) REFERENCES prereq_groups(id),
                                   CONSTRAINT chk_kind CHECK (kind IN (
                                                                       'FEAT', 'RACE', 'CLASS', 'ALIGNMENT', 'CHAR_LEVEL', 'CLASS_LEVEL',
                                                                       'CASTER_LEVEL', 'CAN_CAST', 'KNOWN_SPELL', 'ABILITY_SCORE', 'BAB',
                                                                       'SKILL_RANKS', 'SIZE', 'DEITY', 'TAG'
                                       ))
);