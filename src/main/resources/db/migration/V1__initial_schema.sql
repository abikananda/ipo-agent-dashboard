CREATE TABLE ipo (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, slug VARCHAR(80) NOT NULL UNIQUE, company_name VARCHAR(255) NOT NULL,
 type VARCHAR(20) NOT NULL, status VARCHAR(20) NOT NULL, sector VARCHAR(120), open_date DATE, close_date DATE,
 listing_date DATE, price_min DECIMAL(14,2), price_max DECIMAL(14,2), lot_size INT,
 issue_size_crore DECIMAL(16,2), fresh_issue_crore DECIMAL(16,2), ofs_crore DECIMAL(16,2),
 rhp_url VARCHAR(1000), updated_at TIMESTAMP(6) NULL, INDEX idx_ipo_status_dates(status,open_date,close_date)
);
CREATE TABLE ipo_financial_period (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, ipo_id BIGINT NOT NULL, period_end DATE NOT NULL,
 revenue_crore DECIMAL(16,2), ebitda_crore DECIMAL(16,2), pat_crore DECIMAL(16,2), total_debt_crore DECIMAL(16,2),
 net_worth_crore DECIMAL(16,2), operating_cash_flow_crore DECIMAL(16,2),
 CONSTRAINT fk_financial_ipo FOREIGN KEY(ipo_id) REFERENCES ipo(id), UNIQUE KEY uk_financial_period(ipo_id,period_end)
);
CREATE TABLE ipo_market_snapshot (
 id BIGINT AUTO_INCREMENT PRIMARY KEY, ipo_id BIGINT NOT NULL, gmp DECIMAL(12,2), qib_subscription DECIMAL(12,2),
 nii_subscription DECIMAL(12,2), retail_subscription DECIMAL(12,2), total_subscription DECIMAL(12,2),
 source_name VARCHAR(255) NOT NULL, source_url VARCHAR(1000) NOT NULL, observed_at TIMESTAMP(6) NOT NULL,
 CONSTRAINT fk_market_ipo FOREIGN KEY(ipo_id) REFERENCES ipo(id), INDEX idx_market_observed(ipo_id,observed_at)
);

