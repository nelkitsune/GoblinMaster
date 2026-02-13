-- Create link table for Campaign <-> Homebrew Rules
CREATE TABLE IF NOT EXISTS campaign_rules (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    campaign_id BIGINT NOT NULL,
    rule_id BIGINT NOT NULL,
    CONSTRAINT fk_campaign_rules_campaign FOREIGN KEY (campaign_id)
        REFERENCES campaigns(id) ON DELETE CASCADE,
    CONSTRAINT fk_campaign_rules_rule FOREIGN KEY (rule_id)
        REFERENCES rules(id) ON DELETE CASCADE,
    CONSTRAINT uq_campaign_rule UNIQUE (campaign_id, rule_id)
);

