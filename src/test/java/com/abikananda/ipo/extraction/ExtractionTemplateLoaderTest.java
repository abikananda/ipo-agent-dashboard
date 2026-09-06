package com.abikananda.ipo.extraction;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ExtractionTemplateLoaderTest {
 private final ExtractionTemplateLoader loader=new ExtractionTemplateLoader();
 @Test void loadsIpoDetailsTemplate(){var template=loader.load("ipo-details-v1");assertThat(template.sources()).extracting(ExtractionTemplate.SourceDefinition::sourceId).containsExactly("bse-public-issues","chittorgarh-ipo-list");assertThat(template.outputSchema()).containsKey("properties");}
 @Test void loadsGmpTemplate(){var template=loader.load("gmp-v1");assertThat(template.sources()).extracting(ExtractionTemplate.SourceDefinition::sourceId).containsExactly("investorgain-live-gmp","ipowatch-live-gmp");assertThat(template.datasetType()).isEqualTo("GMP_SNAPSHOT");}
}
