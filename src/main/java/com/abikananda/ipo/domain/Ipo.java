package com.abikananda.ipo.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.*;

@Entity @Table(name = "ipo") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ipo {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, unique=true, length=80) private String slug;
  @Column(nullable=false) private String companyName;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private IpoType type;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private IpoStatus status;
  private String sector;
  private LocalDate openDate;
  private LocalDate closeDate;
  private LocalDate listingDate;
  @Column(precision=14, scale=2) private BigDecimal priceMin;
  @Column(precision=14, scale=2) private BigDecimal priceMax;
  private Integer lotSize;
  @Column(precision=16, scale=2) private BigDecimal issueSizeCrore;
  @Column(precision=16, scale=2) private BigDecimal freshIssueCrore;
  @Column(precision=16, scale=2) private BigDecimal ofsCrore;
  private String rhpUrl;
  private Instant updatedAt;
  public enum IpoType { MAINBOARD, SME }
  public enum IpoStatus { UPCOMING, OPEN, CLOSED, LISTED }
}

