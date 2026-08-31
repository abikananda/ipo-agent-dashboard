INSERT INTO ipo(slug,company_name,type,status,sector,open_date,close_date,listing_date,price_min,price_max,lot_size,issue_size_crore,fresh_issue_crore,ofs_crore,rhp_url,updated_at) VALUES
('aurora-renewables','Aurora Renewables Ltd','MAINBOARD','OPEN','Renewable Energy','2026-08-31','2026-09-02','2026-09-08',310,326,46,1840,1300,540,'https://example.com/aurora-rhp.pdf',CURRENT_TIMESTAMP),
('novacare-health','NovaCare Health Systems','MAINBOARD','UPCOMING','Healthcare','2026-09-04','2026-09-08','2026-09-14',205,216,69,920,600,320,'https://example.com/novacare-rhp.pdf',CURRENT_TIMESTAMP),
('quantum-logistics','Quantum Logistics India','SME','CLOSED','Logistics','2026-08-25','2026-08-27','2026-09-03',88,92,1200,74,52,22,'https://example.com/quantum-rhp.pdf',CURRENT_TIMESTAMP);
INSERT INTO ipo_financial_period(ipo_id,period_end,revenue_crore,ebitda_crore,pat_crore,total_debt_crore,net_worth_crore,operating_cash_flow_crore) VALUES
(1,'2024-03-31',620,104,48,380,510,42),(1,'2025-03-31',805,146,72,340,582,66),(1,'2026-03-31',1040,205,112,295,710,98),
(2,'2024-03-31',410,70,28,155,230,22),(2,'2025-03-31',475,75,30,170,255,24),(2,'2026-03-31',590,101,44,145,310,39),
(3,'2024-03-31',86,12,5,31,42,4),(3,'2025-03-31',112,16,8,29,50,7),(3,'2026-03-31',146,23,12,24,62,11);
INSERT INTO ipo_market_snapshot(ipo_id,gmp,qib_subscription,nii_subscription,retail_subscription,total_subscription,source_name,source_url,observed_at) VALUES
(1,48,3.10,5.80,7.20,5.45,'Demo data','https://example.com',CURRENT_TIMESTAMP),
(2,18,0.00,0.00,0.00,0.00,'Demo data','https://example.com',CURRENT_TIMESTAMP),
(3,21,12.4,28.7,42.1,27.8,'Demo data','https://example.com',CURRENT_TIMESTAMP);

