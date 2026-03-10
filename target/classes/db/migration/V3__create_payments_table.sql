-- Pension payments table: stores monthly pension payouts
CREATE TABLE pension_payments (
    id           BIGSERIAL PRIMARY KEY,
    citizen_id   BIGINT NOT NULL,
    amount       DECIMAL(15, 2) NOT NULL,
    payment_date DATE NOT NULL,
    status       VARCHAR(20) NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payments_citizen FOREIGN KEY (citizen_id) REFERENCES citizens(id),
    CONSTRAINT chk_payment_status CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED'))
);

CREATE INDEX idx_payments_citizen_id ON pension_payments(citizen_id);
CREATE INDEX idx_payments_status ON pension_payments(status);
CREATE INDEX idx_payments_payment_date ON pension_payments(payment_date);
