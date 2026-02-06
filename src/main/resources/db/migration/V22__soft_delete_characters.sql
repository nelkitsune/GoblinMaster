-- Agregar borrado lógico
ALTER TABLE characters ADD COLUMN deleted_at TIMESTAMP(6) NULL;
ALTER TABLE campaign_characters ADD COLUMN deleted_at TIMESTAMP(6) NULL;

