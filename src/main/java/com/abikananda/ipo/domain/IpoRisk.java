package com.abikananda.ipo.domain;
import com.fasterxml.jackson.annotation.JsonIgnore; import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="ipo_risk") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IpoRisk {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @JsonIgnore @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="ipo_id") private Ipo ipo;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Severity severity;
 @Column(nullable=false) private String category; @Column(nullable=false,length=2000) private String description;
 private Integer documentPage; @Column(length=1000) private String sourceUrl; private boolean hardOverride;
 public enum Severity { CRITICAL, HIGH, MEDIUM, LOW }
}

