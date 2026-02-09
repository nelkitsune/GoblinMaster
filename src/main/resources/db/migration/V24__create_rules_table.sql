CREATE TABLE rules (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  original_name VARCHAR(200),
  description LONGTEXT NOT NULL,
  pages VARCHAR(100),
  books VARCHAR(200),
  owner_user_id BIGINT NULL,
  created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at TIMESTAMP(6) NULL,
  CONSTRAINT fk_rules_owner_user FOREIGN KEY (owner_user_id) REFERENCES users(id)
);

CREATE INDEX ix_rules_owner_user ON rules(owner_user_id);
CREATE INDEX ix_rules_name ON rules(name);

