-- A) owner_user_id en spells y feats
ALTER TABLE spells ADD COLUMN owner_user_id BIGINT NULL;
ALTER TABLE feats  ADD COLUMN owner_user_id BIGINT NULL;

ALTER TABLE spells ADD CONSTRAINT fk_spells_owner_user FOREIGN KEY (owner_user_id) REFERENCES users(id);
ALTER TABLE feats  ADD CONSTRAINT fk_feats_owner_user  FOREIGN KEY (owner_user_id) REFERENCES users(id);

CREATE INDEX ix_spells_owner_user ON spells(owner_user_id);
CREATE INDEX ix_feats_owner_user  ON feats(owner_user_id);

-- B) tablas puente campaign_spells y campaign_feats
CREATE TABLE campaign_spells (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    spell_id BIGINT NOT NULL,
    CONSTRAINT fk_cs_campaign FOREIGN KEY (campaign_id) REFERENCES campaigns(id),
    CONSTRAINT fk_cs_spell    FOREIGN KEY (spell_id)    REFERENCES spells(id),
    CONSTRAINT uq_campaign_spell UNIQUE (campaign_id, spell_id)
);
CREATE INDEX ix_cs_campaign ON campaign_spells(campaign_id);
CREATE INDEX ix_cs_spell    ON campaign_spells(spell_id);

CREATE TABLE campaign_feats (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    feat_id BIGINT NOT NULL,
    CONSTRAINT fk_cf_campaign FOREIGN KEY (campaign_id) REFERENCES campaigns(id),
    CONSTRAINT fk_cf_feat     FOREIGN KEY (feat_id)     REFERENCES feats(id),
    CONSTRAINT uq_campaign_feat UNIQUE (campaign_id, feat_id)
);
CREATE INDEX ix_cf_campaign ON campaign_feats(campaign_id);
CREATE INDEX ix_cf_feat     ON campaign_feats(feat_id);

