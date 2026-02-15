-- Índices para mejorar filtros por miembro, estado y nombre (MySQL 8.0.43)

-- Índice compuesto para membership lookup
CREATE INDEX idx_campaign_members_campaign_user ON campaign_members (campaign_id, user_id);

-- Índice por owner y created_at para orden y filtro
CREATE INDEX idx_campaigns_owner_created ON campaigns (owner_id, created_at DESC);

-- Índice por activo
CREATE INDEX idx_campaigns_active ON campaigns (active);

-- Índice por nombre
CREATE INDEX idx_campaigns_name ON campaigns (name);
