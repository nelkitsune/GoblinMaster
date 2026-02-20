-- Asegura que la columna `normal` exista y sea de tipo TEXT usando logic segura compatible con MySQL
-- Crear columna si no existe
SET @sql = (
  SELECT IF(COUNT(*)=0,
    'ALTER TABLE feats ADD COLUMN normal TEXT',
    'SELECT 1')
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'feats' AND COLUMN_NAME = 'normal'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Modificar a TEXT solo si la columna existe
SET @sql = (
  SELECT IF(COUNT(*)>0,
    'ALTER TABLE feats MODIFY COLUMN normal TEXT',
    'SELECT 1')
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'feats' AND COLUMN_NAME = 'normal'
);
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;
