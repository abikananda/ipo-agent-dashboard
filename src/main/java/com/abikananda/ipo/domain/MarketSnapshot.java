package com.abikananda.ipo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name="ipo_market_snapshot") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MarketSnapshot {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne(optional=false, fetch=FetchType.LAZY) @JoinColumn(name="ipo_id") private Ipo ipo;
  @Column(precision=12,scale=2) private BigDecimal gmp;
  @Column(precision=12,scale=2) private BigDecimal qibSubscription;
  @Column(precision=12,scale=2) private BigDecimal niiSubscription;
  @Column(precision=12,scale=2) private BigDecimal retailSubscription;
  @Column(precision=12,scale=2) private BigDecimal totalSubscription;
  @Column(nullable=false) private String sourceName;
  @Column(nullable=false, length=1000) private String sourceUrl;
  @Column(nullable=false) private Instant observedAt;
}

