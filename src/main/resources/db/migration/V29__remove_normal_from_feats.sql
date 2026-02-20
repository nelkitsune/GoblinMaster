-- No-op migration: evitar DROP COLUMN IF EXISTS que falla en esta versión de MySQL.
-- V30/V31 manejan creación/transformación/nulificación de la columna `normal`.
SELECT 1;

