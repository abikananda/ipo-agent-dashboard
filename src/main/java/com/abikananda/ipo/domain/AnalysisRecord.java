package com.abikananda.ipo.domain;
import com.fasterxml.jackson.annotation.JsonIgnore; import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="ipo_analysis") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AnalysisRecord {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @JsonIgnore @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="ipo_id") private Ipo ipo;
 @Column(nullable=false) private int overallScore; @Column(nullable=false) private int confidenceScore;
 @Column(nullable=false) private String recommendation; private String listingRecommendation; private String longTermRecommendation;
 @Column(nullable=false) private String engineVersion; @Column(nullable=false) private String scoringVersion;
 @Column(columnDefinition="JSON") private String scoreBreakdown; @Column(length=3000) private String summary;
 @Column(nullable=false) private Instant analyzedAt;
}

