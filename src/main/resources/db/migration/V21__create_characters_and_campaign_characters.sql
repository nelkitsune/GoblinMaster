CREATE TABLE characters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NULL,
    name VARCHAR(120) NOT NULL,
    max_hp INT NOT NULL,
    base_initiative INT NOT NULL,
    is_npc BOOLEAN NOT NULL,
    CONSTRAINT fk_char_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE campaign_characters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    character_id BIGINT NOT NULL,
    CONSTRAINT fk_cc_campaign FOREIGN KEY (campaign_id) REFERENCES campaigns(id),
    CONSTRAINT fk_cc_character FOREIGN KEY (character_id) REFERENCES characters(id),
    CONSTRAINT uq_campaign_character UNIQUE (campaign_id, character_id)
);

