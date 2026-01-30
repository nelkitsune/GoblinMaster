-- V16: Add join_code and campaign_members.joined_at with robust backfill and indexes (MySQL 8.0.43)

-- 1) Add column join_code as NULL if missing
SET @col := (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
             WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'campaigns' AND COLUMN_NAME = 'join_code');
SET @sql := IF(@col = 0, 'ALTER TABLE campaigns ADD COLUMN join_code VARCHAR(16) NULL', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 2) Backfill join_code for existing rows where null or empty
-- Use GM- prefix + 10 chars from UUID
UPDATE campaigns c
LEFT JOIN (
    SELECT id,
           CONCAT('GM-', SUBSTRING(REPLACE(UPPER(UUID()), '-', ''), 1, 10)) AS gen_code
    FROM campaigns
) g ON g.id = c.id
SET c.join_code = g.gen_code
WHERE c.join_code IS NULL OR c.join_code = '';

-- 2b) Resolve duplicates if any: iteratively update rows that share codes
-- Create a temporary table of duplicate ids
DROP TEMPORARY TABLE IF EXISTS tmp_dup_codes;
CREATE TEMPORARY TABLE tmp_dup_codes (
    id BIGINT PRIMARY KEY
) ENGINE=Memory;
INSERT INTO tmp_dup_codes (id)
SELECT c.id
FROM campaigns c
JOIN (
    SELECT join_code, COUNT(*) cnt
    FROM campaigns
    WHERE join_code IS NOT NULL AND join_code <> ''
    GROUP BY join_code
    HAVING COUNT(*) > 1
) d ON d.join_code = c.join_code;

-- Update duplicates to new unique codes
UPDATE campaigns c
JOIN tmp_dup_codes t ON t.id = c.id
SET c.join_code = CONCAT('GM-', SUBSTRING(REPLACE(UPPER(UUID()), '-', ''), 1, 10));

DROP TEMPORARY TABLE IF EXISTS tmp_dup_codes;

-- 3) Ensure all join_code are NOT NULL and unique index exists
ALTER TABLE campaigns
    MODIFY COLUMN join_code VARCHAR(16) NOT NULL;

SET @idx := (SELECT COUNT(1) FROM INFORMATION_SCHEMA.STATISTICS
             WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'campaigns' AND INDEX_NAME = 'uq_campaigns_join_code');
SET @sql := IF(@idx = 0, 'CREATE UNIQUE INDEX uq_campaigns_join_code ON campaigns(join_code)', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- 4) Ensure updated_at auto-updates (assumes column exists)
ALTER TABLE campaigns
    MODIFY COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- 5) Add joined_at to campaign_members if missing
SET @col2 := (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
              WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'campaign_members' AND COLUMN_NAME = 'joined_at');
SET @sql2 := IF(@col2 = 0, 'ALTER TABLE campaign_members ADD COLUMN joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP', 'SELECT 1');
PREPARE stmt2 FROM @sql2; EXECUTE stmt2; DEALLOCATE PREPARE stmt2;

-- 6) Indexes for campaign_members
SET @idx2 := (SELECT COUNT(1) FROM INFORMATION_SCHEMA.STATISTICS
              WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'campaign_members' AND INDEX_NAME = 'idx_campaign_members_user_id');
SET @sql2 := IF(@idx2 = 0, 'CREATE INDEX idx_campaign_members_user_id ON campaign_members(user_id)', 'SELECT 1');
PREPARE stmt2 FROM @sql2; EXECUTE stmt2; DEALLOCATE PREPARE stmt2;

SET @idx3 := (SELECT COUNT(1) FROM INFORMATION_SCHEMA.STATISTICS
              WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'campaign_members' AND INDEX_NAME = 'uq_campaign_members_campaign_user');
SET @sql3 := IF(@idx3 = 0, 'CREATE UNIQUE INDEX uq_campaign_members_campaign_user ON campaign_members(campaign_id, user_id)', 'SELECT 1');
PREPARE stmt3 FROM @sql3; EXECUTE stmt3; DEALLOCATE PREPARE stmt3;
