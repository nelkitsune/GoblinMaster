CREATE TABLE campaigns (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(120) NOT NULL,
                           description VARCHAR(1000),
                           owner_id BIGINT NOT NULL,
                           active BOOLEAN NOT NULL DEFAULT TRUE,
                           created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                           CONSTRAINT fk_campaign_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE campaign_members (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  campaign_id BIGINT NOT NULL,
                                  user_id BIGINT NOT NULL,
                                  role ENUM('OWNER','PLAYER') NOT NULL,
                                  CONSTRAINT fk_cm_campaign FOREIGN KEY (campaign_id) REFERENCES campaigns(id),
                                  CONSTRAINT fk_cm_user FOREIGN KEY (user_id) REFERENCES users(id),
                                  CONSTRAINT uq_cm UNIQUE (campaign_id, user_id)
);

-- índice útil para listados por usuario
CREATE INDEX ix_cm_user ON campaign_members(user_id);
