-- Añade la columna `normal` a la tabla feats. Para versiones existentes la columna se inicializa en 0 (false).
ALTER TABLE feats
  ADD COLUMN normal TINYINT(1) NOT NULL DEFAULT 0;

-- Asegurar por si acaso (no es estrictamente necesario si la columna se creó con DEFAULT)
UPDATE feats SET normal = 0 WHERE normal IS NULL;

