package com.abikananda.ipo.extraction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
public record ExtractionTemplate(String templateId,String name,String description,String datasetType,Integer version,
                                 List<SourceDefinition> sources,Map<String,Object> outputSchema) {
 @JsonIgnoreProperties(ignoreUnknown=true)
 public record SourceDefinition(String sourceId,String sourceName,String description,String sourceType,String reliability,
                                String url,String listingUrl,String fetchStrategy,String extractionInstruction,
                                DetailTraversal detailTraversal) {
  public String effectiveUrl(){return listingUrl!=null&&!listingUrl.isBlank()?listingUrl:url;}
 }
 @JsonIgnoreProperties(ignoreUnknown=true)
 public record DetailTraversal(Boolean enabled,String linkSelector,Integer maxPages,String extractionInstruction) {
  public boolean isEnabled(){return Boolean.TRUE.equals(enabled);}
 }
}
