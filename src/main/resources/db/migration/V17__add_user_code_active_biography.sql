-- V17__add_user_code_active_biography.sql
-- Compatible con MySQL 8.0.43 y ejecutable aunque ya existan columnas.

-- user_code
SET @sql := (
  SELECT IF(
    (SELECT COUNT(*)
     FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE()
       AND TABLE_NAME = 'users'
       AND COLUMN_NAME = 'user_code') = 0,
    'ALTER TABLE `users` ADD COLUMN `user_code` VARCHAR(36) NULL',
    'SELECT 1'
  )
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- active
SET @sql := (
  SELECT IF(
    (SELECT COUNT(*)
     FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE()
       AND TABLE_NAME = 'users'
       AND COLUMN_NAME = 'active') = 0,
    'ALTER TABLE `users` ADD COLUMN `active` TINYINT(1) NOT NULL DEFAULT 1',
    'SELECT 1'
  )
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- biography
SET @sql := (
  SELECT IF(
    (SELECT COUNT(*)
     FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = DATABASE()
       AND TABLE_NAME = 'users'
       AND COLUMN_NAME = 'biography') = 0,
    'ALTER TABLE `users` ADD COLUMN `biography` TEXT NULL',
    'SELECT 1'
  )
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
