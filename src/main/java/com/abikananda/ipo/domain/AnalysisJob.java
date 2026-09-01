package com.abikananda.ipo.domain;
import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="analysis_job") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AnalysisJob {
 @Id @Column(length=36) private String id; private Long ipoId;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private JobType type;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private JobStatus status;
 private String message; @Column(nullable=false) private Instant createdAt; private Instant completedAt;
 public enum JobType { DISCOVER, REFRESH, ANALYZE } public enum JobStatus { QUEUED, RUNNING, COMPLETED, PARTIAL, FAILED }
}
