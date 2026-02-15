-- XP Log tables
CREATE TABLE IF NOT EXISTS session_xp_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  campaign_id BIGINT NOT NULL,
  xp_gained INT NOT NULL,
  description TEXT NULL,
  created_by_user_id BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_xplog_campaign FOREIGN KEY (campaign_id) REFERENCES campaigns(id) ON DELETE CASCADE,
  CONSTRAINT fk_xplog_created_by FOREIGN KEY (created_by_user_id) REFERENCES users(id) ON DELETE RESTRICT
);
CREATE INDEX idx_xplog_campaign_created ON session_xp_log(campaign_id, created_at);

CREATE TABLE IF NOT EXISTS session_xp_log_participant (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  log_id BIGINT NOT NULL,
  participant_type ENUM('USER','GUEST','NPC') NOT NULL,
  user_id BIGINT NULL,
  display_name VARCHAR(120) NULL,
  CONSTRAINT fk_xplog_part_log FOREIGN KEY (log_id) REFERENCES session_xp_log(id) ON DELETE CASCADE,
  CONSTRAINT fk_xplog_part_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);
CREATE INDEX idx_xplog_part_log ON session_xp_log_participant(log_id);
CREATE INDEX idx_xplog_part_user ON session_xp_log_participant(user_id);

