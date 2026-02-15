-- MySQL 8.0.x compatible idempotent column creation
-- Adds columns only if they don't exist (INFORMATION_SCHEMA + dynamic SQL)

-- users.avatar_url
SET @sql := (
  SELECT IF(
    EXISTS(
      SELECT 1
      FROM INFORMATION_SCHEMA.COLUMNS
      WHERE TABLE_SCHEMA = DATABASE()
        AND TABLE_NAME = 'users'
        AND COLUMN_NAME = 'avatar_url'
    ),
    'SELECT 1',
    'ALTER TABLE users ADD COLUMN avatar_url VARCHAR(500) NULL'
  )
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- users.avatar_public_id
SET @sql := (
  SELECT IF(
    EXISTS(
      SELECT 1
      FROM INFORMATION_SCHEMA.COLUMNS
      WHERE TABLE_SCHEMA = DATABASE()
        AND TABLE_NAME = 'users'
        AND COLUMN_NAME = 'avatar_public_id'
    ),
    'SELECT 1',
    'ALTER TABLE users ADD COLUMN avatar_public_id VARCHAR(200) NULL'
  )
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- campaigns.image_url
SET @sql := (
  SELECT IF(
    EXISTS(
      SELECT 1
      FROM INFORMATION_SCHEMA.COLUMNS
      WHERE TABLE_SCHEMA = DATABASE()
        AND TABLE_NAME = 'campaigns'
        AND COLUMN_NAME = 'image_url'
    ),
    'SELECT 1',
    'ALTER TABLE campaigns ADD COLUMN image_url VARCHAR(500) NULL'
  )
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- campaigns.image_public_id
SET @sql := (
  SELECT IF(
    EXISTS(
      SELECT 1
      FROM INFORMATION_SCHEMA.COLUMNS
      WHERE TABLE_SCHEMA = DATABASE()
        AND TABLE_NAME = 'campaigns'
        AND COLUMN_NAME = 'image_public_id'
    ),
    'SELECT 1',
    'ALTER TABLE campaigns ADD COLUMN image_public_id VARCHAR(200) NULL'
  )
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;