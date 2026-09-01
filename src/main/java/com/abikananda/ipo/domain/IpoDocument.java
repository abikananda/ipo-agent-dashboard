package com.abikananda.ipo.domain;
import com.fasterxml.jackson.annotation.JsonIgnore; import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="ipo_document") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IpoDocument {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @JsonIgnore @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="ipo_id") private Ipo ipo;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private DocumentType documentType;
 @Column(nullable=false,length=1000) private String sourceUrl; private String sha256; private Integer pageCount;
 @JsonIgnore @Column(columnDefinition="LONGTEXT") private String extractedText; @Column(nullable=false) private Instant retrievedAt;
 public enum DocumentType { DRHP, RHP, ADDENDUM, OTHER }
}
