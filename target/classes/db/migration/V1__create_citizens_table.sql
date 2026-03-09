-- Citizens table: stores basic citizen data for pension eligibility
CREATE TABLE citizens (
    id              BIGSERIAL PRIMARY KEY,
    personal_number VARCHAR(20) NOT NULL UNIQUE,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    birth_date      DATE NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_citizens_personal_number ON citizens(personal_number);
CREATE INDEX idx_citizens_created_at ON citizens(created_at);
