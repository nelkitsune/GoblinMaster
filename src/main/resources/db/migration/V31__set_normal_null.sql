-- Forzar que la columna `normal` sea NULL para todas las filas y que la columna sea TEXT NULL.
-- Se asume MySQL 8.x
ALTER TABLE feats
  MODIFY COLUMN normal TEXT NULL;

-- Poner todos los valores a NULL
UPDATE feats SET normal = NULL WHERE normal IS NOT NULL;

