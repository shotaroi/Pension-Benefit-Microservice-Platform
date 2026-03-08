-- Contributions table: stores pension contributions per year per citizen
CREATE TABLE contributions (
    id                  BIGSERIAL PRIMARY KEY,
    citizen_id           BIGINT NOT NULL,
    year                 INTEGER NOT NULL,
    salary               DECIMAL(15, 2) NOT NULL,
    contribution_amount  DECIMAL(15, 2) NOT NULL,
    created_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_contributions_citizen FOREIGN KEY (citizen_id) REFERENCES citizens(id),
    CONSTRAINT uq_contributions_citizen_year UNIQUE (citizen_id, year)
);

CREATE INDEX idx_contributions_citizen_id ON contributions(citizen_id);
CREATE INDEX idx_contributions_year ON contributions(year);
