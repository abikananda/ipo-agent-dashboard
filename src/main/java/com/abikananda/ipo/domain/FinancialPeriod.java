package com.abikananda.ipo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name="ipo_financial_period", uniqueConstraints=@UniqueConstraint(columnNames={"ipo_id","period_end"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FinancialPeriod {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="ipo_id") private Ipo ipo;
  @Column(name="period_end", nullable=false) private LocalDate periodEnd;
  @Column(precision=16,scale=2) private BigDecimal revenueCrore;
  @Column(precision=16,scale=2) private BigDecimal ebitdaCrore;
  @Column(precision=16,scale=2) private BigDecimal patCrore;
  @Column(precision=16,scale=2) private BigDecimal totalDebtCrore;
  @Column(precision=16,scale=2) private BigDecimal netWorthCrore;
  @Column(precision=16,scale=2) private BigDecimal operatingCashFlowCrore;
}

