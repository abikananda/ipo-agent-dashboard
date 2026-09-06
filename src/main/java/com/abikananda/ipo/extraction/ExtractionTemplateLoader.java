package com.abikananda.ipo.extraction;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ExtractionTemplateLoader {
 private static final Map<String,String> TEMPLATES=Map.of(
   "ipo-details-v1","extraction-templates/ipo-details-v1.yml",
   "gmp-v1","extraction-templates/gmp-v1.yml");
 private final YAMLMapper yaml=new YAMLMapper();

 public ExtractionTemplate load(String id){String path=TEMPLATES.get(id);if(path==null)throw new IllegalArgumentException("Unknown extraction template: "+id+". Available: "+TEMPLATES.keySet());try{return yaml.readValue(new ClassPathResource(path).getInputStream(),ExtractionTemplate.class);}catch(IOException e){throw new IllegalStateException("Cannot load extraction template "+id,e);}}
 public List<String> ids(){return TEMPLATES.keySet().stream().sorted().toList();}
}
