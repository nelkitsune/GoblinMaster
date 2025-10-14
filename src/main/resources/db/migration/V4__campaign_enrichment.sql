ALTER TABLE campaigns
    ADD COLUMN `system`     VARCHAR(100),
  ADD COLUMN `setting`    VARCHAR(100),
  ADD COLUMN `image_url`  VARCHAR(255),
  ADD COLUMN `updated_at` TIMESTAMP(6)
      NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
      ON UPDATE CURRENT_TIMESTAMP(6),
    ADD COLUMN `deleted_at` TIMESTAMP(6) NULL;

CREATE INDEX ix_campaigns_active ON campaigns(`active`);
