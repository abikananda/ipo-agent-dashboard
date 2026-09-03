package com.abikananda.ipo.domain;
import com.fasterxml.jackson.annotation.JsonIgnore; import jakarta.persistence.*; import lombok.*; import java.math.BigDecimal; import java.time.Instant;
@Entity @Table(name="ipo_valuation") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IpoValuation {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @JsonIgnore @OneToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="ipo_id",unique=true) private Ipo ipo;
 private BigDecimal marketCapCrore, enterpriseValueCrore, peRatio, priceToBook, evEbitda, evSales, sectorMedianPe, valuationPremiumPct;
 @Column(nullable=false) private Instant calculatedAt;
}

