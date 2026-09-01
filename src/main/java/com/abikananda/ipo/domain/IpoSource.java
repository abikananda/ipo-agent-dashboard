package com.abikananda.ipo.domain;
import com.fasterxml.jackson.annotation.JsonIgnore; import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="ipo_source") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IpoSource {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @JsonIgnore @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="ipo_id") private Ipo ipo;
 @Column(nullable=false) private String sourceName; @Column(nullable=false,length=1000) private String sourceUrl;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private SourceType sourceType;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Reliability reliability;
 @Column(nullable=false) private Instant retrievedAt; private Instant effectiveAt; private String contentHash;
 public enum SourceType { SEBI, NSE, BSE, RHP, REGISTRAR, COMPANY, AGGREGATOR, GMP }
 public enum Reliability { AUTHORITATIVE, HIGH, MEDIUM, UNOFFICIAL }
}
